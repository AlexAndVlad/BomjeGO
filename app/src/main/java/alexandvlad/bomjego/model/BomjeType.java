package alexandvlad.bomjego.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import alexandvlad.bomjego.utils.Utils;

import static android.R.attr.type;

public enum BomjeType {
    ANTON(0, 500),
    JIRNIY(3, 500),
    S_BORODOY(6, 250),
    OPASNI(5, 100),
    WITH_OGNETUSHITEL(9, 50),
    DELOVOI(1, 25),
    DEREVENSKI(2, 25),
    MUTANT(4, 10),
    SEXY(7, 10),
    SOZDATEL(8, 1);

    private final int value;
    private final int probability;
    BomjeType(int value, int probability) {
        this.value = value;
        this.probability = probability;
    }

    public static BomjeType getRandomBomje() {
        int sum = 0;
        List<BomjeType> types = new ArrayList<>();
        for (BomjeType type : BomjeType.values()) {
            types.add(type);
            sum += type.probability;
        }
        int result = Utils.getRandomNumberInRange(0, sum);
        sum = 0;

        for (BomjeType type : BomjeType.values()) {
            sum += type.probability;
            if (result <= sum) {
                return type;
            }
        }
        return ANTON;
    }

    public static BomjeType fromInt(int id) {
        for (BomjeType type : BomjeType.values()) {
            if (type.value == id) {
                return type;
            }
        }
        return null;
    }

    public int getProbability() {
        return probability;
    }

    public int getValue() {
        return value;
    }
}
