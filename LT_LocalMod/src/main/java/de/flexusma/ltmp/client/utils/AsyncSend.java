package de.flexusma.ltmp.client.utils;

public class AsyncSend{

    Runnable run;

    public AsyncSend(Runnable r ){
        this.run=r;
    }

    public void exec(){
        Thread t = new Thread(run);
        t.start();
    }

}
