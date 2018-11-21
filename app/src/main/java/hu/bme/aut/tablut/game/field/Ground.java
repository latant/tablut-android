package hu.bme.aut.tablut.game.field;
import java.util.ArrayList;
import hu.bme.aut.tablut.game.piece.Piece;

public class Ground extends Field {

    @Override
    public boolean canKill(Piece piece) {
        return this.piece != null && this.piece.isEnemyOf(piece);
    }

    @Override
    public ArrayList<Field> extendIfApproachable(ArrayList<Field> fields, int dir) {
            if (this.piece != null)
                return fields;
            fields.add(this);
            Field neighbour = neighbours[dir];
            if (neighbour == null)
                return fields;
            return neighbour.extendIfApproachable(fields, dir);
    }

    @Override
    public Field ifCanLeadToSide(int dir) {
        if (this.piece != null) return null;
        return neighbours[dir] == null ? this : neighbours[dir].ifCanLeadToSide(dir);
    }

}
