package de.flexusma.ltmp.client.connection;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;
import de.flexusma.ltmp.client.connection.listeners.*;
import de.flexusma.ltmp.client.send.Register;
import de.flexusma.ltmp.client.send.SendContainer;
import de.flexusma.ltmp.client.send.Start;
import de.flexusma.ltmp.client.utils.Config;
import de.flexusma.ltmp.client.utils.KryoRegDepend;
import de.flexusma.ltmp.client.utils.LogType;
import de.flexusma.ltmp.client.utils.Logger;

import java.io.IOException;

public class SocketClient {

    private Client kclient;
    private int ksPort;
    private String ksAddress;

    public SendManager manager;

    String lastSend = "";

    private int clientID=-1;

    public boolean registered = false;

    public void setClientID(int id){
        this.clientID=id;
    }

    public int getClientID() {
        return clientID;
    }

    public Client getKclient() {
        return kclient;
    }

    public void setKclient(Client kclient) {
        this.kclient = kclient;
    }

    public boolean isRegistered() {
        return registered;
    }

    public void setRegistered(boolean registered) {
        this.registered = registered;
    }

    public SendManager getManager() {
        return manager;
    }

    public String getLastSend() {
        return lastSend;
    }

    public void setLastSend(String lastSend) {
        this.lastSend = lastSend;
    }

    public SocketClient(Config c){
        if(c==null) throw new NullPointerException("Config value cannot be null! Please check that your config is correct.");
        kclient = new Client(4194304,4194304);
        ksPort=c.getServerport();
        ksAddress=c.getIp();
        manager=new SendManager();

        Kryo kryo = kclient.getKryo();


        kryo.register(Register.class);
        kryo.register(SendContainer.class);
        kryo.register(Start.class);



        KryoRegDepend.register(kryo);

    }

    public void start() throws IOException {
        kclient.start();
        kclient.addListener(new RcvRegisterListener(this));
        kclient.addListener(new GetDataListener(this));

        kclient.addListener(new GetSexStartListener(this));

        manager.addListener(new GetSAListener());
        manager.addListener(new GetPlayerListener(this));
        //manager.addListener(new GetSAListener());

        Logger.log(LogType.INFO,"Connecting to specified Server...");
        kclient.connect(5000,ksAddress,ksPort);
        Logger.log(LogType.INFO,"[OK] Server: connected successfully.");

    }

    public void register(){
        kclient.sendTCP(new Register(clientID));
    }

}
