package yulia;

import java.io.IOException;

public class Write {
    public static void write(JSONFile jf, MessageHistory mh, String fileName)throws IOException {
        if(jf.openOutputFile(fileName)){
            for(Message item : mh.getList()){
                jf.output(item);
            }
            jf.closeOutputFile();
        }
    }
}
