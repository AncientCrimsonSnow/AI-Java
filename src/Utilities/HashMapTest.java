package Utilities;

import java.util.HashMap;

public class HashMapTest {
    public static void main(String[] args) {
        HashMap<byte[], Integer> arrayMoveMap = new HashMap<>();

        // Annahme: Du hast bereits ein Array und den entsprechenden Move
        byte[] array1 = {1, 2, 3, 4, 5, 6, 7, 8, 9};

        // Speichere die Zuordnung in der HashMap
        arrayMoveMap.put(array1, 1);

        // Beispiel: Du hast ein anderes Array mit den gleichen Bytes wie array1
        byte[] array2 = {1, 2, 3, 4, 5, 6, 7, 8, 9};

        // Erhalte den passenden Move aus der HashMap
        Integer value = arrayMoveMap.get(array2);

        // Überprüfe, ob move2 den erwarteten Wert hat
        if (value != null) {
            System.out.println("Der passende Move für array2 wurde gefunden: ");
        } else {
            System.out.println("Es wurde kein passender Move für array2 gefunden.");
        }
    }
}
