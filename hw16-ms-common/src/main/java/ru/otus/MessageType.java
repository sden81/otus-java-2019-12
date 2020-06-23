package ru.otus;

public enum MessageType {
    USER_DATA("UserData"),
    ALL_USERS_DATA("AllUsersData"),
    SAVE_USER("SaveUser");

    private final String value;

    MessageType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
