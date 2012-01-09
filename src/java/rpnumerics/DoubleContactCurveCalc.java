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

    static private int contDC = 0;      //** declarei isso (Leandro)

    //
    // Constructors/Initializers
    //
    int xResolution_;
    int yResolution_;
    int curveFamily_;
    int domainFamily_;

    public DoubleContactCurveCalc(int xResolution, int yResolution, int leftFamily, int rightFamily) {
        this.xResolution_ = xResolution;
        this.yResolution_ = yResolution;
        this.curveFamily_ = leftFamily;
        this.domainFamily_ = rightFamily;
    }

    public DoubleContactCurveCalc() {
    }

    public int getCurveFamily() {
        return curveFamily_;
    }

    public int getDomainFamily() {
        return domainFamily_;
    }

    @Override
    public RpSolution calc() {
        RpSolution result = null;

        try {
            result = (DoubleContactCurve) nativeCalc(xResolution_,yResolution_,curveFamily_,domainFamily_);
           

            //** acrescentei isso (Leandro)
            if (contDC == 0) {
                System.out.println("Entrando em DoubleContactCurveCalc...");

                RPnCurve.lista.add((RPnCurve) result);
                System.out.println("Tamanho da lista: " + RPnCurve.lista.size());

                contDC += 1;
            }
            //*********************************************

            return result;
        } catch (RpException ex) {
            Logger.getLogger(DoubleContactCurveCalc.class.getName()).log(Level.SEVERE, null, ex);
        }

        return result;
    }

    private native RpSolution nativeCalc(int xResolution, int yResolution, int curveFamily, int domainFamily) throws RpException;
}
