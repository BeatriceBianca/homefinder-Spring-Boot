package epa.homefinder.entity;

public enum UserType {
    ADMIN ("ADMIN"),
    USER("USER"),
    AGENT_IMOBILIAR("AGENT IMOBILIAR");

    private final String name;

    UserType(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
