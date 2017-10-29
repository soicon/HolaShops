package com.chatapp.dmr.HolaShop.Model;

/**
 * Created by dmr on 9/8/2017.
 */

public class Message {
    String sender;
    String receiver;
    String senderEmail;
    String userID;
    String message;
    String imageUrl;

    public Message(String sender, String receiver, String senderEmail, String userID, String message, String image, String time) {
        this.sender = sender;
        this.receiver = receiver;
        this.senderEmail = senderEmail;
        this.userID = userID;
        this.message = message;
        this.imageUrl = image;
        this.time = time;
    }



    String time;

    public Message() {

    }

    public Message(String sender, String userID, String message, String time) {
        this.sender = sender;
        this.userID = userID;
        this.message = message;
        this.time = time;
    }

    public Message(String sender, String receiver, String senderEmail, String userID, String message, String time) {
        this.sender = sender;
        this.receiver = receiver;
        this.senderEmail = senderEmail;
        this.userID = userID;
        this.message = message;
        this.time = time;
    }

    public String getSenderEmail() {
        return senderEmail;
    }

    public void setSenderEmail(String senderEmail) {
        this.senderEmail = senderEmail;
    }


    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }


    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTime() {
        return time;
    }

    public String getImage() {
        return imageUrl;
    }

    public void setImage(String image) {
        this.imageUrl = image;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
