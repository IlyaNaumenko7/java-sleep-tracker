package ru.yandex.practicum.sleeptracker;

public enum Chronotype {
    OWL("Owl"),
    LARK("Lark"),
    PIGEON("Pigeon");

    private final String displayName;

    Chronotype(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}