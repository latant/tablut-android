package hu.bme.aut.tablut.game;

public abstract class TimedAction extends Thread {

    private int millis;

    public TimedAction(int millis) { this.millis = millis; }

    public abstract void action();

    @Override
    public void run() {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            action();
        }
    }
}
