package yulia;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.IOException;
import java.text.ParseException;

public class Read {
    public static MessageHistory read(String fileName, JSONFile jf) throws IOException, org.json.simple.parser.ParseException, ParseException {
        MessageHistory mh = new MessageHistory();
        if (jf.openInputFile(fileName)) {
            JSONParser parser = new JSONParser();
            JSONArray array = (JSONArray) parser.parse(jf.read());
            for (Object o : array)
            {
                JSONObject msg = (JSONObject) o;
                mh.addMessage(new Message(msg));
            }
            jf.closeInputFile();
            return mh;
        }
        return null;
    }
}
