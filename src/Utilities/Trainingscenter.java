package Utilities;

import Scripts.ClientModels.MainClient;
import lenz.htw.loki.Move;
import lenz.htw.loki.Server;
import lenz.htw.loki.net.NetworkClient;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.*;

public class Trainingscenter {

    private static final int FREE_PORT_START_INDEX = 22135;
    private static final int PARALLEL_GAME_COUNT = 12;
    private static final int CLIENT_COUNT_PER_GAME = 3;
    private static final int CLIENT_COUNT_TOTAL = CLIENT_COUNT_PER_GAME * PARALLEL_GAME_COUNT;

    private static final float LEARNING_RATE = 0.8f;
    private static final int COEFFICIENT_CHANGES_RESET_THRESHOLD = 9999999;
    private static final int ROUNDS = 9;

    private static final int GAME_TIMEOUT_TIME = 30;

    private static final Random RANDOM = new Random();

    private static  int _coefficientCount = 4;


    public static void main(String[] args) throws ExecutionException, InterruptedException {
        while(true){
            var crrBestEvaluationCoefficients = DeserializeEvaluationCoefficients();

            var newBest = Train(crrBestEvaluationCoefficients);
            for(var coefficientIndex = 0; coefficientIndex != _coefficientCount; coefficientIndex++){
                crrBestEvaluationCoefficients[coefficientIndex].value = newBest[coefficientIndex];
                crrBestEvaluationCoefficients[coefficientIndex].changes += ROUNDS;
            }

            SerializeEvaluationCoefficients(crrBestEvaluationCoefficients);
        }
    }

    private static float[] Train(Coefficient[] crrBestEvaluationCoefficients) throws InterruptedException, ExecutionException {

        _coefficientCount = crrBestEvaluationCoefficients.length;

        var evaluationCoefficientsMap = new Coefficient[CLIENT_COUNT_TOTAL][_coefficientCount];

        for(var client = 0; client != CLIENT_COUNT_TOTAL; client++){
            evaluationCoefficientsMap[client] = new Coefficient[_coefficientCount];
            for(var coefficientIndex = 0; coefficientIndex != _coefficientCount; coefficientIndex++){
                var crrBestEvaluationCoefficient = crrBestEvaluationCoefficients[coefficientIndex];
                evaluationCoefficientsMap[client][coefficientIndex] = new Coefficient(crrBestEvaluationCoefficient.value, crrBestEvaluationCoefficient.changes);
            }
        }

        for(var round = 0; round != ROUNDS; round++){

            ShuffleArray(evaluationCoefficientsMap);

            var evaluationCoefficientsValuesMap = new float[CLIENT_COUNT_TOTAL][_coefficientCount];
            for(var client = 0; client != CLIENT_COUNT_TOTAL; client++){
                for(var coefficientIndex = 0; coefficientIndex != _coefficientCount; coefficientIndex++){
                    evaluationCoefficientsValuesMap[client][coefficientIndex] = evaluationCoefficientsMap[client][coefficientIndex].value;
                }
            }

            float[][] results = StartGames(PARALLEL_GAME_COUNT, evaluationCoefficientsValuesMap.clone());

            for(var game = 0; game != PARALLEL_GAME_COUNT; game++){

                var winner = results[game];

                if(winner.length == 0)
                    continue;

                while(winner.length != _coefficientCount){
                    winner = results[RANDOM.nextInt(0, winner.length)];
                }

                for(var client = 0; client != CLIENT_COUNT_PER_GAME; client++){

                    var clientIndex = game * CLIENT_COUNT_PER_GAME + client;

                    var evaluationCoefficient = evaluationCoefficientsMap[clientIndex];

                    for(var coefficientIndex = 0; coefficientIndex != _coefficientCount; coefficientIndex++){

                        //Keep one winnerclient without mutations
                        if(client == 0)
                            evaluationCoefficient[coefficientIndex].value = winner[coefficientIndex];
                        else{
                            var coefficient = evaluationCoefficient[coefficientIndex];
                            coefficient.value = MutateFloat(winner[coefficientIndex], coefficient.changes);
                            coefficient.changes = (coefficient.changes + 1) % COEFFICIENT_CHANGES_RESET_THRESHOLD;
                        }
                    }
                }
            }
        }

        var winnerValues = new float[PARALLEL_GAME_COUNT][_coefficientCount];
        for(var game = 0; game != PARALLEL_GAME_COUNT; game++){
            var lastWinnerIndex = game * CLIENT_COUNT_PER_GAME;
            var lastWinnerCoefficients = evaluationCoefficientsMap[lastWinnerIndex];
            for(var coefficientIndex = 0; coefficientIndex != _coefficientCount; coefficientIndex++){
                winnerValues[game][coefficientIndex] = lastWinnerCoefficients[coefficientIndex].value;
            }
        }

        return KnockOutTournament(new ArrayList<>(Arrays.asList(winnerValues)))[0];
    }

