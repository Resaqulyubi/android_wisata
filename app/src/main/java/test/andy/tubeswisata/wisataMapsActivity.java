package test.andy.tubeswisata;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.SphericalUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class wisataMapsActivity extends FragmentActivity implements
        OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        GoogleMap.OnMarkerDragListener,
        GoogleMap.OnMapLongClickListener,
        View.OnClickListener{
   static String namaWisata="";
    //Our Map
    private GoogleMap mMap;
    private static final String TAG = wisataMapsActivity.class.getSimpleName();

    //To store longitude and latitude from map
    private double longitude;
    private double latitude;

    //From -> the first coordinate from where we need to calculate the distance
    private double fromLongitude;
    private double fromLatitude;

    //To -> the second coordinate to where we need to calculate the distance
    private double toLongitude;
    private double toLatitude;

    //Google ApiClient
    private GoogleApiClient googleApiClient;

    //Our buttons


    private Button buttonCalcDistance;ImageView btnnav;
              LatLng latLngTO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wisata_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //Initializing googleapi client
        // ATTENTION: This "addApi(AppIndex.API)"was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .addApi(AppIndex.API).build();



        buttonCalcDistance = (Button) findViewById(R.id.buttonCalcDistance);
        btnnav = (ImageView) findViewById(R.id.buttonnavigation);



        buttonCalcDistance.setOnClickListener(this);


        Intent intentExtras = getIntent();
       String latlang = intentExtras.getStringExtra("langlat");
         namaWisata = intentExtras.getStringExtra("nama");

        Log.d(TAG, "get extralanglat: " + latlang);
        Log.d(TAG, "get extranama: " + namaWisata);


        List<String> ListLatlang = Arrays.asList(latlang.split(","));

        toLatitude   =Double.parseDouble(ListLatlang.get(0));
        toLongitude  =Double.parseDouble(ListLatlang.get(1));

          latLngTO = new LatLng( toLatitude, toLongitude);


        btnnav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Uri gmmIntentUri = Uri.parse("google.navigation:q="+toLatitude+",+"+toLongitude+"");
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");


                PackageManager packageManager = getPackageManager();
                if (mapIntent.resolveActivity(packageManager) != null) {
                    startActivity(mapIntent);
                } else {
                    Log.d(TAG, "Maps not found ,already install google Map?!");
                    Toast.makeText(getApplicationContext(),"Maps not found ,already install google Map?!", Toast.LENGTH_LONG).show();


                }
//
//               startActivity(mapIntent);
//

            }
        });

    }

    @Override
    protected void onStart() {
        googleApiClient.connect();
        super.onStart();


        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
//        Action viewAction = Action.newAction(
//                Action.TYPE_VIEW, // TODO: choose an action type.
//                "Maps Page", // TODO: Define a title for the content shown.
//                // TODO: If you have web page content that matches this app activity's content,
//                // make sure this auto-generated web page URL is correct.
//                // Otherwise, set the URL to null.
//                Uri.parse("http://host/path"),
//                // TODO: Make sure this auto-generated app deep link URI is correct.
//                Uri.parse("android-app://net.simplifiedcoding.googlemapsdistancecalc/http/host/path")
//        );
//        AppIndex.AppIndexApi.start(googleApiClient, viewAction);
    }

    @Override
    protected void onStop() {
        googleApiClient.disconnect();
        super.onStop();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
//        Action viewAction = Action.newAction(
//                Action.TYPE_VIEW, // TODO: choose an action type.
//                "Maps Page", // TODO: Define a title for the content shown.
//                // TODO: If you have web page content that matches this app activity's content,
//                // make sure this auto-generated web page URL is correct.
//                // Otherwise, set the URL to null.
//                Uri.parse("http://host/path"),
//                // TODO: Make sure this auto-generated app deep link URI is correct.
//                Uri.parse("android-app://net.simplifiedcoding.googlemapsdistancecalc/http/host/path")
//        );
//        AppIndex.AppIndexApi.end(googleApiClient, viewAction);
    }

    //Getting current location
    private void getCurrentLocation() {

       //  mMap.clear();
        //Creating a location object
        Location location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        if (location != null) {

            //Getting longitude and latitude
            longitude = location.getLongitude();
            latitude = location.getLatitude();

            fromLatitude=latitude;
            fromLongitude=longitude;

            //moving the map to location
         //  moveMap();
        }
// else {
//
//            LocationManager locationManager = (LocationManager)
//                    getSystemService(Context.LOCATION_SERVICE);
//            Criteria criteria = new Criteria();
//
//             locationManager.getBestProvider(criteria, false);
//
//
//
////            double latitude = location2.getLatitude();
////            double longitude = location2.getLongitude();
//
//            fromLatitude=latitude;
//            fromLongitude=longitude;

//            moveMap();

        }








    //Function to move the map
    private void moveMap() {
        //Creating a LatLng Object to store Coordinates
        LatLng latLng = new LatLng(latitude, longitude);

        //Adding marker to map
       Marker me= mMap.addMarker(new MarkerOptions()
                .position(latLng) //setting position
                .draggable(true) //Making the marker draggable
                .title("Current Location")); //Adding a title
        me.showInfoWindow();

        //Moving the camera
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

        //Animating the camera
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
    }

    public String makeURL (double sourcelat, double sourcelog, double destlat, double destlog ){
        StringBuilder urlString = new StringBuilder();
        urlString.append("https://maps.googleapis.com/maps/api/directions/json");
        urlString.append("?origin=");// from
        urlString.append(Double.toString(sourcelat));
        urlString.append(",");
        urlString
                .append(Double.toString( sourcelog));
        urlString.append("&destination=");// to
        urlString
                .append(Double.toString( destlat));
        urlString.append(",");
        urlString.append(Double.toString(destlog));
        urlString.append("&sensor=false&mode=driving&alternatives=true");
        urlString.append("&key=AIzaSyBi3AN0JekIz6GaUiZrt5pH-AusGoSWSDI");
        return urlString.toString();
    }

    private void getDirection(){
        //Getting the URL
        String url = makeURL(fromLatitude, fromLongitude, toLatitude, toLongitude);

        //Showing a dialog till we get the route
        final ProgressDialog loading = ProgressDialog.show(this, "Getting Route", "Please wait...", false, false);

        //Creating a string request
        StringRequest stringRequest = new StringRequest(url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        loading.dismiss();
                        //Calling the method drawPath to draw the path
                        drawPath(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loading.dismiss();
                    }
                });

        //Adding the request to request queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    //The parameter is the server response
    public void drawPath(String result) {
        //Getting both the coordinates
        LatLng from = new LatLng(fromLatitude,fromLongitude);
        LatLng to = new LatLng(toLatitude,toLongitude);

        //Calculating the distance in meters
        Double distance = SphericalUtil.computeDistanceBetween(from, to);

        Double km =distance/1000;

        //Displaying the distance
        Toast.makeText(this,String.valueOf(km+" Kilometers"),Toast.LENGTH_SHORT).show();


        try {
            //Parsing json
            final JSONObject json = new JSONObject(result);
            JSONArray routeArray = json.getJSONArray("routes");
            JSONObject routes = routeArray.getJSONObject(0);
            JSONObject overviewPolylines = routes.getJSONObject("overview_polyline");
            String encodedString = overviewPolylines.getString("points");
            List<LatLng> list = decodePoly(encodedString);
            Polyline line = mMap.addPolyline(new PolylineOptions()
                    .addAll(list)
                    .width(20)
                    .color(Color.RED)
                    .geodesic(true)
            );


        }
        catch (JSONException e) {

        }
    }

    private List<LatLng> decodePoly(String encoded) {
        List<LatLng> poly = new ArrayList<LatLng>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng( (((double) lat / 1E5)),
                    (((double) lng / 1E5) ));
            poly.add(p);
        }

        return poly;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

       Marker to= mMap.addMarker(new MarkerOptions().position(latLngTO).draggable(true)
                .title("Wisata")
                .snippet(namaWisata)
                .rotation((float) -15.0)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
        );
        to.showInfoWindow();
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLngTO));
        mMap.setOnMarkerDragListener(this);
        mMap.setOnMapLongClickListener(this);




     mMap.animateCamera(CameraUpdateFactory.newLatLngZoom((latLngTO), 12.0f));


