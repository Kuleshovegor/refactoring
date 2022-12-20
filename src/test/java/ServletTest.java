import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.kuleshov.sd.dao.ProductDao;
import ru.kuleshov.sd.servlet.AddProductServlet;
import ru.kuleshov.sd.servlet.GetProductsServlet;
import ru.kuleshov.sd.servlet.QueryServlet;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ServletTest {
    private static final String TEST_DATABASE_URL = "jdbc:sqlite:test2.db";

    private final ProductDao productDao = new ProductDao(TEST_DATABASE_URL);
    private final HttpServletRequest request = mock(HttpServletRequest.class);
    private final HttpServletResponse response = mock(HttpServletResponse.class);

    @BeforeAll
    public static void startBD() throws SQLException {
        dbRequest("CREATE TABLE IF NOT EXISTS PRODUCT" +
                "(ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                " NAME           TEXT    NOT NULL, " +
                " PRICE          INT     NOT NULL)");
    }

    @AfterEach
    public void cleanBD() throws SQLException {
        dbRequest("DELETE FROM PRODUCT WHERE 1=1");
    }

    @Test
    public void getProductTest() throws IOException, ServletException, SQLException {
        dbRequest("INSERT INTO PRODUCT (NAME, PRICE) VALUES (\"test\", 1);");
        Assertions.assertEquals(
                "<html><body>" + System.lineSeparator() +
                        "test\t1</br>" + System.lineSeparator() +
                        "</body></html>" + System.lineSeparator(),
                getRequest()
        );
    }

    @Test
    public void addProductTest() throws IOException, ServletException {
        addRequest("test", 1);
    }

    @Test
    public void addAndGetProductTest() throws IOException, ServletException {
        addRequest("test", 1);

        Assertions.assertEquals(
                "<html><body>" + System.lineSeparator() +
                        "test\t1</br>" + System.lineSeparator() +
                        "</body></html>" + System.lineSeparator(),
                getRequest()
        );
    }

    @Test
    public void sumRequestTest() throws ServletException, IOException {
        commandTest(
                "sum",
                "Summary price: " + System.lineSeparator() +
                        "230" + System.lineSeparator()
        );
    }

    @Test
    public void minRequestTest() throws ServletException, IOException {
        commandTest(
                "min",
                "<h1>Product with min price: </h1>" + System.lineSeparator() +
                        "negative\t-1</br>" + System.lineSeparator()
        );
    }

    @Test
    public void maxRequestTest() throws ServletException, IOException {
        commandTest(
                "max",
                "<h1>Product with max price: </h1>" + System.lineSeparator() +
                        "хлеб\t131</br>" + System.lineSeparator()
        );
    }

    @Test
    public void countRequestTest() throws ServletException, IOException {
        commandTest(
                "count",
                "Number of products: " + System.lineSeparator() +
                        "3" + System.lineSeparator()
                );
    }

    private void commandTest(String command, String body) throws ServletException, IOException {
        addRequest("хлеб", 131);
        addRequest("complex name", 100);
        addRequest("negative", -1);


        Map<String, String> parameters = new HashMap<>();
        parameters.put("command", command);

        Assertions.assertEquals(
                "<html><body>" + System.lineSeparator() +
                        body +
                        "</body></html>" + System.lineSeparator(),
                doRequest(new QueryServlet(productDao), parameters)
        );
    }

    private void addRequest(String name, int price) throws IOException, ServletException {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("name", name);
        parameters.put("price", String.valueOf(price));

        Assertions.assertEquals("OK", doRequest(new AddProductServlet(productDao), parameters).trim());
    }

    private String getRequest() throws IOException, ServletException {
        return doRequest(new GetProductsServlet(productDao), new HashMap<>());
    }

    private static void dbRequest(String sql) throws SQLException {
        try (Connection c = DriverManager.getConnection(TEST_DATABASE_URL)) {
            Statement stmt = c.createStatement();

            stmt.executeUpdate(sql);
            stmt.close();
        }
    }

    private String doRequest(Servlet servlet, Map<String, String> parameters) throws IOException, ServletException {
        when(request.getMethod()).thenReturn("GET");
        for (Map.Entry<String, String> entry : parameters.entrySet()) {
            when(request.getParameter(entry.getKey())).thenReturn(entry.getValue());
        }

        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);

        when(response.getWriter()).thenReturn(printWriter);

        servlet.service(request, response);

        printWriter.flush();

        return stringWriter.toString();
    }
}
