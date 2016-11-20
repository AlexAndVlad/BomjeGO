package alexandvlad.bomjego.model;

import android.support.annotation.NonNull;

public class Bomje {

    @NonNull
    public final BomjeType type;

    @NonNull
    public final int wight;

    @NonNull
    public final int height;

    public Bomje(BomjeType type, int wight, int height) {
        this.type = type;
        this.wight = wight;
        this.height = height;
    }

    @Override
    public String toString() {
        return type + ": width=" + wight + ", height=" + height;
    }
}
