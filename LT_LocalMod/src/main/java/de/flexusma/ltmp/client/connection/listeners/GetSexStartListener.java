package de.flexusma.ltmp.client.connection.listeners;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.lilithsthrone.game.character.GameCharacter;
import com.lilithsthrone.game.character.PlayerCharacter;
import com.lilithsthrone.game.character.npc.NPC;
import com.lilithsthrone.game.dialogue.DialogueNode;
import com.lilithsthrone.game.dialogue.responses.Response;
import com.lilithsthrone.game.dialogue.responses.ResponseSex;
import com.lilithsthrone.game.inventory.clothing.AbstractClothing;
import com.lilithsthrone.game.inventory.clothing.DisplacementType;
import com.lilithsthrone.main.Main;
import de.flexusma.ltmp.client.Setup;
import de.flexusma.ltmp.client.connection.SocketClient;
import de.flexusma.ltmp.client.game.PlayerNPC;
import de.flexusma.ltmp.client.game.response.MPResponseSex;
import de.flexusma.ltmp.client.send.SendContainer;
import de.flexusma.ltmp.client.send.Start;
import de.flexusma.ltmp.client.utils.AllSexActions;
import de.flexusma.ltmp.client.utils.AsyncSend;
import de.flexusma.ltmp.client.utils.LogType;
import de.flexusma.ltmp.client.utils.Logger;
import javafx.application.Platform;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.StringReader;

public class GetSexStartListener implements Listener {

    SocketClient client;

    public GetSexStartListener(SocketClient client){
        this.client=client;
    }
    
    @Override
    public void received(Connection connection, Object obj) {
        //super.received(connection, object);
        if(obj instanceof Start) {
            Logger.log(LogType.DEBUG, "Recvieved Start command: ");

            int sid = ((Start)obj).sextype;
            int uid = ((Start)obj).id;

            Logger.log(LogType.DEBUG,"SeTypeID: "+sid+" \nGot npcID: "+uid);

            PlayerNPC partner = null;

            for(NPC npc :Main.game.getAllNPCs()){
                if(npc instanceof PlayerNPC){
                    PlayerNPC pnpc = (PlayerNPC) npc;
                    Logger.log(LogType.DEBUG,"NPC: "+pnpc.getName()+pnpc.uid);
                    if(pnpc.uid == uid)
                        partner=pnpc;
                }
            }
            Logger.log(LogType.DEBUG,"Got playerNPC: "+ (partner != null ? partner.getName() : "null"));
            if(partner!=null) {
                ResponseSex resp = null;
                if (sid == 0) {
                    resp = PlayerNPC.normSREInv(partner);
                } else if (sid == 1) {
                    resp = PlayerNPC.domSREInv(partner);
                } else if (sid == 2) {
                    resp = PlayerNPC.subSREInv(partner);
                }

                Logger.log(LogType.DEBUG,"Got response: "+ (resp != null ? resp.getTitle() : "null"));

                Response finalResp = resp;
                PlayerNPC finalPartner = partner;
                new AsyncSend(() -> {
                    try {
                        Thread.sleep(300);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if(finalResp !=null)
                    Platform.runLater(() -> Main.game.setContent(finalResp));

                    Platform.runLater(()->PlayerNPC.displaceAllClothingOfPlayer(finalPartner));
                }
                ).exec();

            }
        }
    }

}
