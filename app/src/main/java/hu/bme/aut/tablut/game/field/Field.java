package hu.bme.aut.tablut.game.field;
import java.io.Serializable;
import java.util.ArrayList;
import hu.bme.aut.tablut.game.Direction;
import hu.bme.aut.tablut.game.piece.Piece;

public abstract class Field implements Serializable {

    protected Field[] neighbours = new Field[Direction.DIR_COUNT];
    Piece piece;

    public int color;
    public String tag;
    public float x, y;

    public static void bindFields(Field[][] fields) {
        for (int i = 0; i < fields.length; i ++)
            for (int j = 1; j < fields[i].length; j++)
                fields[i][j].bindTo(fields[i][j-1], Direction.WEST);
        for (int i = 1; i < fields.length; i ++)
            for (int j = 0; j < fields[i].length; j++)
                fields[i][j].bindTo(fields[i-1][j], Direction.NORTH);
    }

    private void bindTo(Field field, int dir) {
        this.neighbours[dir] = field;
        field.neighbours[Direction.getOppositeOf(dir)] = this;
    }

    public void setPiece(Piece piece) { this.piece = piece; }
    public void clearPiece() { this.piece = null; }
    public Piece getPiece() { return this.piece; }

    public Field getNeighbourTo(int dir) { return neighbours[dir]; }

    public void strike() {
        for(int i = 0; i < Direction.DIR_COUNT; i++) {
            strikeInDirection(i);
        }
    }

    private void strikeInDirection(int dir) {
        Field neighbour = neighbours[dir];
        if (neighbour == null) return;
        if (neighbour.piece == null) return;
        neighbour.piece.getKilledFrom(Direction.getOppositeOf(dir));
    }

    public abstract boolean canKill(Piece piece);

    public abstract ArrayList<Field> extendIfApproachable(ArrayList<Field> fields, int dir);

    public abstract Field ifCanLeadToSide(int i);

    public boolean isSide() {
        for (Field n : neighbours)
            if (n == null)
                return true;
        return false;
    }
}
