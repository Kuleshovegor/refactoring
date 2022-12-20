package ru.kuleshov.sd.html;

import ru.kuleshov.sd.model.Product;

import java.io.PrintWriter;

public class HtmlWriter {
    private final PrintWriter writer;

    public HtmlWriter(PrintWriter writer) {
        this.writer = writer;
    }

    public void printlnStart() {
        writer.println("<html><body>");
    }

    public void printlnEnd() {
        writer.println("</body></html>");
    }

    public void printlnProduct(Product product) {
        writer.println(product.getName() + "\t" + product.getPrice() + "</br>");
    }

    public void printlnHeader(String string) {
        writer.println("<h1>" + string + "</h1>");
    }

    public void println(String string) {
        writer.println(string);
    }

    public void println(int number) {
        writer.println(number);
    }
}
