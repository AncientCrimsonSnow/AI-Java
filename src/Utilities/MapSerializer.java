package Utilities;

import java.io.*;
import java.util.Map;

public class MapSerializer {
    public static <Key, Value>void serialize(Map<Key, Value> map, String filename) throws IOException {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filename))) {
            out.writeObject(map);
        }
    }

    public static <Key, Value> Map<Key, Value> deserialize(String filename) throws IOException, ClassNotFoundException {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(filename))) {
            return (Map<Key, Value>) in.readObject();
        }
    }
}
