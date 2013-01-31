/*
 *
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpnumerics;

import java.util.List;

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

        result = (DoubleContactCurve) nativeCalc(curveFamily_, domainFamily_);

        if (result == null) {
            throw new RpException("Error in native layer");
        }
 

        return result;
    }


    @Override
    public RpSolution recalc(List<Area> area) throws RpException {

        System.out.println("DoubleContactCurveCalc ::::: Entrando no recalc(List<Area> area)");
        
        return nativeCalc(area, curveFamily_, domainFamily_);
    }


    private native RpSolution nativeCalc(int curveFamily, int domainFamily) throws RpException;
    
    private native RpSolution nativeCalc(List<Area> areasList,int curveFamily, int domainFamily) throws RpException;




 
}
