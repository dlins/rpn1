/*
 *
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpnumerics;

import java.util.logging.Level;
import java.util.logging.Logger;

public class SubInflectionExtensionCurveCalc extends BifurcationCurveCalc {

    //
    // Constructors/Initializers
    //
    int xResolution_;
    int yResolution_;
    int curveFamily_;
    int domainFamily_;
    int characteristicDomain_;

    static public int contSCE = 0;

    public SubInflectionExtensionCurveCalc(int xResolution, int yResolution, int leftFamily, int rightFamily,int characteristicDomain) {
        this.xResolution_ = xResolution;
        this.yResolution_ = yResolution;
        this.curveFamily_ = leftFamily;
        this.domainFamily_ = rightFamily;
        characteristicDomain_=characteristicDomain;
    }

    public SubInflectionExtensionCurveCalc() {
    }

    @Override
    public RpSolution calc() {
        RpSolution result = null;

        try {
            result = (SubInflectionExtensionCurve) nativeCalc(xResolution_, yResolution_, curveFamily_, domainFamily_, characteristicDomain_);

            //** acrescentei isso (Leandro)

            if (contSCE == 0) {
                System.out.println("Entrando em SubInflectionExtCurveCalc...");

                RPnCurve.lista.add((RPnCurve) result);
                System.out.println("Tamanho da lista: " + RPnCurve.lista.size());

                contSCE += 1;
            }

            //*********************************************

            return result;
        } catch (RpException ex) {
            Logger.getLogger(SubInflectionExtensionCurveCalc.class.getName()).log(Level.SEVERE, null, ex);
        }

        return result;
    }

    private native RpSolution nativeCalc(int xResolution, int yResolution, int leftFamily, int rightFamily,int characteristicDomain) throws RpException;
}
