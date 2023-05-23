package Utilities;

import Scripts.ClientModels.MainClient;
import lenz.htw.loki.Move;
import lenz.htw.loki.net.NetworkClient;

import java.io.IOException;

public class ClientStarter {
    public static void main(String[] args) throws IOException {

        var networkClient = new NetworkClient(null, "Ahegoe", Utilities.GetLogo());
        var clientModel = new MainClient(networkClient.getMyPlayerNumber());
        Move receiveMove;
        while (true) {
            while ((receiveMove = networkClient.receiveMove()) != null) {
                clientModel.UpdateBoard(receiveMove);
            }
            var move = clientModel.GetMove();
            networkClient.sendMove(move);
        }
    }
}
