package hu.bme.aut.tablut.ui.fragment;
import android.graphics.Color;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

import hu.bme.aut.tablut.R;
import hu.bme.aut.tablut.game.Player;

public class PlayLocalFragment extends TablutFragment implements View.OnClickListener {

    public static final String TAG = PlayLocalFragment.class.getName();
    protected int getLayoutResourceId() { return R.layout.fra_playlocal; }
    private EditText name1, name2;

    @Override
    protected void setListeners(View view) {
        name1 = view.findViewById(R.id.localPlayer1Name);
        name2 = view.findViewById(R.id.localPlayer2Name);
        view.findViewById(R.id.startLocalButton).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (name1.getText().length() == 0 || name2.getText().length() == 0) {
            Toast.makeText(getActivity(), R.string.fields_unfilled, Toast.LENGTH_SHORT)
                    .show();
        } else {
            Player p1 = new Player(name1.getText().toString(), Color.WHITE, Player.DEFENDER_ID);
            Player p2 = new Player(name2.getText().toString(), Color.BLACK, Player.ATTACKER_ID);
            ArrayList<Player> players = getPlayersPref();
            loadPlayerStatistics(p1, players);
            loadPlayerStatistics(p2, players);
            GameLocalFragment.newGame(p1, p2);
            showFragmentByTag(GameLocalFragment.TAG);
        }
    }

    private void loadPlayerStatistics(Player player, ArrayList<Player> players) {
        Player p = findPlayerByName(player, players);
        if (p != null)
            player.setStatisticsFrom(p);
    }


}
