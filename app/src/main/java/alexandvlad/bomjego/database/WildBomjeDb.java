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

import java.util.ArrayList;
import java.util.List;

import alexandvlad.bomjego.exceptions.BomjeDbException;
import alexandvlad.bomjego.model.Bomje;
import alexandvlad.bomjego.model.BomjeType;
import alexandvlad.bomjego.model.WildBomjeEntry;

public class WildBomjeDb {

    private final Context context;

    @AnyThread
    public WildBomjeDb(@NonNull Context context) {
        this.context = context.getApplicationContext();
    }

    @WorkerThread
    public void put(@NonNull WildBomjeEntry entry) throws BomjeDbException {
        SQLiteDatabase db = BomjeDbHelper.getInstance(context).getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(BomjeDbContract.WildBomjeDb._ID, entry.id);
        values.put(BomjeDbContract.WildBomjeDb.BOMJE_TYPE, entry.bomje.type.getValue());
        values.put(BomjeDbContract.WildBomjeDb.WEIGHT, entry.bomje.weight);
        values.put(BomjeDbContract.WildBomjeDb.HEIGHT, entry.bomje.height);
        values.put(BomjeDbContract.WildBomjeDb.LATITUDE, entry.location.getLatitude());
        values.put(BomjeDbContract.WildBomjeDb.LONGITUDE, entry.location.getLongitude());

        if (db.insert(BomjeDbContract.WildBomjeDb.TABLE, null, values) == -1) {
            throw new BomjeDbException("Can't add into data base: " + entry.toString());
        }
    }

    @WorkerThread
    public List<WildBomjeEntry> getAll() {
        SQLiteDatabase db = BomjeDbHelper.getInstance(context).getReadableDatabase();

        List<WildBomjeEntry> wildBomjes = new ArrayList<>();

        try (Cursor cursor = db.query(
                BomjeDbContract.WildBomjeDb.TABLE,
                BomjeDbContract.WildBomjeDb.ALL,
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
        SQLiteDatabase db = BomjeDbHelper.getInstance(context).getWritableDatabase();
        db.execSQL("DELETE FROM " + BomjeDbContract.WildBomjeDb.TABLE + " WHERE " + BomjeDbContract.WildBomjeDb._ID + "=" + entry.id);
    }

    private static final String TAG = "WildBomjeDb";
}
