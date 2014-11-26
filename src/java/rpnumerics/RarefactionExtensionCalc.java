/*
 *
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpnumerics;

public class RarefactionExtensionCalc extends ContourCurveCalc {
    //
    // Constants
    //
    //
    // Members
    //

    private PhasePoint start_;
    private int increase_;
    int curveFamily_;
    int domainFamily_;
    int characteristicDomain_;

    //
    // Constructors/Initializers
    //
   
    

    public RarefactionExtensionCalc(ContourParams contourParams, PhasePoint startPoint, int increase, int leftFamily, int extensionFamily,int characteristicDomain) {
        super(contourParams);
        curveFamily_ = leftFamily;
        domainFamily_=extensionFamily;
        characteristicDomain_ = characteristicDomain;
        start_ = startPoint;
        increase_ = increase;
        
        configuration_= RPNUMERICS.getConfiguration("rarefactionextension");
        
        configuration_.setParamValue("resolution", RPNUMERICS.getParamValue("rarefactionextension", "resolution"));
        
        
    }

   

    //
    // Methods
    //
    public RpSolution recalc() throws RpException {

        return calc();

    }

    public RpSolution calc() throws RpException {

        RarefactionExtensionCurve result = null;


        result = (RarefactionExtensionCurve) nativeCalc(start_, increase_, curveFamily_,domainFamily_, characteristicDomain_);
        if (result == null) {
            throw new RpException("Error in native layer");
        }

        

        return result;
    }

    public int getCharacteristic() {
        return characteristicDomain_;
    }

    public int getCurveFamily() {
        return curveFamily_;
    }

    public int getDomainFamily() {
        return domainFamily_;
    }

    public int getIncrease() {
        return increase_;
    }

    public PhasePoint getStart() {
        return start_;
    }

    public void setStart(PhasePoint start) {
        start_ = start;
    }



    private native RpSolution nativeCalc(PhasePoint start, int increase, int leftFamily, int rightFamily, int characteristicDomain) throws RpException;

    public RpSolution recalc(Area area) throws RpException {
        return null;
    }


}
