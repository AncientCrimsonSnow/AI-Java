package Utilities;

public class TileNeighbourHelper {
    public int neighbour1;
    public int neighbour2;
    public int neighbour3;

    public TileNeighbourHelper(int neighbour1, int neighbour2, int neighbours3) {
        this.neighbour1 = neighbour1;
        this.neighbour2 = neighbour2;
        this.neighbour3 = neighbours3;
    }

    @Override
    public String toString() {
        return "[" + neighbour1 + " | " + neighbour2 + " | " + neighbour3 + "]";
    }
}
