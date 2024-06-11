package ua.edu.nung.pz.view;

public class IndexView {
    public static String getPage(String title, String body) {
        return "<!doctype html>\r\n" + //
                "<html lang=\"en\">\r\n" + //
                "  <head>\r\n" + //
                "    <meta charset=\"utf-8\">\r\n" + //
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">\r\n" + //
                "    <title>" + title + "</title>\r\n" + //
                "    <link href=\"https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css\" rel=\"stylesheet\" integrity=\"sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH\" crossorigin=\"anonymous\">\r\n"
                + //
                "  </head>\r\n" + //
                "  <body>\r\n" + //
                body +
                "    <script src=\"https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js\" integrity=\"sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz\" crossorigin=\"anonymous\"></script>\r\n"
                + //
                "  </body>\r\n" + //
                "</html>";
    }
}
