package ru.kuleshov.sd.servlet;

import ru.kuleshov.sd.dao.ProductDao;
import ru.kuleshov.sd.html.ContentType;
import ru.kuleshov.sd.model.Product;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author akirakozov
 */
public class AddProductServlet extends HttpServlet {
    private final ProductDao productDao;

    public AddProductServlet(ProductDao productDao) {
        this.productDao = productDao;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String name = request.getParameter("name");
        long price = Long.parseLong(request.getParameter("price"));

        productDao.addProduct(new Product(name, price));

        response.setContentType(ContentType.TEXT_HTML);
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().println("OK");
    }
}