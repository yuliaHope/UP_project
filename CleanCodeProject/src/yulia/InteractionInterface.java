package yulia;

import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.util.List;


public class InteractionInterface {
    private BufferedReader br;
    private MessageHistory mh;
    private JSONFile jf;
    private static final Logger LOG = Logger.getLogger(InteractionInterface.class);

    public InteractionInterface() {
        br = new BufferedReader(new InputStreamReader(System.in));
        mh = new MessageHistory();
        jf = new JSONFile();

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
        mh.addMessage(msg);
        LOG.info("Add message.\n" + msg);
    }

    public void removeId() throws IOException {
        System.out.println("Enter id");
        mh.deleteMessageId(br.readLine(), LOG);
    }

    public void viewChronological() throws IOException {
        mh.sortChronological();
        if (mh.getList().size() == 0) {
            LOG.warn("message history is empty");
            return;
        }
        LOG.info("view message history in chronological order");
        for (Message item : mh.getList()) {
            System.out.println(item);
        }
    }

    public void viewCertainPeriod() throws IOException {
        System.out.println("Enter period from ... to ...(format: dd.MM.yyyy hh:mm)");
        String from = br.readLine();
        String to = br.readLine();
        List<Message> periodList;
        try {
            periodList = mh.historyPeriod(from, to);
            for (Message item : periodList) {
                System.out.println(item);
            }
            LOG.info("view message history from " + from + " to " + to);
        } catch (ParseException e){
            LOG.error("Invalid date format");
        } catch (NullPointerException e){
            LOG.warn("message history from " + from + " to " + to + " was not found");
        }
    }

    public void findAuthor() throws IOException {
        System.out.println("Enter author");
        String author = br.readLine();
        List<Message> authorList = mh.searchAuthor(author);
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
        List<Message> tokenList = mh.searchToken(token);
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
        List<Message> rejexList = mh.searchRejex(rejex);
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
        mh.addMessages(Read.read(fileName, jf));
        LOG.info("read messages from " + fileName);

    }

    public void saveFile() throws IOException {
        System.out.println("Input fileName (*.json)");
        String fileName = br.readLine();
        Write.write(jf, mh, fileName);
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
