package de.flexusma.ltmp.client.game.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lilithsthrone.game.sex.sexActions.SexActionInterface;
import de.flexusma.ltmp.client.connection.SocketClient;
import de.flexusma.ltmp.client.send.SendContainer;

public class PlayerSAListener {

    SocketClient client;
    ObjectMapper mapper;

    public PlayerSAListener(SocketClient client){
        this.client=client;
        mapper = new ObjectMapper();
    }

    public void sendChange(SexActionInterface actionInterface){
        try {
            String str = mapper.writeValueAsString(actionInterface);
            client.getKclient().sendTCP(new SendContainer(str,SexActionInterface.class.getCanonicalName()));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }


    }

}
