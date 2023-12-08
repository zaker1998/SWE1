package map.utils;

public enum EField {
    GRASS(1, Math.max((int) (EMapBorders.HALFMAP.width() * EMapBorders.HALFMAP.height() * 0.48), 1)),
    WATER(1000, Math.max((int) (EMapBorders.HALFMAP.width() * EMapBorders.HALFMAP.height() * 0.10), 1)),
    MOUNTAIN(2, Math.max((int) (EMapBorders.HALFMAP.width() * EMapBorders.HALFMAP.height() * 0.14), 1));

    private final int cost;
    private final int minAmount;

    EField(int cost, int minAmount) {
        this.cost = cost;
        this.minAmount = minAmount;
    }

    public int cost() {
        return cost;
    }

    public int minAmount() {
        return minAmount;
    }
}
