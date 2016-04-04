package by.bsu.up.chat.logging.impl;

import by.bsu.up.chat.logging.Logger;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FileLog implements Logger {
    private static final String TEMPLATE = "[%s] %s";

    private PrintWriter pw;


    private FileLog(String fileName) {
        try {
            pw = new PrintWriter(new FileWriter(fileName));
        } catch (IOException e) {

        }
    }

    public String countedTag() {
        SimpleDateFormat formatDate = new SimpleDateFormat("dd.MM.yyyy hh:mm");
        String tag = String.format(TEMPLATE, formatDate.format(new Date()), "%s");
        return tag;
    }

    @Override
    public void info(String message) {
        pw.write(String.format(countedTag(), message) + "\r\n");
        pw.flush();
    }

    @Override
    public void error(String message, Throwable e) {
        pw.write(String.format(countedTag(), message) + e.getStackTrace() + "\r\n");
        pw.flush();
    }

    @Override
    public void warn(String message) {
        pw.write(String.format("WARN " + countedTag(), message) + "\r\n");
        pw.flush();

    }

    public static FileLog create(String fileName) {
        return new FileLog(fileName);
    }
}
