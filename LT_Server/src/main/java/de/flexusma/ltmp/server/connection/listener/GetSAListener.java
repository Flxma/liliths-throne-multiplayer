package de.flexusma.ltmp.server.connection.listener;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.lilithsthrone.game.character.PlayerCharacter;
import com.lilithsthrone.game.sex.sexActions.SexAction;
import com.lilithsthrone.game.sex.sexActions.SexActionInterface;
import de.flexusma.ltmp.server.connection.SocketServer;
import de.flexusma.ltmp.server.send.SendContainer;

public class GetSAListener implements SendContainerListener {
    SocketServer server;
    public GetSAListener(SocketServer server){
        this.server=server;
    }

 /*   @Override
    public void received(Connection connection, Object object) {
        //super.received(connection, object);
        int uid = connection.getID();
        if(object instanceof SexActionInterface){
            server.addTurnChoice(uid,(SexActionInterface) object);
        }

    }
    */


    @Override
    public void received(Connection connection, Class<?> tClass, SendContainer obj) {
        if(tClass == SexAction.class){
            obj.setSenderID(connection.getID());
            server.addTurnChoice(connection.getID(),obj);
        }
    }
}
