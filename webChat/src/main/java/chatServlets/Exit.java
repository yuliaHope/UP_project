package chatServlets;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class Exit extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        Cookie[] cookies = req.getCookies();
//        try {
//            for (Cookie cookie : cookies) {
//                cookie.setValue(null);
//                cookie.setPath("/");
//                cookie.setMaxAge(0);
//                resp.addCookie(cookie);
//            }
//        } catch (NullPointerException e){}

        resp.sendRedirect("/login.jsp");
    }
}
