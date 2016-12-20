package alexandvlad.bomjego.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import alexandvlad.bomjego.utils.DatabaseCorruptionHandler;

class BomjeDbHelper extends SQLiteOpenHelper {

    private static final String DB_FILE_NAME = "wild_bomje.db";

    private static final int DB_VERSION = 13;

    private static volatile BomjeDbHelper instance;

    static BomjeDbHelper getInstance(Context context) {
        if (instance == null) {
            synchronized (BomjeDbHelper.class) {
                if (instance == null) {
                    instance = new BomjeDbHelper(context);
                }
            }
        }
        return instance;
    }

    private BomjeDbHelper(Context context) {
        super(context, DB_FILE_NAME, null /*factory*/, DB_VERSION,
                new DatabaseCorruptionHandler(context, DB_FILE_NAME));
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(BomjeDbContract.WildBomjeDb.CREATE_TABLE);
        sqLiteDatabase.execSQL(BomjeDbContract.CaughtBomjeDb.CREATE_TABLE);
        sqLiteDatabase.execSQL(BomjeDbContract.GlobalDb.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + BomjeDbContract.WildBomjeDb.TABLE);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + BomjeDbContract.CaughtBomjeDb.TABLE);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + BomjeDbContract.GlobalDb.TABLE);
        onCreate(sqLiteDatabase);
    }

    private static final String TAG = "BomjeDbHelper";
}
