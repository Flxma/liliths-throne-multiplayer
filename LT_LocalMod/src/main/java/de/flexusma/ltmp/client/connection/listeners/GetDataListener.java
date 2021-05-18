package de.flexusma.ltmp.client.connection.listeners;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import de.flexusma.ltmp.client.connection.SocketClient;
import de.flexusma.ltmp.client.send.SendContainer;
import de.flexusma.ltmp.client.utils.LogType;
import de.flexusma.ltmp.client.utils.Logger;

public class GetDataListener implements Listener {
    SocketClient client;
    public GetDataListener(SocketClient client){
        this.client=client;
    }

    @Override
    public void received(Connection connection, Object object) {
        //super.received(connection, object);
        int uid = connection.getID();
        if(object instanceof SendContainer){
            Logger.log(LogType.INFO,"Recieved XML data: "+((SendContainer) object).getData());
            client.getManager().invoke((SendContainer) object,connection);
        }

    }



}
