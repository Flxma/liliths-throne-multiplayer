package de.flexusma.ltmp.server.connection.listener;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import de.flexusma.ltmp.server.connection.SocketServer;
import de.flexusma.ltmp.server.utils.LogType;
import de.flexusma.ltmp.server.utils.Logger;
import jdk.net.Sockets;

public class ServerListener implements Listener {

    SocketServer kserver;

    public ServerListener(SocketServer server){
        this.kserver=server;
    }

    @Override
    public void connected(Connection connection) {

    }

    @Override
    public void disconnected(Connection connection) {
        Logger.logCl(LogType.WARN,connection.getID(),"Client disconnected. Removing from Clientlist...");
        kserver.registeredClients.remove((Integer) connection.getID());
    }
}
