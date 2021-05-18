package de.flexusma.ltmp.server.connection.listener;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.lilithsthrone.game.character.GameCharacter;
import com.lilithsthrone.game.character.PlayerCharacter;
import com.lilithsthrone.main.Main;
import de.flexusma.ltmp.server.connection.SocketServer;
import de.flexusma.ltmp.server.send.SendContainer;
import de.flexusma.ltmp.server.utils.LogType;
import de.flexusma.ltmp.server.utils.Logger;
import jdk.net.Sockets;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.StringReader;

public class GetPlayerListener implements SendContainerListener {
    SocketServer server;
    public GetPlayerListener(SocketServer server){
        this.server=server;
    }

    @Override
    public void received(Connection connection, Class<?> tClass, SendContainer obj) {
        //super.received(connection, object);
        int uid = connection.getID();
        if(tClass==PlayerCharacter.class){
            Logger.logCl(LogType.DEBUG,uid,"Recvieved Playerdata XML with data: "+obj.getData());
            server.updatePlayer(uid,obj);

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
