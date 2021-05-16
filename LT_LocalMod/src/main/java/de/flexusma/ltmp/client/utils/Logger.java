package de.flexusma.ltmp.client.utils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Logger {

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";
    public static LogType logLevel = LogType.WARN;
    public static String loggerName = "LT-SERVER | Log: ";
    public static String folder = "logs/";
    private static Path file = Paths.get("logs/");


    private static String logFileDate = "";

    private static boolean forceDisableFileLog = true;


    public static void setup(String prefix, LogType logLevel) {

    }

    public static void setup(String prefix, LogType logLevel, Path logFile) {
        System.out.println(logLevel.level);
        logLevel = logLevel;
        forceDisableFileLog = false;
        folder = logFile.toString();
        loggerName=prefix;
    }

    public static void setup(String prefix, Path logfile) {

    }

    public static void setup(LogType loglevel, boolean writeToFile, String logfile) {
        System.out.println(logLevel.level);
        logLevel = loglevel;
        forceDisableFileLog = !writeToFile;
        folder = logfile;

    }


    public static void setup(String logfile) {
        folder = logfile;
    }

    public static void setup(LogType loglevel) {
        logLevel = loglevel;
    }

    public static void setLogFilePath(String path) {
        folder = path;
    }

    private static void debugLog(LogType toLog, String info) {
        if (toLog.level <= logLevel.getLevel()) {
            String msg = getColor(toLog) + loggerName + toLog.getType() + " | " + info + ANSI_RESET;
            System.out.println(msg);
        }
    }

    public static void log(LogType toLog, String info) {


        if (logFileDate.equalsIgnoreCase("")) {
            logFileDate = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(new Date(System.currentTimeMillis()));
            logFileDate = logFileDate.replace(" ", "");
            logFileDate = logFileDate.replace(":", "");
            logFileDate += ".log";
            file = Paths.get(folder + logFileDate);
        }

        if (toLog.level >= logLevel.getLevel()) {
            String msg = getColor(toLog) + loggerName + toLog.getType() + " | " + info + ANSI_RESET;
            System.out.println(msg);
            if (!forceDisableFileLog) {
                checkFileCreate();
                FileWriter writer = null;
                try {
                    writer = new FileWriter(String.valueOf(file), true);
                } catch (IOException e) {
                    Logger.debugLog(LogType.WARN, "Logfile path was invalid, using default fallback path ~/logs/ \nPlease check the specified logfile path, to avoid future complications!");
                    try {
                        writer = new FileWriter("logs/" + logFileDate, true);
                    } catch (IOException ioException) {
                       forceDisableFileLog = true;
                        Logger.debugLog(LogType.ERROR, "Force-Disabling FileLog, due to an unknown and critical Error! \n");
                    }
                }

                if (writer != null) {
                    BufferedWriter bufferedWriter = new BufferedWriter(writer);
                    msg = "(" + new Date().toString() + ")" + loggerName + toLog.getType() + " | " + info;
                    try {
                        bufferedWriter.write(msg + "\n");
                        bufferedWriter.close();
                    } catch (IOException e) {
                        Logger.debugLog(LogType.WARN, "An Error occured while writing to the log file:\n" + e.toString());
                    }

                }

            }

        }
    }

    public static void checkFileCreate() {
        try {
            Files.createDirectories(file.getParent());
            Path myObj = Files.createFile(file);
            if (myObj != null) {
                System.out.println("File created: " + myObj.toString());
            }
        } catch (FileAlreadyExistsException ey) {
            // Logger.debugLog(LogType.DEBUG,"File already Exists, continuing...");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    static String getColor(LogType type) {
        switch (type.getLevel()) {
            case 0:
                return ANSI_RED;
            case 1:
                return ANSI_YELLOW;
            case 2:
                return ANSI_GREEN;
            case 3:
                return ANSI_WHITE;
            default:
                return ANSI_CYAN;

        }
    }
}