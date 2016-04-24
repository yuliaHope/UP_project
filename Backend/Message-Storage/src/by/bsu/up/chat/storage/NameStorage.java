package by.bsu.up.chat.storage;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class NameStorage {
    private FileWriter fw;
    private Map<String, String> nameMap;

    public NameStorage() {
        nameMap = new HashMap<>();
        try {
            fw = new FileWriter("names.txt");
        } catch (IOException e) {
        }
    }

    public String newUser(String name, String id) {
        String userId = nameMap.get(name);
        if (userId == null) {
            userId = "-1";
            if(id.equals("")){
                return userId;
            }
            nameMap.put(name, id);
            try {
                fw.write(name + " " + id + ", ");
                fw.flush();
            } catch (IOException e) {
                System.err.println(e.getMessage());
            }
        }
        return userId;
    }
}
