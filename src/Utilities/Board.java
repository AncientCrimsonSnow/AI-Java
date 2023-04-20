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
        move = new Move(_perspectiveMapIn[move.from], _perspectiveMapIn[move.to], _perspectiveMapIn[move.push]);

        var movedFrom = _board[move.from];
        var movedTo = _board[move.to];

        _board[move.from] = TileOccupation.none;

        var other2Players = OTHER_PLAYERS.get(movedFrom);

        if(movedTo == other2Players[0] || movedTo == other2Players[1]){
            _board[move.push] = movedTo;
        }
        _board[move.to] = movedFrom;
    }

    private ArrayList<Move> GetValidMoves(){
        var freeNeighbours = new HashSet<Integer>();
        var myTiles = new ArrayList<Integer>();
        var result = new ArrayList<Move>();
        for(var i = 0; i != _board.length; i++){
            var tile = _board[i];
            if(tile == TileOccupation.p0){
                myTiles.add(i);
                var tileNeighbours = NEIGHBOUR_MAP[i];
                for (var neighbour : tileNeighbours.neighbours) {
                    if(neighbour >= 0)
                        if(_board[neighbour] != TileOccupation.p0){
                            freeNeighbours.add(neighbour);
                        }
                }
            }
        }
        for(var myTile : myTiles){
            var neighbours = NEIGHBOUR_MAP[myTile];
        }

        return result;
    }

    private Boolean TileHasMyNeighbours(int from, int tile){
        var neighbours = NEIGHBOUR_MAP[tile];
        for (var neighbour: neighbours.neighbours) {
            if(neighbour >= 0)
                if(neighbour != from && _board[neighbour] == TileOccupation.p0)
                    return true;
        }
        return false;
    }

    private ArrayList<Move> GenerateMoves(int from, int to){
        if(_board[to] == TileOccupation.p1 || _board[to] == TileOccupation.p2){
            var result = new ArrayList<Move>();
            result.add(new Move(from, to, from));

            var neighbours = NEIGHBOUR_MAP[to];
            for(var i = 0; i != neighbours.neighbours.length; i++){
                if(neighbours.neighbours[i] >= 0)
                    result.add(new Move(from, to, neighbours.neighbours[i]));
            }
            return result;
        }
        var result = new ArrayList<Move>();
        result.add(new Move(from, to, 0));
        return result;
    }


    public Move GetBestMove(){
        var validMoves = GetValidMoves();
        var result = validMoves.get(Utilities.RandomInt(0, validMoves.size()));
        return new Move(_perspectiveMapOut[result.from], _perspectiveMapOut[result.to], _perspectiveMapOut[result.push]);
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

    public static final TileNeighbourHelper NEIGHBOUR_MAP[] = {
            new TileNeighbourHelper(2, -1, -1),

            new TileNeighbourHelper(5,2, -1),
            new TileNeighbourHelper(0,1, 3),
            new TileNeighbourHelper(2,7, -1),

            new TileNeighbourHelper(5,10, -1),
            new TileNeighbourHelper(4,1, 6),
            new TileNeighbourHelper(5,7, 12),
            new TileNeighbourHelper(6,3, 8),
            new TileNeighbourHelper(7,14, -1),

            new TileNeighbourHelper(10,17, -1),
            new TileNeighbourHelper(9,4, 11),
            new TileNeighbourHelper(10,12, 19),
            new TileNeighbourHelper(11,6, 13),
            new TileNeighbourHelper(12,21, 14),
            new TileNeighbourHelper(13,8, 15),
            new TileNeighbourHelper(14,23, -1),

            new TileNeighbourHelper(17,26, -1),
            new TileNeighbourHelper(16,9, 18),
            new TileNeighbourHelper(17,28, 19),
            new TileNeighbourHelper(18,11, 20),
            new TileNeighbourHelper(19,30, 21),
            new TileNeighbourHelper(20,13, 22),
            new TileNeighbourHelper(21,32, 23),
            new TileNeighbourHelper(22,15, 24),
            new TileNeighbourHelper(23,34, -1),

            new TileNeighbourHelper(26,-1, -1),
            new TileNeighbourHelper(25,16, 27),
            new TileNeighbourHelper(26,28, -1),
            new TileNeighbourHelper(27,18, 29),
            new TileNeighbourHelper(28,30, -1),
            new TileNeighbourHelper(29,20, 31),
            new TileNeighbourHelper(30,32, -1),
            new TileNeighbourHelper(31,22, 33),
            new TileNeighbourHelper(32,34, -1),
            new TileNeighbourHelper(33,24, 35),
            new TileNeighbourHelper(34,-1, -1),
    };
}
