package ca.bcit.project.safewalk;

import java.io.Serializable;

public class News implements Serializable {
    private String type;
    private int year;
    private int month;
    private int day;
    private String hour;
    private String minute;
    private String Block;
    private String Neighbourhood;
    private float x;
    private float y;

    public  News(){
        type = "Unknow";
        year = 2018;
        month = 1;
        day = 1;
        hour = "00";
        minute = "00";
        Block = "";
        Neighbourhood = "";
        x = 0;
        y = 0;
    }

    public News(String type, int year, int month, int day, String hour, String minute, String block, String neighbourhood, float x, float y) {
        this.type = type;
        this.year = year;
        this.month = month;
        this.day = day;
        this.hour = hour;
        this.minute = minute;
        Block = block;
        Neighbourhood = neighbourhood;
        this.x = x;
        this.y = y;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public void setMinute(String minute) {
        this.minute = minute;
    }

    public void setBlock(String block) {
        Block = block;
    }

    public void setNeighbourhood(String neighbourhood) {
        Neighbourhood = neighbourhood;
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public String getType() {
        return type;
    }

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public int getDay() {
        return day;
    }

    public String getHour() {
        return hour;
    }

    public String getMinute() {
        return minute;
    }

    public String getBlock() {
        return Block;
    }

    public String getNeighbourhood() {
        return Neighbourhood;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }
}
