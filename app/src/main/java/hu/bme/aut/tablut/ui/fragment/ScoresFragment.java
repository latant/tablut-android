package hu.bme.aut.tablut.ui.fragment;
import android.content.SharedPreferences;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

import hu.bme.aut.tablut.R;
import hu.bme.aut.tablut.game.Player;

public class ScoresFragment extends TablutFragment implements View.OnClickListener {

    public static final String TAG = TablutFragment.class.getName();
    protected int getLayoutResourceId() { return R.layout.fra_scores; }

    public static final String STEP_COUNT = "stepCount";
    public static final String ATTACKERS_KILLED_COUNT = "attackersKilled";
    public static final String DEFENDERS_KILLED_COUNT = "defendersKilled";
    public static final String ATTACKER_VICTORY_COUNT = "attackerVictories";
    public static final String DEFENDER_VICTORY_COUNT = "defenderVictories";
    public static final String PLAYER_STRING_SET = "playerStringSett";

    private TextView stepCountText;
    private TextView attackerKillCountText;
    private TextView defenderKillCountText;
    private TextView attackerVictoryCountText;
    private TextView defenderVictoryCountText;
    private GridLayout recordsGridLayout;

    @Override
    protected void setListeners(View view) {
        Button resetButton = view.findViewById(R.id.resetScores);
        resetButton.setOnClickListener(this);
        this.stepCountText = view.findViewById(R.id.stepCountTextView);
        this.attackerKillCountText = view.findViewById(R.id.attackerKillCountTextView);
        this.defenderKillCountText = view.findViewById(R.id.defenderKillCountTextView);
        this.attackerVictoryCountText = view.findViewById(R.id.attackerVictoryCountTextView);
        this.defenderVictoryCountText = view.findViewById(R.id.defenderVictoryCountTextView);
        this.recordsGridLayout = view.findViewById(R.id.recordsGrid);
        syncValues();
    }

    private void syncValues() {
        stepCountText.setText(""+getIntPref(STEP_COUNT));
        attackerKillCountText.setText(""+getIntPref(ATTACKERS_KILLED_COUNT));
        defenderKillCountText.setText(""+getIntPref(DEFENDERS_KILLED_COUNT));
        attackerVictoryCountText.setText(""+getIntPref(ATTACKER_VICTORY_COUNT));
        defenderVictoryCountText.setText(""+getIntPref(DEFENDER_VICTORY_COUNT));
        ArrayList<Player> players = getPlayersPref();
        Collections.sort(players, new Player.WonMore());
        for (int i = 0; i < players.size(); i++) {
            Player p = players.get(i);
            recordsGridLayout.addView(newTextViewWithWidth(20, (i+1)+"."));
            recordsGridLayout.addView(newTextViewWithWidth(80, p.getName()));
            String scoreStr = "DEF: " + p.getDefendVictories() + "/" + p.getDefendGames()
                    + " ATT: " + p.getAttackVictories() + "/" + p.getAttackGames();
            recordsGridLayout.addView(newTextViewWithWidth(140, scoreStr));
        }
    }

    private TextView newTextViewWithWidth(int w, String text) {
        TextView view = (TextView) LayoutInflater.from(recordsGridLayout.getContext())
                .inflate(R.layout.text_menu, recordsGridLayout, false);
        view.getLayoutParams().width = (int)(w * getDensity());
        view.setText(text);
        return view;
    }

    @Override
    public void onClick(View v) {
        setIntPref(STEP_COUNT, 0);
        setIntPref(DEFENDERS_KILLED_COUNT, 0);
        setIntPref(ATTACKERS_KILLED_COUNT, 0);
        setIntPref(DEFENDER_VICTORY_COUNT, 0);
        setIntPref(ATTACKER_VICTORY_COUNT, 0);
        setPlayersPref(new ArrayList<Player>());
        this.recordsGridLayout.removeAllViews();
        syncValues();
    }

}
