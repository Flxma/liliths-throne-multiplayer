package de.flexusma.ltmp.server.connection.listener;

import com.esotericsoftware.kryonet.Connection;
import de.flexusma.ltmp.server.send.SendContainer;

public interface SendContainerListener{

    public void received(Connection connection, Class<?> tClass,SendContainer obj);

}
