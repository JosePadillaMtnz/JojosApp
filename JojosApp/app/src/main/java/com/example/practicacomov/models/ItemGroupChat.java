package com.example.practicacomov.models;

public class ItemGroupChat {
    private String mensaje;
    private String mensajepropio;
    private String user;
    private String date;
    private String dateP;

    public ItemGroupChat (String user, String mensaje, String date, String mensajepropio, String dateP) {
        this.mensaje = mensaje;
        this.mensajepropio = mensajepropio;
        this.user = user;
        this.date = date;
        this.dateP = dateP;
    }

    public String getMensaje() {return mensaje;}
    public String getMensajepropio() {return mensajepropio;}
    public String getDate() {return date;}
    public String getUser() {return user;}
    public String getDateP() {return dateP;}
}
