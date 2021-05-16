package de.flexusma.ltmp.server.connection;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.lilithsthrone.game.character.GameCharacter;
import com.lilithsthrone.game.character.PlayerCharacter;
import com.lilithsthrone.game.sex.sexActions.SexActionInterface;
import de.flexusma.ltmp.server.connection.listener.GetPlayerListener;
import de.flexusma.ltmp.server.connection.listener.GetPlayerRegisterListener;
import de.flexusma.ltmp.server.connection.listener.GetSAListener;
import de.flexusma.ltmp.server.send.Register;
import de.flexusma.ltmp.server.utils.Config;
import de.flexusma.ltmp.server.utils.LogType;
import de.flexusma.ltmp.server.utils.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class SocketServer {

    HashMap<Integer, PlayerCharacter> playerList;
    HashMap<Integer, SexActionInterface> saiList;
    List<Integer> currentTurnDone;


    private Server kserver;
    private int ksPort;

    public SocketServer(Config c){
        if(c==null) throw new NullPointerException("Config value cannot be null! Please check that your config is correct.");
        kserver=new Server();
        playerList = new HashMap<>();
        currentTurnDone = new ArrayList<>();


        Kryo kryo = kserver.getKryo();
        kryo.register(PlayerCharacter.class);
        kryo.register(SexActionInterface.class);
        kryo.register(Register.class);


    }

    public void start() throws IOException {
        kserver.start();
        Logger.log(LogType.INFO,"Starting Server on port "+ksPort+" ...");
        kserver.bind(ksPort);
        Logger.log(LogType.INFO,"[OK] Server started successfully.");
        //setup listener
        Logger.log(LogType.INFO, "Registering service Listeners...");
        kserver.addListener(new GetPlayerListener(this));
        kserver.addListener(new GetPlayerRegisterListener(this));
        kserver.addListener(new GetSAListener(this));
        Logger.log(LogType.INFO,"[OK] Listeners registered successfully.");

    }

    /*
    Playerdata synchronization
     */

    public void updatePlayer(int uid, PlayerCharacter character){
        if(playerList.containsKey(uid)){
            playerList.replace(uid,character);
        }else{
            playerList.put(uid,character);
        }
    }

    public void InvokePlayerDataUpdate(int uid){
        //TODO: Call rmi other playerServices playerUpdate function
    }
    public List<PlayerCharacter> getOtherPlayers(int uid){
        List<PlayerCharacter> pList = new ArrayList<>();
        for(int id : playerList.keySet()){
            if(id!=uid) pList.add(playerList.get(id));
        }
        return pList;
    }

    /*
    Turn Choice synchronization
     */

    public void addTurnChoice(int uid, SexActionInterface pAction){
        saiList.put(uid,pAction);
        playerDoneTurn(uid);
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

    public void InvokePlayerNextTurn(){
        int nextplayerTurn = playerTurn();
        //TODO: CALL rmi Method of selected player
    }
}
