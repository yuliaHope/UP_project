package yulia;

import org.apache.commons.lang3.RandomStringUtils;
import org.json.simple.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Message {
    private String id;
    private String message;
    private String author;
    private Date timestamp;

    public Message(String author, String message) {
        id = RandomStringUtils.random(32, 0, 20, true, true, "qw32rfHIJk9iQ8Ud7h0X".toCharArray());
        this.message = new String(message);
        this.author = new String(author);
        timestamp = new Date();
    }

    public Message(JSONObject object) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy hh:mm");
        id = (String) object.get("id");
        author = (String) object.get("Author");
        message = (String) object.get("Message");
        timestamp = format.parse((String)object.get("Timestamp"));
    }

    public JSONObject convertToJson() {
        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy hh:mm");
        JSONObject object = new JSONObject();
        object.put("id", id);
        object.put("Author", author);
        object.put("Message", message);
        object.put("Timestamp", format.format(timestamp));
        return object;
    }

    public String getId() {
        return id;
    }

    public String getMessage() {
        return message;
    }

    public String getAuthor() {
        return author;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(id).append("\n").append(author).append("\n");
        sb.append(message).append("\n").append(timestamp);
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        Message messageId = (Message) o;
        return id.equals(messageId.getId());
    }

}
