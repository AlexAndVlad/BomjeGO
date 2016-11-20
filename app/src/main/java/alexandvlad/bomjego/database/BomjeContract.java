package alexandvlad.bomjego.database;

import android.provider.BaseColumns;

public class BomjeContract {

    interface BomjeColumns extends BaseColumns {
        String BOMJE_TYPE = "bomje_type";
        String WIDTH = "width";
        String HEIGHT = "height";
    }

    private static final String TAG = "BomjeContract";
}
