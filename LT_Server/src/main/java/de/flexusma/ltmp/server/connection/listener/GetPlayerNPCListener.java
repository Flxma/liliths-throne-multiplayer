package de.flexusma.ltmp.server.connection.listener;

import com.esotericsoftware.kryonet.Connection;
import com.lilithsthrone.game.character.PlayerCharacter;
import com.lilithsthrone.game.character.npc.NPC;
import de.flexusma.ltmp.server.connection.SocketServer;
import de.flexusma.ltmp.server.send.SendContainer;
import de.flexusma.ltmp.server.utils.LogType;
import de.flexusma.ltmp.server.utils.Logger;

public class GetPlayerNPCListener implements SendContainerListener {
    SocketServer server;
    public GetPlayerNPCListener(SocketServer server){
        this.server=server;
    }

    @Override
    public void received(Connection connection, Class<?> tClass, SendContainer obj) {
        //super.received(connection, object);
        int uid = connection.getID();
        if(tClass== NPC.class){
            Logger.logCl(LogType.DEBUG,uid,"Recvieved PlayerNPCdata XML with data: "+obj.getData());
            obj.setSenderID(uid);
            server.updatePlayerNPC(uid,obj);

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
