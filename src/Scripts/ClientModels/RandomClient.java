package Scripts.ClientModels;

import Utilities.Board;
import Utilities.PlayerNumber;
import Utilities.Utilities;
import lenz.htw.loki.Move;
import lenz.htw.loki.net.NetworkClient;

import java.io.IOException;

public class RandomClient {
    //Spiegelung
    //Symetrie
    //minimax algo



    public static void main(String[] args) throws IOException {
        var board = new Board(PlayerNumber.Player0);
        var client = new NetworkClient(null, "Ahegoe", Utilities.GetLogo());


        Move receiveMove;
        while (true) {
            while ((receiveMove = client.receiveMove()) != null) {
                // verarbeite Zug
                // Brettkonfiguration aktualisieren
            }
            // berechne genialen eigenen Zug
            client.sendMove(board.GetBestMove());
        }

    }
}
