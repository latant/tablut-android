package hu.bme.aut.tablut.game.piece;
import hu.bme.aut.tablut.game.Direction;
import hu.bme.aut.tablut.game.Player;
import hu.bme.aut.tablut.game.Tablut;
import hu.bme.aut.tablut.game.field.Field;
import hu.bme.aut.tablut.game.field.Ground;

public class Soldier extends Piece {

    public Soldier(Player player, Ground field, Tablut game) {
        super(player, field, game);
    }

    @Override
    public void getKilledFrom(int dir) {
        Field neighbour1 = field.getNeighbourTo(dir);
        if (neighbour1 == null) return;
        Field neighbour2 = field.getNeighbourTo(Direction.getOppositeOf(dir));
        if (neighbour2 == null) return;
        if (neighbour1.canKill(this) && neighbour2.canKill(this))
            die();
    }

    @Override
    public int getVectorId() {
        return player.getSoldierId();
    }

}
