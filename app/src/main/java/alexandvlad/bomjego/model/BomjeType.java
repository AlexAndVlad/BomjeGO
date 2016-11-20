package alexandvlad.bomjego.model;

public enum BomjeType {
    NORMAL(0),
    WITH_BOX(1),
    RAIL_STATION(2),
    PARK(3);

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