    private static float[][] KnockOutTournament(ArrayList<float[]> participantEvaluationCoefficientsMap) throws ExecutionException, InterruptedException {
        for(var i = participantEvaluationCoefficientsMap.size() - 1; i >= 0; i--){
            var participantEvaluationCoefficients = participantEvaluationCoefficientsMap.get(i);
            if(participantEvaluationCoefficients.length != _coefficientCount)
                participantEvaluationCoefficientsMap.remove(i);
        }

        var participantAmount = participantEvaluationCoefficientsMap.size();

        if(participantAmount == 1)
            return new float[][]{
                    participantEvaluationCoefficientsMap.get(0)
            };

        var clientAmount = ((participantAmount + CLIENT_COUNT_PER_GAME - 1) / CLIENT_COUNT_PER_GAME) * CLIENT_COUNT_PER_GAME;

        float[][] newParticipantEvaluationCoefficientsMap = new float[clientAmount][_coefficientCount];

        var clientIndex = 0;
        while(clientIndex < participantAmount){
            newParticipantEvaluationCoefficientsMap[clientIndex] = participantEvaluationCoefficientsMap.get(clientIndex);
            clientIndex++;
        }
        while(clientIndex < clientAmount){
            newParticipantEvaluationCoefficientsMap[clientIndex] = (float[]) GetRandomElement(participantEvaluationCoefficientsMap.toArray());
            clientIndex++;
        }

        ShuffleArray(newParticipantEvaluationCoefficientsMap);

        var roundResult = StartGames(clientAmount / CLIENT_COUNT_PER_GAME, newParticipantEvaluationCoefficientsMap);
        return KnockOutTournament(new ArrayList<>(Arrays.asList(roundResult)));
    }

    private static float[][] StartGames(int gamesAmount, float[][] evaluationCoefficientsValuesMap) throws ExecutionException, InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(gamesAmount);
        var results = new float[gamesAmount][_coefficientCount];

        for(var game = 0; game != gamesAmount; game++){
            final int gameNumber = game;
            var clientEvaluationCoefficients = new float[CLIENT_COUNT_PER_GAME][_coefficientCount];
            for(var client = 0; client != CLIENT_COUNT_PER_GAME; client++){
                var clientEvaluationCoefficientValues = evaluationCoefficientsValuesMap[game * CLIENT_COUNT_PER_GAME + client];
                clientEvaluationCoefficients[client] =  clientEvaluationCoefficientValues;
            }
            results[game] = executor.submit(() -> StartGame(gameNumber, clientEvaluationCoefficients)).get();
        }

