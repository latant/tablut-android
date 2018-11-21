package hu.bme.aut.tablut.game.field;
import java.util.ArrayList;
import hu.bme.aut.tablut.game.piece.Piece;

public class Throne extends Field {

    @Override
    public boolean canKill(Piece piece) {
        return true;
    }

    @Override
    public ArrayList<Field> extendIfApproachable(ArrayList<Field> fields, int dir) {
        return fields;
    }

    @Override
    public Field ifCanLeadToSide(int i) {
        return null;
    }

}
