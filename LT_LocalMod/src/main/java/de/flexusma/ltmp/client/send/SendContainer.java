package de.flexusma.ltmp.client.send;

public class SendContainer {

    String data;
    String classname;
    int senderID;

    public SendContainer(String json, String classname){
        this.data = json;
        this.classname = classname;
    }

    public SendContainer(String json, String classname, int senderID){
        this.data = json;
        this.classname = classname;
        this.senderID=senderID;
    }


    public SendContainer(){}

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getClassname() {
        return classname;
    }

    public void setClassname(String classname) {
        this.classname = classname;
    }

    public int getSenderID() {
        return senderID;
    }

    public void setSenderID(int senderID) {
        this.senderID = senderID;
    }
}
