package Scripts.ClientModels;

import Utilities.Board;
import Utilities.PlayerNumber;
import Utilities.Utilities;
import lenz.htw.loki.Move;
import lenz.htw.loki.net.NetworkClient;

import java.io.IOException;
public class RandomClient {
    public static void main(String[] args) throws IOException {

        var board = new Board(PlayerNumber.p0);

        var client = new NetworkClient(null, "Ahegoe", Utilities.GetLogo());
        var playerNumberInt = client.getMyPlayerNumber();
        var playerNumber = PlayerNumber.p0;
        if(playerNumberInt == 1){
            playerNumber = PlayerNumber.p1;
        }
        else if(playerNumberInt == 2){
            playerNumber = PlayerNumber.p2;
        }
        Utilities.Log(playerNumber);



        Move receiveMove;
        while (true) {
            while ((receiveMove = client.receiveMove()) != null) {
                board.UpdateBoard(receiveMove);
            }
            var move = board.GetBestMove();
            Utilities.Log(move);
            client.sendMove(move);
        }
    }
}
