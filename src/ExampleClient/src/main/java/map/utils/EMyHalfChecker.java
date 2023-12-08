package map.utils;

public enum EMyHalfChecker {
    LONGMAPORIGIN(0, 9, 0, 4), LONGMAPOPPOSITE(10, 19, 0, 4), SQUAREMAPORIGIN(0, 9, 0, 4), SQUAREMAPOPPOSITE(0, 9, 5, 9);

    private final int xlower;
    private final int xupper;
    private final int ylower;
    private final int yupper;

    EMyHalfChecker(int xlower, int xupper, int ylower, int yupper) {
        this.xlower = xlower;
        this.xupper = xupper;
        this.ylower = ylower;
        this.yupper = yupper;
    }

    // always inclusive
    public int getxLowerBound() {
        return xlower;
    }

    public int getxUpperBound() {
        return xupper;
    }

    public int getyLowerBound() {
        return ylower;
    }

    public int getyUpperBound() {
        return yupper;
    }

    public EMyHalfChecker getOppositeHalf() {
        switch (this) {
            case LONGMAPORIGIN:
                return LONGMAPOPPOSITE;
            case LONGMAPOPPOSITE:
                return LONGMAPORIGIN;
            case SQUAREMAPORIGIN:
                return SQUAREMAPOPPOSITE;
            case SQUAREMAPOPPOSITE:
                return SQUAREMAPORIGIN;
        }
        throw new RuntimeException("The ENUM value was not defined!");
    }
}
