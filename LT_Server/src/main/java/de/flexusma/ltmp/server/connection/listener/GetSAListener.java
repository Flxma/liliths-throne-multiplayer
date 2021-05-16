package de.flexusma.ltmp.server.connection.listener;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.lilithsthrone.game.character.PlayerCharacter;
import com.lilithsthrone.game.sex.sexActions.SexActionInterface;
import de.flexusma.ltmp.server.connection.SocketServer;

public class GetSAListener extends Listener {
    SocketServer server;
    public GetSAListener(SocketServer server){
        this.server=server;
    }

    @Override
    public void received(Connection connection, Object object) {
        //super.received(connection, object);
        int uid = connection.getID();
        if(object instanceof SexActionInterface){
            server.addTurnChoice(uid,(SexActionInterface) object);
        }

    }




}
