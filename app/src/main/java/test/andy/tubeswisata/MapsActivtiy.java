package test.andy.tubeswisata;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;


import com.snail.bingandroid.BingMap;
import com.snail.bingandroid.MapView;
import com.snail.bingandroid.OnMapReadyCallback;
import com.snail.bingandroid.serialization.entry.Color;
import com.snail.bingandroid.serialization.entry.Location;
import com.snail.bingandroid.util.PushpinCreator;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;

public class MapsActivtiy extends AppCompatActivity implements OnMapReadyCallback, BingMap.OnMapClickListener {
    private BingMap mBingMap;
    private MapView mMapView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

    }

    private void initMap() {
        mMapView = (MapView) findViewById(R.id.map_view);
        mMapView.getMapAsync("AtPnNOQaK1hx92pIu8OZY4IJ9YGZKEGebIssLWIUpg-L74ek_s4do5kI7eFunUV6", this);
    }

    @Override
    public void onMapReady(BingMap bingMap) {
        mBingMap = bingMap;
        mBingMap.setOnMapClickListener(this);
        setMarker();
    }

    private void setMarker(){
        String s=  getIntent().getStringExtra("lnglat")!=null?getIntent().getStringExtra("lnglat"):"";

        String[] s1= s.split(",");


        JSONObject jsObject = new JSONObject();
        try {
            if (s1.length>0){
                jsObject.put("latitude",s1[0]);
                jsObject.put("longitude",s1[1]);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Location targetLocation = new Location(String.valueOf(jsObject));//provider name is unnecessary
        createPushpin(targetLocation);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initMap();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onMapClick(Location latLng) {
        createPushpin(latLng);
    }

    private void createPushpin(Location location) {
        PushpinCreator pushpinCreator = new PushpinCreator(String.valueOf(new Random().nextInt()));
        pushpinCreator.setLocation(location);
        pushpinCreator.setPushingColor(Color.Colors.BLUE);
        pushpinCreator.setInfobox("Test", "Infobox");
        pushpinCreator.setPushpinDraggableMode(true);

        pushpinCreator.setIcon("https://www.bingmapsportal.com/Content/images/poi_custom.png");


        mBingMap.addPushpin(pushpinCreator.create());
    }
}
