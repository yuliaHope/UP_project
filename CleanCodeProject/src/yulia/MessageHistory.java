package yulia;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MessageHistory {
    private List<Message> list;

    public MessageHistory() {
        list = new ArrayList<Message>();
    }

    public MessageHistory(List<Message> list) {
        this.list = new ArrayList<Message>(list);
    }

    public void addMessage(Message msg) {
        list.add(msg);
    }

    public void addMessages(MessageHistory mh){
        list.addAll(mh.getList());
    }

    public void cleanMessageHistory() {
        list.clear();
    }

    public void deleteMessageId(String id) {
        list.remove(id);
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
            if (item.getTimestamp().compareTo(fromD) > 0 && item.getTimestamp().compareTo(toD) < 0)
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

    public List<Message> searchRejex(String token) {
        List<Message> rejexList = new ArrayList<Message>();
        for (Message item : list) {
            if (item.getMessage().matches(token))
                rejexList.add(item);
        }
        return rejexList;
    }


    public List<Message> getList() {
        return list;
    }
}
