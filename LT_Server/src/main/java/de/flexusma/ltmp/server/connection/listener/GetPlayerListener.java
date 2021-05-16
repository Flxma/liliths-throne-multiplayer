package de.flexusma.ltmp.server.connection.listener;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.lilithsthrone.game.character.GameCharacter;
import com.lilithsthrone.game.character.PlayerCharacter;
import de.flexusma.ltmp.server.connection.SocketServer;
import jdk.net.Sockets;

public class GetPlayerListener extends Listener {
    SocketServer server;
    public GetPlayerListener(SocketServer server){
        this.server=server;
    }

    @Override
    public void received(Connection connection, Object object) {
        //super.received(connection, object);
        int uid = connection.getID();
        if(object instanceof PlayerCharacter){
            server.updatePlayer(uid,(PlayerCharacter) object);
        }

    }




}
