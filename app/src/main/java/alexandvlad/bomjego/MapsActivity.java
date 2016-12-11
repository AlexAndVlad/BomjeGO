package alexandvlad.bomjego;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;
import java.util.Random;

import alexandvlad.bomjego.database.CaughtBomjeDb;
import alexandvlad.bomjego.database.WildBomjeDb;
import alexandvlad.bomjego.exceptions.BomjeDbException;
import alexandvlad.bomjego.model.Bomje;
import alexandvlad.bomjego.model.BomjeType;
import alexandvlad.bomjego.model.WildBomjeEntry;

import static com.google.android.gms.location.LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY;

public class MapsActivity extends FragmentActivity implements
        OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    private static final int MY_PERMISSIONS_REQUEST_FINE_LOCATION = 1;

    private GoogleMap googleMap;
    private GoogleApiClient googleApiClient;
    private WildBomjeDb bomjeDb;
    private CaughtBomjeDb caughtBomjeDb;
    private LatLng myLatLng;

    Marker marker_1;

    public void onClickShowBomje(View view) {
        Intent intent = new Intent(this, CaughtListActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
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

        bomjeDb = new WildBomjeDb(this);
        caughtBomjeDb = new CaughtBomjeDb(this);
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

    private static int getRandomNumberInRange(int min, int max) {
        if (min >= max) {
            throw new IllegalArgumentException("max must be greater than min");
        }
        Random r = new Random();
        return r.nextInt((max - min) + 1) + min;
    }

    private int id = 0;

    public void addBomje(View view) {
        Location temp = new Location(LocationManager.GPS_PROVIDER);
        Random r = new Random();
        temp.setLatitude(r.nextDouble() * 90);
        temp.setLongitude(r.nextDouble() * 180);

        Bomje bomje = new Bomje(BomjeType.fromInt(getRandomNumberInRange(0, 3)), 10, 10);
        boolean f = true;
        while (f) {
            WildBomjeEntry wildBomje = new WildBomjeEntry(id++, bomje, temp);
            try {
                bomjeDb.put(wildBomje);
                addBomjeMarker(wildBomje);
                f = false;
            } catch (BomjeDbException ignored) {
            }
        }
    }

    private void addBomjeMarker(WildBomjeEntry wildBomje) {
        Marker a = null;
        if (wildBomje.bomje.type.equals(BomjeType.NORMAL)) {
            a = googleMap.addMarker(new MarkerOptions()
                    .position(new LatLng(wildBomje.location.getLatitude(), wildBomje.location.getLongitude()))
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.bomje1))
            );
        } else if (wildBomje.bomje.type.equals(BomjeType.WITH_BOX)) {
            a = googleMap.addMarker(new MarkerOptions()
                    .position(new LatLng(wildBomje.location.getLatitude(), wildBomje.location.getLongitude()))
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.bomje2))
            );
        } else if (wildBomje.bomje.type.equals(BomjeType.RAIL_STATION)) {
            a = googleMap.addMarker(new MarkerOptions()
                    .position(new LatLng(wildBomje.location.getLatitude(), wildBomje.location.getLongitude()))
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.bomje3))
            );
        } else if (wildBomje.bomje.type.equals(BomjeType.PARK)) {
            a = googleMap.addMarker(new MarkerOptions()
                    .position(new LatLng(wildBomje.location.getLatitude(), wildBomje.location.getLongitude()))
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.bomje4))
            );
        }
        if (a != null) {
            a.setTag(wildBomje);
        }
    }

    private void drawDbBomjes() {
        List<WildBomjeEntry> wildBomjes = bomjeDb.getAll();
        for (WildBomjeEntry i : wildBomjes) {
            addBomjeMarker(i);
        }
    }

    @SuppressWarnings("MissingPermission")
    private void doStuff() {
        googleMap.setMyLocationEnabled(true);
        googleMap.setBuildingsEnabled(true);

        LocationRequest request = new LocationRequest();
        request.setInterval(500);
        request.setFastestInterval(100);
        request.setPriority(PRIORITY_BALANCED_POWER_ACCURACY);
        LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, request, this);

        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Log.d(TAG, "YEAH!");
                try {
                    caughtBomjeDb.put((WildBomjeEntry) marker.getTag());
                } catch (BomjeDbException e) {
                    Log.wtf(TAG, "Can't put caught bomje: ", e);
                }
                bomjeDb.delete((WildBomjeEntry) marker.getTag());
                marker.remove();
                return true;
            }

        });

        drawDbBomjes();
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
        //mLastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        LatLng currentPosition = new LatLng(location.getLatitude(), location.getLongitude());

        myLatLng = new LatLng(location.getLatitude(), location.getLongitude());

    }

    private static final String TAG = "MapActivity";
}
