package de.flexusma.ltmp.client;

import de.flexusma.ltmp.client.connection.SocketClient;
import de.flexusma.ltmp.client.utils.*;

import java.io.*;
import java.util.Arrays;
import java.util.Set;

public class Setup {

    public static SocketClient socketClient;
    public static AllSexActions allSA;


    public static void initAllSA(){
            Setup.allSA=new AllSexActions();
    }


    public static void main(String[] args) {
        System.setOut(new PrintStream(new FileOutputStream(FileDescriptor.out)));
        System.setErr(new PrintStream(new FileOutputStream(FileDescriptor.out)));

        Setup s = new Setup();
    }

    public Setup(){
        ConfigManager m = new ConfigManager();
        int v = -1;
        if(socketClient!=null) v= socketClient.getClientID();
        m.initConfig();
        Config c = new Config();
        try {
            c= m.readConfig("config.yml");

            socketClient = new SocketClient(c);
        } catch (IOException e) {
            e.printStackTrace();
            if(e instanceof FileNotFoundException){
                try {
                    m.createDefault();
                    socketClient = new SocketClient(c);
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
        }

        Logger.log(LogType.INFO, "Starting client...");
        try {
            socketClient.start();
            Thread.sleep(300);
        } catch (IOException | InterruptedException e) {
            Logger.log(LogType.ERROR, Arrays.toString(e.getStackTrace()));
            e.printStackTrace();
        }

        Logger.log(LogType.INFO, "Registering client...");



        socketClient.register();

        Logger.log(LogType.WARN, "If you are NOT getting a successful register response after this, something went wrong!");


    }

}
