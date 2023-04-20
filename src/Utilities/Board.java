package Utilities;


import lenz.htw.loki.Move;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;

public class Board {
    private TileOccupation[] _board;
    private PlayerNumber _perspective;

    private byte[] _perspectiveMapOut;
    private byte[] _perspectiveMapIn;

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
            case p2:
                _perspectiveMapIn = PLAYER_1_PERSPECTIVE_MAP;
                _perspectiveMapOut = PLAYER_2_PERSPECTIVE_MAP;
        }
        _board = GetStartBoard();
    }

    public void UpdateBoard(Move move){

        var movedFrom = _board[_perspectiveMapIn[move.from]];
        var movedTo = _board[_perspectiveMapIn[move.to]];

        _board[_perspectiveMapIn[move.from]] = TileOccupation.none;

        var other2Players = OTHER_PLAYERS.get(movedFrom);

        _board[_perspectiveMapIn[move.to]] = movedFrom;

        if(movedTo == other2Players[0])
            UpdateBoardPushHelper(other2Players[0], _perspectiveMapIn[move.push]);
        else if(movedTo == other2Players[1])
            UpdateBoardPushHelper(other2Players[1], _perspectiveMapIn[move.push]);

        CheckTilesForLoneliness();
    }

    private void UpdateBoardPushHelper(TileOccupation playerWhoseGetPushed, byte pushedTo){
        Utilities.Log("PUSH");
        _board[pushedTo] = playerWhoseGetPushed;

        var neighboursOfPlayerCount = 0;
        var neighbours = NEIGHBOUR_MAP[pushedTo];

        for(var neighbour : neighbours)
            if(_board[neighbour] == playerWhoseGetPushed)
                neighboursOfPlayerCount++;

        if(neighboursOfPlayerCount == 0){
            Utilities.Log("DELETE");
            _board[pushedTo] = TileOccupation.none;
        }
    }

    private void CheckTilesForLoneliness(){
        for(byte i = 0; i != _board.length; i++){
            var tile = _board[i];
            if(tile == TileOccupation.p0){
                var neighbours = NEIGHBOUR_MAP[i];
                var myOccupationCount = 0;
                for(var neighbour : neighbours)
                    if(_board[neighbour] == TileOccupation.p0)
                        myOccupationCount++;
                if(myOccupationCount == 0)
                    _board[i] = TileOccupation.none;
            }
            else if(tile == TileOccupation.p1){
                var neighbours = NEIGHBOUR_MAP[i];
                var myOccupationCount = 0;
                for(var neighbour : neighbours)
                    if(_board[neighbour] == TileOccupation.p1)
                        myOccupationCount++;
                if(myOccupationCount == 0)
                    _board[i] = TileOccupation.none;
            }
            else if(tile == TileOccupation.p2){
                var neighbours = NEIGHBOUR_MAP[i];
                var myOccupationCount = 0;
                for(var neighbour : neighbours)
                    if(_board[neighbour] == TileOccupation.p2)
                        myOccupationCount++;
                if(myOccupationCount == 0)
                    _board[i] = TileOccupation.none;
            }
        }
    }

    private ArrayList<Move> GetValidMoves(){
        var notMyOccupiedNeighbours = new HashSet<Byte>();
        var myTiles = new ArrayList<Byte>();

        for(byte i = 0; i != _board.length; i++){
            var tile = _board[i];
            if(tile == TileOccupation.p0){
                myTiles.add(i);
                for (var neighbour : NEIGHBOUR_MAP[i]) {
                    if(_board[neighbour] != TileOccupation.p0){
                        notMyOccupiedNeighbours.add(neighbour);
                    }
                }
            }
        }
        Utilities.Log("MY Tiles: " + myTiles.size());
        Utilities.Log("Neighbours which are not my: " + notMyOccupiedNeighbours.size());


        var result = new ArrayList<Move>();

        for(var myTile : myTiles){
            //CHANGE, gives wrong if 2 tiles left, even if u can move them since u can move the tile again next to one neighbour
            var neighbours = NEIGHBOUR_MAP[myTile];
            var lonelyNeighboursNotOccupiedByMeNeighbours = new ArrayList<ArrayList<Byte>>();
            for(var neighbour : neighbours)
                if(_board[neighbour] == TileOccupation.p0){
                    var notOccupiedByMe = GetNeighbourCount(neighbour);
                    if(notOccupiedByMe.size() == 2)
                        lonelyNeighboursNotOccupiedByMeNeighbours.add(notOccupiedByMe);
                }

            if(lonelyNeighboursNotOccupiedByMeNeighbours.size() == 0){
                for (var notMyOccupiedNeighbour : notMyOccupiedNeighbours) {
                    var neighbourInformation = GetNeighbourInformation(myTile, notMyOccupiedNeighbour);
                    //Utilities.Log("NeighboursInformation: from: " + myTile + " to " + notMyOccupiedNeighbour + " :" + neighbourInformation);
                    if(neighbourInformation.neighboursOccupiedByMeCount >= 1)
                        if(_board[notMyOccupiedNeighbour] == TileOccupation.none)
                            result.add(new Move(_perspectiveMapOut[myTile], _perspectiveMapOut[notMyOccupiedNeighbour], 255));
                        else
                            for (var posToPush : neighbourInformation.freePos)
                                result.add(new Move(_perspectiveMapOut[myTile], _perspectiveMapOut[notMyOccupiedNeighbour], _perspectiveMapOut[posToPush]));

                }
            }
            else{
                var commonNeighbours = GetCommonBytes(lonelyNeighboursNotOccupiedByMeNeighbours);
                for (var commonNeighbour : commonNeighbours) {
                    var neighbourInformation = GetNeighbourInformation(myTile, commonNeighbour);
                        if(_board[commonNeighbour] == TileOccupation.none)
                            result.add(new Move(_perspectiveMapOut[myTile], _perspectiveMapOut[commonNeighbour], 255));
                        else
                            for (var posToPush : neighbourInformation.freePos)
                                result.add(new Move(_perspectiveMapOut[myTile], _perspectiveMapOut[commonNeighbour], _perspectiveMapOut[posToPush]));
                }
            }
        }

        return result;
    }
    private NeighbourInformation GetNeighbourInformation(byte moveFromTile, byte moveToTile){
        var result = new NeighbourInformation();

        for (var neighbour : NEIGHBOUR_MAP[moveToTile]) {
            var occupation = _board[neighbour];
            if(occupation == TileOccupation.p0)
                if(neighbour != moveFromTile)
                    result.neighboursOccupiedByMeCount++;
                else{
                    result.freePos.add(moveFromTile);
                }
            else if(occupation == TileOccupation.none)
                result.freePos.add(neighbour);
        }
        return result;
    }

    private class NeighbourInformation{
        public byte neighboursOccupiedByMeCount = 0;
        public ArrayList<Byte> freePos = new ArrayList<>();

        @Override
        public String toString() {
            return "count: " + neighboursOccupiedByMeCount + "freePosCount: " + freePos.size();
        }
    }

    private ArrayList<Byte> GetNeighbourCount(byte tile){
        ArrayList<Byte> result = new ArrayList<>();
        var neighbours = NEIGHBOUR_MAP[tile];
        for (var neighbour : neighbours) {
            if(_board[neighbour] != TileOccupation.p0)
                result.add(neighbour);
        }
        return result;
    }


    public Move GetBestMove(){
        var validMoves = GetValidMoves();
        Utilities.Log("Number of Moves: " + validMoves.size());
        var result = validMoves.get(Utilities.RandomInt(0, validMoves.size()));
        return result;
    }

    private static TileOccupation[] GetStartBoard(){
        var o = TileOccupation.none;
        var p0 = TileOccupation.p0;
        var p1 = TileOccupation.p1;
        var p2 = TileOccupation.p2;

        return new TileOccupation[]{
                                    p0,
                                p0, p0, p0,
                            o,  o,  o,  o,  o,
                        o,  o,  o,  o,  o,  o,  o,
                    p1, o,  o,  o,  o,  o,  o,  o,  p2,
                p1, p1, p1, o,  o,  o,  o,  o,  p2, p2, p2
        };
    }

    public static Byte[] GetCommonBytes(ArrayList<ArrayList<Byte>> arrayList) {
        // Überprüfen, ob die übergebene ArrayList leer ist
        if (arrayList == null || arrayList.size() == 0) {
            return null;
        }

        // Überprüfen, ob die Byte-Arrays in der ArrayList leer sind
        for (ArrayList<Byte> arr : arrayList) {
            if (arr == null || arr.size() == 0) {
                return null;
            }
        }

        // Erstellen eines HashSet, um die gemeinsamen Byte zu speichern
        HashSet<Byte> commonBytes = new HashSet<>();
        // Füllen Sie das HashSet mit den Byte aus dem ersten Byte-Array in der ArrayList
        for (Byte i : arrayList.get(0)) {
            commonBytes.add(i);
        }

        // Durchlaufen der restlichen Byte-Arrays in der ArrayList und entfernen von Byte, die nicht in allen Arrays vorkommen
        for (int i = 1; i < arrayList.size(); i++) {
            HashSet<Byte> currentBytes = new HashSet<>();
            for (Byte j : arrayList.get(i)) {
                currentBytes.add(j);
            }
            commonBytes.retainAll(currentBytes);
        }

        // Erstellen eines neuen Byte-Arrays, um die gemeinsamen Byte zu speichern
        Byte[] result = new Byte[commonBytes.size()];
        int index = 0;
        for (Byte i : commonBytes) {
            result[index++] = i;
        }

        return result;
    }



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

    public static final Map<TileOccupation,TileOccupation[]> OTHER_PLAYERS;
    static{
        OTHER_PLAYERS = Map.of(
                TileOccupation.p0, new TileOccupation[]{TileOccupation.p1, TileOccupation.p2},
                TileOccupation.p1, new TileOccupation[]{TileOccupation.p0, TileOccupation.p2},
                TileOccupation.p2, new TileOccupation[]{TileOccupation.p0, TileOccupation.p1});
    }

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
