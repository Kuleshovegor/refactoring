package ru.kuleshov.sd.servlet;

import ru.kuleshov.sd.dao.ProductDao;
import ru.kuleshov.sd.html.ContentType;
import ru.kuleshov.sd.html.HtmlWriter;
import ru.kuleshov.sd.model.Product;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * @author akirakozov
 */
public class GetProductsServlet extends HttpServlet {
    private final ProductDao productDao;

    public GetProductsServlet(ProductDao productDao) {
        this.productDao = productDao;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        List<Product> products = productDao.getAllProducts();
        HtmlWriter htmlWriter = new HtmlWriter(response.getWriter());

        htmlWriter.printlnStart();
        for (Product product : products) {
            htmlWriter.printlnProduct(product);
        }
        htmlWriter.printlnEnd();

        response.setContentType(ContentType.TEXT_HTML);
        response.setStatus(HttpServletResponse.SC_OK);
    }
}