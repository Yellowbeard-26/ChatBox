package com.example.chatbox.Model;

public class Chatslists {
    public String id;
    private String Sendid;

    public Chatslists()
    {

    }

    public Chatslists(String id,String sendid) {
        this.id = id;
        Sendid=Sendid;
    }

    public String getSendid() {
        return Sendid;
    }

    public void setSendid(String sendid) {
        Sendid = sendid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
