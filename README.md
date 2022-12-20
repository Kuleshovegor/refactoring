# Рефакторинг
Были добавлены тесты, затем проведен рефакторинг с сохранением работы тех же тестов, т.е.
изменение поведения не произошло, была добавлена модель [Product](src/main/java/ru/kuleshov/sd/model/Product.java),
выделен отдельный слой работы с базой данных [ProductDao](src/main/java/ru/kuleshov/sd/dao/ProductDao.java), добавлен
[HtmlWriter](src/main/java/ru/kuleshov/sd/html/HtmlWriter.java) для общей работы с HTML.

В [QueryServlet](src/main/java/ru/kuleshov/sd/servlet/QueryServlet.java) для обработки разных команд
код выглядит схожим, но вынести его не получится, потому что идет наполнение разным контентом,
и также он оставлен для сохранения поведения ```pritln()```, так как после каждого из них происходит ```flush()```.