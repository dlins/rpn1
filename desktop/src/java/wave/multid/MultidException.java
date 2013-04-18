/*
* Instituto de Matematica Pura e Aplicada - IMPA
* Departamento de Dinamica dos Fluidos
*
*/

package wave.multid;

public class MultidException extends Exception {
    //
    // Constants
    //
    public static final String DEFAULT_MESG = "JMultid has generated an Exception";
    //
    // Members
    //
    private CoordsArray coords_;

    //
    // Constructor
    //
    public MultidException(String mesg) { super(mesg); }

    public MultidException(CoordsArray exCoords) {
        coords_ = new CoordsArray(exCoords);
    }

    //
    // Accessors/Mutators
    //
    public String getMessage() {
        StringBuffer sBuffer = new StringBuffer(DEFAULT_MESG);
        if (coords_ != null)
            sBuffer.append(coords_);
        sBuffer.append(super.getMessage());
        return sBuffer.toString();
    }
}
