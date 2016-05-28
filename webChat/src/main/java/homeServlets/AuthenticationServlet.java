package homeServlets;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AuthenticationServlet extends HttpServlet {
    private static final String PARAM_USERNAME = "login";
    public static final String COOKIE_USER_ID = "pass";
    public static final String PARAM_UID = COOKIE_USER_ID;

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
            req.setAttribute("errorMsg", "This user doesn't registrate");
            req.getRequestDispatcher("/login.jsp").forward(req, resp);
        } else if (nameFromPas.equals(name)) {
            Cookie userIdCookie = new Cookie(COOKIE_USER_ID, pas);
            Cookie userNameCookie = new Cookie(PARAM_USERNAME, name);
            userIdCookie.setMaxAge(cookieLifeTime);
            resp.addCookie(userIdCookie);
            resp.addCookie(userNameCookie);
            resp.sendRedirect("/ConversationM.html");
        } else {
            req.setAttribute("errorMsg", "Incorrect password or name");
            req.getRequestDispatcher("/login.jsp").forward(req, resp);
        }
    }
}
