package alexandvlad.bomjego;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

import alexandvlad.bomjego.model.BomjeType;
import alexandvlad.bomjego.model.WildBomjeEntry;

import static com.google.android.gms.location.LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY;

public class MapsActivity extends FragmentActivity implements
        OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener,
        AddBomjeListener {

    private static final int MY_PERMISSIONS_REQUEST_FINE_LOCATION = 1;


    private GoogleMap googleMap;
    private GoogleApiClient googleApiClient;
    private BomjeLogic logic;

    private ArrayList<Marker> Markers = new ArrayList<>();

    private Location prevLocation;
    private double distance = 0d;

    public void onClickShowBomje(View view) {
        Intent intent = new Intent(this, CaughtListActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        logic = new BomjeLogic(this, this, (Button)findViewById(R.id.button_show_caught));
    }

    public void addBomje(View view) {
        logic.addBomje();
    }

    public void addBomjeToLocation(Location location) {
        logic.addBomjeToLocation(location);
    }

    private Bitmap resizeMapIcons(String iconName, int width, int height) {
        Bitmap imageBitmap = BitmapFactory.decodeResource(getResources(), getResources().getIdentifier(iconName, "drawable", getPackageName()));
        return Bitmap.createScaledBitmap(imageBitmap, width, height, false);
    }

    @Override
    public void addBomje(WildBomjeEntry wildBomje) {
        if (wildBomje.bomje.type.equals(BomjeType.ANTON)) {
            Markers.add(googleMap.addMarker(new MarkerOptions()
                    .position(new LatLng(wildBomje.location.getLatitude(), wildBomje.location.getLongitude()))
                    .icon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons("bomje_anton", 130, 130))))
            );
        } else if (wildBomje.bomje.type.equals(BomjeType.OPASNI)) {
            Markers.add(googleMap.addMarker(new MarkerOptions()
                    .position(new LatLng(wildBomje.location.getLatitude(), wildBomje.location.getLongitude()))
                    .icon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons("bomje_opasni", 130, 130))))
            );
        } else if (wildBomje.bomje.type.equals(BomjeType.DEREVENSKI)) {
            Markers.add(googleMap.addMarker(new MarkerOptions()
                    .position(new LatLng(wildBomje.location.getLatitude(), wildBomje.location.getLongitude()))
                    .icon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons("bomje_derevenski", 130, 130))))
            );
        } else if (wildBomje.bomje.type.equals(BomjeType.DELOVOI)) {
            Markers.add(googleMap.addMarker(new MarkerOptions()
                    .position(new LatLng(wildBomje.location.getLatitude(), wildBomje.location.getLongitude()))
                    .icon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons("bomje_delovoi", 130, 130))))
            );
        } else if (wildBomje.bomje.type.equals(BomjeType.JIRNIY)) {
            Markers.add(googleMap.addMarker(new MarkerOptions()
                    .position(new LatLng(wildBomje.location.getLatitude(), wildBomje.location.getLongitude()))
                    .icon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons("bomje_jirniy", 130, 130))))
            );
        } else if (wildBomje.bomje.type.equals(BomjeType.MUTANT)) {
            Markers.add(googleMap.addMarker(new MarkerOptions()
                    .position(new LatLng(wildBomje.location.getLatitude(), wildBomje.location.getLongitude()))
                    .icon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons("bomje_mutant", 130, 130))))
            );
        } else if (wildBomje.bomje.type.equals(BomjeType.OPASNI)) {
            Markers.add(googleMap.addMarker(new MarkerOptions()
                    .position(new LatLng(wildBomje.location.getLatitude(), wildBomje.location.getLongitude()))
                    .icon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons("bomje_opasni", 130, 130))))
            );
        } else if (wildBomje.bomje.type.equals(BomjeType.S_BORODOY)) {
            Markers.add(googleMap.addMarker(new MarkerOptions()
                    .position(new LatLng(wildBomje.location.getLatitude(), wildBomje.location.getLongitude()))
                    .icon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons("bomje_s_borodoy", 130, 130))))
            );
        } else if (wildBomje.bomje.type.equals(BomjeType.SEXY)) {
            Markers.add(googleMap.addMarker(new MarkerOptions()
                    .position(new LatLng(wildBomje.location.getLatitude(), wildBomje.location.getLongitude()))
                    .icon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons("bomje_sexy", 130, 130))))
            );
        } else if (wildBomje.bomje.type.equals(BomjeType.SOZDATEL)) {
            Markers.add(googleMap.addMarker(new MarkerOptions()
                    .position(new LatLng(wildBomje.location.getLatitude(), wildBomje.location.getLongitude()))
                    .icon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons("bomje_sozdatel", 130, 130))))
            );
        } else if (wildBomje.bomje.type.equals(BomjeType.WITH_OGNETUSHITEL)) {
            Markers.add(googleMap.addMarker(new MarkerOptions()
                    .position(new LatLng(wildBomje.location.getLatitude(), wildBomje.location.getLongitude()))
                    .icon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons("bomje_with_ognetushitel", 130, 130))))
            );
        }
        Markers.get(Markers.size() - 1).setTag(wildBomje);
        if (Markers.get(Markers.size() - 1) != null) {
            Markers.get(Markers.size() - 1).setTag(wildBomje);
        }
    }

    @Override
    protected void onStart() {
        googleApiClient.connect();
        super.onStart();
    }

    @Override
    protected void onStop() {
        googleApiClient.disconnect();
        super.onStop();
    }

    @SuppressWarnings("MissingPermission")
    private void doStuff() {
        //     googleMap.getUiSettings().setAllGesturesEnabled(false);
        googleMap.getUiSettings().setRotateGesturesEnabled(true);
        googleMap.setMyLocationEnabled(true);
        googleMap.setBuildingsEnabled(true);

        LocationRequest request = new LocationRequest();
        request.setInterval(500);
        request.setFastestInterval(100);
        request.setPriority(PRIORITY_BALANCED_POWER_ACCURACY);
        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, request, this);

        googleMap.setOnMarkerClickListener(logic.getClickListener());
        logic.drawDbBomjes();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_FINE_LOCATION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    doStuff();
                } else {
                    // TODO: WE GOT NO PERMISSION
                }
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap map) {
        Log.d(TAG, "GoogleMap Ready");
        googleMap = map;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.d(TAG, "GoogleApi Connected");

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_FINE_LOCATION);
        } else {
            doStuff();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d(TAG, "GoogleApi Suspended");
        // TODO: X3
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "GoogleApi Connection Failed");
        // TODO: X3
    }

    @Override
    public void onLocationChanged(Location location) {
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 17);
        //   googleMap.animateCamera(cameraUpdate);

        if (prevLocation == null)
            prevLocation = location;
        double distanceToLast = location.distanceTo(prevLocation);
        if (distanceToLast < 10.00) {
            Log.d("DISTANCE", "Values too close, so not used.");
        } else {
            distance += distanceToLast;
            if (distanceToLast > 30.00) {
                for (Marker a : Markers) {
                    Location a1 = new Location(location);
                    a1.setLongitude(a.getPosition().longitude);
                    a1.setLatitude(a.getPosition().latitude);
                    if (location.distanceTo(a1) > 50) {
                        a.setVisible(false);
                        Log.d("bomje become invisible", "");
                    } else if (!a.isVisible()) {
                        a.setVisible(true);
                    }
                }
                addBomjeToLocation(location);
                distance = 0.00;
            }
            Log.d("DISTANCE", String.valueOf(distance));
        }
        prevLocation = location;
    }

    private static final String TAG = "MapActivity";
}
