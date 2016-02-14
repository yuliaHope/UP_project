package yulia;

import org.apache.log4j.Logger;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MessageHistory {
    private List<Message> list;

    public MessageHistory() {
        list = new ArrayList<Message>();
    }

    public void addMessage(Message msg) {
        list.add(msg);
    }

    public void addMessages(MessageHistory mh) {
        try {
            list.addAll(mh.getList());
        } catch (NullPointerException e) {

        }
    }

    public void deleteMessageId(String id, Logger LOG) {
        for (Message item : list) {
            if (item.getId().equals(id)) {
                LOG.info("Delete message.\n" + item);
                list.remove(item);
                break;
            }
        }
        LOG.info("Failed to delete the message. This id was not found");
    }

    public void sortChronological() {
        list.sort((a, b) -> (a.getTimestamp().compareTo(b.getTimestamp())));
    }

    public List<Message> historyPeriod(String from, String to) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat();
        format.applyPattern("dd.MM.yyyy hh:mm");
        Date fromD = format.parse(from);
        Date toD = format.parse(to);
        List<Message> periodList = new ArrayList<Message>();
        for (Message item : list) {
            if (item.getTimestamp().compareTo(fromD) >= 0 && item.getTimestamp().compareTo(toD) <= 0)
                periodList.add(item);
        }
        return periodList;
    }

    public List<Message> searchAuthor(String author) {
        List<Message> authorList = new ArrayList<Message>();
        for (Message item : list) {
            if (item.getAuthor().equals(author))
                authorList.add(item);
        }
        return authorList;
    }

    public List<Message> searchToken(String token) {
        List<Message> authorList = new ArrayList<Message>();
        for (Message item : list) {
            if (item.getMessage().contains(token))
                authorList.add(item);
        }
        return authorList;
    }

    public List<Message> searchRejex(String rejex) {
        List<Message> rejexList = new ArrayList<Message>();
        Pattern p = Pattern.compile(rejex);
        for (Message item : list) {
            Matcher m = p.matcher(item.getMessage());
            if (m.find())
                rejexList.add(item);
        }
        return rejexList;
    }


    public List<Message> getList() {
        return list;
    }
}
