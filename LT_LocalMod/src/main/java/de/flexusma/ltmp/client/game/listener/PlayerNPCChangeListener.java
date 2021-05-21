package de.flexusma.ltmp.client.game.listener;

import com.lilithsthrone.game.character.CharacterChangeEventListener;
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

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.StringWriter;

public class PlayerNPCChangeListener extends CharacterChangeEventListener {
    TransformerFactory factory;
    Transformer transformer;
    SocketClient client;

    public PlayerNPCChangeListener(SocketClient client){
        factory=TransformerFactory.newInstance();
        try {
            transformer = factory.newTransformer();
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        }

        this.client=client;
    }


    @Override
    public void onChange() {
        if(Main.game.isInSex()) {
            for (PlayerNPC playerNPC : PlayerNPC.getAllPNPC()) {
                Logger.log(LogType.INFO, "Notified of change in NPC Inventory/Stats, sending updated data to server...");

                if (playerNPC.uid == Setup.socketClient.blockNPCSend) return;
                //get npc xml
                Document doc = Main.getDocBuilder().newDocument();
                // Element game = doc.createElement("game");
                //doc.appendChild(game);
                //adding version data, etc for compatibility -> see Game.java#553
                // Main.game.getInfoXML(doc,game);

                Element characterNode = doc.createElement("playerNPC");
                doc.appendChild(characterNode);
                playerNPC.saveAsXML(characterNode, doc);

                StringWriter writer = new StringWriter();
                try {
                    transformer.transform(new DOMSource(doc), new StreamResult(writer));
                } catch (TransformerException e) {
                    e.printStackTrace();
                }
                Logger.log(LogType.DEBUG, "Encoded XML: " + writer.toString());
                String res = writer.toString();
                if (!res.equals(client.getLastNPCSend().get(playerNPC.uid))) {
                    if (client.getLastNPCSend().containsKey(playerNPC.uid))
                        client.removeLastNPCSend(playerNPC.uid);
                    client.addLastNPCSend(playerNPC.uid, res);
                    SendContainer cont = new SendContainer(res, NPC.class.getCanonicalName());
                    Setup.socketClient.getKclient().sendTCP(cont);
                    Logger.log(LogType.INFO, "Done sending playerNPCdata...");
                } else Logger.log(LogType.WARN, "PlayerNPCdata same as last request, omitting from sending duplicate");


            }
        }
    }
}
