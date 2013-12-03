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
    private    List<RealSegment> list_;
    boolean singular_;
    private List<RealVector> areaSelected_; 

    //
    // Constructors/Initializers
    //
    private  int inSideArea_;
   
    

    public ExtensionCurveCalc(ContourParams contourParams, List<RealSegment> list, List<RealVector> areaSelected,int extensionFamily, int characteristicDomain, boolean singular,int inSideArea) {
        super(contourParams);
        list_ = list;
        domainFamily_=extensionFamily;
        singular_ = singular;
        characteristicDomain_ = characteristicDomain;
        areaSelected_=areaSelected;
        inSideArea_=inSideArea;
        
    }

   

    //
    // Methods
    //
    public RpSolution recalc() throws RpException {

        return calc();

    }

    public RpSolution calc() throws RpException {

        RpSolution result = null;


        result = (BifurcationCurve) nativeCalc(list_, areaSelected_,domainFamily_, characteristicDomain_, singular_,inSideArea_);
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



    private native RpSolution nativeCalc(List<RealSegment> list, List<RealVector> areaSelected,int family, int characteristicDomain, boolean singular,int inSideArea) throws RpException;

    public RpSolution recalc(Area area) throws RpException {
        return null;
    }


}
