package alexandvlad.bomjego;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
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

    public void addBomjeToLocation(Location location) {
        Random r = new Random();
        location.setLatitude(location.getLatitude() +((double)getRandomNumberInRange(-1, 1)) / 1000);
        location.setLongitude(location.getLongitude() + ((double)getRandomNumberInRange(-1,1)) / 1000);
        Bomje bomje = new Bomje(BomjeType.fromInt(getRandomNumberInRange(0, 3)), 10, 10);
        boolean f = true;
        while (f) {
            WildBomjeEntry wildBomje = new WildBomjeEntry(id++, bomje, location);
            try {
                bomjeDb.put(wildBomje);
                addBomjeMarker(wildBomje);
                f = false;
            } catch (BomjeDbException ignored) {
            }
        }
    }

    public Bitmap resizeMapIcons(String iconName,int width, int height){
        Bitmap imageBitmap = BitmapFactory.decodeResource(getResources(),getResources().getIdentifier(iconName, "drawable", getPackageName()));
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(imageBitmap, width, height, false);
        return resizedBitmap;
    }

    private void addBomjeMarker(WildBomjeEntry wildBomje) {
        Marker a = null;

        if (wildBomje.bomje.type.equals(BomjeType.ANTON)) {
            a = googleMap.addMarker(new MarkerOptions()
                    .position(new LatLng(wildBomje.location.getLatitude(), wildBomje.location.getLongitude()))
                    .icon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons("bomje_anton",130,130)))
            );
        } else if (wildBomje.bomje.type.equals(BomjeType.OPASNI)) {
            a = googleMap.addMarker(new MarkerOptions()
                    .position(new LatLng(wildBomje.location.getLatitude(), wildBomje.location.getLongitude()))
                    .icon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons("bomje_opasni",130,130)))
            );
        } else if (wildBomje.bomje.type.equals(BomjeType.DEREVENSKI)) {
            a = googleMap.addMarker(new MarkerOptions()
                    .position(new LatLng(wildBomje.location.getLatitude(), wildBomje.location.getLongitude()))
                    .icon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons("bomje_derevenski",130,130)))
            );
        } else if (wildBomje.bomje.type.equals(BomjeType.DELOVOI)) {
            a = googleMap.addMarker(new MarkerOptions()
                    .position(new LatLng(wildBomje.location.getLatitude(), wildBomje.location.getLongitude()))
                    .icon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons("bomje_delovoi",130,130)))
            );
        } else if (wildBomje.bomje.type.equals(BomjeType.JIRNIY)) {
            a = googleMap.addMarker(new MarkerOptions()
                    .position(new LatLng(wildBomje.location.getLatitude(), wildBomje.location.getLongitude()))
                    .icon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons("bomje_jirniy",130,130)))
            );
        } else if (wildBomje.bomje.type.equals(BomjeType.MUTANT)) {
            a = googleMap.addMarker(new MarkerOptions()
                    .position(new LatLng(wildBomje.location.getLatitude(), wildBomje.location.getLongitude()))
                    .icon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons("bomje_mutant",130,130)))
            );
        } else if (wildBomje.bomje.type.equals(BomjeType.OPASNI)) {
            a = googleMap.addMarker(new MarkerOptions()
                    .position(new LatLng(wildBomje.location.getLatitude(), wildBomje.location.getLongitude()))
                    .icon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons("bomje_opasni",130,130)))
            );
        } else if (wildBomje.bomje.type.equals(BomjeType.S_BORODOY)) {
            a = googleMap.addMarker(new MarkerOptions()
                    .position(new LatLng(wildBomje.location.getLatitude(), wildBomje.location.getLongitude()))
                    .icon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons("bomje_s_borodoy",130,130)))
            );
        } else if (wildBomje.bomje.type.equals(BomjeType.SEXY)) {
            a = googleMap.addMarker(new MarkerOptions()
                    .position(new LatLng(wildBomje.location.getLatitude(), wildBomje.location.getLongitude()))
                    .icon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons("bomje_sexy",130,130)))
            );
        } else if (wildBomje.bomje.type.equals(BomjeType.SOZDATEL)) {
            a = googleMap.addMarker(new MarkerOptions()
                    .position(new LatLng(wildBomje.location.getLatitude(), wildBomje.location.getLongitude()))
                    .icon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons("bomje_sozdatel",130,130)))
            );
        } else if (wildBomje.bomje.type.equals(BomjeType.WITH_OGNETUSHITEL)) {
            a = googleMap.addMarker(new MarkerOptions()
                    .position(new LatLng(wildBomje.location.getLatitude(), wildBomje.location.getLongitude()))
                    .icon(BitmapDescriptorFactory.fromBitmap(resizeMapIcons("bomje_with_ognetushitel",130,130)))
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
        googleMap.getUiSettings().setAllGesturesEnabled(false);
        googleMap.getUiSettings().setRotateGesturesEnabled(true);
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
        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 17);
        googleMap.animateCamera(cameraUpdate);

        myLatLng = new LatLng(location.getLatitude(), location.getLongitude());

        if(prevLocation == null)
            prevLocation = location;
        double distanceToLast = location.distanceTo(prevLocation);
        if (distanceToLast < 10.00) {
            Log.d("DISTANCE", "Values too close, so not used.");
        } else {
            distance += distanceToLast;
            if(distanceToLast > 20.00) {
                addBomjeToLocation(location);
                distance = 0.00;
            }
            Log.d("DISTANCE", String.valueOf(distance));
        }
        prevLocation = location;
    }

    private static final String TAG = "MapActivity";
}
