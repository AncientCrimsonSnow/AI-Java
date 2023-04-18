package Utilities;


import lenz.htw.loki.Move;

import java.util.Set;
import java.util.TreeSet;

public class Board {
    private PlayerNumber[] _board;
    private PlayerNumber _perspective;

    private int[] _perspectiveMap;

    public Board(PlayerNumber perspective) {

        _perspective = perspective;

        if(_perspective == PlayerNumber.Player0)
            _perspectiveMap = _player0PerspectiveMap;
        else if(_perspective == PlayerNumber.Player1)
            _perspectiveMap = _player1PerspectiveMap;
        else if(_perspective == PlayerNumber.Player2)
            _perspectiveMap = _player2PerspectiveMap;

    }

    private PlayerNumber[] GetStartBoard(){
        return new PlayerNumber[]{

        };
    }

    public Move[] UpdateBoardAndGetValidMoves(Move move){
        UpdateBoard(move);
        return GetValidMoves();
    }

    private void UpdateBoard(Move move){

    }

    private Move[] GetValidMoves(){
        var neighbourTiles = GetAllNeighboursBoardIndices();


        return new Move[]{
                new Move(0,0,0),
        };
    }

    private Set<Integer> GetAllNeighboursBoardIndices(){
        var result = new TreeSet<Integer>();
        for(var i = 0; i != _board.length; i++){
            if(_board[i] == PlayerNumber.Player0){
                var neighbours = _neighbourMap[i];
                result.add(neighbours.neighbour1);
                result.add(neighbours.neighbour2);
                result.add(neighbours.neighbour3);
            }
        }
        return result;
    }

    public Move GetBestMove(){
        var validMoves = GetValidMoves();
        return validMoves[0];

    }


    private static final int[] _player1PerspectiveMap = {
                                25,
                            27, 26, 16,
                        29, 28, 18, 17, 9,
                    31, 30, 20, 19, 11, 10, 4,
                33, 32, 22, 21, 13, 12, 6,  5,  1,
            35, 34, 24, 23, 15, 14, 8,  7,  3,  2,  0
    };
    private static final int[] _player2PerspectiveMap = {
                                35,
                            24, 34, 33,
                        15, 23, 22, 32, 31,
                    8, 14, 13, 21, 20, 30, 29,
                3, 7, 6, 12, 11, 19, 18,  28,  27,
            0, 2, 1, 5, 4, 10, 9,  17,  16,  26,  25
    };

    private static final int[] _player0PerspectiveMap = {
                                0,
                            1,  2,  3,
                        4,  5,  6,  7,  8,
                    9,  10, 11, 12, 13, 14, 15,
                16, 17, 18, 19, 20, 21, 22, 23, 24,
            25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35
    };

    public static final TileNeighbourHelper _neighbourMap[] = {
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
