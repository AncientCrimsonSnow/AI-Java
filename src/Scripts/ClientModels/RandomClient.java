package Scripts.ClientModels;

import Utilities.Utilities;
import lenz.htw.loki.net.NetworkClient;

import java.io.IOException;

public class RandomClient {

    public static void main(String[] args) throws IOException {
        var client = new NetworkClient(null, "Hornyaf", Utilities.GetLogo());
    }
}
