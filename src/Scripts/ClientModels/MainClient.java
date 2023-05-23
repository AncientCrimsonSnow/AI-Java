package Scripts.ClientModels;

import AStart.AStarAlgorithm;
import Utilities.Board;
import Utilities.Utilities;
import lenz.htw.loki.Move;

import java.util.Random;

public class MainClient {
    private Board _board;
    private float[] _evaluationCoefficients;

    private static Random random = new Random();

    public MainClient(int playerNumber) {
        Utilities.Log(playerNumber);

        _board = new Board(playerNumber);
        _evaluationCoefficients = new float []{20 * random.nextFloat(), 20 * random.nextFloat(), 1 * random.nextFloat(), 1 * random.nextFloat()};
    }

    public MainClient(int playerNumber, float[] evaluationCoefficients) {
        _board = new Board(playerNumber);
        _evaluationCoefficients = evaluationCoefficients;
    }

    public void UpdateBoard(Move move){
        _board.UpdateBoard(move);
    }

    public Move GetMove(){
        return _board.GetBestMove(0, _evaluationCoefficients);
    }

    public void PrintBoard(){
        Utilities.Log(_board.GetBoard());
    }

    private static int GetDistance(int index0, int index1){
        var aStar = new AStarAlgorithm();
        return aStar.GetPathLength(index0, index1);
    }


}
