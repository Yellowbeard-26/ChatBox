package com.example.chatbox.Model;

public class Chats {
    private String Sender;
    private String Reciever;
    private String Message;

    public Chats()
    {}

    public Chats(String sender, String reciever, String message) {
        Sender = sender;
        Reciever = reciever;
        Message = message;

    }


    public String getSender() {
        return Sender;
    }

    public void setSender(String sender) {
        Sender = sender;
    }

    public String getReciever() {
        return Reciever;
    }

    public void setReciever(String reciever) {
        Reciever = reciever;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }
}
