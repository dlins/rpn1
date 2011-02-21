/*
 *
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpnumerics;

import java.util.logging.Level;
import java.util.logging.Logger;

public class DoubleContactCurveCalc extends BifurcationCurveCalc {

    //
    // Constructors/Initializers
    //
    public DoubleContactCurveCalc() {
    }

    @Override
    public RpSolution calc() {
        RpSolution result = null;

        try {
            result = (DoubleContactCurve) nativeCalc();
            //          if (result == null) {
            //            throw new RpException("Error in native layer");
            //        }
            return result;
        } catch (RpException ex) {
            Logger.getLogger(DoubleContactCurveCalc.class.getName()).log(Level.SEVERE, null, ex);
        }

        return result;
    }

    private native RpSolution nativeCalc() throws RpException;
}
