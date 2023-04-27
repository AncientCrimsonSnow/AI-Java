package Scripts.ClientModels;

public class FullKnowClient {

    /*
    public static void main(String[] args) throws IOException {
        var client = new NetworkClient(null, "Ahegoe", Utilities.GetLogo());
        var playerNumberInt = client.getMyPlayerNumber();
        var _adjacencyList

        var playerNumber = PlayerNumber.p0;
        if (playerNumberInt == 1) {
            playerNumber = PlayerNumber.p1;
        } else if (playerNumberInt == 2) {
            playerNumber = PlayerNumber.p2;
        }
        var board = new Board(playerNumber);

        Move receiveMove;
        while (true) {
            while ((receiveMove = client.receiveMove()) != null) {
                board.UpdateBoard(receiveMove);
            }

            var hash = HashBoard(board.GetBoard());




            var move = board.GetBestMove();
            client.sendMove(move);
        }
    }

    private static long HashBoard(byte[] board) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader("HashTable"));
        String line;
        long currentIndex = 0;
        while ((line = reader.readLine()) != null) {
            if (StringToBoard(line) == board) {
                return currentIndex;
            }
            currentIndex++;
        }
        return -1;
    }

    private static byte[] GetBoardByHash(long hash) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader("HashTable"));
        String line;
        long currentIndex = 0;
        while ((line = reader.readLine()) != null) {
            if (hash == currentIndex) {
                return StringToBoard(line);
            }
            currentIndex++;
        }
        return new byte[0];
    }

    private static byte[] StringToBoard(String value){
        return new byte[0];
    }

     */
}
