package hu.bme.aut.tablut.ui.fragment;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Point;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.devs.vectorchildfinder.VectorChildFinder;
import com.devs.vectorchildfinder.VectorDrawableCompat;
import java.util.ArrayList;
import hu.bme.aut.tablut.R;
import hu.bme.aut.tablut.persistence.Serializer;
import hu.bme.aut.tablut.game.Player;
import hu.bme.aut.tablut.game.Tablut;
import hu.bme.aut.tablut.game.field.Field;
import hu.bme.aut.tablut.game.piece.Piece;
import hu.bme.aut.tablut.game.piece.PieceListener;

public class GameLocalFragment extends TablutFragment implements View.OnTouchListener {

    private static final int FOCUS_COLOR = Color.rgb(204, 255, 204);
    private static final int SELECT_COLOR = Color.rgb(204, 255, 153);
    private static final int CANT_STEP_COLOR = Color.rgb(255, 204, 204);
    private static final String LOCAL_GAME_FILE = "localgame.tablut";
    public static String TAG = GameLocalFragment.class.getName();
    protected int getLayoutResourceId() { return R.layout.fra_game; }
    private static Tablut game;

    public static void newGame(Player defender, Player attacker) {
        game = new Tablut(defender, attacker);
    }
    public static boolean canResume() {
        return game != null;
    }
    public static void saveGame(Context context) {
        if (game.getWinner() == null) {
            Serializer.save(context, game, LOCAL_GAME_FILE);
        } else {
            Serializer.save(context, null, LOCAL_GAME_FILE);
        }
    }
    public static void loadGame(Context context) {
        game = (Tablut) Serializer.load(context, LOCAL_GAME_FILE);
    }

    private Field[][] fields;
    private Piece selectedPiece;
    private VectorChildFinder fieldPathFinder;
    private FrameLayout frame;
    private ImageView tableView;
    private Point screenSize = new Point();
    private float fieldDistance, paddingX, paddingY, centerX, centerY;
    private Player winner;

    @Override
    protected void setListeners(View view) {
        frame = view.findViewById(R.id.tableFrame);
        getActivity().getWindowManager().getDefaultDisplay().getSize(screenSize);
        fieldDistance = screenSize.x * 0.10140478241253437f;
        paddingX = screenSize.x * 12.5f / 279.5f;
        paddingY = screenSize.x * 12.1f / 279.5f;
        centerX = paddingX + 4 * fieldDistance;
        centerY = paddingY + 4 * fieldDistance;
        frame.getLayoutParams().height = screenSize.x;
        frame.requestLayout();
        tableView = view.findViewById(R.id.tableImageView);
        tableView.setOnTouchListener(this);
        fieldPathFinder = new VectorChildFinder(getActivity(), R.drawable.ic_table, tableView);
        initGame();
        animateNames(view);
    }

