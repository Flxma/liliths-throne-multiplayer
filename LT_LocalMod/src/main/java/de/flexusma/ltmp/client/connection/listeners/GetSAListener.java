package de.flexusma.ltmp.client.connection.listeners;

import com.esotericsoftware.kryonet.Connection;
import com.lilithsthrone.game.sex.sexActions.SexAction;
import com.lilithsthrone.game.sex.sexActions.SexActionUtility;
import de.flexusma.ltmp.client.Setup;
import de.flexusma.ltmp.client.game.manager.MPSexManagerDefault;
import de.flexusma.ltmp.client.send.SendContainer;
import de.flexusma.ltmp.client.utils.LogType;
import de.flexusma.ltmp.client.utils.Logger;

public class GetSAListener implements SendContainerListener {


    public GetSAListener(){
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
        if(tClass == SexAction.class){
            Logger.log(LogType.INFO,"Revieved Sexaction Data: "+obj.getData());

            String saname = obj.getData();

                if (Setup.allSA == null)
                    Setup.initAllSA();
                SexAction action = Setup.allSA.getFromName(saname);

            MPSexManagerDefault.invokeInterfaceRecieved(obj.getSenderID(),action);
            Logger.log(LogType.INFO,"Successfully added Sexaction data: "+action.getActionTitle());


        }
    }
}
