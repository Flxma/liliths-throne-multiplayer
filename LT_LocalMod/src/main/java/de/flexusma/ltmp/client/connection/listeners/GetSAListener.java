package de.flexusma.ltmp.client.connection.listeners;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lilithsthrone.game.sex.sexActions.SexActionInterface;
import de.flexusma.ltmp.client.game.manager.MPSexManagerDefault;
import de.flexusma.ltmp.client.send.SendContainer;
import de.flexusma.ltmp.client.utils.LogType;
import de.flexusma.ltmp.client.utils.Logger;

public class GetSAListener implements SendContainerListener {

    ObjectMapper mapper;
    public GetSAListener(){
        mapper = new ObjectMapper();
    }

  /*  @Override
    public void received(Connection connection, Object object) {
        //super.received(connection, object);
        int uid = connection.getID();
        if(object instanceof SexActionInterface){
            SexActionInterface sai = (SexActionInterface) object;
            MPSexManagerDefault.invokeInterfaceRecieved(uid,sai);
        }

    }
*/

    @Override
    public void received(Connection connection, Class<?> tClass, SendContainer obj) {
        if(tClass.getSuperclass() == SexActionInterface.class){
            Logger.log(LogType.INFO,"Revieved Sexaction Data: "+obj.getData());
            SexActionInterface sai = null;
            try {
                sai = (SexActionInterface) mapper.readValue(obj.getData(),tClass);
                MPSexManagerDefault.invokeInterfaceRecieved(obj.getSenderID(),sai);
                Logger.log(LogType.INFO,"Successfully added Sexaction data: "+sai.getActionTitle());
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                Logger.log(LogType.ERROR,"Error de-serializing SexAction data: "+e.getLocalizedMessage());
            }

        }
    }
}
