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

public class InMemoryMessageStorage implements MessageStorage {

    private static final String DEFAULT_MEMORY_FILE = "messages.json";

    private static final Logger logger = Log.create(InMemoryMessageStorage.class);

    private List<Message> messages = new ArrayList<>();

    private WorkFile wf = new WorkFile();

    @Override
    public void loadMessages() {
        try {
            JSONArray array = (JSONArray) Read.read(DEFAULT_MEMORY_FILE, wf);
            List<Message> list = new ArrayList<>();
            for (Object o : array) {
                list.add(MessageHelper.JSONObjectToMessage((JSONObject) o));
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
    public void addMessage(Message message){
        messages.add(message);
        try {
            Write.write(wf, MessageHelper.messageToJSONArray(messages), DEFAULT_MEMORY_FILE);
        } catch (IOException e) {
            logger.error("Could not open file", e);
        }
    }

    @Override
    public boolean updateMessage(Message message) {
        for(Message item: messages){
            if(item.getId().equals(message.getId())){
                item.setText(message.getText());
                item.setTimestamp(message.getTimestamp());
                try {
                    Write.write(wf, MessageHelper.messageToJSONArray(messages), DEFAULT_MEMORY_FILE);
                } catch (IOException e){
                    logger.error("Could not open file", e);
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public synchronized boolean removeMessage(String messageId){
        for (Message item : messages) {
            if (item.getId().equals(messageId)) {
                messages.remove(item);
                try {
                    Write.write(wf, MessageHelper.messageToJSONArray(messages), DEFAULT_MEMORY_FILE);
                } catch (IOException e){
                    logger.error("Could not open file", e);
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public int size() {
        return messages.size();
    }
}
