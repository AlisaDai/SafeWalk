package ca.bcit.project.safewalk;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mapbox.android.core.location.LocationEngine;
import com.mapbox.android.core.location.LocationEngineListener;
import com.mapbox.android.core.location.LocationEnginePriority;
import com.mapbox.android.core.location.LocationEngineProvider;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.api.directions.v5.DirectionsCriteria;
import com.mapbox.api.directions.v5.models.DirectionsResponse;
import com.mapbox.api.directions.v5.models.DirectionsRoute;
import com.mapbox.api.geocoding.v5.models.CarmenFeature;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.geometry.LatLngBounds;
import com.mapbox.mapboxsdk.location.modes.CameraMode;
import com.mapbox.mapboxsdk.location.modes.RenderMode;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.plugins.locationlayer.LocationLayerPlugin;
import com.mapbox.mapboxsdk.plugins.places.autocomplete.PlaceAutocomplete;
import com.mapbox.mapboxsdk.plugins.places.autocomplete.model.PlaceOptions;
import com.mapbox.services.android.navigation.ui.v5.NavigationLauncher;
import com.mapbox.services.android.navigation.ui.v5.NavigationLauncherOptions;
import com.mapbox.services.android.navigation.ui.v5.route.NavigationMapRoute;
import com.mapbox.services.android.navigation.v5.navigation.NavigationRoute;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, LocationEngineListener, PermissionsListener, MapboxMap.OnMapClickListener{

    //App variables
    private PermissionsManager permissionManager;
    private RelativeLayout loadingPanel;
    private TextView loadingValue;
    //Mapbox variables
    private MapView mapView;
    private MapboxMap map;
    private Button startButton;
    private FloatingActionButton searchFab;
    private LocationEngine locationEngine;
    private LocationLayerPlugin locationLayerPlugin;
    private Location originLocation;
    //private MapboxOptimization optimizedClient;  2.optimization version excluded
    //private MapboxMapMatching matcher;  3.map matching version commented out
    private Point originPosition;
    private Point destinationPosition;
    private Marker destinationMarker;
    private NavigationMapRoute navigationMapRoute;
    private DirectionsRoute currentRoute;
    //Async Task to load data from firebase
    private FireBaseHelper loadFireBase;
    //local data structure variables after reading from firebase
    private Map<String, SafeRoute> safeRoutes;
    private ArrayList<Point> wayPoints;
    private boolean ready = false;

    private static final String TAG = "MainActivity";
    private static final boolean SIMULATE_ROUTE = true;         //simulate movement towards destination. used for testing
    //private static final double DEVIATE_TOLERANCE = 30d; excluded//30 degree tolerance in off route detection
    private static final int CALL_REQUEST_CODE = 101;           //Permission for call
    private static final int REQUEST_CODE_AUTOCOMPLETE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this,getString(R.string.access_token));
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mapView = findViewById(R.id.mapView);
        startButton = findViewById(R.id.startButton);
        loadingPanel = findViewById(R.id.loadingPanel);
        loadingValue = findViewById(R.id.loadingValue);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavigationLauncherOptions options = NavigationLauncherOptions.builder()
                        .directionsRoute(currentRoute)
                        .shouldSimulateRoute(SIMULATE_ROUTE)
                        .build();
                NavigationLauncher.startNavigation(MainActivity.this, options);
            }
        });

        loadFireBase = new FireBaseHelper();
        loadFireBase.execute();
        wayPoints = new ArrayList<>();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu. This adds items to the app bar.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
