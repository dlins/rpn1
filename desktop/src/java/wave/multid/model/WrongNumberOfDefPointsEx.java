/*
* Instituto de Matematica Pura e Aplicada - IMPA
* Departamento de Dinamica dos Fluidos
*
*/

package wave.multid.model;

import wave.multid.CoordsArray;
import wave.multid.MultidException;

public class WrongNumberOfDefPointsEx extends MultidException {
    //
    // Constants
    //
    public static final String DEFAULT_MESG = "Wrong number of Definition Points Exception";

    //
    // Constructor
    //
    public WrongNumberOfDefPointsEx() { super(DEFAULT_MESG); }

    public WrongNumberOfDefPointsEx(CoordsArray exCoords) { super(exCoords); }

    //
    // Accessors/Mutators
    //
    public String getMesg() {
        return DEFAULT_MESG + super.getMessage();
    }
}
