/*
* Instituto de Matematica Pura e Aplicada - IMPA
* Departamento de Dinamica dos Fluidos
*
*/

package wave.multid.map;

import wave.multid.MultidException;
import wave.multid.CoordsArray;

public class NoInverseMapEx extends MultidException {
    //
    // Constants
    //
    public static final String DEFAULT_MESG = "Inverse Map not Supported Exception";

    //
    // Constructor
    //
    public NoInverseMapEx(String mesg) { super(mesg); }

    public NoInverseMapEx(CoordsArray exCoords) { super(exCoords); }

    //
    // Methods
    //
    public String toString() {
        return (DEFAULT_MESG + super.toString());
    }
}
