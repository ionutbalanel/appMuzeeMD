package com.ltstefanesti.muzeemd.models;

import com.google.firebase.database.ServerValue;

public class Comment {
    private String content, uId, uImg, uName, id;
    private Object timeStamp;

    public Comment() {
    }

    public Comment(String content, String uId, String uImg, String uName) {
        this.content = content;
        this.uId = uId;
        this.uImg = uImg;
        this.uName = uName;
        this.timeStamp = ServerValue.TIMESTAMP;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getuId() {
        return uId;
    }

    public String getuImg() {
        return uImg;
    }

    public String getuName() {
        return uName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Object getTimeStamp() {
        return timeStamp;
    }
}