package de.flexusma.ltmp.client.connection.listeners;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.minlog.Log;
import com.lilithsthrone.controller.MainController;
import com.lilithsthrone.game.character.PlayerCharacter;
import com.lilithsthrone.game.character.npc.NPC;
import com.lilithsthrone.main.Main;
import de.flexusma.ltmp.client.connection.SocketClient;
import de.flexusma.ltmp.client.game.PlayerNPC;
import de.flexusma.ltmp.client.send.SendContainer;
import de.flexusma.ltmp.client.utils.LogType;
import de.flexusma.ltmp.client.utils.Logger;
import javafx.application.Platform;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;

public class GetPlayerListener implements SendContainerListener {

    SocketClient client;

    public GetPlayerListener(SocketClient client){
        this.client=client;
    }
    
    @Override
    public void received(Connection connection, Class<?> tClass, SendContainer obj) {
        //super.received(connection, object);
        if(tClass==PlayerCharacter.class){
            Logger.log(LogType.DEBUG,"Recvieved Playerdata XML with data: "+obj.getData());
            Document document = null;
            try {
                document = Main.getDocBuilder().parse(new InputSource(new StringReader(obj.getData())));
            } catch (SAXException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if(document==null) return;
            Logger.log(LogType.DEBUG,"Parsed document not null, trying to parse PlayerCharacter");

           // Element game = (Element) document.getElementsByTagName("game").item(0);
            Element characterNode = (Element) ((Element) document.getElementsByTagName("playerCharacter").item(0));
            PlayerCharacter character = null;
            try {
                 character = PlayerCharacter.loadFromXML(null, characterNode, document);
            }catch (Exception e){
                Logger.log(LogType.ERROR,"Error creating player instance from data");
            }
            if(character!=null) {
                Logger.log(LogType.INFO, "Parsed PlayerCharacter with name: " + character.getName());

                boolean hasChange = false;
                for (NPC npc : Main.game.getAllNPCs()) {
                    if (npc instanceof PlayerNPC) {
                        PlayerNPC pnpc = (PlayerNPC) npc;
                        Logger.log(LogType.DEBUG,"Found npc: "+pnpc.getName()+" "+pnpc.uid);
                        if (pnpc.uid == client.getClientID()) {
                            Logger.log(LogType.INFO, "Updating data on npc");
                            hasChange = true;
                            pnpc.updateData(character);
                            pnpc.updateRender();
                            //Platform.runLater(MainController::updateUI);
                        }
                    }
                }

                if (!hasChange) {
                    Logger.log(LogType.WARN, "No NPC with ID found, adding new one");
                    PlayerNPC playerNPC = new PlayerNPC(character, client.getClientID());
                    try {
                        Main.game.addNPC(playerNPC, false);
                    } catch (Exception e) {
                        Logger.log(LogType.ERROR, "Error adding PlayerNPC instance to Game: "+e.getLocalizedMessage());
                    }
                }

            }

        }


      /*  if(object instanceof PlayerCharacter){
            server.updatePlayer(uid,(PlayerCharacter) object);
        }
        */
                   /* try {
                Document doc = Main.getDocBuilder().parse(new InputSource(new StringReader(obj.getData())))
                Element xmle = (Element) doc.getElementsByTagName("playercharacter").item(0);
                    PlayerCharacter character = PlayerCharacter.loadFromXML(null,xmle,doc);

            } catch (SAXException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }*/

    }

}
