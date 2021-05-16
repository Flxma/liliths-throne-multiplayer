package de.flexusma.ltmp.server.connection.listener;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.lilithsthrone.game.sex.sexActions.SexActionInterface;
import de.flexusma.ltmp.server.connection.SocketServer;
import de.flexusma.ltmp.server.send.Register;

public class GetPlayerRegisterListener extends Listener {
    SocketServer server;
    public GetPlayerRegisterListener(SocketServer server){
        this.server=server;
    }

    @Override
    public void received(Connection connection, Object object) {
        //super.received(connection, object);
        int uid = connection.getID();
        if(object instanceof Register){
            connection.sendTCP(new Register(uid));
        }

    }




}

