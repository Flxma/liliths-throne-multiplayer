package de.flexusma.ltmp.client.connection;

import com.esotericsoftware.kryonet.Connection;
import de.flexusma.ltmp.client.connection.listeners.SendContainerListener;
import de.flexusma.ltmp.client.send.SendContainer;
import de.flexusma.ltmp.client.utils.LogType;
import de.flexusma.ltmp.client.utils.Logger;

import java.util.ArrayList;
import java.util.List;

public class SendManager {

    List<SendContainerListener> objListener;

    public SendManager(){
        objListener = new ArrayList<>();
    }

    public void addListener(SendContainerListener l){
        objListener.add(l);
    }

    public void invoke(SendContainer container, Connection connection){
        try {
            Class<?> objclass = Class.forName(container.getClassname());
            for(SendContainerListener l:objListener){
                l.received(connection,objclass,container);
            }

        } catch (ClassNotFoundException e) {
            Logger.log(LogType.ERROR, "Could not get Class for classname: " + container.getClassname() + " | Cannot de-serialize Object!");
            return;
        }


    }


}
