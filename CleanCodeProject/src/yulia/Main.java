package yulia;

import java.io.IOException;
import java.text.ParseException;

public class Main {
    public static void main(String[] args) {
        run();
    }

    public static void run(){
        InteractionInterface ii = new InteractionInterface();
        try {
            ii.printFacilities();
            ii.answerInquiry();
        }
        catch(IOException e){
            ii.logErr(e.getMessage());
        } catch (ParseException e){
            ii.logErr(e.getMessage());
        } catch(org.json.simple.parser.ParseException e){
            ii.logErr(e.getMessage());
        }

    }
}
