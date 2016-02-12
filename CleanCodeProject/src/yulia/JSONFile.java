package yulia;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonWriter;
import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class JSONFile {
    private BufferedReader br;
    private PrintWriter pw;

    public boolean openInputFile(String fileName) {
        try {
            br = new BufferedReader(new FileReader(fileName));
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public boolean openOutputFile(String fileName) {
        try {
            pw = new PrintWriter(fileName);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public void closeOutputFile() {
        pw.close();
    }

    public void closeInputFile() throws IOException{
        br.close();
    }

    public String read() throws IOException {
        String s;
        StringBuilder sb = new StringBuilder();
        while ((s = br.readLine()) != null) {
            sb.append(s);
        }
        return sb.toString();
    }

    public void output(Message msg) {
        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy hh:mm");
        JsonObject object = Json.createObjectBuilder().add("id", msg.getId())
                .add("Author", msg.getAuthor())
                .add("Message", msg.getMessage())
                .add("Timestamp", format.format(msg.getTimestamp()))
                .build();
        StringWriter buffer = new StringWriter();
        JsonWriter writer = Json.createWriter(buffer);
        writer.writeObject(object);
        writer.close();
        pw.print(buffer.getBuffer().toString());
    }

    public Message input(/*String msgJSONInfo*/JsonObject object) throws ParseException{
//        JsonReader reader = Json.createReader(new StringReader(msgJSONInfo));
//        JsonObject object = reader.readObject();
//        reader.close();
        SimpleDateFormat format = new SimpleDateFormat();
        format.applyPattern("dd.MM.yyyy hh:mm");
        Date timestamp= format.parse(object.getString("Timestamp"));
        Message msg = new Message(object.getInt("id"), object.getString("Author"), object.getString("Message"), timestamp);
        return msg;
    }
}
