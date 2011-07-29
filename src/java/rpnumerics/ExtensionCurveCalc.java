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
    int edge_;
    int edgeResolution_;

    static private int contEC = 0;      //** declarei isso (Leandro)

    public ExtensionCurveCalc(int xResolution, int yResolution, int edgeResolution,int leftFamily, int rightFamily,int edge, int characteristicDomain) {
        this.xResolution_ = xResolution;
        this.yResolution_ = yResolution;
        this.curveFamily_ = leftFamily;
        this.domainFamily_ = rightFamily;
        edge_=edge;
        edgeResolution_=edgeResolution;
        characteristicDomain_=characteristicDomain;
    }

    public ExtensionCurveCalc() {
    }

    @Override
    public RpSolution calc() {
        RpSolution result = null;

        try {
            result = (ExtensionCurve) nativeCalc(xResolution_,yResolution_,edgeResolution_,curveFamily_,domainFamily_,edge_,characteristicDomain_);

            //** acrescentei isso (Leandro)
            if (contEC == 0) {
                System.out.println("Entrando em ExtensionCurveCalc...");

                RPnCurve.lista.add((RPnCurve) result);
                System.out.println("Tamanho da lista: " + RPnCurve.lista.size());

                contEC += 1;
            }
            //*********************************************

            return result;
        } catch (RpException ex) {
            Logger.getLogger(ExtensionCurveCalc.class.getName()).log(Level.SEVERE, null, ex);
        }

        return result;
    }

    private native RpSolution nativeCalc(int xResolution, int yResolution,int edgeResolution, int leftFamily, int rightFamily,int edge,int characteristicDomain) throws RpException;
}
