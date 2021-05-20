package de.flexusma.ltmp.client.game.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lilithsthrone.game.character.GameCharacter;
import com.lilithsthrone.game.sex.sexActions.SexAction;
import com.lilithsthrone.game.sex.sexActions.SexActionInterface;
import com.lilithsthrone.game.sex.sexActions.SexActionType;
import com.lilithsthrone.game.sex.sexActions.baseActions.ClitAnus;
import com.lilithsthrone.main.Main;
import de.flexusma.ltmp.client.Setup;
import de.flexusma.ltmp.client.connection.SocketClient;
import de.flexusma.ltmp.client.game.PlayerNPC;
import de.flexusma.ltmp.client.send.SendContainer;
import de.flexusma.ltmp.client.utils.LogType;
import de.flexusma.ltmp.client.utils.Logger;

import java.util.Set;

public class PlayerSAListener {

    SocketClient client;
    ObjectMapper mapper;

    public PlayerSAListener(SocketClient client){
        this.client=client;
        mapper = new ObjectMapper();
    }

    public void sendChange(SexAction actionInterface){
        if(Setup.allSA==null)
            Setup.initAllSA();
        String str = Setup.allSA.getFromObject(actionInterface);
        Logger.log(LogType.DEBUG,"Name of Action: "+str);
        if(!str.equals("")) {
            client.getKclient().sendTCP(new SendContainer(str, SexAction.class.getCanonicalName()));

        }


    }

}
