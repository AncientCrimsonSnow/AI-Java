package Utilities;
public class BoardData {

    private final byte[] _data = new byte[9];

    @Override
    public String toString() {
        return  GetTileValueAt((byte)0, GetTileAndItsNeighbours(25)) + " " +
                GetTileValueAt((byte)0, GetTileAndItsNeighbours(26)) + " " +
                GetTileValueAt((byte)0, GetTileAndItsNeighbours(27)) + " " +
                GetTileValueAt((byte)0, GetTileAndItsNeighbours(28)) + " " +
                GetTileValueAt((byte)0, GetTileAndItsNeighbours(29)) + " " +
                GetTileValueAt((byte)0, GetTileAndItsNeighbours(30)) + " " +
                GetTileValueAt((byte)0, GetTileAndItsNeighbours(31)) + " " +
                GetTileValueAt((byte)0, GetTileAndItsNeighbours(32)) + " " +
                GetTileValueAt((byte)0, GetTileAndItsNeighbours(33)) + " " +
                GetTileValueAt((byte)0, GetTileAndItsNeighbours(34)) + " " +
                GetTileValueAt((byte)0, GetTileAndItsNeighbours(35)) + "\n" +

                "  " + GetTileValueAt((byte)0, GetTileAndItsNeighbours(16)) + " " +
                GetTileValueAt((byte)0, GetTileAndItsNeighbours(17)) + " " +
                GetTileValueAt((byte)0, GetTileAndItsNeighbours(18)) + " " +
                GetTileValueAt((byte)0, GetTileAndItsNeighbours(19)) + " " +
                GetTileValueAt((byte)0, GetTileAndItsNeighbours(20)) + " " +
                GetTileValueAt((byte)0, GetTileAndItsNeighbours(21)) + " " +
                GetTileValueAt((byte)0, GetTileAndItsNeighbours(22)) + " " +
                GetTileValueAt((byte)0, GetTileAndItsNeighbours(23)) + " " +
                GetTileValueAt((byte)0, GetTileAndItsNeighbours(24)) + "\n" +

                "    " + GetTileValueAt((byte)0, GetTileAndItsNeighbours(9)) + " " +
                GetTileValueAt((byte)0, GetTileAndItsNeighbours(10)) + " " +
                GetTileValueAt((byte)0, GetTileAndItsNeighbours(11)) + " " +
                GetTileValueAt((byte)0, GetTileAndItsNeighbours(12)) + " " +
                GetTileValueAt((byte)0, GetTileAndItsNeighbours(13)) + " " +
                GetTileValueAt((byte)0, GetTileAndItsNeighbours(14)) + " " +
                GetTileValueAt((byte)0, GetTileAndItsNeighbours(15)) + "\n" +

                "      " + GetTileValueAt((byte)0, GetTileAndItsNeighbours(4)) + " " +
                GetTileValueAt((byte)0, GetTileAndItsNeighbours(5)) + " " +
                GetTileValueAt((byte)0, GetTileAndItsNeighbours(6)) + " " +
                GetTileValueAt((byte)0, GetTileAndItsNeighbours(7)) + " " +
                GetTileValueAt((byte)0, GetTileAndItsNeighbours(8)) + "\n" +

                "        " + GetTileValueAt((byte)0, GetTileAndItsNeighbours(1)) + " " +
                GetTileValueAt((byte)0, GetTileAndItsNeighbours(2)) + " " +
                GetTileValueAt((byte)0, GetTileAndItsNeighbours(3)) + "\n" +

                "          " + GetTileValueAt((byte)0, GetTileAndItsNeighbours(0));
    }

    public BoardData() {
        SetTile((byte)0, (byte)1);
        SetTile((byte)1, (byte)1);
        SetTile((byte)2, (byte)1);
        SetTile((byte)3, (byte)1);

        SetTile((byte)16, (byte)2);
        SetTile((byte)25, (byte)2);
        SetTile((byte)26, (byte)2);
        SetTile((byte)27, (byte)2);

        SetTile((byte)24, (byte)3);
        SetTile((byte)33, (byte)3);
        SetTile((byte)34, (byte)3);
        SetTile((byte)35, (byte)3);
    }

    public void SetTile(byte index, byte valueByte) {
        byte value = (byte)(valueByte & BIT_INDEX_MASKS[0]);  // ersten 2 Bits geben den Wert an

        byte byteIndex = (byte)(index / 4);
        byte bitElementIndex = (byte)(index % 4);

        byte shiftedValue = (byte) (value << (bitElementIndex*2));
        _data[byteIndex] = (byte)((~BIT_INDEX_MASKS[bitElementIndex] & _data[byteIndex]) | (BIT_INDEX_MASKS[bitElementIndex] & shiftedValue));
    }

    public void SetTile(int index, int value){
        SetTile((byte)index, (byte)value);
    }

    public byte GetTileAndItsNeighbours(byte index){
        var byteIndex = index / 4;
        var bitIndex = index % 4;

        var neighboursIndices = Board.NEIGHBOUR_MAP[index];

        //tileValue shifted to Left
        var result = (byte) (BIT_INDEX_MASKS[3] & (_data[byteIndex] << (6 - (bitIndex * 2))));

        for(byte i = 0; i != neighboursIndices.length; i++){
            byteIndex = neighboursIndices[i] / 4;
            bitIndex = neighboursIndices[i] % 4;

            var mask = BIT_INDEX_MASKS[2 - i];
            var byteData = (int)_data[byteIndex];
            var shiftDigits = (byte) (4 - ((bitIndex + i) * 2));
            
            var shiftedByteData = shiftDigits > 0 ? (byteData << shiftDigits) : (byteData >> Math.abs(shiftDigits));

            var neighbourBit = (byte) (mask & shiftedByteData);
            result |= neighbourBit;
        }
        return result;
    }
    public byte GetTileAndItsNeighbours(int index){
        return GetTileAndItsNeighbours((byte)index);
    }

    public byte GetTileWithoutNeighbours(int index){
        return GetTileValueAt((byte) 0, GetTileAndItsNeighbours(index));
    }

    public byte[] GetDataClone(){
        return _data.clone();
    }

    //left to right
    //index 0 : 11000000
    //index 1 : 00110000
    //index 2 : 00001100
    //index 3 : 00000011
    public static byte GetTileValueAt(byte index, byte tileAndItsNeighbours){
        return (byte) ((tileAndItsNeighbours >> (6 - (index * 2))) & BIT_INDEX_MASKS[0]);
    }

    public static byte GetTileValueAt(int index, byte tileAndItsNeighbours){
        return GetTileValueAt((byte) index, tileAndItsNeighbours);
    }


    public static final int[] BIT_INDEX_MASKS = {
            0b00000011,
            0b00001100,
            0b00110000,
            0b11000000,
    };
}
