/*
 *
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpnumerics;

import java.util.logging.Level;
import java.util.logging.Logger;

public class ExtensionCurveCalc extends BifurcationCurveCalc {

    //
    // Constructors/Initializers
    //
    int xResolution_;
    int yResolution_;
    int leftFamily_;
    int rightFamily_;
    int caracteristicDomain_;

    public ExtensionCurveCalc(int xResolution, int yResolution, int leftFamily, int rightFamily,int caracteristicDomain) {
        this.xResolution_ = xResolution;
        this.yResolution_ = yResolution;
        this.leftFamily_ = leftFamily;
        this.rightFamily_ = rightFamily;
        caracteristicDomain_=caracteristicDomain;
    }

    public ExtensionCurveCalc() {
    }

    @Override
    public RpSolution calc() {
        RpSolution result = null;

        try {
            result = (ExtensionCurve) nativeCalc(xResolution_,yResolution_,leftFamily_,rightFamily_,caracteristicDomain_);
            return result;
        } catch (RpException ex) {
            Logger.getLogger(ExtensionCurveCalc.class.getName()).log(Level.SEVERE, null, ex);
        }

        return result;
    }

    private native RpSolution nativeCalc(int xResolution, int yResolution, int leftFamily, int rightFamily,int caracteristicDomain) throws RpException;
}
