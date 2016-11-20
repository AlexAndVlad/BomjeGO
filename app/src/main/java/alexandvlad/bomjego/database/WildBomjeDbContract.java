package alexandvlad.bomjego.database;

import android.provider.BaseColumns;

import java.util.Arrays;

public class WildBomjeDbContract {

    private interface WildBomjeColumns extends BaseColumns, BomjeContract.BomjeColumns {
        String LATITUDE = "latitude";
        String LONGITUDE = "longitude";
        String[] ALL = {_ID, BOMJE_TYPE, WIDTH, HEIGHT, LATITUDE, LONGITUDE};
    }

    static final class WildBomjeDb implements WildBomjeColumns {
        static final String TABLE = "wild_bomje";

        static final String CREATE_TABLE = "CREATE TABLE " + TABLE
                + " ("
                + _ID + " INTEGER PRIMARY KEY, "
                + BOMJE_TYPE + " INTEGER, "
                + WIDTH + " INTEGER, "
                + HEIGHT + " INTEGER, "
                + LATITUDE + " REAL, "
                + LONGITUDE + " REAL)";
    }

    private static final String TAG = "WildBomjeDbContract";
}
