package com.example.practicacomov.models;

import java.util.Comparator;

public class ItemListView implements Comparable {

    private int image;
    private String user;
    private String lastMessage;
    private String date;

    public ItemListView(int image, String user, String lastMessage, String date) {
        this.image = image;
        this.user = user;
        this.lastMessage = lastMessage;
        this.date = date;
    }

    public int getImage() {return image;}
    public String getUser() {return user;}
    public String getLastMessage() {return lastMessage;}
    public String getDate() {return date;}

    @Override
    public int compareTo(Object iteme) {
        ItemListView item = (ItemListView) iteme;
        String[] other = item.getDate().split(" ");
        String[] dthis = this.getDate().split(" ");
        if (other[0] == dthis[0]) { //If same day
            String[] otherHour = other[1].split(":");
            int otherH = Integer.parseInt(otherHour[0]);
            int otherM = Integer.parseInt(otherHour[1]);
            String[] thisHour = dthis[1].split(":");
            int thisH = Integer.parseInt(thisHour[0]);
            int thisM = Integer.parseInt(thisHour[1]);
            if (otherH == thisH) return otherM-thisM; //If same hour
            else return otherH-thisH;
        } else { //If not same day
            String[] otherHour = other[0].split("/");
            int otherD = Integer.parseInt(otherHour[0]);
            int otherM = Integer.parseInt(otherHour[1]);
            int otherY = Integer.parseInt(otherHour[2]);
            String[] thisHour = dthis[0].split("/");
            int thisD = Integer.parseInt(thisHour[0]);
            int thisM = Integer.parseInt(thisHour[1]);
            int thisY = Integer.parseInt(thisHour[2]);
            if (otherY == thisY) { //If same year
                if (otherM == thisM) return otherD-thisD; //If same month
                else return otherM-thisM;
            } else return otherY-thisY;
        }
    }
}
