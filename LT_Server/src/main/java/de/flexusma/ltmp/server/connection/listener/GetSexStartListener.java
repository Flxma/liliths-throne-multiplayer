package de.flexusma.ltmp.server.connection.listener;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import de.flexusma.ltmp.server.connection.SocketServer;
import de.flexusma.ltmp.server.send.Start;
import de.flexusma.ltmp.server.utils.LogType;
import de.flexusma.ltmp.server.utils.Logger;
import javafx.application.Platform;

public class GetSexStartListener implements Listener {

    SocketServer server;

    public GetSexStartListener(SocketServer server){
        this.server =server;
    }
    
    @Override
    public void received(Connection connection, Object obj) {
        //super.received(connection, object);
        if(obj instanceof Start) {
            Logger.logCl(LogType.DEBUG,connection.getID(), "Recvieved Start command ");
                server.addStartCmd(connection.getID(),(Start)obj);

        }
    }

}
