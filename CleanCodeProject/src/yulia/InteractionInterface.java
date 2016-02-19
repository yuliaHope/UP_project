package yulia;

import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.util.List;


public class InteractionInterface {
    private BufferedReader br;
    private MessageHistory msgHistory;
    private WorkFile workFile;
    private static final Logger LOG = Logger.getLogger(InteractionInterface.class);

    public InteractionInterface() {
        br = new BufferedReader(new InputStreamReader(System.in));
        msgHistory = new MessageHistory();
        workFile = new WorkFile();

    }

    public void printFacilities() {
        System.out.println("Enter number from 0 to 9");
        System.out.println("1 - add message");
        System.out.println("2 - remove using id");
        System.out.println("3 - view message history in chronological order");
        System.out.println("4 - find message by author");
        System.out.println("5 - find message by key word");
        System.out.println("6 - find message by regex");
        System.out.println("7 - view message history for a certain period");
        System.out.println("8 - save messages in file");
        System.out.println("9 - download messages from file");
        System.out.println("0 - exit");
    }

    public void logErr(String str) {
        LOG.error(str);
    }

    public void addMsg() throws IOException {
        System.out.println("Enter author, then message");
        Message msg = new Message(br.readLine(), br.readLine());
        msgHistory.addMessage(msg);
        LOG.info("Add message.\n" + msg);
    }

    public void removeId() throws IOException {
        System.out.println("Enter id");
        String id = br.readLine();
        if (msgHistory.deleteMessageId(id)) {
            LOG.info("Delete message with id: " + id);
        } else
            LOG.warn("Failed to delete the message. This id was not found");
    }

    public void viewChronological() throws IOException {
        if (msgHistory.getMessagesList().size() == 0) {
            LOG.warn("message history is empty");
            return;
        }
        msgHistory.sortChronological();
        LOG.info("view message history in chronological order");
        for (Message item : msgHistory.getMessagesList()) {
            System.out.println(item);
        }
    }

    public void viewCertainPeriod() throws IOException {
        System.out.println("Enter period from ... to ...(format: dd.MM.yyyy hh:mm)");
        String dateFrom = br.readLine();
        String dateTo = br.readLine();
        List<Message> periodList;
        try {
            periodList = msgHistory.historyPeriod(dateFrom, dateTo);
            for (Message item : periodList) {
                System.out.println(item);
            }
            LOG.info("view message history from " + dateFrom + " to " + dateTo);
        } catch (ParseException e) {
            LOG.error("Invalid date format");
        } catch (NullPointerException e) {
            LOG.warn("message history from " + dateFrom + " to " + dateTo + " was not found");
        }
    }

    public void findAuthor() throws IOException {
        System.out.println("Enter author");
        String author = br.readLine();
        List<Message> authorList = msgHistory.searchAuthor(author);
        if (authorList.size() == 0) {
            LOG.warn("Find message by author:" + author + " was not found");
            return;
        }
        for (Message item : authorList) {
            System.out.println(item);
            LOG.info("Find message by author:" + author + " " + item);
        }
    }

    public void findToken() throws IOException {
        System.out.println("Enter token");
        String token = br.readLine();
        List<Message> tokenList = msgHistory.searchToken(token);
        if (tokenList.size() == 0) {
            LOG.warn("Find message by token:" + token + " was not found");
            return;
        }
        for (Message item : tokenList) {
            System.out.println(item);
            LOG.info("Find message by token:" + token + " " + item);
        }
    }

    public void findRejex() throws IOException {
        System.out.println("Enter rejex");
        String rejex = br.readLine();
        List<Message> rejexList = msgHistory.searchRejex(rejex);
        if (rejexList.size() == 0) {
            LOG.warn("Find message by rejex:" + rejex + " was not found");
            return;
        }
        for (Message item : rejexList) {
            System.out.println(item);
            LOG.info("Find message by token:" + rejex + " " + item);
        }
    }

    public void readFile() throws IOException, ParseException, org.json.simple.parser.ParseException {
        System.out.println("Input fileName (*.json)");
        String fileName = br.readLine();
        msgHistory.addMessages(Read.read(fileName, workFile));
        LOG.info("read messages from " + fileName);

    }

    public void saveFile() throws IOException {
        System.out.println("Input fileName (*.json)");
        String fileName = br.readLine();
        Write.write(workFile, msgHistory, fileName);
        LOG.info("Save messages in " + fileName);

    }

    public void workFile(int answer) throws IOException, ParseException, org.json.simple.parser.ParseException {
        switch (answer) {
            case 9: {
                readFile();
                break;
            }
            case 8: {
                saveFile();
                break;
            }
        }
        System.out.println("Enter the operation number");
    }

    public void viewHistory(int answer) throws IOException, ParseException {
        switch (answer) {
            case 3: {
                viewChronological();
                break;
            }
            case 7: {
                viewCertainPeriod();
                break;
            }
        }
        System.out.println("Enter the operation number");
    }

    public void searchMessage(int answer) throws IOException {
        switch (answer) {
            case 4: {
                findAuthor();
                break;
            }
            case 5: {
                findToken();
                break;
            }

            case 6: {
                findRejex();
                break;
            }
        }
        System.out.println("Enter the operation number");
    }

    public void actionMsg(int answer) throws IOException {
        switch (answer) {
            case 1: {
                addMsg();
                break;
            }
            case 2: {
                removeId();
                break;
            }
        }
        System.out.println("Enter the operation number");
    }

    public void answerInquiry() throws IOException, ParseException, org.json.simple.parser.ParseException {
        boolean flag = true;
        int answer;
        while (flag) {
            try {
                answer = Integer.parseInt(br.readLine());
                if (answer == 1 || answer == 2) {
                    actionMsg(answer);
                } else if (answer == 3 || answer == 7) {
                    viewHistory(answer);
                } else if (answer >= 4 && answer <= 6) {
                    searchMessage(answer);
                } else if (answer == 8 || answer == 9) {
                    workFile(answer);
                } else if (answer == 0) {
                    flag = false;
                    System.out.println("Goodbye:)");
                } else {
                    LOG.warn("Invalid input. Try again.");
                }
            } catch (NumberFormatException e) {
                LOG.warn("Invalid input. Try again.");
            }
        }
    }
}
