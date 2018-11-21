package hu.bme.aut.tablut.game;

public class Direction {
    public static final int DIR_COUNT = 4;
    public static final int NORTH = 0;
    public static final int EAST = 1;
    public static final int SOUTH = 2;
    public static final int WEST = 3;
    public static int getOppositeOf(int dir) { return (dir + 2) % DIR_COUNT; }
}
