package ru.kuleshov.sd.servlet;

import ru.kuleshov.sd.dao.ProductDao;
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

        if ("max".equals(command)) {
            Product product = productDao.getMaxPriceProduct();

            response.getWriter().println("<html><body>");
            response.getWriter().println("<h1>Product with max price: </h1>");
            response.getWriter().println(product.getName() + "\t" + product.getPrice() + "</br>");
            response.getWriter().println("</body></html>");
        } else if ("min".equals(command)) {
            Product product = productDao.getMinPriceProduct();

            response.getWriter().println("<html><body>");
            response.getWriter().println("<h1>Product with min price: </h1>");
            response.getWriter().println(product.getName() + "\t" + product.getPrice() + "</br>");
            response.getWriter().println("</body></html>");
        } else if ("sum".equals(command)) {
            int sum = productDao.getPricesSum();

            response.getWriter().println("<html><body>");
            response.getWriter().println("Summary price: ");
            response.getWriter().println(sum);
            response.getWriter().println("</body></html>");

        } else if ("count".equals(command)) {
            int count = productDao.getProductCount();

            response.getWriter().println("<html><body>");
            response.getWriter().println("Number of products: ");
            response.getWriter().println(count);
            response.getWriter().println("</body></html>");
        } else {
            response.getWriter().println("Unknown command: " + command);
        }

        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
    }

}