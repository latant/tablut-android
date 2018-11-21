package hu.bme.aut.tablut.game;

import java.io.Serializable;
import java.util.Comparator;

import hu.bme.aut.tablut.R;

public class Player implements Serializable {

    private static final String SEPARATOR = "ß@@ß";
    public static final int KING_ID = R.drawable.ic_king;
    public static final int DEFENDER_ID = R.drawable.ic_defender;
    public static final int ATTACKER_ID = R.drawable.ic_attacker;

    private String name;
    private int soldierId;
    private int color;

    public int getDefendGames() {
        return defendGames;
    }
    public int getDefendVictories() {
        return defendVictories;
    }
    public int getAttackGames() {
        return attackGames;
    }
    public int getAttackVictories() {
        return attackVictories;
    }

    private int defendGames, defendVictories, attackGames, attackVictories;

    public Player(String name, int color, int soldierId) {
        this.name = name;
        this.color = color;
        this.soldierId = soldierId;
        defendGames = defendVictories = attackGames = attackVictories = 0;
    }

    public int getColor() { return color; }
    public String getName() { return name; }
    public int getSoldierId() { return soldierId; }

    public static String toString(Player p) {
        int[] args = { p.defendGames, p.defendVictories, p.attackGames, p.attackVictories};
        String result = p.name;
        for (int i = 0; i < args.length; i++)
            result += SEPARATOR + args[i];
        return result;
    }
    public static Player fromString(String s) {
        String[] args = s.split(SEPARATOR);
        Player p = new Player(args[0], 0, 0);
        p.defendGames = Integer.parseInt(args[1]);
        p.defendVictories = Integer.parseInt(args[2]);
        p.attackGames = Integer.parseInt(args[3]);
        p.attackVictories = Integer.parseInt(args[4]);
        return p;
    }

    public void setStatisticsFrom(Player p) {
        this.defendGames = p.defendGames;
        this.defendVictories = p.defendVictories;
        this.attackGames = p.attackGames;
        this.attackVictories = p.attackVictories;
    }

    public void win() {
        if (this.soldierId == DEFENDER_ID) {
            defendVictories++;
            defendGames++;
        } else {
            attackVictories++;
            attackGames++;
        }
    }
    public void lose() {
        if (this.soldierId == DEFENDER_ID) {
            defendGames++;
        } else {
            attackGames++;
        }
    }

    public static class WonMore implements Comparator<Player> {
        public int compare(Player o1, Player o2) {
            return (o2.attackVictories + o2.attackVictories)
                    - (o1.attackVictories + o1.defendVictories);
        }
    }
}
