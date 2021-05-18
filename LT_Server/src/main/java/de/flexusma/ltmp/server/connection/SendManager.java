package de.flexusma.ltmp.server.connection;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lilithsthrone.utils.XMLSaving;
import de.flexusma.ltmp.server.connection.listener.SendContainerListener;
import de.flexusma.ltmp.server.send.SendContainer;
import de.flexusma.ltmp.server.utils.LogType;
import de.flexusma.ltmp.server.utils.Logger;

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
            Logger.logCl(LogType.ERROR, connection.getID(), "Could not get Class for classname: " + container.getClassname() + " | Cannot de-serialize Object!");
            return;
        }


    }


}
