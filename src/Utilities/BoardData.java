package Utilities;
public class BoardData {

    private final byte[] _data = new byte[9];

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
        byte value = (byte)(valueByte & BIT_INDEX_MASKS[0]);  // Letzte 2 Bits geben den Wert an

        byte byteIndex = (byte)(index / 4);
        byte bitElementIndex = (byte)(index % 4);

        byte shiftedValue = (byte) (value << (bitElementIndex*2));
        _data[byteIndex] = (byte)((~BIT_INDEX_MASKS[bitElementIndex] & _data[byteIndex]) | (BIT_INDEX_MASKS[bitElementIndex] & shiftedValue));
    }

    public void SetTile(int index, int value){
        SetTile((byte)index, (byte)value);
    }

    public byte GetTileAndItsNeighbours(byte index){
        switch (index){
            case 0: return (byte)(((BIT_INDEX_MASKS[0] & _data[0]) << 6) | ((BIT_INDEX_MASKS[2] & _data[0]) >> 0));
            case 1: return (byte)(((BIT_INDEX_MASKS[1] & _data[0]) << 4) | ((BIT_INDEX_MASKS[1] & _data[1]) << 2) | ((BIT_INDEX_MASKS[2] & _data[0]) >> 2));
            case 2: return (byte)(((BIT_INDEX_MASKS[2] & _data[0]) << 2) | ((BIT_INDEX_MASKS[0] & _data[0]) << 4) | ((BIT_INDEX_MASKS[1] & _data[0]) >> 0) | ((BIT_INDEX_MASKS[3] & _data[0]) >> 6));
            case 3: return (byte)(((BIT_INDEX_MASKS[3] & _data[0]) >> 0) | ((BIT_INDEX_MASKS[2] & _data[0]) >> 0) | ((BIT_INDEX_MASKS[3] & _data[1]) >> 4));
            case 4: return (byte)(((BIT_INDEX_MASKS[0] & _data[1]) << 6) | ((BIT_INDEX_MASKS[1] & _data[1]) << 2) | ((BIT_INDEX_MASKS[2] & _data[2]) >> 2));
            case 5: return (byte)(((BIT_INDEX_MASKS[1] & _data[1]) << 4) | ((BIT_INDEX_MASKS[0] & _data[1]) << 4) | ((BIT_INDEX_MASKS[1] & _data[0]) >> 0) | ((BIT_INDEX_MASKS[2] & _data[1]) >> 4));
            case 6: return (byte)(((BIT_INDEX_MASKS[2] & _data[1]) << 2) | ((BIT_INDEX_MASKS[1] & _data[1]) << 2) | ((BIT_INDEX_MASKS[3] & _data[1]) >> 4) | ((BIT_INDEX_MASKS[0] & _data[3]) >> 0));
            case 7: return (byte)(((BIT_INDEX_MASKS[3] & _data[1]) >> 0) | ((BIT_INDEX_MASKS[2] & _data[1]) >> 0) | ((BIT_INDEX_MASKS[3] & _data[0]) >> 4) | ((BIT_INDEX_MASKS[0] & _data[2]) >> 0));
            case 8: return (byte)(((BIT_INDEX_MASKS[0] & _data[2]) << 6) | ((BIT_INDEX_MASKS[3] & _data[1]) >> 2) | ((BIT_INDEX_MASKS[2] & _data[3]) >> 2));
            case 9: return (byte)(((BIT_INDEX_MASKS[1] & _data[2]) << 4) | ((BIT_INDEX_MASKS[2] & _data[2]) >> 0) | ((BIT_INDEX_MASKS[1] & _data[4]) >> 0));
            case 10: return (byte)(((BIT_INDEX_MASKS[2] & _data[2]) << 2) | ((BIT_INDEX_MASKS[1] & _data[2]) << 2) | ((BIT_INDEX_MASKS[0] & _data[1]) << 2) | ((BIT_INDEX_MASKS[3] & _data[2]) >> 6));
            case 11: return (byte)(((BIT_INDEX_MASKS[3] & _data[2]) >> 0) | ((BIT_INDEX_MASKS[2] & _data[2]) >> 0) | ((BIT_INDEX_MASKS[0] & _data[3]) << 2) | ((BIT_INDEX_MASKS[3] & _data[4]) >> 6));
            case 12: return (byte)(((BIT_INDEX_MASKS[0] & _data[3]) << 6) | ((BIT_INDEX_MASKS[3] & _data[2]) >> 2) | ((BIT_INDEX_MASKS[2] & _data[1]) >> 2) | ((BIT_INDEX_MASKS[1] & _data[3]) >> 2));
            case 13: return (byte)(((BIT_INDEX_MASKS[1] & _data[3]) << 4) | ((BIT_INDEX_MASKS[0] & _data[3]) << 4) | ((BIT_INDEX_MASKS[1] & _data[5]) >> 0) | ((BIT_INDEX_MASKS[2] & _data[3]) >> 4));
            case 14: return (byte)(((BIT_INDEX_MASKS[2] & _data[3]) << 2) | ((BIT_INDEX_MASKS[1] & _data[3]) << 2) | ((BIT_INDEX_MASKS[0] & _data[2]) << 2) | ((BIT_INDEX_MASKS[3] & _data[3]) >> 6));
            case 15: return (byte)(((BIT_INDEX_MASKS[3] & _data[3]) >> 0) | ((BIT_INDEX_MASKS[2] & _data[3]) >> 0) | ((BIT_INDEX_MASKS[3] & _data[5]) >> 4));
            case 16: return (byte)(((BIT_INDEX_MASKS[0] & _data[4]) << 6) | ((BIT_INDEX_MASKS[1] & _data[4]) << 2) | ((BIT_INDEX_MASKS[2] & _data[6]) >> 2));
            case 17: return (byte)(((BIT_INDEX_MASKS[1] & _data[4]) << 4) | ((BIT_INDEX_MASKS[0] & _data[4]) << 4) | ((BIT_INDEX_MASKS[1] & _data[2]) >> 0) | ((BIT_INDEX_MASKS[2] & _data[4]) >> 4));
            case 18: return (byte)(((BIT_INDEX_MASKS[2] & _data[4]) << 2) | ((BIT_INDEX_MASKS[1] & _data[4]) << 2) | ((BIT_INDEX_MASKS[0] & _data[7]) << 2) | ((BIT_INDEX_MASKS[3] & _data[4]) >> 6));
            case 19: return (byte)(((BIT_INDEX_MASKS[3] & _data[4]) >> 0) | ((BIT_INDEX_MASKS[2] & _data[4]) >> 0) | ((BIT_INDEX_MASKS[3] & _data[2]) >> 4) | ((BIT_INDEX_MASKS[0] & _data[5]) >> 0));
            case 20: return (byte)(((BIT_INDEX_MASKS[0] & _data[5]) << 6) | ((BIT_INDEX_MASKS[3] & _data[4]) >> 2) | ((BIT_INDEX_MASKS[2] & _data[7]) >> 2) | ((BIT_INDEX_MASKS[1] & _data[5]) >> 2));
            case 21: return (byte)(((BIT_INDEX_MASKS[1] & _data[5]) << 4) | ((BIT_INDEX_MASKS[0] & _data[5]) << 4) | ((BIT_INDEX_MASKS[1] & _data[3]) >> 0) | ((BIT_INDEX_MASKS[2] & _data[5]) >> 4));
            case 22: return (byte)(((BIT_INDEX_MASKS[2] & _data[5]) << 2) | ((BIT_INDEX_MASKS[1] & _data[5]) << 2) | ((BIT_INDEX_MASKS[0] & _data[8]) << 2) | ((BIT_INDEX_MASKS[3] & _data[5]) >> 6));
            case 23: return (byte)(((BIT_INDEX_MASKS[3] & _data[5]) >> 0) | ((BIT_INDEX_MASKS[2] & _data[5]) >> 0) | ((BIT_INDEX_MASKS[3] & _data[3]) >> 4) | ((BIT_INDEX_MASKS[0] & _data[6]) >> 0));
            case 24: return (byte)(((BIT_INDEX_MASKS[0] & _data[6]) << 6) | ((BIT_INDEX_MASKS[3] & _data[5]) >> 2) | ((BIT_INDEX_MASKS[2] & _data[8]) >> 2));
            case 25: return (byte)(((BIT_INDEX_MASKS[1] & _data[6]) << 4) | ((BIT_INDEX_MASKS[2] & _data[6]) >> 0));
            case 26: return (byte)(((BIT_INDEX_MASKS[2] & _data[6]) << 2) | ((BIT_INDEX_MASKS[1] & _data[6]) << 2) | ((BIT_INDEX_MASKS[0] & _data[4]) << 2) | ((BIT_INDEX_MASKS[3] & _data[6]) >> 6));
            case 27: return (byte)(((BIT_INDEX_MASKS[3] & _data[6]) >> 0) | ((BIT_INDEX_MASKS[2] & _data[6]) >> 0) | ((BIT_INDEX_MASKS[0] & _data[7]) << 2));
            case 28: return (byte)(((BIT_INDEX_MASKS[0] & _data[7]) << 6) | ((BIT_INDEX_MASKS[3] & _data[6]) >> 2) | ((BIT_INDEX_MASKS[2] & _data[4]) >> 2) | ((BIT_INDEX_MASKS[1] & _data[7]) >> 2));
            case 29: return (byte)(((BIT_INDEX_MASKS[1] & _data[7]) << 4) | ((BIT_INDEX_MASKS[0] & _data[7]) << 4) | ((BIT_INDEX_MASKS[2] & _data[7]) >> 2));
            case 30: return (byte)(((BIT_INDEX_MASKS[2] & _data[7]) << 2) | ((BIT_INDEX_MASKS[1] & _data[7]) << 2) | ((BIT_INDEX_MASKS[0] & _data[5]) << 2) | ((BIT_INDEX_MASKS[3] & _data[7]) >> 6));
            case 31: return (byte)(((BIT_INDEX_MASKS[3] & _data[7]) >> 0) | ((BIT_INDEX_MASKS[2] & _data[7]) >> 0) | ((BIT_INDEX_MASKS[0] & _data[8]) << 2));
            case 32: return (byte)(((BIT_INDEX_MASKS[0] & _data[8]) << 6) | ((BIT_INDEX_MASKS[3] & _data[7]) >> 2) | ((BIT_INDEX_MASKS[2] & _data[5]) >> 2) | ((BIT_INDEX_MASKS[1] & _data[8]) >> 2));
            case 33: return (byte)(((BIT_INDEX_MASKS[1] & _data[8]) << 4) | ((BIT_INDEX_MASKS[0] & _data[8]) << 4) | ((BIT_INDEX_MASKS[2] & _data[8]) >> 2));
            case 34: return (byte)(((BIT_INDEX_MASKS[2] & _data[8]) << 2) | ((BIT_INDEX_MASKS[1] & _data[8]) << 2) | ((BIT_INDEX_MASKS[0] & _data[6]) << 2) | ((BIT_INDEX_MASKS[3] & _data[8]) >> 6));
            case 35: return (byte)(((BIT_INDEX_MASKS[3] & _data[8]) >> 0) | ((BIT_INDEX_MASKS[2] & _data[8]) >> 0));
        }

        return TILE_AND_ITS_NEIGHBOURS[index];
    }
    public byte GetTileAndItsNeighbours(int index){
        return GetTileAndItsNeighbours((byte)index);
    }

    public byte[] GetDataClone(){
        return _data.clone();
    }


    public static final byte[] BIT_INDEX_MASKS = {
            (byte)0b00000011,
            (byte)0b00001100,
            (byte)0b00110000,
            (byte)0b11000000,
    };

    private final byte[] TILE_AND_ITS_NEIGHBOURS = {
            (byte)(((BIT_INDEX_MASKS[0] & _data[0]) << 6) | ((BIT_INDEX_MASKS[2] & _data[0]) >> 0)),
            (byte)(((BIT_INDEX_MASKS[1] & _data[0]) << 4) | ((BIT_INDEX_MASKS[1] & _data[1]) << 2) | ((BIT_INDEX_MASKS[2] & _data[0]) >> 2)),
            (byte)(((BIT_INDEX_MASKS[2] & _data[0]) << 2) | ((BIT_INDEX_MASKS[0] & _data[0]) << 4) | ((BIT_INDEX_MASKS[1] & _data[0]) >> 0) | ((BIT_INDEX_MASKS[3] & _data[0]) >> 6)),
            (byte)(((BIT_INDEX_MASKS[3] & _data[0]) >> 0) | ((BIT_INDEX_MASKS[2] & _data[0]) >> 0) | ((BIT_INDEX_MASKS[3] & _data[1]) >> 4)),
            (byte)(((BIT_INDEX_MASKS[0] & _data[1]) << 6) | ((BIT_INDEX_MASKS[1] & _data[1]) << 2) | ((BIT_INDEX_MASKS[2] & _data[2]) >> 2)),
            (byte)(((BIT_INDEX_MASKS[1] & _data[1]) << 4) | ((BIT_INDEX_MASKS[0] & _data[1]) << 4) | ((BIT_INDEX_MASKS[1] & _data[0]) >> 0) | ((BIT_INDEX_MASKS[2] & _data[1]) >> 4)),
            (byte)(((BIT_INDEX_MASKS[2] & _data[1]) << 2) | ((BIT_INDEX_MASKS[1] & _data[1]) << 2) | ((BIT_INDEX_MASKS[3] & _data[1]) >> 4) | ((BIT_INDEX_MASKS[0] & _data[3]) >> 0)),
            (byte)(((BIT_INDEX_MASKS[3] & _data[1]) >> 0) | ((BIT_INDEX_MASKS[2] & _data[1]) >> 0) | ((BIT_INDEX_MASKS[3] & _data[0]) >> 4) | ((BIT_INDEX_MASKS[0] & _data[2]) >> 0)),
            (byte)(((BIT_INDEX_MASKS[0] & _data[2]) << 6) | ((BIT_INDEX_MASKS[3] & _data[1]) >> 2) | ((BIT_INDEX_MASKS[2] & _data[3]) >> 2)),
            (byte)(((BIT_INDEX_MASKS[1] & _data[2]) << 4) | ((BIT_INDEX_MASKS[2] & _data[2]) >> 0) | ((BIT_INDEX_MASKS[1] & _data[4]) >> 0)),
            (byte)(((BIT_INDEX_MASKS[2] & _data[2]) << 2) | ((BIT_INDEX_MASKS[1] & _data[2]) << 2) | ((BIT_INDEX_MASKS[0] & _data[1]) << 2) | ((BIT_INDEX_MASKS[3] & _data[2]) >> 6)),
            (byte)(((BIT_INDEX_MASKS[3] & _data[2]) >> 0) | ((BIT_INDEX_MASKS[2] & _data[2]) >> 0) | ((BIT_INDEX_MASKS[0] & _data[3]) << 2) | ((BIT_INDEX_MASKS[3] & _data[4]) >> 6)),
            (byte)(((BIT_INDEX_MASKS[0] & _data[3]) << 6) | ((BIT_INDEX_MASKS[3] & _data[2]) >> 2) | ((BIT_INDEX_MASKS[2] & _data[1]) >> 2) | ((BIT_INDEX_MASKS[1] & _data[3]) >> 2)),
            (byte)(((BIT_INDEX_MASKS[1] & _data[3]) << 4) | ((BIT_INDEX_MASKS[0] & _data[3]) << 4) | ((BIT_INDEX_MASKS[1] & _data[5]) >> 0) | ((BIT_INDEX_MASKS[2] & _data[3]) >> 4)),
            (byte)(((BIT_INDEX_MASKS[2] & _data[3]) << 2) | ((BIT_INDEX_MASKS[1] & _data[3]) << 2) | ((BIT_INDEX_MASKS[0] & _data[2]) << 2) | ((BIT_INDEX_MASKS[3] & _data[3]) >> 6)),
            (byte)(((BIT_INDEX_MASKS[3] & _data[3]) >> 0) | ((BIT_INDEX_MASKS[2] & _data[3]) >> 0) | ((BIT_INDEX_MASKS[3] & _data[5]) >> 4)),
            (byte)(((BIT_INDEX_MASKS[0] & _data[4]) << 6) | ((BIT_INDEX_MASKS[1] & _data[4]) << 2) | ((BIT_INDEX_MASKS[2] & _data[6]) >> 2)),
            (byte)(((BIT_INDEX_MASKS[1] & _data[4]) << 4) | ((BIT_INDEX_MASKS[0] & _data[4]) << 4) | ((BIT_INDEX_MASKS[1] & _data[2]) >> 0) | ((BIT_INDEX_MASKS[2] & _data[4]) >> 4)),
            (byte)(((BIT_INDEX_MASKS[2] & _data[4]) << 2) | ((BIT_INDEX_MASKS[1] & _data[4]) << 2) | ((BIT_INDEX_MASKS[0] & _data[7]) << 2) | ((BIT_INDEX_MASKS[3] & _data[4]) >> 6)),
            (byte)(((BIT_INDEX_MASKS[3] & _data[4]) >> 0) | ((BIT_INDEX_MASKS[2] & _data[4]) >> 0) | ((BIT_INDEX_MASKS[3] & _data[2]) >> 4) | ((BIT_INDEX_MASKS[0] & _data[5]) >> 0)),
            (byte)(((BIT_INDEX_MASKS[0] & _data[5]) << 6) | ((BIT_INDEX_MASKS[3] & _data[4]) >> 2) | ((BIT_INDEX_MASKS[2] & _data[7]) >> 2) | ((BIT_INDEX_MASKS[1] & _data[5]) >> 2)),
            (byte)(((BIT_INDEX_MASKS[1] & _data[5]) << 4) | ((BIT_INDEX_MASKS[0] & _data[5]) << 4) | ((BIT_INDEX_MASKS[1] & _data[3]) >> 0) | ((BIT_INDEX_MASKS[2] & _data[5]) >> 4)),
            (byte)(((BIT_INDEX_MASKS[2] & _data[5]) << 2) | ((BIT_INDEX_MASKS[1] & _data[5]) << 2) | ((BIT_INDEX_MASKS[0] & _data[8]) << 2) | ((BIT_INDEX_MASKS[3] & _data[5]) >> 6)),
            (byte)(((BIT_INDEX_MASKS[3] & _data[5]) >> 0) | ((BIT_INDEX_MASKS[2] & _data[5]) >> 0) | ((BIT_INDEX_MASKS[3] & _data[3]) >> 4) | ((BIT_INDEX_MASKS[0] & _data[6]) >> 0)),
            (byte)(((BIT_INDEX_MASKS[0] & _data[6]) << 6) | ((BIT_INDEX_MASKS[3] & _data[5]) >> 2) | ((BIT_INDEX_MASKS[2] & _data[8]) >> 2)),
            (byte)(((BIT_INDEX_MASKS[1] & _data[6]) << 4) | ((BIT_INDEX_MASKS[2] & _data[6]) >> 0)),
            (byte)(((BIT_INDEX_MASKS[2] & _data[6]) << 2) | ((BIT_INDEX_MASKS[1] & _data[6]) << 2) | ((BIT_INDEX_MASKS[0] & _data[4]) << 2) | ((BIT_INDEX_MASKS[3] & _data[6]) >> 6)),
            (byte)(((BIT_INDEX_MASKS[3] & _data[6]) >> 0) | ((BIT_INDEX_MASKS[2] & _data[6]) >> 0) | ((BIT_INDEX_MASKS[0] & _data[7]) << 2)),
            (byte)(((BIT_INDEX_MASKS[0] & _data[7]) << 6) | ((BIT_INDEX_MASKS[3] & _data[6]) >> 2) | ((BIT_INDEX_MASKS[2] & _data[4]) >> 2) | ((BIT_INDEX_MASKS[1] & _data[7]) >> 2)),
            (byte)(((BIT_INDEX_MASKS[1] & _data[7]) << 4) | ((BIT_INDEX_MASKS[0] & _data[7]) << 4) | ((BIT_INDEX_MASKS[2] & _data[7]) >> 2)),
            (byte)(((BIT_INDEX_MASKS[2] & _data[7]) << 2) | ((BIT_INDEX_MASKS[1] & _data[7]) << 2) | ((BIT_INDEX_MASKS[0] & _data[5]) << 2) | ((BIT_INDEX_MASKS[3] & _data[7]) >> 6)),
            (byte)(((BIT_INDEX_MASKS[3] & _data[7]) >> 0) | ((BIT_INDEX_MASKS[2] & _data[7]) >> 0) | ((BIT_INDEX_MASKS[0] & _data[8]) << 2)),
            (byte)(((BIT_INDEX_MASKS[0] & _data[8]) << 6) | ((BIT_INDEX_MASKS[3] & _data[7]) >> 2) | ((BIT_INDEX_MASKS[2] & _data[5]) >> 2) | ((BIT_INDEX_MASKS[1] & _data[8]) >> 2)),
            (byte)(((BIT_INDEX_MASKS[1] & _data[8]) << 4) | ((BIT_INDEX_MASKS[0] & _data[8]) << 4) | ((BIT_INDEX_MASKS[2] & _data[8]) >> 2)),
            (byte)(((BIT_INDEX_MASKS[2] & _data[8]) << 2) | ((BIT_INDEX_MASKS[1] & _data[8]) << 2) | ((BIT_INDEX_MASKS[0] & _data[6]) << 2) | ((BIT_INDEX_MASKS[3] & _data[8]) >> 6)),
            (byte)(((BIT_INDEX_MASKS[3] & _data[8]) >> 0) | ((BIT_INDEX_MASKS[2] & _data[8]) >> 0)),
    };
}
