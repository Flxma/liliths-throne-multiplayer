package de.flexusma.ltmp.client.game.listener;

import com.lilithsthrone.controller.xmlParsing.XMLUtil;
import com.lilithsthrone.game.character.CharacterChangeEventListener;
import com.lilithsthrone.game.character.PlayerCharacter;
import com.lilithsthrone.main.Main;
import de.flexusma.ltmp.client.Setup;
import de.flexusma.ltmp.client.connection.SocketClient;
import de.flexusma.ltmp.client.game.PlayerNPC;
import de.flexusma.ltmp.client.send.SendContainer;
import de.flexusma.ltmp.client.utils.AsyncSend;
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
import java.util.Set;

public class PlayerDataChangeListener extends CharacterChangeEventListener {


    TransformerFactory factory;
    Transformer transformer;
    SocketClient client;

    public PlayerDataChangeListener(SocketClient client){
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
        if(SocketClient.isCurrentPlayerUpdating){
            Logger.log(LogType.INFO, "Ignoring playerdata update as update is caused by own playerupdate function!");
            return;
        }



        Logger.log(LogType.INFO,"Notified of change in playerdata, sending updated data to server...");

        Document doc = Main.getDocBuilder().newDocument();
       // Element game = doc.createElement("game");
        //doc.appendChild(game);
        //adding version data, etc for compatibility -> see Game.java#553
       // Main.game.getInfoXML(doc,game);

        Element characterNode = doc.createElement("playerCharacter");
        doc.appendChild(characterNode);
        Main.game.getPlayer().saveAsXML(characterNode, doc);

        StringWriter writer = new StringWriter();
        try {
            transformer.transform(new DOMSource(doc),new StreamResult(writer));
        } catch (TransformerException e) {
            e.printStackTrace();
        }
        Logger.log(LogType.DEBUG,"Encoded XML: "+writer.toString());
        String res = writer.toString();
        if(!res.equals(client.getLastSend())) {
            client.setLastSend(res);
            SendContainer cont = new SendContainer(res, Main.game.getPlayer().getClass().getCanonicalName());
            Setup.socketClient.getKclient().sendTCP(cont);
            Logger.log(LogType.INFO, "Done sending playerdata...");
        }else Logger.log(LogType.WARN,"Playerdata same as last request, omitting from sending duplicate");


        for(PlayerNPC npc : PlayerNPC.getAllPNPC()) {
            npc.turnUpdate();
        }

        new AsyncSend(()->{

        }).exec();

    }


}
