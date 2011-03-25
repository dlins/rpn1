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
    int curveFamily_;
    int domainFamily_;
    int characteristicDomain_;

    public ExtensionCurveCalc(int xResolution, int yResolution, int leftFamily, int rightFamily,int characteristicDomain) {
        this.xResolution_ = xResolution;
        this.yResolution_ = yResolution;
        this.curveFamily_ = leftFamily;
        this.domainFamily_ = rightFamily;
        characteristicDomain_=characteristicDomain;
    }

    public ExtensionCurveCalc() {
    }

    @Override
    public RpSolution calc() {
        RpSolution result = null;

        try {
            result = (ExtensionCurve) nativeCalc(xResolution_,yResolution_,curveFamily_,domainFamily_,characteristicDomain_);
            return result;
        } catch (RpException ex) {
            Logger.getLogger(ExtensionCurveCalc.class.getName()).log(Level.SEVERE, null, ex);
        }

        return result;
    }

    private native RpSolution nativeCalc(int xResolution, int yResolution, int leftFamily, int rightFamily,int characteristicDomain) throws RpException;
}
