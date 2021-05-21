package de.flexusma.ltmp.server.connection;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Server;
import com.lilithsthrone.game.character.PlayerCharacter;
import com.lilithsthrone.game.sex.sexActions.SexActionInterface;
import de.flexusma.ltmp.server.connection.listener.*;
import de.flexusma.ltmp.server.send.SendContainer;
import de.flexusma.ltmp.server.send.Register;
import de.flexusma.ltmp.server.send.Start;
import de.flexusma.ltmp.server.utils.Config;
import de.flexusma.ltmp.server.utils.KryoRegDepend;
import de.flexusma.ltmp.server.utils.LogType;
import de.flexusma.ltmp.server.utils.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class SocketServer {

    HashMap<Integer, SendContainer> playerList;
    HashMap<Integer, SendContainer> npcList;
    HashMap<Integer, SendContainer> saiList;
    HashMap<Integer, Start> startList;
    List<Integer> currentTurnDone;

    public List<Integer> registeredClients;

    public static SendManager manager;


    private Server kserver;
    private int ksPort;

    public SocketServer(Config c){
        if(c==null) throw new NullPointerException("Config value cannot be null! Please check that your config is correct.");
        kserver=new Server(419430400,419430400);
        playerList = new HashMap<>();
        currentTurnDone = new ArrayList<>();
        ksPort=c.getServerport();
        registeredClients = new ArrayList<>();
        manager = new SendManager();
        startList=new HashMap<>();
        saiList=new HashMap<>();
        npcList=new HashMap<>();

        Kryo kryo = kserver.getKryo();
        kryo.register(Register.class);
        kryo.register(SendContainer.class);
        kryo.register(Start.class);



    }

    public void start() throws IOException {
        kserver.start();
        Logger.log(LogType.INFO,"Starting Server on port "+ksPort+" ...");
        kserver.bind(ksPort);
        Logger.log(LogType.INFO,"[OK] Server started successfully.");
        //setup listener
        Logger.log(LogType.INFO, "Registering service Listeners...");
        kserver.addListener(new GetDataListener(this));
        kserver.addListener(new GetPlayerRegisterListener(this));

        kserver.addListener(new GetSexStartListener(this));

        kserver.addListener(new ServerListener(this));
        manager.addListener(new GetSAListener(this));
        manager.addListener(new GetPlayerListener(this));
        manager.addListener(new GetPlayerNPCListener(this));
        //manager.addListener(new GetSAListener(this));
        KryoRegDepend.register(kserver.getKryo());

        Logger.log(LogType.INFO,"[OK] Listeners registered successfully.");

    }

    public SendManager getManager() {
        return manager;
    }


    public Connection getConByID(int uid){
        for(Connection con:kserver.getConnections()){
            if(con.getID()==uid){
                return con;
            }
        }
        return null;
    }

    /*
    Playerdata synchronization
     */

    public void updatePlayer(int uid, SendContainer character){
        if(registeredClients.contains(uid)) {
            if (playerList.containsKey(uid)) {
                playerList.replace(uid, character);
            } else {
                playerList.put(uid, character);
            }

            InvokePlayerDataUpdate(uid);
        }
    }

    public void InvokePlayerDataUpdate(int uid){
        //TODO: Call rmi other playerServices playerUpdate function
        int r =1;
        for(int id: registeredClients){
            if(uid!=id){
                Connection con = getConByID(id);
                if(con==null) break;

                int finalR = r;
                Thread t = new Thread(()->{
                    SendContainer c = playerList.get(uid);
                    try {
                        Thread.sleep((50 *(long) finalR));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    con.sendTCP(c);
                });
                t.start();
                r++;
            }
        }

    }
    public List<SendContainer> getOtherPlayers(int uid){
        List<SendContainer> pList = new ArrayList<>();
        for(int id : playerList.keySet()){
            if(id!=uid) pList.add(playerList.get(id));
        }
        return pList;
    }

    public void updatePlayerNPC(int uid, SendContainer character){
        if(registeredClients.contains(uid)) {
            if (npcList.containsKey(uid)) {
                npcList.replace(uid, character);
            } else {
                npcList.put(uid, character);
            }

            InvokePlayerNPCDataUpdate(uid);
        }
    }

    public void InvokePlayerNPCDataUpdate(int uid){
        //TODO: Call rmi other playerServices playerUpdate function
        int r =1;
        for(int id: registeredClients){
            if(uid!=id){
                Connection con = getConByID(id);
                if(con==null) break;
                Logger.logCl(LogType.INFO,id,"Sending NPC Data from: "+uid);
                int finalR = r;
                Thread t = new Thread(()->{
                    SendContainer c = npcList.get(uid);
                    try {
                        Thread.sleep((50 *(long) finalR));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    con.sendTCP(c);
                });
                t.start();
                r++;
            }
        }

    }

    /*
    Turn Choice synchronization
     */

    public void addTurnChoice(int uid, SendContainer pAction){
        pAction.setSenderID(uid);
        saiList.put(uid,pAction);
        InvokePlayerNextTurn(uid);
        //playerDoneTurn(uid);
    }

    public void addStartCmd(int uid, Start start){
        startList.put(uid,start);
        InvokePlayerStart(uid);
        //playerDoneTurn(uid);
    }

    public List<Integer> getPlayerIDs(){
        List<Integer> idList =  new ArrayList<>(playerList.keySet());
        Collections.sort(idList);
        return idList;
    }

    public int playerTurn(){
        List<Integer> idList = getPlayerIDs();
        Logger.log(LogType.DEBUG,"Playerlist: "+idList);
        idList.removeAll(currentTurnDone);
        Logger.log(LogType.DEBUG,"Players next turn order: "+idList);
        if(idList.isEmpty()) {
            currentTurnDone = new ArrayList<>();
            idList=getPlayerIDs();
        }
        return idList.get(0);
    }

    public void playerDoneTurn(int uid){
        currentTurnDone.add(uid);
    }

    public void InvokePlayerNextTurn(int uid){
        for(int id : registeredClients){
            if(id!=uid)
            for(int sid : saiList.keySet()) {
                Logger.logCl(LogType.DEBUG,id,"Sending turnData from player: "+sid);
                getConByID(id).sendTCP(saiList.get(sid));

            }
        }
        saiList=new HashMap<>();
        //TODO: CALL rmi Method of selected player
    }

    public void InvokePlayerStart(int uid){
        for(int id : registeredClients){
            if(id!=uid)
            for(int sid : startList.keySet()) {
                Logger.logCl(LogType.DEBUG, id, "Sending start command from player: " + sid);
                getConByID(id).sendTCP(startList.get(sid));
            }
        }
        startList = new HashMap<>();
        //TODO: CALL rmi Method of selected player
    }
}
