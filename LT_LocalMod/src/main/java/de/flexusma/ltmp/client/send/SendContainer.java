package de.flexusma.ltmp.client.send;

public class SendContainer {

    String data;
    String classname;

    public SendContainer(String json, String classname){
        this.data = json;
        this.classname = classname;
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



}
