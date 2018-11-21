package hu.bme.aut.tablut.ui.activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import hu.bme.aut.tablut.ui.fragment.GameLocalFragment;
import hu.bme.aut.tablut.ui.fragment.MainFragment;
import hu.bme.aut.tablut.R;
import hu.bme.aut.tablut.ui.fragment.PlayNetworkFragment;
import hu.bme.aut.tablut.ui.fragment.PlayLocalFragment;
import hu.bme.aut.tablut.ui.fragment.ScoresFragment;

public class MainActivity extends AppCompatActivity {

    public static SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = getSharedPreferences(getApplicationInfo().name, Context.MODE_PRIVATE);
        showFragmentByTag(MainFragment.TAG);
        setContentView(R.layout.act_main);
    }

    public void showFragmentByTag(String tag) {
        Fragment fragment = getFragmentByTag(tag);
        if (fragment == null) return;
        getFragmentManager().beginTransaction()
                .replace(R.id.container, fragment, tag)
                .addToBackStack(tag)
                .commit();
    }

    private Fragment getFragmentByTag(String tag) {
        Fragment fragment = getFragmentManager().findFragmentByTag(tag);
        if (fragment != null) return fragment;
        if (MainFragment.TAG.equals(tag)) return new MainFragment();
        if (PlayLocalFragment.TAG.equals(tag)) return new PlayLocalFragment();
        if (GameLocalFragment.TAG.equals(tag)) return new GameLocalFragment();
        if (PlayNetworkFragment.TAG.equals(tag)) return new PlayNetworkFragment();
        if (ScoresFragment.TAG.equals(tag)) return new ScoresFragment();
        return null;
    }

    @Override
    public void onBackPressed() {
        if (getBackStackFragmentNameAt(0).equals(GameLocalFragment.TAG)) {
            if (getBackStackFragmentNameAt(1).equals(PlayLocalFragment.TAG))
                super.onBackPressed();
            GameLocalFragment.saveGame(this);
        }
        if (getBackStackFragmentNameAt(0).equals(MainFragment.TAG)) {
            super.onBackPressed();
        }
        super.onBackPressed();
    }

    private String getBackStackFragmentNameAt(int i) {
        FragmentManager fm = getFragmentManager();
        return fm.getBackStackEntryAt(fm.getBackStackEntryCount() - 1 - i).getName();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }

}
