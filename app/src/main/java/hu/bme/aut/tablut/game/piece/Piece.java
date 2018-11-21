package hu.bme.aut.tablut.game.piece;
import java.io.Serializable;
import java.util.ArrayList;
import hu.bme.aut.tablut.game.Direction;
import hu.bme.aut.tablut.game.Player;
import hu.bme.aut.tablut.game.Tablut;
import hu.bme.aut.tablut.game.TimedAction;
import hu.bme.aut.tablut.game.field.Field;

public abstract class Piece implements Serializable {

    public static final int STEP_DELAY_MILLIS = 500;
    public static final int DIE_DELAY_MILLIS = 500;

    protected Field field;
    protected Player player;
    private Tablut game;
    transient protected PieceListener listener;

    Piece(Player player, Field field, Tablut game) {
        this.player = player;
        this.field = field;
        this.game = game;
        field.setPiece(this);
    }

    public boolean isEnemyOf(Piece soldier) {
        return this.player != soldier.player;
    }

    public abstract void getKilledFrom(int dir);

    void die() {
        if (listener != null)
            listener.beforeDied(this);
        final Piece thisPiece = this;
        (new TimedAction(DIE_DELAY_MILLIS) {
            public void action() {
                field.clearPiece();
                if (listener != null)
                    listener.afterDied(thisPiece);
            }
        }).start();
    }

    public ArrayList<Field> getApproachableFields() {
        ArrayList<Field> result = new ArrayList<>();
        for (int i = 0; i < Direction.DIR_COUNT; i++) {
            Field neighbour = this.field.getNeighbourTo(i);
            if ( neighbour != null)
                neighbour.extendIfApproachable(result, i);
        }
        return result;
    }

    public void stepTo(final Field field) {
        this.field.clearPiece();
        this.field = field;
        field.setPiece(this);
        game.nextPlayer();
        final Piece thisPiece = this;
        if (listener != null)
            listener.beforeStepped(this);
        (new TimedAction(STEP_DELAY_MILLIS) {
            public void action() {
                field.strike();
                if (listener != null)
                    listener.afterStepped(thisPiece);
            }
        }).start();
    }

    public Field getField() { return this.field; }
    public Player getOwner() { return this.player; }

    public void setListener(PieceListener listener) { this.listener = listener; }

    public abstract int getVectorId();
}
