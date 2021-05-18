package de.flexusma.ltmp.client.connection.listeners;

import com.esotericsoftware.kryonet.Connection;
import de.flexusma.ltmp.client.send.SendContainer;

public interface SendContainerListener {

    public void received(Connection connection, Class<?> tClass, SendContainer obj);

}
