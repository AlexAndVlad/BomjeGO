package alexandvlad.bomjego.database;

import android.provider.BaseColumns;

import static alexandvlad.bomjego.database.BomjeContract.BomjeColumns.BOMJE_TYPE;

class BomjeDbContract {

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

    static final class GlobalDb implements BaseColumns {
        static final String TABLE = "global_variables";

        static final String VALUE = "value";

        static final String CREATE_TABLE = "CREATE TABLE " + TABLE
                + " ("
                + _ID + " TEXT, "
                + VALUE + " BLOB)";
    }

    private static final String TAG = "BomjeDbContract";
}
