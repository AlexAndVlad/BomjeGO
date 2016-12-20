package alexandvlad.bomjego.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.AnyThread;
import android.support.annotation.NonNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import alexandvlad.bomjego.exceptions.BomjeDbException;
import alexandvlad.bomjego.model.WildBomjeEntry;
import alexandvlad.bomjego.utils.Utils;

public class GlobalValues {
    private final Context context;

    @AnyThread
    public GlobalValues(@NonNull Context context) {
        this.context = context.getApplicationContext();
    }

    public void put(String name, Object value) throws BomjeDbException {
        SQLiteDatabase db = BomjeDbHelper.getInstance(context).getWritableDatabase();
        db.execSQL("DELETE FROM " + BomjeDbContract.GlobalDb.TABLE + " WHERE " + BomjeDbContract.GlobalDb._ID + "='" + name + "'");

        ContentValues values = new ContentValues();
        values.put(BomjeDbContract.GlobalDb._ID, name);
        try {
            values.put(BomjeDbContract.GlobalDb.VALUE, Utils.convertToBytes(value));
        } catch (IOException e) {
            throw new BomjeDbException("Can't add into data base: " + value.toString());
        }
        if (db.insert(BomjeDbContract.GlobalDb.TABLE, null, values) == -1) {
            throw new BomjeDbException("Can't add into data base: " + value.toString());
        }
    }

    private Object get(String name) throws BomjeDbException {
        SQLiteDatabase db = BomjeDbHelper.getInstance(context).getReadableDatabase();

        List<WildBomjeEntry> wildBomjes = new ArrayList<>();

        Object result;
        try (Cursor cursor = db.query(
                BomjeDbContract.GlobalDb.TABLE,
                new String[]{BomjeDbContract.GlobalDb.VALUE},
                BomjeDbContract.GlobalDb._ID + "=?",
                new String[]{name},
                null,
                null,
                null)) {
            if (cursor != null && cursor.moveToFirst() && cursor.isLast()) {
                result = Utils.convertFromBytes(cursor.getBlob(0));
            } else {
                throw new BomjeDbException("Can't get from data base: " + name);
            }
        } catch (ClassNotFoundException | IOException e) {
            throw new BomjeDbException("Can't get from data base: " + name);
        }

        return result;
    }

    public int getOrPutInt(String name, int defaultValue) throws BomjeDbException {
        int result = defaultValue;
        try {
            Object tmp = get(name);
            result = (int) tmp;
        } catch (BomjeDbException e) {
            put(name, defaultValue);
        }
        return result;
    }

    private static final String TAG = "GlobalValues";
}
