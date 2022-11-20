package application.controller;

public class Composition {
    private final PlayerController circle;
    private final PlayerController line;

    public PlayerController getCircle() {
        return circle;
    }

    public PlayerController getLine() {
        return line;
    }

    public boolean contains(String name) {
        return circle.getPlayerName().equals(name) || line.getPlayerName().equals(name);
    }

    public Composition(PlayerController circle, PlayerController line) {
        this.circle = circle;
        this.line = line;
    }
}
