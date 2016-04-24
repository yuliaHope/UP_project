package by.bsu.up.chat.storage;

import by.bsu.up.chat.common.models.Message;
import by.bsu.up.chat.logging.Logger;
import by.bsu.up.chat.logging.impl.Log;
import by.bsu.up.chat.utils.MessageHelper;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import yulia.Read;
import yulia.WorkFile;
import yulia.Write;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class InFileMessageStorage implements MessageStorage {

    private static final String DEFAULT_MEMORY_FILE = "messages.json";

    private static final Logger logger = Log.create(InFileMessageStorage.class);

    private List<Message> messages = new ArrayList<>();

    private WorkFile wf = new WorkFile();

    @Override
    public void loadMessages(NameStorage nameStorage) {
        try {
            JSONArray array = (JSONArray) Read.read(DEFAULT_MEMORY_FILE, wf);
            List<Message> list = new ArrayList<>();
            for (Object o : array) {
                Message message = MessageHelper.JSONObjectToMessage((JSONObject) o);
                list.add(message);
                nameStorage.newUser(message.getAuthor(), message.getUserId());
            }
            messages.addAll(list);
        } catch (ParseException e) {
        } catch (IOException e) {
        }
    }

    @Override
    public synchronized List<Message> getPortion(Portion portion) {
        int from = portion.getFromIndex();
        if (from < 0) {
            throw new IllegalArgumentException(String.format("Portion from index %d can not be less then 0", from));
        }
        int to = portion.getToIndex();
        if (to != -1 && to < portion.getFromIndex()) {
            throw new IllegalArgumentException(String.format("Porting last index %d can not be less then start index %d", to, from));
        }
        to = Math.max(to, messages.size());
        return messages.subList(from, to);
    }

    @Override
    public void addMessage(Message message) {
        messages.add(message);
        try {
            Write.write(wf, MessageHelper.messageToJSONArray(messages), DEFAULT_MEMORY_FILE);
        } catch (IOException e) {
            logger.error("Could not open file", e);
        }
    }

    @Override
    public void updateMessage(Message message) {
        messages.add(message);
        try {
            Write.write(wf, MessageHelper.messageToJSONArray(messages), DEFAULT_MEMORY_FILE);
        } catch (IOException e) {
            logger.error("Could not open file", e);
        }
    }

    @Override
    public synchronized void removeMessage(String messageId) {
        for (Message item : messages) {
            if (item.getId().equals(messageId)) {
                Message removedMsg = new Message();
                removedMsg.setText("This message has been removed.");
                removedMsg.setRemoved(true);
                removedMsg.setUserId(item.getUserId());
                removedMsg.setId(messageId);
                removedMsg.setAuthor(item.getAuthor());
                messages.add(removedMsg);
                break;
            }
        }
        try {
            Write.write(wf, MessageHelper.messageToJSONArray(messages), DEFAULT_MEMORY_FILE);
        } catch (IOException e) {
            logger.error("Could not open file", e);
        }
    }

    @Override
    public int size() {
        return messages.size();
    }
}
