package ru.kuleshov.sd;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import ru.kuleshov.sd.dao.ProductDao;
import ru.kuleshov.sd.servlet.AddProductServlet;
import ru.kuleshov.sd.servlet.GetProductsServlet;
import ru.kuleshov.sd.servlet.QueryServlet;

/**
 * @author akirakozov
 */
public class Main {
    public static void main(String[] args) throws Exception {
        ProductDao productDao = new ProductDao("jdbc:sqlite:test.db");

        Server server = new Server(8081);

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        server.setHandler(context);

        context.addServlet(new ServletHolder(new AddProductServlet(productDao)), "/add-product");
        context.addServlet(new ServletHolder(new GetProductsServlet(productDao)), "/get-products");
        context.addServlet(new ServletHolder(new QueryServlet(productDao)), "/query");

        server.start();
        server.join();
    }
}
