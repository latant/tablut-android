package hu.bme.aut.tablut.game.piece;

public interface PieceListener {

    public abstract void afterDied(Piece piece);

    public abstract void beforeDied(Piece piece);

    public abstract void beforeStepped(Piece piece);

    public abstract void afterStepped(Piece piece);

}
