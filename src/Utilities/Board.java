package Utilities;


import lenz.htw.loki.Move;

import java.util.ArrayList;

public class Board {

    private static final byte  TILE_COUNT = 36;
    private final BoardData _board;
    private final PlayerNumber _perspective;

    private byte[] _perspectiveMapOut;
    private byte[] _perspectiveMapIn;

    private byte _stoneCount = 4;

    public Board(PlayerNumber perspective) {

        _perspective = perspective;

        switch (_perspective) {
            case p0:
                _perspectiveMapIn = PLAYER_0_PERSPECTIVE_MAP;
                _perspectiveMapOut = PLAYER_0_PERSPECTIVE_MAP;
                break;
            case p1:
                _perspectiveMapIn = PLAYER_2_PERSPECTIVE_MAP;
                _perspectiveMapOut = PLAYER_1_PERSPECTIVE_MAP;
                break;
            default:
                _perspectiveMapIn = PLAYER_1_PERSPECTIVE_MAP;
                _perspectiveMapOut = PLAYER_2_PERSPECTIVE_MAP;
                break;
        }
        _board = new BoardData();
    }

    public Board(PlayerNumber perspective, BoardData board) {

        _perspective = perspective;

        switch (_perspective) {
            case p0:
                _perspectiveMapIn = PLAYER_0_PERSPECTIVE_MAP;
                _perspectiveMapOut = PLAYER_0_PERSPECTIVE_MAP;
                break;
            case p1:
                _perspectiveMapIn = PLAYER_2_PERSPECTIVE_MAP;
                _perspectiveMapOut = PLAYER_1_PERSPECTIVE_MAP;
                break;
            case p2:
                _perspectiveMapIn = PLAYER_1_PERSPECTIVE_MAP;
                _perspectiveMapOut = PLAYER_2_PERSPECTIVE_MAP;
        }
        _board = board;
    }

    public void UpdateBoard(Move move){
        var perspectiveMoveFrom = _perspectiveMapIn[move.from];
        var perspectiveMoveTo = _perspectiveMapIn[move.to];

        var moveFromTile = _board.GetTileAndItsNeighbours(perspectiveMoveFrom) >> 6;
        var moveToTile = _board.GetTileAndItsNeighbours(perspectiveMoveTo) >> 6;

        _board.SetTile(perspectiveMoveFrom, 0);
        _board.SetTile(perspectiveMoveTo, moveFromTile);

        if(move.push <= 35){
            _board.SetTile(_perspectiveMapIn[move.push], moveToTile);
        }

        CheckTilesForLoneliness();
    }

    public byte[] GetBoard(){
        return _board.GetDataClone();
    }

    public boolean DeadBoard(){
        return _stoneCount <= 2;
    }

    public boolean WinningBoard(){
        var stonesOnFinishLine = 0;
        for(var finishTile : FINISH_LINE)
            if((_board.GetTileAndItsNeighbours(finishTile) >> 6) == 1)
                stonesOnFinishLine++;
        return stonesOnFinishLine == 2;
    }

    private void CheckTilesForLoneliness(){
        for(byte i = 0; i != TILE_COUNT; i++){
            var tileAndItsNeighbours = _board.GetTileAndItsNeighbours(i);
            var tile = (byte)(tileAndItsNeighbours >> 6);
            if(!(
                    (tile == ((tileAndItsNeighbours & BoardData.BIT_INDEX_MASKS[0])))       ||
                    (tile == ((tileAndItsNeighbours & BoardData.BIT_INDEX_MASKS[1]) >> 2))  ||
                    (tile == ((tileAndItsNeighbours & BoardData.BIT_INDEX_MASKS[2]) >> 4)))) {
                _board.SetTile(i, 0);
                if(tile == 1)
                    _stoneCount--;
            }
        }
    }

    public ArrayList<Move> GetValidMoves(){
        var indices_MyTilesAndTheirNeighbours = new ArrayList<byte[]>();

        for(byte i = 0; i != TILE_COUNT; i++){
            var tileAndItsNeighbours = _board.GetTileAndItsNeighbours(i);
            var tile = tileAndItsNeighbours  >> 6;
            if(tile == 1){
                //My Tile and its Neighbours
                indices_MyTilesAndTheirNeighbours.add(new byte[]{i, tileAndItsNeighbours});
            }
        }

        var result = new ArrayList<Move>();

        for(var index_MyTilesAndTheirNeighbours : indices_MyTilesAndTheirNeighbours){
            var myTileIndex = index_MyTilesAndTheirNeighbours[0];
            //Geht alle meine Tiles durch
            for(var neighboursForTile : indices_MyTilesAndTheirNeighbours){
                //Geht alle Nachbarn meiner Tiles durch
                var neighboursIndices = NEIGHBOUR_MAP[neighboursForTile[0]];
                for(byte n = 0; n != neighboursIndices.length; n++){
                    var neighboursIndex = neighboursIndices[n];
                    var neighboursValue = BoardData.GetNeighboursValueAt(n, neighboursForTile[1]);

                    if(neighboursValue == 0){
                        //hat der nachbar genug nachbarn vom eigenen Typ -> außer das myTile
                        var myTilesCount = 0;

                        var neighboursNeighboursValues = _board.GetTileAndItsNeighbours(neighboursIndex);
                        var neighboursNeighboursIndices = NEIGHBOUR_MAP[neighboursIndex];

                        for(byte nn = 0; nn != neighboursNeighboursIndices.length; nn++){
                            var neighboursNeighboursIndex = neighboursNeighboursIndices[nn];
                            var neighboursNeighboursValue = BoardData.GetNeighboursValueAt(nn, neighboursNeighboursValues);
                            if(neighboursNeighboursIndex == myTileIndex)
                                continue;

                            if(neighboursNeighboursValue == 1){
                                myTilesCount++;
                            }
                        }

                        if(myTilesCount > 0){
                            result.add(new Move(_perspectiveMapOut[myTileIndex], _perspectiveMapOut[neighboursIndex], 255));
                        }
                    }
                    else if(neighboursValue == 2){
                        EvaluateEnemiesTile(result, myTileIndex, neighboursIndex);
                    }
                    else if(neighboursValue == 3){
                        EvaluateEnemiesTile(result, myTileIndex, neighboursIndex);
                    }
                }
            }
        }
        return result;
    }

