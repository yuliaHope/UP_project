package homeServlets;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class Registration extends HttpServlet {
    private static final String PARAM_USERNAME = "login";
    public static final String COOKIE_USER_ID = "pass";

    private int cookieLifeTime = 30;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        FileKeyStorage.input();
        String name = req.getParameter(PARAM_USERNAME);
        String pas = Encryption.encryptPassword(req.getParameter(COOKIE_USER_ID));
        String nameFromPas = FileKeyStorage.getUserByUid(pas);

        if (nameFromPas == null) {
            FileKeyStorage.output(name, pas);
            Cookie userIdCookie = new Cookie(COOKIE_USER_ID, pas);
            Cookie userNameCookie = new Cookie(PARAM_USERNAME, name);
            userIdCookie.setMaxAge(cookieLifeTime);
            resp.addCookie(userIdCookie);
            resp.addCookie(userNameCookie);
            resp.sendRedirect("/ConversationM.html");
        } else if (nameFromPas.equals(name)) {
            req.setAttribute("errorMsg", "This name or password has employed!");
            req.getRequestDispatcher("/login.jsp").forward(req, resp);
        }
    }
}