//            case R.id.action_map:
//                finish();
//                startActivity(new Intent(this, MainActivity.class));
//                return true;
            case R.id.action_news:
                //Toast.makeText(this, "News", Toast.LENGTH_SHORT).show();
                //Intent i = new Intent(this, NewsActivity.class);
                startActivity(new Intent(this, NewsActivity.class));
                return true;
            case R.id.action_contact:
                //Toast.makeText(this, "Map", Toast.LENGTH_SHORT).show();
                //Intent j = new Intent(this, ContactActivity.class);
                startActivity(new Intent(this, ContactActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onMapReady(MapboxMap mapboxMap) {
        map =  mapboxMap;
        map.addOnMapClickListener(this);
        initSearchFab();
        enableLocation();
    }

    private void initSearchFab(){
        searchFab = findViewById(R.id.floatingActionButton);
        searchFab.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new PlaceAutocomplete.IntentBuilder()
                        .accessToken(Mapbox.getAccessToken())
                        .placeOptions(PlaceOptions.builder()
                                .backgroundColor(Color.parseColor("#EEEEEE"))
                                .limit(10).build(PlaceOptions.MODE_CARDS))
                        .build(MainActivity.this);
                startActivityForResult(intent, REQUEST_CODE_AUTOCOMPLETE);
            }

        });
    }

    //End of location search result
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_AUTOCOMPLETE) {
            // Retrieve selected location's CarmenFeature
            CarmenFeature selectedCarmenFeature = PlaceAutocomplete.getPlace(data);
            // Create a new FeatureCollection and add a new Feature to it using selectedCarmenFeature above
            FeatureCollection featureCollection = FeatureCollection.fromFeatures(
                    new Feature[]{Feature.fromJson(selectedCarmenFeature.toJson())});
            LatLng destinationLatLng = new LatLng(((Point) Objects.requireNonNull(selectedCarmenFeature.geometry())).latitude(),
                    ((Point) Objects.requireNonNull(selectedCarmenFeature.geometry())).longitude());

            onMapClick(destinationLatLng);
        }
    }

    private void enableLocation(){
        if(PermissionsManager.areLocationPermissionsGranted(this)){
            initializeLocationEngine();
            initializeLocationLayer();
        }
        else{
            permissionManager = new PermissionsManager(this);
            permissionManager.requestLocationPermissions(this);
        }
    }

    @Override
    public void onExplanationNeeded(List<String> permissionsToExplain) {
        //Present reasons for permissions
    }

    @Override
    public void onPermissionResult(boolean granted) {
        if(granted){
            enableLocation();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
        permissionManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @SuppressWarnings("MissingPermission")
    private void initializeLocationEngine(){
        locationEngine = new LocationEngineProvider(this).obtainBestLocationEngineAvailable();
        locationEngine.setPriority(LocationEnginePriority.HIGH_ACCURACY);
        locationEngine.activate();

        Location lastLocation = locationEngine.getLastLocation();
        if(lastLocation != null){
            originLocation = lastLocation;
        }else{
            locationEngine.addLocationEngineListener(this);
        }
    }

    @SuppressWarnings("MissingPermission")
    private void initializeLocationLayer(){
        locationLayerPlugin = new LocationLayerPlugin(mapView, map, locationEngine);
        locationLayerPlugin.setLocationLayerEnabled(true);
        locationLayerPlugin.setCameraMode(CameraMode.TRACKING_GPS_NORTH);
        locationLayerPlugin.setRenderMode(RenderMode.COMPASS);
        loadingPanel.setVisibility(View.GONE);
        loadingValue.setText(getString(R.string.loading));
        ready = true;
    }

    private void setCameraPosition(Location location){
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(),location.getLongitude()), 13.0));
    }

    @Override
    public void onMapClick(@NonNull LatLng point) {
        if(ready) {
            routeCleanup();
            LatLng originLatLng = new LatLng(originLocation.getLatitude(), originLocation.getLongitude());
            destinationMarker = map.addMarker(new MarkerOptions().position(point));

            //Bound camera within start and end point so it's clearer for user to see
            //move the camera to focus on start and end points
            LatLngBounds latLngBounds = new LatLngBounds.Builder()
                    .include(originLatLng)
                    .include(point)
                    .build();
            map.easeCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, 400), 250);

            destinationPosition = Point.fromLngLat(point.getLongitude(), point.getLatitude());
            originPosition = Point.fromLngLat(originLocation.getLongitude(), originLocation.getLatitude());
            getRoute(originPosition, destinationPosition);
        }
    }

    // May show more than one route when routing, but nagivates the shortest one when you start navigation
    // If not using map matching version. Results would improve if dataset improves
    // Three ways to approach:
    //  1. Basic version (add waypoints)
    //  2. Optimized version    (optimization client with navigation route)
    //  3. Safety > Optimized version (advanced map matching: map matching -> optimization -> navigation route)
    // You can only use one of the three as main calling for building route
    // Map matching, Navigation route or optimization client.
    // Current version: Basic
    private void getRoute(Point origin, Point destination){

        //double bearing = Float.valueOf(originLocation.getBearing()).doubleValue(); excluded
        //wayPoints.add(origin); map matching version

        //1. Basic version: Find safe waypoints to add for routing purpose
        for(SafeRoute sR : safeRoutes.values()){
            Coordinate wayPointCoord = sR.findWayPointInRange(origin, destination); //basic version/ optimize version
            //wayPoints.addAll(sR.findAllPointsInRange(origin,destination)); map matching version
            if(wayPointCoord != null){
                wayPoints.add(Point.fromLngLat(wayPointCoord.getLongitude(), wayPointCoord.getLatitude()));
            }
        }

        //Navigation builder for building routes
        NavigationRoute.Builder builder = NavigationRoute.builder(this)
                .accessToken(getString(R.string.access_token))
                .origin(origin) // could add these 2 params: bearing, DEVIATE_TOLERANCE
                .destination(destination)
                .profile(DirectionsCriteria.PROFILE_WALKING);

        //Before build add safe waypoints
        for(Point wayPoint : wayPoints){
            builder.addWaypoint(wayPoint);
        }
        //builder.addApproaches("curb","curb","curb","curb"); this line decides which side of road should user be at
        //Log.d("Number of wayPoints", " " + wayPoints.size());

        //API function call for getting route and display
        builder.build().getRoute(new Callback<DirectionsResponse>() {
            @Override
            public void onResponse(Call<DirectionsResponse> call, Response<DirectionsResponse> response) {
                // You can get the generic HTTP info about the response
                if (response.body() == null) {
                    Log.e(TAG, "No routes found, check user right and access token.");
                    return;
                } else if (response.body().routes().size() == 0) {
                    Log.e(TAG, "No routes found");
                    return;
                }

                currentRoute = response.body().routes().get(0);

                // Draw the route on the map
                if (navigationMapRoute != null) {
                    navigationMapRoute.removeRoute();
                } else {
                    navigationMapRoute = new NavigationMapRoute(null, mapView, map, R.style.NavigationMapRoute);
                }
                navigationMapRoute.addRoute(currentRoute);
                //UI button change here
                startButton.setEnabled(true);
                startButton.setBackgroundResource(R.color.colorPrimary);
                startButton.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.mapboxBlack));
            }

            @Override
            public void onFailure(Call<DirectionsResponse> call, Throwable t) {
                Log.e(TAG, "Error:" + t.getMessage());
            }
        });

        //2. Optimization version excluded: code upon request

        //wayPoints.add(destination); use only when using map matching version

        //3. Map matching version: if use need navigation vairable and comment part of 1.basic version
