package com.example.practicacomov.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class Message {

    public static final int TYPE_PLAINTEXT = 0;
    public static final int TYPE_IMAGE = 1;
    public static final int TYPE_LOCATION = 2;

    @SerializedName("date")
    @Expose
    private Date date;
    @SerializedName("body")
    @Expose
    private String body;
    @SerializedName("from")
    @Expose
    private String from;
    @SerializedName("to")
    @Expose
    private String to;
    @SerializedName("type")
    @Expose
    private int type;

    public Message(String body, String from, String to, int type) {
        this.body = body;
        this.from = from;
        this.to = to;
        this.type = type;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String toString() {
        return body + "\n" + from + "\n" + to + "\n" + type + "\n" + date;
    }
}