    private void initGame() {
        fields = game.getFields();
        for (int i = 0; i < Tablut.SIZE; i++) {
            for (int j = 0; j < Tablut.SIZE; j++) {
                Field f = fields[i][j];
                f.tag = "field" + (i * Tablut.SIZE + j);
                f.color = fieldPathFinder.findPathByName(f.tag).getFillColor();
                f.x = paddingX + j * fieldDistance;
                f.y = paddingY + i * fieldDistance;
                final Piece p = f.getPiece();
                if (p != null) {
                    PieceHolder ph = new PieceHolder(p);
                    ph.animateToPlace();
                }
            }
        }
    }
    private void animateNames(View view) {
        final View defenderLayout = view.findViewById(R.id.defenderLayout);
        final View attackerLayout = view.findViewById(R.id.attackerLayout);
        ImageView defenderImage = view.findViewById(R.id.defenderLabel);
        ImageView attackerImage = view.findViewById(R.id.attackerLabel);
        TextView defenderText = view.findViewById(R.id.gameDefenderName);
        TextView attackerText = view.findViewById(R.id.gameAttackerName);
        defenderText.setText(game.getDefender().getName());
        attackerText.setText(game.getAttacker().getName());
        VectorChildFinder finder;
        finder = new VectorChildFinder(getActivity(), Player.DEFENDER_ID, defenderImage);
        finder.findPathByName("path").setFillColor(game.getDefender().getColor());
        finder = new VectorChildFinder(getActivity(), Player.ATTACKER_ID, attackerImage);
        finder.findPathByName("path").setFillColor(game.getAttacker().getColor());
        float x = screenSize.x / 2f - 85 * getDensity();
        ObjectAnimator.ofFloat(attackerLayout, "translationX", x).setDuration(1000).start();
        ObjectAnimator.ofFloat(defenderLayout, "translationX", -x).setDuration(1000).start();
    }
    private void pronounceWinner() {
        String sentence = getString(R.string.pronounceWinnerName, winner.getName());
        Toast.makeText(getActivity().getApplicationContext(), sentence, Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (winner != null) {
            pronounceWinner();
            return true;
        }
        if (selectedPiece == null) return true;
        int i = (int) ((event.getY() - paddingX) / fieldDistance);
        int j = (int) ((event.getX() - paddingY) / fieldDistance);
        if (0 <= i && i < Tablut.SIZE && 0 <= j && j < Tablut.SIZE) {
            clearSelectedPiece();
            tableView.invalidate();
            if (!selectedPiece.getApproachableFields().contains(fields[i][j]))
                return true;
            selectedPiece.stepTo(fields[i][j]);
            selectedPiece = null;
        }
        return true;
    }
    private void clearSelectedPiece() {
        setFieldColor(selectedPiece.getField(), selectedPiece.getField().color);
        for (Field field : selectedPiece.getApproachableFields())
            setFieldColor(field, field.color);
    }
    private void setFieldColor(Field field, int color) {
        VectorDrawableCompat.VFullPath path = fieldPathFinder.findPathByName(field.tag);
        path.setFillColor(color);
    }

    private class PieceHolder implements PieceListener, View.OnClickListener {

        private final Piece piece;
        private ImageView view;
        public PieceHolder(Piece p) {
            this.piece = p;
            p.setListener(this);
            view = new ImageView(getActivity());
            view.setX(centerX);
            view.setY(centerY);
            frame.addView(view);
            view.getLayoutParams().width = Math.round(fieldDistance);
            view.getLayoutParams().height = Math.round(fieldDistance);
            Resources res = getActivity().getBaseContext().getResources();
            view.setImageDrawable(res.getDrawable(p.getVectorId()));
            VectorChildFinder finder = new VectorChildFinder(getActivity(), p.getVectorId(), view);
            finder.findPathByName("path").setFillColor(p.getOwner().getColor());
            view.setOnClickListener(this);
        }

        public void animateToPlace() {
            ObjectAnimator.ofFloat(view, "translationX", piece.getField().x)
                    .setDuration(Piece.STEP_DELAY_MILLIS).start();
            ObjectAnimator.ofFloat(view, "translationY", piece.getField().y)
                    .setDuration(Piece.STEP_DELAY_MILLIS).start();
        }

        @Override
        public void afterDied(Piece p) {
            getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    view.setVisibility(View.INVISIBLE);
                }
            });
            if (p.getOwner() == game.getAttacker()) {
                incrementIntPref(ScoresFragment.ATTACKERS_KILLED_COUNT);
            } else {
                incrementIntPref(ScoresFragment.DEFENDERS_KILLED_COUNT);
            }
        }

        @Override
        public void beforeDied(Piece p) {
            getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    AnimatorSet as = new AnimatorSet();
                    as.playSequentially(
                            ObjectAnimator.ofFloat(view, "rotation", 0, 20),
                            ObjectAnimator.ofFloat(view, "rotation", 20, -20),
                            ObjectAnimator.ofFloat(view, "rotation", 20, -20),
                            ObjectAnimator.ofFloat(view, "rotation", -20, 0));
                    as.setDuration(Piece.STEP_DELAY_MILLIS / 4);
                    as.start();
                }
            });

        }

        @Override
        public void beforeStepped(Piece p) {
            incrementIntPref(ScoresFragment.STEP_COUNT);
            animateToPlace();
        }

        @Override
        public void afterStepped(Piece piece) {
            winner = game.getWinner();
            if (winner == null) return;
            getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    pronounceWinner();
                }
            });
            if (winner == game.getAttacker()) {
                incrementIntPref(ScoresFragment.ATTACKER_VICTORY_COUNT);
                game.getAttacker().win();
                game.getDefender().lose();
            } else {
                incrementIntPref(ScoresFragment.DEFENDER_VICTORY_COUNT);
                game.getDefender().win();
                game.getAttacker().lose();
            }
            ArrayList<Player> players = getPlayersPref();
            updatePlayerInList(game.getAttacker(), players);
            updatePlayerInList(game.getDefender(), players);
            setPlayersPref(players);
        }

        @Override
        public void onClick(View v) {
            if (winner != null) {
                pronounceWinner();
                return;
            }
            Piece sp = selectedPiece;
            if (sp != null) clearSelectedPiece();
            if (piece.getOwner() != game.getActualPlayer()) {
                selectedPiece = null;
            } else {
                selectedPiece = piece;
                ArrayList<Field> appr = piece.getApproachableFields();
                setFieldColor(piece.getField(),
                        appr.size() == 0 ? CANT_STEP_COLOR : SELECT_COLOR);
                for (Field field : appr)
                    setFieldColor(field, FOCUS_COLOR);
            }
            tableView.invalidate();
        }

    }
}
