/*
 *
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpnumerics;

import java.util.List;
import wave.util.RealSegment;
import wave.util.RealVector;

public class ExtensionCurveCalc extends ContourCurveCalc {
    //
    // Constants
    //
    //
    // Members
    //

    private int increase_;
    int domainFamily_;
    int characteristicDomain_;
    List<RealSegment> list_;
    boolean singular_;

    //
    // Constructors/Initializers
    //
   
    

    public ExtensionCurveCalc(ContourParams contourParams, List<RealSegment> list, int extensionFamily, int characteristicDomain, boolean singular) {
        super(contourParams);
        list_ = list;
        domainFamily_=extensionFamily;
        singular_ = singular;
        characteristicDomain_ = characteristicDomain;
        
    }

   

    //
    // Methods
    //
    public RpSolution recalc() throws RpException {

        return calc();

    }

    public RpSolution calc() throws RpException {

        RpSolution result = null;


        result = (BifurcationCurve) nativeCalc(list_, domainFamily_, characteristicDomain_, singular_);
        if (result == null) {
            throw new RpException("Error in native layer");
        }



        return result;
    }

    public int getCharacteristic() {
        return characteristicDomain_;
    }


    public int getDomainFamily() {
        return domainFamily_;
    }

    public int getIncrease() {
        return increase_;
    }



    private native RpSolution nativeCalc(List<RealSegment> list, int family, int characteristicDomain, boolean singular) throws RpException;

    public RpSolution recalc(Area area) throws RpException {
        return null;
    }


}
