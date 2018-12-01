package ca.bcit.project.safewalk;

import com.mapbox.geojson.Point;
import java.util.ArrayList;

public class SafeRoute {

    private ArrayList<Coordinate> route;
    private String name;

    //package private
    SafeRoute(String name, double longitude, double latitude){
        this.name = name;
        route = new ArrayList<>();
        route.add(new Coordinate(longitude, latitude));
    }

    public String getName(){return name;}
    public ArrayList<Coordinate> getRoute(){return route;}
    public void setName(String name){this.name = name;}
    public void addCoordinate(double lon, double lat){
        route.add(new Coordinate(lon, lat));
    }

    //Method used for 1. Basic version & 2. Optimization of routing. Return optimal safe coordinate closest to midpoint.
    public Coordinate findWayPointInRange(Point origin, Point destination){
        double latLower = 0;
        double latUpper = 0;
        double longLower = 0;
        double longUpper = 0;
        Coordinate originCoord = new Coordinate(origin.longitude(), origin.latitude());
        Coordinate lowestCoordinate = null;
        Coordinate midPointCoord;
        if(origin.latitude() < destination.latitude()){
            latLower = origin.latitude();
            latUpper = destination.latitude();
        }else{
            latLower = destination.latitude();
            latUpper = origin.latitude();
        }

        if(origin.longitude() < destination.longitude()){
            longLower = origin.longitude();
            longUpper = destination.longitude();
        }else{
            longLower = destination.longitude();
            longUpper = origin.longitude();
        }

        midPointCoord = new Coordinate(longLower + ((longUpper-longLower)/2), latLower+((latUpper - latLower) / 2));

        for(Coordinate c : route){
            if(c.withinRange(longLower,longUpper,latLower,latUpper)){
                if(lowestCoordinate == null)
                    lowestCoordinate = c;
                if(c.findDistanceTo(midPointCoord) < lowestCoordinate.findDistanceTo(midPointCoord)) {
                    lowestCoordinate = c;
                }
            }
        }
        return lowestCoordinate;
    }
    // Method used for 3.map matching version
    public ArrayList<Point> findAllPointsInRange(Point origin, Point destination){
        double latLower = 0;
        double latUpper = 0;
        double longLower = 0;
        double longUpper = 0;
        ArrayList<Point> results = new ArrayList<>();

        if(origin.latitude() < destination.latitude()){
            latLower = origin.latitude();
            latUpper = destination.latitude();
        }else{
            latLower = destination.latitude();
            latUpper = origin.latitude();
        }

        if(origin.longitude() < destination.longitude()){
            longLower = origin.longitude();
            longUpper = destination.longitude();
        }else{
            longLower = destination.longitude();
            longUpper = origin.longitude();
        }
        for(Coordinate c : route){
            if(c.withinRange(longLower,longUpper,latLower,latUpper))
                results.add(Point.fromLngLat(c.getLongitude(), c.getLatitude()));
        }
        return results;
    }
    @Override
    public String toString(){
        String results = "";
        results += "Route: " + name + " First: " + route.get(0) + " Last: " + route.get(route.size()-1) + "\n";
        return results;
    }


}
