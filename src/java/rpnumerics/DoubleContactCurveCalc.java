/*
 *
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpnumerics;

public class DoubleContactCurveCalc extends BifurcationCurveCalc {

    //
    // Constructors/Initializers
    //
    int curveFamily_;
    int domainFamily_;

    public DoubleContactCurveCalc(BifurcationParams params, int leftFamily, int rightFamily) {
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

        //result = (DoubleContactCurve) nativeCalc(getParams().getResolution(), curveFamily_, domainFamily_);

        //if (result == null) {
        //    throw new RpException("Error in native layer");
        //}
        
        try {

            result = (DoubleContactCurve) nativeCalc(xResolution_,yResolution_,curveFamily_,domainFamily_);
	   
	    return result;
        } catch (RpException ex) {
            Logger.getLogger(DoubleContactCurveCalc.class.getName()).log(Level.SEVERE, null, ex);
        }
        

        return result;
    }

    //private native RpSolution nativeCalc(int [] resolution, int curveFamily, int domainFamily) throws RpException;

    private native RpSolution nativeCalc(int xResolution, int yResolution, int curveFamily, int domainFamily) throws RpException;
}
