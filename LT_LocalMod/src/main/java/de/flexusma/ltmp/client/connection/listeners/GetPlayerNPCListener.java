package de.flexusma.ltmp.client.connection.listeners;

import com.esotericsoftware.kryonet.Connection;
import com.lilithsthrone.game.character.PlayerCharacter;
import com.lilithsthrone.game.character.npc.NPC;
import com.lilithsthrone.main.Main;
import de.flexusma.ltmp.client.Setup;
import de.flexusma.ltmp.client.connection.SocketClient;
import de.flexusma.ltmp.client.game.PlayerNPC;
import de.flexusma.ltmp.client.send.SendContainer;
import de.flexusma.ltmp.client.utils.LogType;
import de.flexusma.ltmp.client.utils.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.StringReader;
import java.util.Set;

public class GetPlayerNPCListener implements SendContainerListener {

    SocketClient client;

    public GetPlayerNPCListener(SocketClient client){
        this.client=client;
    }
    
    @Override
    public void received(Connection connection, Class<?> tClass, SendContainer obj) {
        //super.received(connection, object);
        if(tClass==NPC.class){
            Logger.log(LogType.DEBUG,"Recvieved PlayerNPCdata XML with data: "+obj.getData());
            Document document = null;
            try {
                document = Main.getDocBuilder().parse(new InputSource(new StringReader(obj.getData())));
            } catch (SAXException | IOException e) {
                e.printStackTrace();
                return;
            }
            if(document==null) return;
            Logger.log(LogType.DEBUG,"Parsed document not null, trying to parse PlayerNPC");

           // Element game = (Element) document.getElementsByTagName("game").item(0);
            Element characterNode = (Element) ((Element) document.getElementsByTagName("playerNPC").item(0));

            PlayerNPC character = new PlayerNPC(Main.game.getPlayer());
            try {
                 character.loadFromXML(characterNode, document);
            }catch (Exception e){
                Logger.log(LogType.ERROR,"Error creating playerNPC instance from data");
                return;
            }
            if(character!=null) {
                Logger.log(LogType.INFO, "Parsed PlayerNPC with name: " + character.getName());

                //update data on player without changelistener firing to avoid infinite send loop

                SocketClient.isCurrentPlayerUpdating=true;

                Logger.log(LogType.DEBUG,"Updating player data from recieved NPCData");
                PlayerNPC.updatePlayerData(character);

                //end sendlock for Playerdata
                SocketClient.isCurrentPlayerUpdating=false;
                Logger.log(LogType.DEBUG,"Removing temporary NPC from Game");
                Main.game.removeNPC(character);

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
