package filters;

import homeServlets.AuthenticationServlet;
import homeServlets.FileKeyStorage;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AuthenticationFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain filterChain) throws IOException, ServletException {
        String uidParam = req.getParameter(AuthenticationServlet.PARAM_UID);
        if (uidParam == null && req instanceof HttpServletRequest) {
            Cookie[] cookies = ((HttpServletRequest) req).getCookies();
            try {
                for (Cookie cookie : cookies) {
                    if (cookie.getName().equals(AuthenticationServlet.COOKIE_USER_ID)) {
                        uidParam = cookie.getValue();
                    }
                }
            } catch (NullPointerException e) {
            }
        }
        boolean valid = checkRequest(uidParam);
        if (valid) {
            filterChain.doFilter(req, resp);
        } else if (resp instanceof HttpServletResponse) {
            req.getRequestDispatcher("/unauthorized.html").forward(req, resp);
        } else {
            req.setAttribute("errorMsg", "403, Forbidden");
            req.getRequestDispatcher("/login.jsp").forward(req, resp);
        }
    }

    private boolean checkRequest(String uid) {
        return FileKeyStorage.getUserByUid(uid) != null;
    }

    @Override
    public void destroy() {

    }
}
