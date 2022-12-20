package ru.kuleshov.sd.servlet;

import ru.kuleshov.sd.dao.ProductDao;
import ru.kuleshov.sd.html.ContentType;
import ru.kuleshov.sd.html.HtmlWriter;
import ru.kuleshov.sd.model.Product;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author akirakozov
 */
public class QueryServlet extends HttpServlet {
    private final ProductDao productDao;

    public QueryServlet(ProductDao productDao) {
        this.productDao = productDao;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String command = request.getParameter("command");
        HtmlWriter htmlWriter = new HtmlWriter(response.getWriter());

        switch (command) {
            case "max":
                Product maxPriceProduct = productDao.getMaxPriceProduct();

                htmlWriter.printlnStart();
                htmlWriter.printlnHeader("Product with max price: ");
                htmlWriter.printlnProduct(maxPriceProduct);
                htmlWriter.printlnEnd();
                break;
            case "min":
                Product minPriceProduct = productDao.getMinPriceProduct();

                htmlWriter.printlnStart();
                htmlWriter.printlnHeader("Product with min price: ");
                htmlWriter.printlnProduct(minPriceProduct);
                htmlWriter.printlnEnd();
                break;
            case "sum":
                int sum = productDao.getPricesSum();

                htmlWriter.printlnStart();
                htmlWriter.println("Summary price: ");
                htmlWriter.println(sum);
                htmlWriter.printlnEnd();
                break;
            case "count":
                int count = productDao.getProductCount();

                htmlWriter.printlnStart();
                htmlWriter.println("Number of products: ");
                htmlWriter.println(count);
                htmlWriter.printlnEnd();
                break;
            default:
                htmlWriter.println("Unknown command: " + command);
        }

        response.setContentType(ContentType.TEXT_HTML);
        response.setStatus(HttpServletResponse.SC_OK);
    }

}