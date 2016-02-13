package yulia;

import java.io.IOException;
import java.text.ParseException;

public class Main {
    public static void main(String[] args) {
        try {
            run();
        } catch(IOException e){
            System.err.println(e.getMessage());
        } catch (ParseException e){
            System.err.println(e.getMessage());
        } catch(org.json.simple.parser.ParseException e){
            System.err.println(e.getMessage());
        }

    }

    public static void run()throws IOException, ParseException, org.json.simple.parser.ParseException{
        InteractionInterface ii = new InteractionInterface();
        ii.printFacilities();
        ii.answerInquiry();
    }
}
