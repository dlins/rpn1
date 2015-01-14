/*
* Instituto de Matematica Pura e Aplicada - IMPA
* Departamento de Dinamica dos Fluidos
*
*/

package wave.multid;

public class DimMismatchEx extends MultidException {
    //
    // Constants
    //
    public static final String DEFAULT_MESG = "Dimension Mismatch Exception";

    //
    // Constructor
    //
    public DimMismatchEx(String mesg) { super(mesg); }

    public DimMismatchEx(CoordsArray exCoords) { super(exCoords); }

    //
    // Accessors/Mutators
    //
    public String getMesg() {
        return DEFAULT_MESG + super.getMessage();
    }
}
