package de.flexusma.ltmp.client.connection.listeners;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.lilithsthrone.game.sex.sexActions.SexActionInterface;

public class GetSAListener implements Listener {


    @Override
    public void received(Connection connection, Object object) {
        //super.received(connection, object);
        int uid = connection.getID();
        if(object instanceof SexActionInterface){
        }

    }




}
