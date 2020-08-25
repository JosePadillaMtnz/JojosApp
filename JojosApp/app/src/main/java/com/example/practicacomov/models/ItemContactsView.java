package com.example.practicacomov.models;

public class ItemContactsView {

    private int image;
    private String user;
    private String realuser;

    public ItemContactsView(int image, String user, String realuser) {
        this.image = image;
        this.user = user;
        this.realuser = realuser;
    }

    public int getImage() {return image;}
    public String getUser() {return user;}
    public String getRealUser() {return realuser;}

}
