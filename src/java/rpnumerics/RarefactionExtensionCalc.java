/*
 *
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpnumerics;

import wave.ode.ODESolver;

public class RarefactionExtensionCalc extends BifurcationCurveCalc {
    //
    // Constants
    //
    //
    // Members
    //

    private PhasePoint start_;
    private int timeDirection_;
    private int increase_;
    private String methodName_;
    private int familyIndex_;
    private String flowName_;
    int [] resolution_;
    int yResolution_;
    int curveFamily_;
    int domainFamily_;
    int characteristicDomain_;

    //
    // Constructors/Initializers
    //
   
    

    public RarefactionExtensionCalc(int [] resolution, PhasePoint startPoint, int increase, int leftFamily, int rightFamily, int characteristicDomain) {
        super(new BifurcationParams(resolution));
        resolution_ = resolution;
        this.curveFamily_ = leftFamily;
        this.domainFamily_ = rightFamily;

        System.out.println("Curve Family:"+curveFamily_);
        System.out.println("Domain Family:" + domainFamily_);


        characteristicDomain_ = characteristicDomain;
        start_ = startPoint;
        increase_ = increase;
    }

   

    //
    // Methods
    //
    public RpSolution recalc() throws RpException {

        return calc();

    }

    public RpSolution calc() throws RpException {

        RpSolution result = null;


        result = (RarefactionExtensionCurve) nativeCalc(resolution_, start_, increase_, curveFamily_, domainFamily_, characteristicDomain_);
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

    public int[] getResolution() {
        return resolution_;
    }

    public PhasePoint getStart() {
        return start_;
    }



    //private native RpSolution nativeCalc(int [] resolution, PhasePoint start, int increase, int leftFamily, int rightFamily, int characteristicDomain) throws RpException;

    public RpSolution recalc(Area area) throws RpException {
        return null;
    }

    private native RpSolution nativeCalc(int xResolution, int yResolution, PhasePoint start, int increase, int leftFamily, int rightFamily, int characteristicDomain) throws RpException;

}
