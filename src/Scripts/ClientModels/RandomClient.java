package Scripts.ClientModels;

import Utilities.Utilities;
import Utilities.Board;
import Utilities.PlayerNumber;
import lenz.htw.loki.net.NetworkClient;

import java.io.IOException;

public class RandomClient {

    public static void main(String[] args) throws IOException {
        var board = new Board(PlayerNumber.Player0);
        var client = new NetworkClient(null, "Hornyaf", Utilities.GetLogo());
    }
}
