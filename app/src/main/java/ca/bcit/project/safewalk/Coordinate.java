package ca.bcit.project.safewalk;

public class Coordinate {

    private double lon;
    private double lat;


    //package private
    Coordinate(double lon, double lat){
        this.lon = lon;
        this.lat = lat;
    }

    public double getLongitude(){return lon;}
    public double getLatitude(){return lat;}
    public void setLongitude(double lon){this.lon = lon;}
    public void setLatitude(double lat){this.lat = lat;}
    //Check if coordinate is within range of bounding box
    public boolean withinRange(double longLower, double longUpper, double latLower, double latUpper){
        return lon >= longLower && lon <= longUpper && lat >= latLower && lat <=latUpper;
    }
    //find distance of this coordinate to another
    public double findDistanceTo(Coordinate other){
        return Math.sqrt(Math.pow(Math.abs(lon-other.getLongitude()), 2) + Math.pow(Math.abs(lat-other.getLatitude()),2));
    }

    @Override
    public String toString(){
        return lon + " " + lat;
    }

}
