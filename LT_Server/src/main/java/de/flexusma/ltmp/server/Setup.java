package de.flexusma.ltmp.server;


import de.flexusma.ltmp.server.connection.SocketServer;
import de.flexusma.ltmp.server.utils.Config;
import de.flexusma.ltmp.server.utils.ConfigManager;
import de.flexusma.ltmp.server.utils.LogType;
import de.flexusma.ltmp.server.utils.Logger;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;

public class Setup {

    public SocketServer socketServer;

    public static void main(String[] args) {
        Setup s = new Setup(args);
    }

    public Setup(String[] args){
        ConfigManager m = new ConfigManager();

        m.initConfig();
        Config c = new Config();
        try {
            c= m.readConfig("config.yml");
            socketServer = new SocketServer(c);
        } catch (IOException e) {
            e.printStackTrace();
            if(e instanceof FileNotFoundException){
                try {
                    m.createDefault();
                    socketServer = new SocketServer(c);
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
        }

        Logger.log(LogType.INFO, "Starting client...");
        try {
            socketServer.start();
        } catch (IOException e) {
            Logger.log(LogType.ERROR, Arrays.toString(e.getStackTrace()));
            e.printStackTrace();
        }

    }
}
