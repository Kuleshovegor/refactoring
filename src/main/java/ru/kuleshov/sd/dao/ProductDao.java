package ru.kuleshov.sd.dao;

import ru.kuleshov.sd.model.Product;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ProductDao {
    private static final String NAME_COLUMN = "name";
    private static final String PRICE_COLUMN = "price";

    private final String url;

    public ProductDao(String url) {
        this.url = url;
        createProductTable();
    }

    private void createProductTable() {
        try (Connection c = DriverManager.getConnection(url)) {
            String sql = "CREATE TABLE IF NOT EXISTS PRODUCT" +
                    "(ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                    " NAME           TEXT    NOT NULL, " +
                    " PRICE          INT     NOT NULL)";
            Statement stmt = c.createStatement();

            stmt.executeUpdate(sql);
            stmt.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void addProduct(Product product) {
        try (Connection c = DriverManager.getConnection(url)) {
            String sql = "INSERT INTO PRODUCT " +
                    "(" + NAME_COLUMN + ", " + PRICE_COLUMN + ") VALUES (\"" + product.getName() + "\"," + product.getPrice() + ")";
            Statement stmt = c.createStatement();
            stmt.executeUpdate(sql);
            stmt.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Product> getAllProducts() {
        try (Connection c = DriverManager.getConnection(url)) {
            List<Product> products = new ArrayList<>();
            Statement stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM PRODUCT");

            while (rs.next()) {
                String name = rs.getString(NAME_COLUMN);
                int price = rs.getInt(PRICE_COLUMN);
                products.add(new Product(name, price));
            }

            rs.close();
            stmt.close();

            return products;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Product getMinPriceProduct() {
        return getFirstProduct("SELECT * FROM PRODUCT ORDER BY PRICE LIMIT 1");
    }

    public Product getMaxPriceProduct() {
        return getFirstProduct("SELECT * FROM PRODUCT ORDER BY PRICE DESC LIMIT 1");
    }

    public int getPricesSum() {
        try (Connection c = DriverManager.getConnection(url)) {
            Statement stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT SUM(price) FROM PRODUCT");

            rs.next();
            int sum = rs.getInt(1);

            rs.close();
            stmt.close();

            return sum;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public int getProductCount() {
        try (Connection c = DriverManager.getConnection(url)) {
            Statement stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM PRODUCT");

            rs.next();
            int sum = rs.getInt(1);

            rs.close();
            stmt.close();

            return sum;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Product getFirstProduct(String sql) {
        try (Connection c = DriverManager.getConnection(url)) {
            Statement stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            rs.next();
            String name = rs.getString(NAME_COLUMN);
            int price = rs.getInt(PRICE_COLUMN);
            Product product = new Product(name, price);

            rs.close();
            stmt.close();

            return product;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