//
//
//        longitude=toLongitude;
//        latitude=toLatitude;
//        moveMap();

    }

    @Override
    public void onConnected(Bundle bundle) {
       // super.onStart();
//        if (googleApiClient != null) {
//            googleApiClient.connect();
//        }


        getCurrentLocation();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void onMapLongClick(LatLng latLng) {
        //Clearing all the markers
        mMap.clear();
        //Adding a new marker to the current pressed position
        mMap.addMarker(new MarkerOptions()
                .position(latLng)
                .draggable(true));

        latitude = latLng.latitude;
        longitude = latLng.longitude;
    }

    @Override
    public void onMarkerDragStart(Marker marker) {

    }

    @Override
    public void onMarkerDrag(Marker marker) {

    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        //Getting the coordinates
        latitude = marker.getPosition().latitude;
        longitude = marker.getPosition().longitude;

        //Moving the map
        moveMap();
    }

    @Override
    public void onClick(View v) {












        if(v == buttonCalcDistance){
            LatLng CurrentlatLng = new LatLng( getLastKnownLocation().getLatitude(),   getLastKnownLocation().getLongitude());


            Marker to= mMap.addMarker(new MarkerOptions().position(CurrentlatLng).draggable(true)
                    .title("Current location")
                    .snippet(String.valueOf((CurrentlatLng.latitude)+","+String.valueOf((CurrentlatLng.longitude))) )
                    .rotation((float) -15.0)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA))
            );
            to.showInfoWindow();


            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            builder.include(CurrentlatLng);
            builder.include(latLngTO);
            LatLngBounds bounds = builder.build();

            CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, 50);
            mMap.animateCamera(cu, new GoogleMap.CancelableCallback(){
                public void onCancel(){}
                public void onFinish(){
                    CameraUpdate zout = CameraUpdateFactory.zoomBy(-1);
                    mMap.animateCamera(zout);
                }
            });

//            LocationManager locationManager = (LocationManager)
//                    getSystemService(Context.LOCATION_SERVICE);
//            Criteria criteria = new Criteria();
//
//            Location location = locationManager.getLastKnownLocation(locationManager
//                    .getBestProvider(criteria, false));
//            double latitude = location.getLatitude();
//            double longitude = location.getLongitude();
//
//             final LatLng me = new LatLng(latitude,longitude);
//            Marker melbourne = mMap.addMarker(new MarkerOptions()
//                    .position(me)
//                    .title("Melbourne"));
//            melbourne.showInfoWindow();

            getDirection();
        }
    }

    private Location getLastKnownLocation() {
        LocationManager mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        List<String> providers = mLocationManager.getProviders(true);
        Location bestLocation = null;
        for (String provider : providers) {
            Location l = mLocationManager.getLastKnownLocation(provider);
            Log.d("last known location, provider: %s, location: %s", provider);

            if (l == null) {
                continue;
            }
            if (bestLocation == null
                    || l.getAccuracy() < bestLocation.getAccuracy()) {
                Log.d("found best last known location: %s", "a");
                bestLocation = l;
            }
        }
        if (bestLocation == null) {
            return null;
        }
        return bestLocation;
    }


}