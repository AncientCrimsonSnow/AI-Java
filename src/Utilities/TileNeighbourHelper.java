package Utilities;

public class TileNeighbourHelper {
    public int[] neighbours;

    public TileNeighbourHelper(int neighbour1, int neighbour2, int neighbours3) {
        neighbours = new int[]{
                neighbour1, neighbour2, neighbours3
        };
    }

    @Override
    public String toString() {
        return "[" + neighbours[0] + " | " + neighbours[1] + " | " + neighbours[2] + "]";
    }
}
