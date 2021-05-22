package de.flexusma.ltmp.server.connection.listener;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import de.flexusma.ltmp.server.connection.SocketServer;
import de.flexusma.ltmp.server.send.SendContainer;
import de.flexusma.ltmp.server.utils.LogType;
import de.flexusma.ltmp.server.utils.Logger;

public class GetDataListener implements Listener {
    SocketServer server;
    public GetDataListener(SocketServer server){
        this.server=server;
    }

    @Override
    public void received(Connection connection, Object object) {
        //super.received(connection, object);
        int uid = connection.getID();
        if(object instanceof SendContainer){
            if(((SendContainer)object).getData().length()<300)
                Logger.logCl(LogType.INFO,uid,"Recieved json data: "+((SendContainer) object).getData()+"\nClass:"+((SendContainer) object).getClassname());
            else
                Logger.logCl(LogType.INFO,uid,"Recieved json data: "+((SendContainer) object).getData().substring(0,200)+"\nClass:"+((SendContainer) object).getClassname());
            server.getManager().invoke((SendContainer) object,connection);
        }

    }



}
