package hu.bme.aut.tablut.ui.fragment;
import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import hu.bme.aut.tablut.game.Player;
import hu.bme.aut.tablut.ui.activity.MainActivity;

public abstract class TablutFragment extends Fragment {

    protected SharedPreferences sharedPreferences;

    protected void showFragmentByTag(String tag) {
        ((MainActivity)getActivity()).showFragmentByTag(tag);
    }
    protected abstract int getLayoutResourceId();
    protected abstract void setListeners(View view);

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView =  inflater.inflate(getLayoutResourceId(), container, false);
        sharedPreferences = getActivity().getPreferences(Context.MODE_PRIVATE);
        setListeners(rootView);
        return rootView;
    }

    protected int getIntPref(String key) {
        return sharedPreferences.getInt(key, 0);
    }
    protected void setIntPref(String key, int value) {
        sharedPreferences.edit().putInt(key, value).apply();
    }
    protected void incrementIntPref(String key) {
        setIntPref(key, getIntPref(key) + 1);
    }
    protected ArrayList<Player> getPlayersPref() {
        Set set = sharedPreferences.getStringSet(ScoresFragment.PLAYER_STRING_SET, null);
        if (set == null)
            return new ArrayList<>();
        ArrayList<Player> result = new ArrayList<>();
        for (Object o : set) {
            result.add(Player.fromString((String) o));
        }
        return result;
    }
    protected void setPlayersPref(ArrayList<Player> players) {
        Set<String> playerStrings = new HashSet<>();
        for(Player p : players) {
            playerStrings.add(Player.toString(p));
        }
        sharedPreferences
                .edit()
                .putStringSet(ScoresFragment.PLAYER_STRING_SET, playerStrings)
                .apply();
    }
    protected Player findPlayerByName(Player player, ArrayList<Player> players) {
        for(Player p : players) {
            if(p.getName().equals(player.getName())) {
                return p;
            }
        }
        return null;
    }
    protected void updatePlayerInList(Player player, ArrayList<Player> players) {
        Player p = findPlayerByName(player, players);
        if (p != null)
            players.remove(p);
        players.add(player);
    }

    protected float getDensity() {
        return getActivity().getResources().getDisplayMetrics().density;
    }

}
