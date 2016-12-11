package alexandvlad.bomjego.database;

import android.provider.BaseColumns;

public class BomjeDbContract {

    private interface WildBomjeColumns extends BaseColumns, BomjeContract.BomjeColumns {
        String LATITUDE = "latitude";
        String LONGITUDE = "longitude";
        String[] ALL = {_ID, BOMJE_TYPE, WEIGHT, HEIGHT, LATITUDE, LONGITUDE};
    }

    static final class WildBomjeDb implements WildBomjeColumns {
        static final String TABLE = "wild_bomje";

        static final String CREATE_TABLE = "CREATE TABLE " + TABLE
                + " ("
                + _ID + " INTEGER PRIMARY KEY, "
                + BOMJE_TYPE + " INTEGER, "
                + WEIGHT + " INTEGER, "
                + HEIGHT + " INTEGER, "
                + LATITUDE + " REAL, "
                + LONGITUDE + " REAL)";
    }

    static final class CaughtBomjeDb implements WildBomjeColumns {
        static final String TABLE = "caught_bomje";

        static final String CREATE_TABLE = "CREATE TABLE " + TABLE
                + " ("
                + _ID + " INTEGER PRIMARY KEY, "
                + BOMJE_TYPE + " INTEGER, "
                + WEIGHT + " INTEGER, "
                + HEIGHT + " INTEGER, "
                + LATITUDE + " REAL, "
                + LONGITUDE + " REAL)";
    }

    private static final String TAG = "BomjeDbContract";
}
