package yulia;

import org.json.simple.JSONArray;

import java.io.IOException;
import java.io.StringWriter;

public class Write {
    public static void write(JSONFile jf, MessageHistory mh, String fileName)throws IOException {
        if(jf.openOutputFile(fileName)){
            if(mh.getList().size() == 0) {
                jf.printText("Message history is empty");
                jf.closeOutputFile();
                return;
            }
            JSONArray array = new JSONArray();
            for(Message item : mh.getList()) {
                array.add(item.convertToJson());
            }
            StringWriter sw = new StringWriter();
            array.writeJSONString(sw);
            jf.printText(sw.toString());
            jf.closeOutputFile();
        }
    }
}
