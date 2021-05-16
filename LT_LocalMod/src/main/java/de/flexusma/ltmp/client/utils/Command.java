package de.flexusma.ltmp.client.utils;

public enum  Command {

    PLAYER_TURN(20),
    SEND_OTHER_PLAYER(30),
    SEND_OTHER_PLAYER_SAI(31),
    SEND_PLAYER_IS_TURN(40);



    public final int methodID;

    private Command(int mID){
        this.methodID= mID;
    }

}
