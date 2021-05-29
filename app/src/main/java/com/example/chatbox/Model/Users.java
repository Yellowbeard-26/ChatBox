package com.example.chatbox.Model;

import androidx.annotation.Keep;

@Keep
public class Users {

    private String Id;
    private String username;
    private String imageUrl;
    private String Status;

    public Users() {
    }

    public Users(String id, String username, String imageUrl,String Status) {
        Id = id;
        this.username = username;
        this.imageUrl = imageUrl;
        this.Status=Status;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
       Id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}