package alexandvlad.bomjego.model;

import android.location.Location;
import android.support.annotation.NonNull;

public class WildBomjeEntry {

    @NonNull
    public final int id;

    @NonNull
    public final Bomje bomje;

    @NonNull
    public final Location location;

    public WildBomjeEntry(int id, @NonNull Bomje bomje, @NonNull Location location) {
        this.id = id;
        this.bomje = bomje;
        this.location = location;
    }

    @Override
    public String toString() {
        return "Bomje(" + bomje.toString() + "): latitude=" + location.getLatitude() + ", longitude=" + location.getLongitude();
    }

    private static final String TAG = "WildBomjeEntry";
}
