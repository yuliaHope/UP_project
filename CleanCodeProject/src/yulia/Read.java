package yulia;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.io.IOException;
import java.io.StringReader;
import java.text.ParseException;

public class Read {
    public static MessageHistory read(String fileName, JSONFile jf) throws IOException, ParseException {
        MessageHistory mh = new MessageHistory();
        if (jf.openInputFile(fileName)) {
            JsonReader reader = Json.createReader(new StringReader(jf.read()));
            JsonObject object;
            while((object = reader.readObject()) != null) {
                mh.addMessage(jf.input(object));
            }
            reader.close();
            jf.closeInputFile();
            return mh;
        }
        return null;
    }
}
