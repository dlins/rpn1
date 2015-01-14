/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */

package rpn.controller.ui;

import wave.util.RealVector;

public class UserInputTable {
    private boolean[] inputFlag_;
    private RealVector inputValue_;
    private RealVector lastValue_;

    public UserInputTable(int size) {
        inputValue_ = new RealVector(size);
        lastValue_ = new RealVector(size);
        inputFlag_ = new boolean[size];
    }

    public void setElement(int i, double value) {
        inputFlag_[i] = true;
        inputValue_.setElement(i, value);
        
    }

    public double getElement(int i) { return inputValue_.getElement(i); }

    public RealVector values() { return inputValue_; }

    public RealVector lastValues() { return lastValue_; }

    public boolean[] flags() { return inputFlag_; }

    //
    // Methods
    //
    public void reset() {

        for (int i = 0; i < inputValue_.getSize(); i++)
            inputFlag_[i] = false;

	lastValue_ = new RealVector(inputValue_);
    }

    public boolean isComplete(int i) {

        if (!(inputFlag_[i]))
            return false;
        return true;
    }

    public boolean isComplete() {

        for (int i = 0; i < inputValue_.getSize(); i++)
            if (!(inputFlag_[i]))
                return false;
        return true;
    }
}
