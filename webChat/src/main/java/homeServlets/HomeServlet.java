package homeServlets;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class HomeServlet extends HttpServlet {

    private BufferedReader br;
    private Map<String, String> nameMap = new HashMap<String, String>();

    public void input() throws IOException {
        br = new BufferedReader(new FileReader("C:\\webChat\\src\\main\\resources\\NameBase.txt"));
        String s;
        while ((s = br.readLine()) != null) {
            String[] strs = s.split("[\\s;]+");
            for (int i = 0; i < strs.length; ++i) {
                nameMap.put(strs[i], strs[++i]);
            }
        }
        br.close();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        input();
        String name = req.getParameter("login");
        String inputPas = Encryption.encryptPassword(req.getParameter("pass"));
        String pas = nameMap.get(name);


        if (pas == null) {
            req.setAttribute("errorMsg", "This user doesn't registrate");
            req.getRequestDispatcher("/login.jsp").forward(req, resp);
            System.out.println(pas);
        } else if (pas.equals(inputPas)) {
            resp.sendRedirect("/ConversationM.html");
        } else {
            req.setAttribute("errorMsg", "Incorrect password");
            req.getRequestDispatcher("/login.jsp").forward(req, resp);
        }
    }
}

