package be.kdg.processor.fine;

public enum FineType {
    EMISSION("Emission"),
    SPEED("Speed");

    private final String string;

    FineType(String string) {
        this.string = string;
    }

    @Override
    public String toString() {
        return string;
    }
}
