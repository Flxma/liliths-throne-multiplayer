package de.flexusma.ltmp.client.connection.listeners;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.lilithsthrone.main.Main;
import de.flexusma.ltmp.client.connection.SocketClient;
import de.flexusma.ltmp.client.send.Register;
import de.flexusma.ltmp.client.utils.LogType;
import de.flexusma.ltmp.client.utils.Logger;

public class RcvRegisterListener implements Listener {
    SocketClient client;
    public RcvRegisterListener(SocketClient client){
        this.client =client;
    }

    @Override
    public void received(Connection connection, Object object) {
        //super.received(connection, object);
        Logger.log(LogType.DEBUG, "Received any data: " + object);
            int uid = connection.getID();
            if(object instanceof Register){
                Register register = (Register) object;
                client.setClientID(register.id);
                client.registered = true;

                Main.isConnected = true;

                Logger.log(LogType.INFO,"Registering done, received ID: "+register.id);

            }
        }

    }
