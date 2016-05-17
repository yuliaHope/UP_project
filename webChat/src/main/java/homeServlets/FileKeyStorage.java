package homeServlets;



import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class FileKeyStorage {

    private static final Map<String, String> userMap = new HashMap<String, String>();

    public static void input() throws IOException {
        BufferedReader br = new BufferedReader(new FileReader("C:\\webChat\\src\\main\\resources\\NameBase.txt"));
        String s;
        while ((s = br.readLine()) != null) {
            String[] strs = s.split("[\\s;]+");
            for (int i = 0; i < strs.length; ++i) {
                userMap.put(strs[++i], strs[i - 1]);
            }
        }
        br.close();
    }

    public static void output(String name, String password) throws IOException {
        FileWriter fw = new FileWriter("C:\\webChat\\src\\main\\resources\\NameBase.txt", true);
        fw.write(name + " " + password + ";\n");
        userMap.put(password, name);
        fw.close();
    }

    public static String getByUsername(String username) {
                return userMap.entrySet().stream()
                                .filter(v -> v.getValue().equals(username))
                                .map(Map.Entry::getKey)
                                .findAny()
                                .orElse(null);
            }

    public static String getUserByUid(String uid) {
        return userMap.get(uid);
    }
}
