package ca.bcit.project.safewalk;

import java.io.Serializable;

public class News implements Serializable {
    private String type;
    private int year;
    private int month;
    private int day;
    private int hour;
    private int minute;
    private String Block;
    private String Neighbourhood;
    private float x;
    private float y;

    public  News(){
        type = "Unknow";
        year = 2018;
        month = 1;
        day = 1;
        hour = 0;
        minute = 0;
        Block = "";
        Neighbourhood = "";
        x = 0;
        y = 0;
    }

    public News(String type, int year, int month, int day, int hour, int minute, String block, String neighbourhood, float x, float y) {
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

    public void setHour(int hour) {
        this.hour = hour;
    }

    public void setMinute(int minute) {
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

    public int getHour() {
        return hour;
    }

    public int getMinute() {
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
