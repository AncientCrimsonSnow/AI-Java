package Utilities;

import java.util.Iterator;

public class TileNeighbourHelper implements Iterable<Byte>{
    private byte[] neighbours;

    public TileNeighbourHelper(byte[] neighbours) {
        neighbours = neighbours;
    }

    @Override
    public String toString() {
        return "[" + neighbours[0] + " | " + neighbours[1] + " | " + neighbours[2] + "]";
    }

    @Override
    public Iterator<Byte> iterator() {
        return new Iterator<Byte>() {
            private byte pos = 0;
            @Override
            public boolean hasNext() {
                return pos < neighbours.length;
            }

            @Override
            public Byte next() {
                return neighbours[pos++];
            }
        };
    }
}
