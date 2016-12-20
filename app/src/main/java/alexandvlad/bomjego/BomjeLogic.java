package alexandvlad.bomjego;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import java.util.List;
import java.util.Random;

import alexandvlad.bomjego.database.CaughtBomjeDb;
import alexandvlad.bomjego.database.GlobalValues;
import alexandvlad.bomjego.database.WildBomjeDb;
import alexandvlad.bomjego.exceptions.BomjeDbException;
import alexandvlad.bomjego.model.Bomje;
import alexandvlad.bomjego.model.BomjeType;
import alexandvlad.bomjego.model.WildBomjeEntry;

public class BomjeLogic {
    private static final String LAST_INDEX = "last_index";
    private static final String TOTAL_CAUGHT_COUNT = "total_caught_count";
    private final AddBomjeListener bomjeListener;
    private final Button listButton;

    private GlobalValues globalValues;
    private WildBomjeDb bomjeDb;
    private CaughtBomjeDb caughtBomjeDb;

    private Context context;

    private GoogleMap.OnMarkerClickListener clickListener = new GoogleMap.OnMarkerClickListener() {
        @Override
        public boolean onMarkerClick(Marker marker) {
            Log.d(TAG, "YEAH!");
            try {
                caughtBomjeDb.put((WildBomjeEntry) marker.getTag());
                int count = globalValues.getOrPutInt(TOTAL_CAUGHT_COUNT, 0) + 1;
                globalValues.put(TOTAL_CAUGHT_COUNT, count);
                listButton.setText(context.getResources().getText(R.string.show_caught) + "(" + count + ")");
            } catch (BomjeDbException e) {
                Log.wtf(TAG, "Can't put caught bomje: ", e);
            }
            bomjeDb.delete((WildBomjeEntry) marker.getTag());
            marker.remove();
            return true;
        }
    };

    BomjeLogic(Context context, AddBomjeListener bomjeListener, Button listButton) {
        this.context = context;
        this.listButton = listButton;
        this.bomjeListener = bomjeListener;
        globalValues = new GlobalValues(context);
        bomjeDb = new WildBomjeDb(context);
        caughtBomjeDb = new CaughtBomjeDb(context);
    }

    private static int getRandomNumberInRange(int min, int max) {
        if (min >= max) {
            throw new IllegalArgumentException("max must be greater than min");
        }
        Random r = new Random();
        return r.nextInt((max - min) + 1) + min;
    }

    private void addBomjeToDb(Bomje bomje, Location location) {
        try {
            int id = globalValues.getOrPutInt(LAST_INDEX, 0);
            globalValues.put(LAST_INDEX, id + 1);
            WildBomjeEntry wildBomje = new WildBomjeEntry(id, bomje, location);
            bomjeDb.put(wildBomje);
            bomjeListener.addBomje(wildBomje);
        } catch (BomjeDbException e) {
            Log.wtf(TAG, e);
        }
    }

    void addBomje() {
        Location temp = new Location(LocationManager.GPS_PROVIDER);
        Random r = new Random();
        temp.setLatitude(r.nextDouble() * 90);
        temp.setLongitude(r.nextDouble() * 180);

        addBomjeToDb(new Bomje(BomjeType.fromInt(getRandomNumberInRange(0, 9)), 10, 10), temp); //TODO: different values
    }

    void addBomjeToLocation(Location location) {
        location.setLatitude(location.getLatitude() + ((double) getRandomNumberInRange(-1, 1)) / 1000);
        location.setLongitude(location.getLongitude() + ((double) getRandomNumberInRange(-1, 1)) / 1000);
        Bomje bomje = new Bomje(BomjeType.fromInt(getRandomNumberInRange(0, 9)), 10, 10); //TODO: different values
        addBomjeToDb(bomje, location);
    }

    void drawDbBomjes() {
        List<WildBomjeEntry> wildBomjes = bomjeDb.getAll();
        for (WildBomjeEntry i : wildBomjes) {
            bomjeListener.addBomje(i);
        }
        try {
            listButton.setText(context.getResources().getText(R.string.show_caught) + "(" + globalValues.getOrPutInt(TOTAL_CAUGHT_COUNT, 0) + ")");
        } catch (BomjeDbException ignored) {
        }
    }

    private static final String TAG = "BomjeLogic";

    public GoogleMap.OnMarkerClickListener getClickListener() {
        return clickListener;
    }
}
