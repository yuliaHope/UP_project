package yulia;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

public class WorkFile {
    private BufferedReader br;
    private PrintWriter pw;

    public boolean openInputFile(String fileName) {
        try {
            br = new BufferedReader(new FileReader(fileName));
            return true;
        } catch (IOException e) {
            System.err.println(e.getMessage());
            return false;
        }
    }

    public boolean openOutputFile(String fileName) {
        try {
            pw = new PrintWriter(fileName);
            return true;
        } catch (IOException e) {
            System.err.println(e.getMessage());
            return false;
        }
    }

    public void closeOutputFile() {
        pw.close();
    }

    public void closeInputFile() throws IOException{
        br.close();
    }

    public String read() throws IOException {
        String s;
        StringBuilder sb = new StringBuilder();
        while ((s = br.readLine()) != null) {
            sb.append(s);
        }
        return sb.toString();
    }

    public void printText(String text){
        pw.println(text);
    }
}
