package de.flexusma.ltmp.client.utils;

public class LogType {
    public final static LogType INFO = new LogType("INFO", 2);
    public final static LogType WARN = new LogType("WARN", 1);
    public final static LogType DEBUG = new LogType("DEBUG", 3);
    public final static LogType ERROR = new LogType("ERROR", 0);


    String type;
    int level;

    public LogType(String type, int level) {
        this.type = type;
        this.level = level;
    }

    public int getLevel() {
        return level;
    }

    public String getType() {
        return type;
    }

}