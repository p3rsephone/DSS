package business.courses;

public enum Year {
    FIRST,
    SECOND,
    THIRD,
    FOURTH,
    FIFTH;

    public Year fromInteger(int x){
        switch (x){
            case 1:
            case 2:
                return FIRST;
            case 3:
            case 4:
                return SECOND;
            case 5:
            case 6:
                return THIRD;
            case 7:
            case 8:
                return FOURTH;
            case 9:
            case 10:
                return FIFTH;
            default: throw new IllegalStateException("Illegal semester");
        }
    }

}
