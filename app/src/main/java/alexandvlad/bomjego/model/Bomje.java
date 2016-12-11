package alexandvlad.bomjego.model;

import android.support.annotation.NonNull;

public class Bomje {

    @NonNull
    public final BomjeType type;

    @NonNull
    public final int weight;

    @NonNull
    public final int height;

    public Bomje(BomjeType type, int weight, int height) {
        this.type = type;
        this.weight = weight;
        this.height = height;
    }

    @Override
    public String toString() {
        return type + ": width=" + weight + ", height=" + height;
    }
}
