package com.example.practicacomov.models;

public class ItemPrivateChat {
    private String mensaje;
    private String mensajepropio;
    private String date;
    private String dateP;

    public ItemPrivateChat(String mensaje, String date, String mensajeP, String dateP) {
        this.mensaje = mensaje;
        this.mensajepropio = mensajeP;
        this.date = date;
        this.dateP = dateP;
    }

    public String getMensaje() {return mensaje;}
    public String getMensajepropio() {return mensajepropio;}
    public String getDate() {return date;}
    public String getDateP() {return dateP;}
}
