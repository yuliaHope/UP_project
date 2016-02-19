package yulia;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MessageHistory {
    private List<Message> messagesList;

    public MessageHistory() {
        messagesList = new ArrayList<>();
    }

    public void addMessage(Message msg) {
        messagesList.add(msg);
    }

    public void addMessages(MessageHistory msgHistory) {
        try {
            messagesList.addAll(msgHistory.getMessagesList());
        } catch (NullPointerException ignored) {

        }
    }

    public boolean deleteMessageId(String id) {
        for (Message item : messagesList) {
            if (item.getId().equals(id)) {
                messagesList.remove(item);
                return true;
            }
        }
        return false;
    }

    public void sortChronological() {
        messagesList.sort((a, b) -> (a.getTimestamp().compareTo(b.getTimestamp())));
    }

    public List<Message> historyPeriod(String dateFrom, String dateTo) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat();
        format.applyPattern("dd.MM.yyyy hh:mm");
        Date fromD = format.parse(dateFrom);
        Date toD = format.parse(dateTo);
        List<Message> periodList = new ArrayList<>();
        for (Message item : messagesList) {
            if (item.getTimestamp().compareTo(fromD) >= 0 && item.getTimestamp().compareTo(toD) <= 0)
                periodList.add(item);
        }
        return periodList;
    }

    public List<Message> searchAuthor(String author) {
        List<Message> authorList = new ArrayList<>();
        for (Message item : messagesList) {
            if (item.getAuthor().equals(author))
                authorList.add(item);
        }
        return authorList;
    }

    public List<Message> searchToken(String token) {
        List<Message> authorList = new ArrayList<>();
        for (Message item : messagesList)
            if (item.getMessageText().contains(token))
                authorList.add(item);
        return authorList;
    }

    public List<Message> searchRejex(String rejex) {
        List<Message> rejexList = new ArrayList<>();
        Pattern p = Pattern.compile(rejex);
        for (Message item : messagesList) {
            Matcher m = p.matcher(item.getMessageText());
            if (m.find())
                rejexList.add(item);
        }
        return rejexList;
    }


    public List<Message> getMessagesList() {
        return messagesList;
    }
}
