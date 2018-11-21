package hu.bme.aut.tablut.game;
import java.io.Serializable;
import hu.bme.aut.tablut.game.field.Ground;
import hu.bme.aut.tablut.game.field.Throne;
import hu.bme.aut.tablut.game.piece.King;
import hu.bme.aut.tablut.game.field.Field;
import hu.bme.aut.tablut.game.piece.Soldier;

public class Tablut implements Serializable {

    public static Player player1Arg, player2Arg;
    public static final int SIZE = 9;
    private static final int HALF_SIZE = SIZE / 2;
    private Player defender, attacker, actual;

    private Field[][] fields;
    private King king;

    public Tablut(Player defender, Player attacker) {
        this.defender = defender;
        this.attacker = attacker;
        actual = defender;
        fields = new Field[SIZE][];
        for (int i = 0; i < SIZE; i++) fields[i] = new Field[SIZE];
        createKing(defender);
        createSoldiers(1, HALF_SIZE, defender);
        createSoldiers(2, HALF_SIZE, defender);
        createSoldiers(3, HALF_SIZE, this.attacker);
        createSoldierRows(4, HALF_SIZE - 1, HALF_SIZE + 1, this.attacker);
        fillWithGrounds();
        Field.bindFields(fields);
    }
    private void createKing(Player player) {
        Throne throne = new Throne();
        king = new King(player, throne, this);
        fields[HALF_SIZE][HALF_SIZE] = throne;
    }
    private void createSoldierAt(int i, int j, Player player) {
        Ground ground = new Ground();
        new Soldier(player, ground, this);
        fields[i][j] = ground;
    }
    private void createSoldiers(int cylinder, int at, Player player) {
        createSoldierAt(HALF_SIZE - cylinder, at, player);
        createSoldierAt(HALF_SIZE + cylinder, at, player);
        createSoldierAt(at, HALF_SIZE - cylinder, player);
        createSoldierAt(at, HALF_SIZE + cylinder, player);
    }
    private void createSoldierRows(int cylinder, int start, int end, Player player) {
        for (int i = start; i <= end; i++) {
            createSoldiers(cylinder, i, player);
        }
    }
    private void fillWithGrounds() {
        for (int i = 0; i < SIZE; i++)
            for (int j = 0; j < SIZE; j++)
                if (fields[i][j] == null)
                    fields[i][j] = new Ground();
    }

    public Field[][] getFields() {
        return fields;
    }

    public Player getActualPlayer() {
        return actual;
    }
    public void nextPlayer() {
        actual =  actual == defender ? attacker : defender;
    }
    public Player getDefender() { return defender; }
    public Player getAttacker() { return attacker; }

    public Player getWinner() {
        if (king.isDied())
            return attacker;
        if (king.hasWon())
            return defender;
        return null;
    }

    public King getKing() { return king; }

}
