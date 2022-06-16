package sogorae.billage.domain;

public enum Status {
    AVAILABLE, PENDING, UNAVAILABLE;

    public boolean isAvailable() {
        return this == AVAILABLE;
    }
}
