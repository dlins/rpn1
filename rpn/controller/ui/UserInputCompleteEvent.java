/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */

package rpn.controller.ui;

import wave.util.RealVector;

/** This class holds all user inputs . This inputs are stored as a {@link RealVector } object . */

public class UserInputCompleteEvent {
    //
    // Members
    //
    private RealVector input_;

    public UserInputCompleteEvent(RealVector input) {
        input_ = new RealVector(input);
    }

    /** Returns the input made by the user. */

    public RealVector userInput() { return input_; }
}