        executor.shutdown();
        return results;
    }

    private static <T> T GetRandomElement(T[] array) {
        Random random = new Random();
        int randomIndex = random.nextInt(array.length);
        return array[randomIndex];
    }

    private static void ShuffleArray(Object[] array){
        Random random = new Random();

        for (int i = array.length - 1; i > 0; i--) {
            int index = random.nextInt(i + 1);

            Object temp = array[i];
            array[i] = array[index];
            array[index] = temp;
        }
    }

    private static float[] StartGame(int index, float[][] clientEvaluationCoefficients){
        var executor = Executors.newFixedThreadPool(CLIENT_COUNT_PER_GAME);
        var playerNumbers = new Integer[CLIENT_COUNT_PER_GAME];

        var port = FREE_PORT_START_INDEX + index;

        for(var client  = 0; client != CLIENT_COUNT_PER_GAME; client++){
            int finalClientIndex = client;
            executor.submit(() -> {
                try {
                    StartClient(port, clientEvaluationCoefficients[finalClientIndex], playerNumbers, finalClientIndex);
                } catch (InterruptedException | IOException e) {
                    throw new RuntimeException(e);
                }
            });
        }

        executor.shutdown();

        var serverExecutor = Executors.newSingleThreadExecutor();
        Future<Integer> future = serverExecutor.submit(() -> Server.runOnceAndReturnTheWinner(8, port));

        try {
            var winner = future.get(GAME_TIMEOUT_TIME, TimeUnit.SECONDS);

            var winnerIndex = -1;
            for(var i = 0; i != 3; i++){
                if(playerNumbers[i] == winner-1)
                    winnerIndex = i;
            }

            if(winnerIndex == -1)
                return new float[]{};

            return clientEvaluationCoefficients[winnerIndex];

        } catch (TimeoutException e) {
            future.cancel(true);
            Utilities.Log(
                    "-----------------------------------------\n\n\n\n" +
                          "                     TIME               \n\n\n\n" +
                          "-----------------------------------------");
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        serverExecutor.shutdown();
        executor.shutdownNow();

        return new float[]{};
    }

    private static void StartClient(int port, float[] evaluationCoefficients, Integer[] playerNumber, int index) throws InterruptedException, IOException {
        Thread.sleep(100);
        var networkClient = new NetworkClient(null,port,  "Ahegoe", Utilities.GetLogo());
        playerNumber[index] = networkClient.getMyPlayerNumber();
        var clientModel = new MainClient(playerNumber[index], evaluationCoefficients);
        Move receiveMove;
        while (true) {
            while ((receiveMove = networkClient.receiveMove()) != null) {
                clientModel.UpdateBoard(receiveMove);
                if(playerNumber[index] == 0)
                    clientModel.PrintBoard();
            }
            var move = clientModel.GetMove();
            networkClient.sendMove(move);
        }
    }

    public static float MutateFloat(float currentValue, int numMutations) {
        var deviationBase = (RANDOM.nextFloat() - 0.5f) * (2 * Math.max(0, Math.min(1, LEARNING_RATE)));
        float deviation = currentValue * deviationBase/numMutations;
        var result = currentValue + deviation;
        return result;
    }

    public static Coefficient[] DeserializeEvaluationCoefficients() {
        var path = "src/Assets/EvaluationCoefficientsMap.ser";
        Coefficient[] coefficients;
        try {
            coefficients = Deserialize(path);
        } catch (IOException | ClassNotFoundException e) {
            coefficients =  new Coefficient[]{
                    new Coefficient(20, 1),
                    new Coefficient(20, 1),
                    new Coefficient(1, 1),
                    new Coefficient(1, 1),
            };
        }
        return coefficients;
    }

    private static <T> void  Serialize(T coefficientsMap, String path) {
        try {
            FileOutputStream fileOut = new FileOutputStream(path);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(coefficientsMap);
            out.close();
            fileOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static <T> T Deserialize(String path) throws IOException, ClassNotFoundException {
        T data;
        FileInputStream fileIn = new FileInputStream(path);
        ObjectInputStream in = new ObjectInputStream(fileIn);
        data = (T) in.readObject();
        in.close();
        fileIn.close();
        return data;
    }

    private static void SerializeEvaluationCoefficients(Coefficient[] data){
        var path = "src/Assets/EvaluationCoefficientsMap.ser";
        Serialize(data, path);
    }

}
