package Scripts.ClientModels;

import AStart.AStarAlgorithm;
import Utilities.Board;
import Utilities.Utilities;
import lenz.htw.loki.Move;

public class MainClient {
    private Board _board;
    private float[] _evaluationCoefficients;

    public MainClient(int playerNumber) {
        Utilities.Log(playerNumber);

        _board = new Board(playerNumber);
        _evaluationCoefficients = new float []{1, -1, 1, -1};
    }

    public MainClient(int playerNumber, float[] evaluationCoefficients) {
        _board = new Board(playerNumber);
        _evaluationCoefficients = evaluationCoefficients;
    }

    public void UpdateBoard(Move move){
        _board.UpdateBoard(move);
        Utilities.Log(_board.GetBoard());
    }

    public Move GetMove(){
        return _board.GetBestMove(0, _evaluationCoefficients);
    }


    private static int GetDistance(int index0, int index1){
        var aStar = new AStarAlgorithm();
        return aStar.GetPathLength(index0, index1);
    }


}
