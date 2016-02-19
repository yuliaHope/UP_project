package yulia;

import org.json.simple.JSONArray;

import java.io.IOException;
import java.io.StringWriter;

public class Write {
    @SuppressWarnings("unchecked")
    public static void write(WorkFile workFile, MessageHistory msgHistory, String fileName)throws IOException {
        if(workFile.openOutputFile(fileName)){
            if(msgHistory.getMessagesList().size() == 0) {
                workFile.printText("Message history is empty");
                workFile.closeOutputFile();
                return;
            }
            JSONArray array = new JSONArray();
            for(Message item : msgHistory.getMessagesList()) {
                array.add(item.convertToJson());
            }
            StringWriter sw = new StringWriter();
            array.writeJSONString(sw);
            workFile.printText(sw.toString());
            workFile.closeOutputFile();
        }
    }
}
