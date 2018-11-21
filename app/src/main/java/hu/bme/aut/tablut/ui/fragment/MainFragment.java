package hu.bme.aut.tablut.ui.fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import hu.bme.aut.tablut.R;

public class MainFragment extends TablutFragment implements View.OnClickListener {

    public static final String TAG = MainFragment.class.getName();
    protected int getLayoutResourceId() { return R.layout.fra_main; }

    private Button playLocalBtn, resumeLocalBtn, playNetworkBtn, scoresBtn;

    @Override
    protected void setListeners(View view) {

        ViewGroup mainMenu = view.findViewById(R.id.mainMenu);
        playLocalBtn = view.findViewById(R.id.playLocalButton);
        playNetworkBtn = view.findViewById(R.id.playNetworkButton);
        scoresBtn = view.findViewById(R.id.scoresButton);

        playLocalBtn.setOnClickListener(this);
        playNetworkBtn.setOnClickListener(this);
        scoresBtn.setOnClickListener(this);

        GameLocalFragment.loadGame(getActivity());
        if (GameLocalFragment.canResume()) {
            resumeLocalBtn = (Button) LayoutInflater.from(mainMenu.getContext())
                    .inflate(R.layout.btn_menu, mainMenu, false);
            resumeLocalBtn.setText(R.string.resumeLocal);
            mainMenu.addView(resumeLocalBtn, 0);
            resumeLocalBtn.setOnClickListener(this);
        }

    }

    @Override
    public void onClick(View v) {
        if (v == playLocalBtn) {
            showFragmentByTag(PlayLocalFragment.TAG);
        } else if (v == resumeLocalBtn) {
            showFragmentByTag(GameLocalFragment.TAG);
        } else if (v == playNetworkBtn) {
            showFragmentByTag(PlayNetworkFragment.TAG);
        } else if (v == scoresBtn) {
            showFragmentByTag(ScoresFragment.TAG);
        }
    }

}
