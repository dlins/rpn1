/*
 *
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpnumerics;

public class DoubleContactCurveCalc extends ContourCurveCalc {

    //
    // Constructors/Initializers
    //

    int xResolution_;
    int yResolution_;
    int curveFamily_;
    int domainFamily_;

    public DoubleContactCurveCalc(ContourParams params, int leftFamily, int rightFamily) {
        super(params);
        this.curveFamily_ = leftFamily;
        this.domainFamily_ = rightFamily;
    }

    
    public int getCurveFamily() {
        return curveFamily_;
    }

    public int getDomainFamily() {
        return domainFamily_;
    }

    @Override
    public RpSolution calc() throws RpException {
        RpSolution result = null;

        System.out.println("Entrando em DoubleContactCurveCalc... " + curveFamily_ + " " + domainFamily_);

        result = (DoubleContactCurve) nativeCalc(getParams().getResolution(), curveFamily_, domainFamily_);

        if (result == null) {
            throw new RpException("Error in native layer");
        }
 

        return result;
    }

    private native RpSolution nativeCalc(int [] resolution, int curveFamily, int domainFamily) throws RpException;
 
}
