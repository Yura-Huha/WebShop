package ua.edu.nung.pz.conroller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import ua.edu.nung.pz.dao.entity.Cart;
import ua.edu.nung.pz.dao.entity.Good;
import ua.edu.nung.pz.dao.entity.User;
import ua.edu.nung.pz.view.MainPage;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

@WebServlet(name = "CartServlet", urlPatterns = {"/cart/*"})
public class CartServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        HttpSession session = request.getSession(false);
        Cart cart = (session != null) ? (Cart) session.getAttribute("cart") : new Cart();

        int cartItemCount = (cart != null && cart.getGoods() != null) ? cart.getGoods().values().stream().mapToInt(Integer::intValue).sum() : 0;
        request.setAttribute("cartItemCount", cartItemCount);

        StringBuilder context = new StringBuilder("<h2>Cart!</h2>\n");
        if (cart != null && cart.getGoods() != null) {
            context.append("<div class='container'><div class='row'>");
            for (Map.Entry<Good, Integer> entry : cart.getGoods().entrySet()) {
                Good good = entry.getKey();
                Integer quantity = entry.getValue();
                context.append("<div class='col-12 col-sm-6 col-lg-4 col-xl-3 my-2'>")
                        .append("<div class='card'>")
                        .append("<img src='/img/").append(good.getPhoto().length > 0 ? good.getPhoto()[0] : "placeholder.jpg")
                        .append("' class='card-img-top' alt='").append(good.getName()).append("'>")
                        .append("<div class='card-body'>")
                        .append("<h5 class='card-title'>").append(good.getName()).append("</h5>")
                        .append("<p class='card-text'>Price: ").append(good.getPrice().getFor_client()).append(" UAH</p>")
                        .append("<p class='card-text'>Quantity: ").append(quantity).append("</p>")
                        .append("</div></div></div>");
            }
            context.append("</div></div>");
        } else {
            context.append("<div class='alert alert-warning'>Your cart is empty.</div>");
        }

        context.append("<script>")
                .append("document.addEventListener('DOMContentLoaded', function() {")
                .append("const cartItemCount = ").append(cartItemCount).append(";")
                .append("document.getElementById('cart-count').textContent = cartItemCount;")
                .append("});")
                .append("</script>");

        String builderPage = MainPage.Builder.newInstance()
                .setTitle("Green Shop")
                .setHeader("")
                .setBody(context.toString())
                .setFooter()
                .build()
                .getFullPage();

        out.println(builderPage);
    }
}