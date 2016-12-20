package alexandvlad.bomjego.model;

public enum BomjeType {
    ANTON(0),
    DELOVOI(1),
    DEREVENSKI(2),
    JIRNIY(3),
    MUTANT(4),
    OPASNI(5),
    S_BORODOY(6),
    SEXY(7),
    SOZDATEL(8),
    WITH_OGNETUSHITEL(9);

    private final int value;
    BomjeType(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static BomjeType fromInt(int id) {
        for (BomjeType type : BomjeType.values()) {
            if (type.value == id) {
                return type;
            }
        }
        return null;
    }
}
