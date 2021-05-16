package de.flexusma.ltmp.server.utils;

public class Config {

    int serverport = 6969;
    String ip = "localhost";

     public Config(){

     }

    public Config(int serverport, String ip) {
        this.serverport = serverport;
        this.ip = ip;
    }

    public int getServerport() {
        return serverport;
    }

    public void setServerport(int serverport) {
        this.serverport = serverport;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
}
