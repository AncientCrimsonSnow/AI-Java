package Scripts.ClientModels;

import java.io.IOException;
import java.util.Objects;
import javax.imageio.ImageIO;
import lenz.htw.loki.Move;
import lenz.htw.loki.net.NetworkClient;


public class TestClient {

    public static void main(String[] args) throws IOException {
        // Vorbereitung ohne Server
        // Startbrettkonfiguration erstellen

        NetworkClient client = new NetworkClient("127.0.0.1", "Die allerbesten", ImageIO.read(Objects.requireNonNull(TestClient.class.getResourceAsStream("/Assets/ahegoe.png"))));

        // in diesem Moment läuft das Spiel

        client.getMyPlayerNumber();
        client.getExpectedNetworkLatencyInMilliseconds();
        client.getTimeLimitInSeconds();

        Move move;

        while (true) {
            while ((move = client.receiveMove()) != null) {
                // verarbeite Zug
                // Brettkonfiguration aktualisieren
            }
            // berechne genialen eigenen Zug
            client.sendMove(new Move(0,0,0));
        }
    }
}