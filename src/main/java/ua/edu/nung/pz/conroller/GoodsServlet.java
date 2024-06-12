package ua.edu.nung.pz.conroller;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import ua.edu.nung.pz.dao.entity.Cart;
import ua.edu.nung.pz.dao.entity.Good;
import ua.edu.nung.pz.dao.entity.Order;
import ua.edu.nung.pz.dao.entity.User;
import ua.edu.nung.pz.dao.repository.GoodRepository;
import ua.edu.nung.pz.dao.repository.OrderRepository;
import ua.edu.nung.pz.view.MainPage;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Collectors;

@WebServlet(name = "GoodsServlet", urlPatterns = {"/goods/*"})
public class GoodsServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        HttpSession session = request.getSession(false);
        User user = null;
        String userName = "";
        if (session != null) {
            user = (User) session.getAttribute(User.USER_SESSION_NAME);
            userName = user == null ? "" : user.getDisplayName();
        }

        Cart cart = addItem(request, response, user);

        GoodRepository goodRepository = new GoodRepository();
        ArrayList<Good> goods = goodRepository.getByBrand("Naturalis");

        String body = goods.stream().map(good -> {
            String goodId = String.valueOf(good.getId());
            return "<div class=\"col-12 col-sm-6 col-lg-4 col-xl-3 my-2\">" +
                    "<div class=\"card\">\n" +
                    "<div id=\"carouselExample" + goodId + "\" class=\"carousel slide\">" +
                    "<div class=\"carousel-inner\">" +
                    "<div class=\"carousel-item active\">" +
                    "<img src=\"/img/" + (good.getPhoto().length > 0 ? good.getPhoto()[0] : "") + "\" class=\"d-block w-100\" alt=\"good image\">" +
                    "</div>" +
                    "<div class=\"carousel-item\">" +
                    "<img src=\"/img/" + (good.getPhoto().length > 1 ? good.getPhoto()[1] : "") + "\" class=\"d-block w-100\" alt=\"good image\">" +
                    "</div>" +
                    "</div>" +
                    "<button class=\"carousel-control-prev\" type=\"button\" data-bs-target=\"#carouselExample" + goodId + "\" data-bs-slide=\"prev\"style=\"color: red;\">" +
                    "<span class=\"carousel-control-prev-icon\" aria-hidden=\"true\"></span>" +
                    "<span class=\"visually-hidden\">Previous</span>" +
                    "</button>" +
                    "<button class=\"carousel-control-next\" type=\"button\" data-bs-target=\"#carouselExample" + goodId + "\" data-bs-slide=\"next\"style=\"color: red;\">" +
                    "<span class=\"carousel-control-next-icon\" aria-hidden=\"true\"></span>" +
                    "<span class=\"visually-hidden\">Next</span>" +
                    "</button>" +
                    "</div>" +
                    "<style>\n" +
                    "    .carousel-control-prev-icon,\n" +
                    "    .carousel-control-next-icon {\n" +
                    "        background-color: green; /* Додали !important для переваги цього стилю над можливими іншими */\n" +
                    "    }\n" +
                    "</style>"+
                    "  <div class=\"card-body\">\n" +
                    "    <h5 class=\"card-title position-relative me-4\">" + good.getName() +
                    "<span class=\"position-absolute top-0 start-100 translate-middle badge rounded-pill text-body\">" +
                    "<i class=\"bi-heart-fill me-1 text-danger\"></i>" +
                    good.getLikes() +
                    "</span>" +
                    "</h5>\n" +
                    "    <h6 class=\"card-subtitle mb-2 text-body-secondary\">Price:" + good.getPrice().getFor_client() + " UAH</h6>\n" +
                    "    <p class=\"card-text\">" + good.getShortDescription() + "</p>\n" +
                    "<a href=\"/goods/add-item?priceid=" + good.getPrice().getId() + "\" class=\"btn btn-success\">\n" +
                    "   <svg xmlns=\"http://www.w3.org/2000/svg\" width=\"16\" height=\"16\" fill=\"currentColor\" class=\"bi bi-bag-plus\" viewBox=\"0 0 16 16\">\n" +
                    "  <path fill-rule=\"evenodd\" d=\"M8 7.5a.5.5 0 0 1 .5.5v1.5H10a.5.5 0 0 1 0 1H8.5V12a.5.5 0 0 1-1 0v-1.5H6a.5.5 0 0 1 0-1h1.5V8a.5.5 0 0 1 .5-.5\"></path>\n" +
                    "  <path d=\"M8 1a2.5 2.5 0 0 1 2.5 2.5V4h-5v-.5A2.5 2.5 0 0 1 8 1m3.5 3v-.5a3.5 3.5 0 1 0-7 0V4H1v10a2 2 0 0 0 2 2h10a2 2 0 0 0 2-2V4zM2 5h12v9a1 1 0 0 1-1 1H3a1 1 0 0 1-1-1\"></path>\n" +
                    "</svg>\n" +
                    "                Add\n" +
                    "              </a>" +
                    "  </div>\n" +
                    "</div>"
                    + "</div>";
        }).collect(Collectors.joining());

        body = "<div class=\"container-fluid\"> <div class=\"row\">" + body + "</div> </div>";

        String builderPage = MainPage.Builder.newInstance()
                .setTitle("Green Shop")
                .setHeader(userName)
                .setBody(body)
                .setFooter()
                .build()
                .getFullPage();

        out.println(builderPage);

        out.println("<script>");
        out.println("document.addEventListener('DOMContentLoaded', function() {");
        out.println("const cartItemCount = " + getCartItemCount(session) + ";");
        out.println("document.getElementById('cart-count').textContent = cartItemCount;");
        out.println("});");
        out.println("</script>");
    }


    private Cart addItem(HttpServletRequest request, HttpServletResponse response, User user) throws IOException {
        Cart cart = (Cart) request.getSession().getAttribute("cart");
        if (cart == null) {
            cart = new Cart();
            request.getSession().setAttribute("cart", cart);
        }

        String priceStr = request.getParameter("priceid");
        if (priceStr != null) {
            long priceId = 0L;
            try {
                priceId = Long.parseLong(priceStr);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
            if(user != null) {

                Order order = new Order(
                        0l,
                        user.getId(),
                        priceId,
                        false,
                        "2024-04-29",
                        null
                );
                System.out.println("order " + order);
                OrderRepository orderRepository = new OrderRepository();
                orderRepository.saveOrUpdate(order);
            }
            if (priceId > 0) {
                GoodRepository goodRepository = new GoodRepository();
                Good good = goodRepository.findById(priceId);

                if (good != null) {
                    int quantity = 1; // default quantity
                    for (Map.Entry<Good, Integer> entry : cart.getGoods().entrySet()) {
                        Good cartGood = entry.getKey();
                        if (cartGood.getId() == good.getId()) {
                            quantity = entry.getValue() + 1; // increase quantity if the good is already in the cart
                            break;
                        }
                    }
                    cart.getGoods().put(good, quantity); // add the good with the new quantity
                    request.getSession().setAttribute("cart", cart);
                }

                response.sendRedirect("/goods/");
            }
        }

        return cart;
    }
    private int getCartItemCount(HttpSession session) {
        if (session != null) {
            User user = (User) session.getAttribute(User.USER_SESSION_NAME);
            if (user != null) {
                Cart cart = (Cart) session.getAttribute("cart");
                if (cart != null && cart.getGoods() != null) {
                    return cart.getGoods().values().stream().mapToInt(Integer::intValue).sum();
                }
            }
        }
        return 0;
    }
}