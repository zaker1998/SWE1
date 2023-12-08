package map.utils;

public enum EMapBorders {
    HALFMAP(10, 5), LONGMAP(20, 5);

    private final int width;
    private final int height;

    EMapBorders(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public int width() {
        return width;
    }

    public int height() {
        return height;
    }
}
