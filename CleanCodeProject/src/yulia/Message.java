package yulia;

import java.util.Date;

public class Message {
    private static int id;
    private String message;
    private String author;
    private Date timestamp;

    public Message(String message, String author) {
        ++id;
        this.message = new String(message);
        this.author = new String(author);
        timestamp = new Date();
    }

    public Message(int id, String message, String author, Date timestamp) {
        this.id = id;
        this.message = new String(message);
        this.author = new String(author);
        this.timestamp = timestamp;
    }

    public int getId() {
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
        sb.append(id).append("\n").append(message).append("\n");
        sb.append(author).append("\n").append(timestamp);
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        int messageId = (int) o;
        return (id - messageId) != 0 ? false : true;
    }

}
