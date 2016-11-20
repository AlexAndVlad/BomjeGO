package alexandvlad.bomjego.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import alexandvlad.bomjego.utils.DatabaseCorruptionHandler;

class WildBomjeDbHelper extends SQLiteOpenHelper {

    private static final String DB_FILE_NAME = "wild_bomje.db";

    private static final int DB_VERSION = 3;

    private static volatile WildBomjeDbHelper instance;

    static WildBomjeDbHelper getInstance(Context context) {
        if (instance == null) {
            synchronized (WildBomjeDbHelper.class) {
                if (instance == null) {
                    instance = new WildBomjeDbHelper(context);
                }
            }
        }
        return instance;
    }

    private WildBomjeDbHelper(Context context) {
        super(context, DB_FILE_NAME, null /*factory*/, DB_VERSION,
                new DatabaseCorruptionHandler(context, DB_FILE_NAME));
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(WildBomjeDbContract.WildBomjeDb.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE " + WildBomjeDbContract.WildBomjeDb.TABLE);
        onCreate(sqLiteDatabase);
    }

    private static final String TAG = "WildBomjeDbHelper";
}