    private void EvaluateEnemiesTile(ArrayList<Move> result, byte myTileIndex, byte index) {
        var myTilesCount = 0;

        var freeSpacesIndices = new ArrayList<Byte>();

        var neighboursNeighboursValues = _board.GetTileAndItsNeighbours(index);
        var neighboursNeighboursIndices = NEIGHBOUR_MAP[index];

        for(byte nn = 0; nn != neighboursNeighboursIndices.length; nn++){

            var neighboursNeighboursIndex = neighboursNeighboursIndices[nn];
            var neighboursNeighboursValue = BoardData.GetNeighboursValueAt(nn, neighboursNeighboursValues);

            if(neighboursNeighboursIndex == myTileIndex)
                freeSpacesIndices.add(neighboursNeighboursIndex);
            else if(neighboursNeighboursValue == 1){
                myTilesCount++;
            }
            else if(neighboursNeighboursValue == 0)
                freeSpacesIndices.add(neighboursNeighboursIndex);
        }
        if(myTilesCount > 0){
            for(var freeSpaceIndex : freeSpacesIndices){
                result.add(new Move(_perspectiveMapOut[myTileIndex], _perspectiveMapOut[index], _perspectiveMapOut[freeSpaceIndex]));
            }
        }
    }


    public Move GetBestMove(){
        var validMoves = GetValidMoves();
        var result = validMoves.get(Utilities.RandomInt(0, validMoves.size()));
        Utilities.Log(_board);
        Utilities.Log("N: " + validMoves.size());
        return result;
    }
    private static final byte[] FINISH_LINE = {
        25,27,29,31,33,35
    };

    private static final byte[] PLAYER_1_PERSPECTIVE_MAP = {
                                25,
                            27, 26, 16,
                        29, 28, 18, 17, 9,
                    31, 30, 20, 19, 11, 10, 4,
                33, 32, 22, 21, 13, 12, 6,  5,  1,
            35, 34, 24, 23, 15, 14, 8,  7,  3,  2,  0
    };
    private static final byte[] PLAYER_2_PERSPECTIVE_MAP = {
                                35,
                            24, 34, 33,
                        15, 23, 22, 32, 31,
                    8, 14, 13, 21, 20, 30, 29,
                3, 7, 6, 12, 11, 19, 18,  28,  27,
            0, 2, 1, 5, 4, 10, 9,  17,  16,  26,  25
    };

    private static final byte[] PLAYER_0_PERSPECTIVE_MAP = {
                                0,
                            1,  2,  3,
                        4,  5,  6,  7,  8,
                    9,  10, 11, 12, 13, 14, 15,
                16, 17, 18, 19, 20, 21, 22, 23, 24,
            25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35
    };

    public static final byte[][] NEIGHBOUR_MAP = {
            new byte[]{2},

            new byte[]{5, 2},
            new byte[]{0,1,3},
            new byte[]{2,7},

            new byte[]{5,10},
            new byte[]{4,1,6},
            new byte[]{5,7,12},
            new byte[]{6,3,8},
            new byte[]{7,14},

            new byte[]{10,17},
            new byte[]{9,4,11},
            new byte[]{10,12,19},
            new byte[]{11,6,13},
            new byte[]{12,21,14},
            new byte[]{13,8,15},
            new byte[]{14,23},

            new byte[]{17,26},
            new byte[]{16,9,18},
            new byte[]{17,28,19},
            new byte[]{18,11,20},
            new byte[]{19,30,21},
            new byte[]{20,13,22},
            new byte[]{21,32,23},
            new byte[]{22,15,24},
            new byte[]{23,34},

            new byte[]{26},
            new byte[]{25,16,27},
            new byte[]{26,28},
            new byte[]{27,18,29},
            new byte[]{28,30},
            new byte[]{29,20,31},
            new byte[]{30,32},
            new byte[]{31,22,33},
            new byte[]{32,34},
            new byte[]{33,24,35},
            new byte[]{34},
    };
}