//        matcher.builder()
//        .accessToken(getString(R.string.access_token))
//        .coordinates(wayPoints)   //need to be within bounding box and
//        .steps(true)
//        .voiceInstructions(true)
//        .bannerInstructions(true)
//        .profile(DirectionsCriteria.PROFILE_WALKING)
//        .build()
//        .enqueueCall(new Callback<MapMatchingResponse>() {
//
//        @Override
//        public void onResponse(Call<MapMatchingResponse> call, Response<MapMatchingResponse> response) {
//            if (response.isSuccessful()) {
//                currentRoute = response.body().matchings().get(0).toDirectionRoute(); current route before optimization
//            }
//            else{
//                Log.e(TAG, "No route matched.");
//            }
//
//             if (navigationMapRoute != null) {
//                    navigationMapRoute.removeRoute();
//                } else {
//                    navigationMapRoute = new NavigationMapRoute(null, mapView, map, R.style.NavigationMapRoute);
//                }
//
//            navigationMapRoute.addRoute(currentRoute);
//                startButton.setEnabled(true);
//                startButton.setBackgroundResource(R.color.colorPrimary);
//                startButton.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.mapboxBlack));
//        }
//
//        @Override
//        public void onFailure(Call<MapMatchingResponse> call, Throwable t) {
//            Log.e(TAG, "Map Matching Error:" + t.getMessage());
//        }
//        });
//        navigation.addOffRouteListener(new OffRouteListener() {
//        @Override
//        public void userOffRoute(Location location) {
//        // Make the Map Matching request here
//        // Call MapboxNavigation.startNavigation if you want to start navigation here
//        }
//        });

    }

    //Cleanup for route
    private void routeCleanup(){
        if(destinationMarker != null){
            map.removeMarker(destinationMarker);
            wayPoints.clear();
        }
        if (navigationMapRoute != null) {
            navigationMapRoute.removeRoute();
        }
        startButton.setEnabled(false);
        startButton.setBackgroundResource(R.color.mapboxGreyLight);
        startButton.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.mapboxWhite));
    }

    @SuppressWarnings("MissingPermission")
    @Override
    public void onConnected() {
        locationEngine.requestLocationUpdates();
    }

    @Override
    public void onLocationChanged(Location location) {
        if(location != null){
            originLocation = location;
            setCameraPosition(location);
        }
    }

    //set up map box
    @SuppressWarnings("MissingPermission")
    @Override
    protected void onStart() {
        super.onStart();
        if (locationEngine != null) {
            locationEngine.removeLocationUpdates();
        }
        if (locationLayerPlugin != null) {
            locationLayerPlugin.onStart();
        }
        mapView.onStart();
    }

    @Override
    protected void onResume(){
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause(){
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onStop(){
        super.onStop();
        if(locationEngine != null){
            locationEngine.removeLocationUpdates();
        }
        if(locationLayerPlugin != null){
            locationLayerPlugin.onStop();
        }
        mapView.onStop();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory(){
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        if (map != null) {
            map.removeOnMapClickListener(this);
        }
        if(locationEngine != null) {
            locationEngine.deactivate();
        }
        mapView.onDestroy();
    }

    //Async task for loading Saferoutes data from Firebase. Currently set to Read ONLY permission
    private class FireBaseHelper extends AsyncTask<Void, Integer, Map<String, SafeRoute>> {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference safeRoutesDatabase = database.getReference(getString(R.string.safe_routes));
        Map<String, SafeRoute> results;

        @Override
        protected void onPreExecute(){
            results = new HashMap<>();
        }
        @Override
        protected Map<String, SafeRoute> doInBackground(Void ...params) {
            safeRoutesDatabase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    // This method is called once with the initial value and again
                    // whenever data at this location is updated.
                    double lon = 0;
                    double lat = 0;
                    String name;
                    results.clear();
                    for(DataSnapshot route : dataSnapshot.getChildren()){
                        for(DataSnapshot coord : route.child("geometry").child("coordinates").getChildren()){
                            lon = (Double) coord.child("0").getValue();
                            lat = (Double) coord.child("1").getValue();
                        }
                        name = (String) route.child("properties").child("Route").getValue();
                        addSafeRoutes(name,lon,lat);
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Failed to read value
                    Log.w(TAG, "Failed to read value.", error.toException());
                }
            });

            return results;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            loadingValue.setText("Loading." + values[0].toString() );
        }

        @Override
        protected void onPostExecute(Map<String, SafeRoute> results) {
            safeRoutes = results;
        }

        //check and add safe route into map of safe routes
        private void addSafeRoutes(String name, double lon, double lat){
            if(!results.containsKey(name)) {
                results.put(name, new SafeRoute(name, lon, lat));
            }else{
                results.get(name).addCoordinate(lon, lat);
            }
        }
    }
}
