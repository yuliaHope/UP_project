package yulia;

import org.apache.commons.lang3.RandomStringUtils;
import org.json.simple.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Message {
    private String id;
    private String messageText;
    private String author;
    private Date timestamp;

    public Message(String author, String messageText) {
        id = RandomStringUtils.random(32, 0, 20, true, true, "qw32rfHIJk9iQ8Ud7h0X".toCharArray());
        this.messageText = messageText;
        this.author = author;
        timestamp = new Date();
    }

    public Message(JSONObject object) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy hh:mm");
        id = (String) object.get("id");
        author = (String) object.get("Author");
        messageText = (String) object.get("Message");
        timestamp = format.parse((String) object.get("Timestamp"));
    }

    @SuppressWarnings("unchecked")
    public JSONObject convertToJson() {
        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy hh:mm");
        JSONObject object = new JSONObject();
        object.put("id", id);
        object.put("Author", author);
        object.put("Message", messageText);
        object.put("Timestamp", format.format(timestamp));
        return object;
    }

    public String getId() {
        return id;
    }

    public String getMessageText() {
        return messageText;
    }

    public String getAuthor() {
        return author;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return id + "\n" + author + "\n" +
                messageText + "\n" + timestamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Message)) return false;

        Message message = (Message) o;

        return id != null ? id.equals(message.id) : message.id == null;

    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
