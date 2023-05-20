package Utilities;

import lenz.htw.loki.Move;

import java.util.HashMap;
import java.util.HashSet;

public class AlphaBetaSearch {

    public static Move GetBestMove(BoardData boardData, int depth, float[] evaluationCoefficients){
            return GetBestMoveIntern(boardData, depth, evaluationCoefficients).move;
    }

    private static AlphaBetaHelper GetBestMoveIntern(BoardData boardData, int depth, float[] evaluationCoefficients){
        Move bestMove = null;
        var bestMoveEvaluation = -999999f;
        var consequenceStatesPerMove = GetConsequenceStatesPerMove(boardData);

        for(var move : consequenceStatesPerMove.keySet()){
            var consequenceStates = consequenceStatesPerMove.get(move);
            var minEvaluation = 999999f;

            for(var consequenceState : consequenceStates){
                var evaluation = (depth == 0)?
                        EvaluateBoardData(consequenceState, evaluationCoefficients) :
                        GetBestMoveIntern(consequenceState, depth - 1, evaluationCoefficients).evaluation;

                if(evaluation < minEvaluation)
                    minEvaluation = evaluation;
            }

            if(bestMoveEvaluation < minEvaluation){
                bestMove = move;
                bestMoveEvaluation = minEvaluation;
            }
        }
        return new AlphaBetaHelper(bestMove, bestMoveEvaluation);
    }

    private static class AlphaBetaHelper{
        public Move move;
        public float evaluation;

        public AlphaBetaHelper(Move move, float evaluation) {
            this.move = move;
            this.evaluation = evaluation;
        }
    }

    private static HashMap<Move, HashSet<BoardData>> GetConsequenceStatesPerMove(BoardData data){
        var result = new HashMap<Move, HashSet<BoardData>>();
        var board = new Board(0, data);

        var validMoves = board.GetValidMoves();

        for (var validMove : validMoves) {
            var newBoard = new Board(0, board.GetBoard());
            newBoard.UpdateBoard(validMove);

            var p1BoardResults = GetConsequenceStates(1, newBoard.GetBoard());

            for(var p1BoardResult : p1BoardResults){
                var p2BoardResults = GetConsequenceStates(2, p1BoardResult);

                if(result.containsKey(validMove)){
                    result.get(validMove).addAll(p2BoardResults);
                }
                else{
                    result.put(validMove, p2BoardResults);
                }
            }
        }
        return result;
    }

    private static HashSet<BoardData> GetConsequenceStates(int playerNumber, BoardData boardData){
        var result = new HashSet<BoardData>();

        var board = new Board(playerNumber, boardData);

        if(board.WinningBoard())
            return result;

        if(board.DeadBoard()){
            result.add(boardData);
            return result;
        }
        var validMoves = board.GetValidMoves();

        for (var validMove : validMoves) {
            var newBoard = new Board(playerNumber, board.GetBoard());
            newBoard.UpdateBoard(validMove);

            result.add(newBoard.GetBoard());
        }
        return result;
    }
    private static float EvaluateBoardData(BoardData _data, float[] evaluationCoefficients){
        var myBoard = new Board(0, _data);

        if(myBoard.WinningBoard())
            return 99999f;
        if(myBoard.DeadBoard())
            return -99999f;

        var player1Board = new Board(1, _data);
        var player2Board = new Board(2, _data);

        var allPlayerTileCounts = new int[3];

        byte myClosestDistanceToFinish = 35;
        byte mySecClosestDistanceToFinish = 35;

        byte player1ClosestDistanceToFinish = 35;
        byte player1SecClosestDistanceToFinish = 35;

        byte player2ClosestDistanceToFinish = 35;
        byte player2SecClosestDistanceToFinish = 35;

        for(byte i = 0; i != 36; i++){
            var tileData = _data.GetTileWithoutNeighbours(i);
            if(tileData == 1){
                allPlayerTileCounts[0]++;
                var distanceToFinish = myBoard.DistanceToFinish(i);
                if(distanceToFinish < myClosestDistanceToFinish){
                    mySecClosestDistanceToFinish = myClosestDistanceToFinish;
                    myClosestDistanceToFinish = distanceToFinish;
                }
                else if(distanceToFinish < mySecClosestDistanceToFinish)
                    mySecClosestDistanceToFinish = distanceToFinish;
            }

            else if(tileData == 2){
                allPlayerTileCounts[1]++;
                var distanceToFinish = player1Board.DistanceToFinish(i);
                if(distanceToFinish < player1ClosestDistanceToFinish){
                    player1SecClosestDistanceToFinish = player1ClosestDistanceToFinish;
                    player1ClosestDistanceToFinish = distanceToFinish;
                }
                else if(distanceToFinish < player1SecClosestDistanceToFinish)
                    player1SecClosestDistanceToFinish = distanceToFinish;
            }
            else if(tileData == 3){
                allPlayerTileCounts[2]++;
                var distanceToFinish = player2Board.DistanceToFinish(i);
                if(distanceToFinish < player2ClosestDistanceToFinish){
                    player2SecClosestDistanceToFinish = player2ClosestDistanceToFinish;
                    player2ClosestDistanceToFinish = distanceToFinish;
                }
                else if(distanceToFinish < player2SecClosestDistanceToFinish)
                    player2SecClosestDistanceToFinish = distanceToFinish;
            }
        }

        var result = (
                +   evaluationCoefficients[0] * allPlayerTileCounts[0])                                                                                                       //MyTiles
                +   evaluationCoefficients[1] * (allPlayerTileCounts[1] + allPlayerTileCounts[2])                                                                             //EnemyTiles;
                +   evaluationCoefficients[2] * ((10 - myClosestDistanceToFinish) * (10 - mySecClosestDistanceToFinish))                                                      //My Distance to FinishLine
                +   ((allPlayerTileCounts[1] <= 2)? 0 : evaluationCoefficients[3] * ((10 - player1ClosestDistanceToFinish) * (10 - player1SecClosestDistanceToFinish)))       //P1 Distance to his Finish Line
                +   ((allPlayerTileCounts[2] <= 2)? 0 : evaluationCoefficients[3] * ((10 - player2ClosestDistanceToFinish) * (10 - player2SecClosestDistanceToFinish)));      //p2 Distance to his Finish Line

        return result;
    }
}
