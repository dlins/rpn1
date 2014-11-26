/*
 *
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpnumerics;

import java.util.logging.Level;
import java.util.logging.Logger;
import rpn.configuration.CommandConfiguration;

public class SubInflectionExtensionCurveCalc extends ContourCurveCalc {

    //
    // Constructors/Initializers
    //
    int xResolution_;
    int yResolution_;
    int curveFamily_;
    int domainFamily_;
    int characteristicDomain_;

    public SubInflectionExtensionCurveCalc(ContourParams params, int leftFamily, int rightFamily, int characteristicDomain) {
        super(params);
        this.curveFamily_ = leftFamily;
        this.domainFamily_ = rightFamily;
        characteristicDomain_ = characteristicDomain;



        String className = getClass().getSimpleName().toLowerCase();

        String curveName = className.replace("calc", "");

        configuration_ = new CommandConfiguration(curveName);
        configuration_.setParamValue("curvefamily", String.valueOf(leftFamily));
        configuration_.setParamValue("domainfamily", String.valueOf(rightFamily));
        configuration_.setParamValue("characteristicdomain", String.valueOf(characteristicDomain));

    }

    @Override
    public RpSolution calc() {
        RpSolution result = null;

        try {

            //int resolution[] = getParams().getResolution();
            //result = (SubInflectionExtensionCurve) nativeCalc(resolution[0],resolution[1], curveFamily_, domainFamily_, characteristicDomain_);

            result = (SubInflectionExtensionCurve) nativeCalc(xResolution_, yResolution_, curveFamily_, domainFamily_, characteristicDomain_);

            return result;

        } catch (RpException ex) {
            Logger.getLogger(SubInflectionExtensionCurveCalc.class.getName()).log(Level.SEVERE, null, ex);
        }

        return result;
    }

    private native RpSolution nativeCalc(int xResolution, int yResolution, int leftFamily, int rightFamily, int characteristicDomain) throws RpException;
}
