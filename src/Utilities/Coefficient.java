package Utilities;


import java.io.Serializable;

public class Coefficient implements Serializable {
    public float value;
    public int changes;

    public Coefficient(float value, int changes) {
        this.value = value;
        this.changes = changes;
    }
}
