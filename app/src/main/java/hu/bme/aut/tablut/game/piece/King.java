package hu.bme.aut.tablut.game.piece;
import java.io.Serializable;
import java.util.ArrayList;

import hu.bme.aut.tablut.game.Direction;
import hu.bme.aut.tablut.game.Player;
import hu.bme.aut.tablut.game.Tablut;
import hu.bme.aut.tablut.game.field.Field;
import hu.bme.aut.tablut.game.field.Throne;

public class King extends Piece {

    private boolean isDied = false;

    public King(Player player, Throne field, Tablut game) {
        super(player, field, game);
    }

    @Override
    public void getKilledFrom(int dir) {
        for (int i = 0; i < Direction.DIR_COUNT; i++) {
            Field neighbour = this.field.getNeighbourTo(i);
            if (neighbour == null)
                return;
            if (!neighbour.canKill(this))
                return;
        }
        die();
    }

    @Override
    public int getVectorId() {
        return Player.KING_ID;
    }

    @Override
    void die() {
        this.isDied = true;
        super.die();
    }

    public boolean isDied() { return this.isDied; }
    public boolean hasWon() { return getVisitableSides().size() >= 2; }

    public ArrayList<Field> getVisitableSides() {
        ArrayList<Field> result = new ArrayList<>();
        for (int i = 0; i < Direction.DIR_COUNT; i++) {
            Field neighbour = field.getNeighbourTo(i);
            if (neighbour == null) continue;
            Field side = neighbour.ifCanLeadToSide(i);
            if (side != null)
                result.add(side);
        }
        return result;
    }

}
