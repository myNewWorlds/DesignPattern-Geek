package com.ljt;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Logger {
    private final FileWriter fileWriter;
    private static final Logger instance = new Logger();

    private Logger() {
        File file = new File("E:\\TestEverything\\logInfo.txt");
        try {
            fileWriter = new FileWriter(file, true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Logger getInstance() {
        return instance;
    }

    public void log(String message) {
        try {
            synchronized (Logger.class) {  //类级别锁，保证写入安全
                fileWriter.write(message);
                fileWriter.flush();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
