package yulia;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.util.List;

public class InteractionInterface {
    private BufferedReader br;
    private MessageHistory mh;
    private JSONFile jf;

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

    public void addMsg() throws IOException {
        System.out.println("Enter author, then message");
        mh.addMessage(new Message(br.readLine(), br.readLine()));
    }

    public void removeId() throws IOException {
        System.out.println("Enter id");
        mh.deleteMessageId(br.readLine());
    }

    public void viewCronological() throws IOException {
        mh.sortChronological();
        for (Message item : mh.getList()) {
            System.out.println(item);
        }
    }

    public void viewCertainPeriod() throws IOException {
        System.out.println("Enter period from ... to ...(format: dd.MM.yyyy hh:mm)");
        String from = br.readLine();
        String to = br.readLine();
        try {
            for (Message item : mh.historyPeriod(from, to)) {
                System.out.println(item);
            }
        } catch(ParseException e){
            System.out.println("Invalid input. Try again.");
        }
    }

    public void findAuthor() throws IOException {
        System.out.println("Enter author");
        String author = br.readLine();
        List<Message> authorList =  mh.searchAuthor(author);
        if(authorList.size() == 0) {
            System.out.println(author + " was not found");
            return;
        }
        for (Message item : authorList) {
            System.out.println(item);
        }
    }

    public void findToken() throws IOException {
        System.out.println("Enter token");
        String token = br.readLine();
        List<Message> tokenList =  mh.searchToken(token);
        if(tokenList.size() == 0) {
            System.out.println(token + " was not found");
            return;
        }
        for (Message item :tokenList ) {
            System.out.println(item);
        }
    }

    public void findRejex() throws IOException {
        System.out.println("Enter rejex");
        String rejex = br.readLine();
        List<Message> rejexList =  mh.searchRejex(rejex);
        if(rejexList.size() == 0) {
            System.out.println(rejex + " was not found");
            return;
        }
        for (Message item : rejexList) {
            System.out.println(item);
        }
    }

    public void readFile() throws IOException, ParseException, org.json.simple.parser.ParseException {
        System.out.println("Input fileName (*.json)");
        mh.addMessages(Read.read(br.readLine(), jf));
    }

    public void saveFile() throws IOException {
        System.out.println("Input fileName (*.json)");
        Write.write(jf, mh, br.readLine());
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
                viewCronological();
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
                } else
                    System.out.println("Invalid input. Try again.");
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Try again.");
            }
        }
    }
}
