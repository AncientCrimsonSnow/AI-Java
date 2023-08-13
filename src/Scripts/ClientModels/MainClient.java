package Scripts.ClientModels;

import AStart.AStarAlgorithm;
import Utilities.Board;
import Utilities.Trainingscenter;
import Utilities.*;
import lenz.htw.loki.Move;

import java.util.Random;

public class MainClient {
    private Board _board;
    private float[] _evaluationCoefficients;

    private static Random random = new Random();

    public MainClient(int playerNumber) {
        Utilities.Log(playerNumber);

        _board = new Board(playerNumber);

        _evaluationCoefficients = new float [4];
        var crrBestEvaluationCoefficients = Trainingscenter.DeserializeEvaluationCoefficients();
        for (var i = 0; i != crrBestEvaluationCoefficients.length; i++) {
            _evaluationCoefficients[i] = crrBestEvaluationCoefficients[i].value;
        }
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
        //Utilities.Log(_board.GetBoard());
    }

    private static int GetDistance(int index0, int index1){
        var aStar = new AStarAlgorithm();
        return aStar.GetPathLength(index0, index1);
    }
}
