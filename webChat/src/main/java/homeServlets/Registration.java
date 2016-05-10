package homeServlets;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class Registration extends HttpServlet {
    private BufferedReader br;
    private FileWriter fw;
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

    public void output(String name, String password) throws IOException{
        fw = new FileWriter("C:\\webChat\\src\\main\\resources\\NameBase.txt", true);
        fw.write(name + " " + password + ";\n");
        fw.close();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        input();
        String name = req.getParameter("login");
        String inputPas = Encryption.encryptPassword(req.getParameter("pass"));
        String pas = nameMap.get(name);

        if (pas == null) {
            output(name, inputPas);
            resp.sendRedirect("/ConversationM.html");
        } else if (pas.equals(inputPas)) {
            req.setAttribute("errorMsg", "This name or password has employed!");
            req.getRequestDispatcher("/login.jsp").forward(req, resp);
        }
    }
}
