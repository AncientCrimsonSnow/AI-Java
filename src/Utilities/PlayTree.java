package Utilities;

public class PlayTree {
    /*
    private HashMap<Long, MoveToBoards> _adjacencyMap;

    public PlayTree() {
        _adjacencyMap = new HashMap<>();
    }
    public PlayTree(HashMap<Long, MoveToBoards>  adjacencyList) {
        _adjacencyMap = adjacencyList;
    }

    public PlayTree(String fileName, String path) throws IOException {
        _adjacencyMap = new HashMap<>();
        BufferedReader reader = new BufferedReader(new FileReader(path + "/" + fileName));
        String line;
        long currentIndex = 0;
        while ((line = reader.readLine()) != null) {
            var values = line.split(" ");
            var moveToBoards = new MoveToBoards(
                    new Move(
                            Integer.parseInt(values[0]),
                            Integer.parseInt(values[1]),
                            Integer.parseInt(values[2])), new HashSet<>());
            for(var i = 3; 3 != values.length; i++){
                moveToBoards.boards.add(Long.parseLong(values[i]));
            }
            _adjacencyMap.put(currentIndex, moveToBoards);
            currentIndex ++;
        }
    }

    public void AddEdge(Move move, byte[] state1, byte[] state2) {




        if(!(_adjacencyMap.containsKey(state1))){
            var adjacentVertices = new HashSet<Long>();
            adjacentVertices.add(state2);
            _adjacencyMap.put(state1, new MoveToBoards(move,adjacentVertices));
        }
        else
            _adjacencyMap.get(state1).boards.add(state2);
    }
    public void SaveToFile(String fileName, String path) throws IOException {
        var writer = new BufferedWriter(new FileWriter(path + "/" + fileName));
        for (var entry : _adjacencyMap.entrySet()) {
            MoveToBoards moveToBoards = entry.getValue();
            writer.write(moveToBoards.move.from + " " + moveToBoards.move.to + " " + moveToBoards.move.push + " ");
            for (long board : moveToBoards.boards) {
                writer.write(board + " ");
            }
            writer.newLine();
        }
        writer.close();
    }

    private static long HashBoard(byte[] board, String hashTablePath) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(hashTablePath + "/HashTable"));
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

    private static byte[] StringToBoard(String value){
        return new byte[0];
    }

    private static StringToBoard(byte[] )

    private class MoveToBoards{
        public final Move move;
        public final HashSet<Long> boards;

        public MoveToBoards(Move move, HashSet<Long> boards) {
            this.move = move;
            this.boards = boards;
        }
    }

     */
}
