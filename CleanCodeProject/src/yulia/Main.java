package yulia;

import java.io.IOException;
import java.text.ParseException;

public class Main {
    public static void main(String[] args) {
        run();
    }

    public static void run(){
        InteractionInterface interInterface = new InteractionInterface();
        try {
            interInterface.printFacilities();
            interInterface.answerInquiry();
        }
        catch(IOException | ParseException e){
            interInterface.logErr(e.getMessage());
        } catch(org.json.simple.parser.ParseException e){
            interInterface.logErr(e.getMessage());
        }

    }
}
