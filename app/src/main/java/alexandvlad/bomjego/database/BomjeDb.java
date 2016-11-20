package alexandvlad.bomjego.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.location.LocationManager;
import android.support.annotation.AnyThread;
import android.support.annotation.NonNull;
import android.support.annotation.WorkerThread;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import alexandvlad.bomjego.exceptions.BomjeDbException;
import alexandvlad.bomjego.model.Bomje;
import alexandvlad.bomjego.model.BomjeType;
import alexandvlad.bomjego.model.WildBomjeEntry;

import static android.R.attr.id;
import static android.R.attr.version;
import static android.os.Build.ID;

public class BomjeDb {

    private final Context context;

    @AnyThread
    public BomjeDb(@NonNull Context context) {
        this.context = context.getApplicationContext();
    }

    @WorkerThread
    public void put(@NonNull WildBomjeEntry entry) throws BomjeDbException {
        SQLiteDatabase db = WildBomjeDbHelper.getInstance(context).getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(WildBomjeDbContract.WildBomjeDb._ID, entry.id);
        values.put(WildBomjeDbContract.WildBomjeDb.BOMJE_TYPE, entry.bomje.type.getValue());
        values.put(WildBomjeDbContract.WildBomjeDb.WIDTH, entry.bomje.wight);
        values.put(WildBomjeDbContract.WildBomjeDb.HEIGHT, entry.bomje.height);
        values.put(WildBomjeDbContract.WildBomjeDb.LATITUDE, entry.location.getLatitude());
        values.put(WildBomjeDbContract.WildBomjeDb.LONGITUDE, entry.location.getLongitude());

        if (db.insert(WildBomjeDbContract.WildBomjeDb.TABLE, null, values) == -1) {
            throw new BomjeDbException("Can't add into data base: " + entry.toString());
        }
    }

    @WorkerThread
    public List<WildBomjeEntry> getAll() {
        SQLiteDatabase db = WildBomjeDbHelper.getInstance(context).getReadableDatabase();

        List<WildBomjeEntry> wildBomjes = new ArrayList<>();

        try (Cursor cursor = db.query(
                WildBomjeDbContract.WildBomjeDb.TABLE,
                WildBomjeDbContract.WildBomjeDb.ALL,
                "1",
                null,
                null,
                null,
                null)) {
            if (cursor != null && cursor.moveToFirst()) {
                for (; !cursor.isAfterLast(); cursor.moveToNext()) {
                    int i = 0;
                    int id = cursor.getInt(i++);
                    BomjeType type = BomjeType.fromInt(cursor.getInt(i++));
                    int width = cursor.getInt(i++);
                    int height = cursor.getInt(i++);
                    double latitude = cursor.getDouble(i++);
                    double longitude = cursor.getDouble(i);
                    Location temp = new Location(LocationManager.GPS_PROVIDER);
                    temp.setLatitude(latitude);
                    temp.setLongitude(longitude);
                    WildBomjeEntry entry = new WildBomjeEntry(id, new Bomje(type, width, height), temp);
                    wildBomjes.add(entry);
                }
            }
        }

        return wildBomjes;
    }

    @WorkerThread
    public void delete(WildBomjeEntry entry) {
        SQLiteDatabase db = WildBomjeDbHelper.getInstance(context).getWritableDatabase();
        db.execSQL("DELETE FROM " + WildBomjeDbContract.WildBomjeDb.TABLE + " WHERE " + WildBomjeDbContract.WildBomjeDb._ID + "=" + entry.id);
    }

    private static final String TAG = "BomjeDb";
}
