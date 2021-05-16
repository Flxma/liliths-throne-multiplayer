package de.flexusma.ltmp.client.connection;

import com.esotericsoftware.kryonet.Client;
import de.flexusma.ltmp.client.utils.Config;
import de.flexusma.ltmp.client.utils.LogType;
import de.flexusma.ltmp.client.utils.Logger;

import java.io.IOException;

public class SocketClient {

    private Client kclient;
    private int ksPort;
    private String ksAddress;

    private int clientID;

    public SocketClient(Config c){
        if(c==null) throw new NullPointerException("Config value cannot be null! Please check that your config is correct.");
        kclient = new Client();
        ksPort=c.getServerport();
        ksAddress=c.getIp();

    }

    public void start() throws IOException {
        kclient.start();
        Logger.log(LogType.INFO,"Connecting to specified Server...");
        kclient.connect(5000,ksAddress,ksPort);
        Logger.log(LogType.INFO,"[OK] Server: connected successfully.");




    }

}
