/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */

package wave.util;

public class LoopConstruct implements Runnable {
    private Runnable callback_;
    private int upperBound_;
    private int lowerBound_;
    private int current_;
    private int increment_;

    public LoopConstruct(Runnable callback, int lower, int upper, int increment) {
        lowerBound_ = lower;
        upperBound_ = upper;
        increment_ = increment;
        current_ = lowerBound_;
        callback_ = callback;
    }

    public void run() {
        while (!isFinished()) {
            callback_.run();
            increment();
        }
        reset();
    }

    protected void reset() {
        current_ = lowerBound_;
    }

    protected void increment() {
        current_ += increment_;
        //        current_ = Math.floor(current_) + increment_;
    }

    protected boolean isFinished() {
        if (current_ < upperBound_)
            return false;
        return true;
    }

    public int current() { return current_; }
}
