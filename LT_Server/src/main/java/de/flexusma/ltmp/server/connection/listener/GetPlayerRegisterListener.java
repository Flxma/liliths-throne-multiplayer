package de.flexusma.ltmp.server.connection.listener;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.lilithsthrone.game.sex.sexActions.SexActionInterface;
import de.flexusma.ltmp.server.connection.SocketServer;
import de.flexusma.ltmp.server.send.Register;
import de.flexusma.ltmp.server.utils.LogType;
import de.flexusma.ltmp.server.utils.Logger;

public class GetPlayerRegisterListener implements Listener {
    SocketServer server;
    public GetPlayerRegisterListener(SocketServer server){
        this.server=server;
    }

    @Override
    public void received(Connection connection, Object object) {
        //super.received(connection, object);
        int uid = connection.getID();
        if(object instanceof Register){
            Register r = (Register) object;
            if(r.id==-1) {
                Logger.logCl(LogType.INFO, uid, "Received register object, sending response...");
            }else {
                uid=r.id;
                Logger.logCl(LogType.INFO, uid, "Received RE-register object, sending response...");
            }
            server.registeredClients.add(uid);
            connection.sendTCP(new Register(uid));
        }

    }




}

