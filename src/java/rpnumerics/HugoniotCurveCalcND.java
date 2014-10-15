/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpnumerics;

import java.util.List;
import rpn.configuration.Configuration;

import rpn.configuration.CurveConfiguration;
import rpn.parser.RPnDataModule;
import static rpnumerics.RPNUMERICS.getParamValue;

import wave.util.RealVector;
import wave.util.RealMatrix2;

public class HugoniotCurveCalcND extends ContourCurveCalc implements HugoniotCurveCalc {
    //
    // Constants
    //

    static public final double UMINUS_SHIFT = .01;

  
    
    //
    // Constructors
    //
//    public HugoniotCurveCalcND(HugoniotParams params) {
//        super(params);
//
//        CurveConfiguration config = (CurveConfiguration) RPNUMERICS.getConfiguration("fundamentalcurve");
//
//        configuration_ = config.clone();
//
//        String[] parameterToKeep = {"direction"};
//
//        configuration_.keepParameters(parameterToKeep);
//        configuration_.setParamValue("resolution", RPNUMERICS.getParamValue("hugoniotcurve", "resolution"));
//
//    }

    public HugoniotCurveCalcND(PhasePoint input, CurveConfiguration hugoniotConfiguration) {

        super(createHugoniotParams(input, hugoniotConfiguration));

        configuration_ = hugoniotConfiguration.clone();

    }

    private static HugoniotParams createHugoniotParams(PhasePoint input, Configuration config) {

        int[] resolution = RPnDataModule.processResolution(getParamValue("hugoniotcurve", "resolution"));

        Integer direction = new Integer(getParamValue("fundamentalcurve", "direction"));

        String methodName =RPNUMERICS.getParamValue("hugoniotcurve", "method");

        HugoniotParams params = new HugoniotParams(new PhasePoint(input), direction, resolution, methodName);

        return params;

    }

    //
    // Accessors/Mutators
    //
    public void uMinusChangeNotify(PhasePoint uMinus) {

        setUMinus(uMinus);

    }

    public void setUMinus(PhasePoint pPoint) {
//        Uminus_ = pPoint.getCoords();
//        Fminus_ = rpnumerics.RPNUMERICS.fluxFunction().F(Uminus_);
//        hugoniotParams_.uMinusChangeNotify(pPoint);
//        hugoniotParams_.setFMinus(rpnumerics.RPNUMERICS.fluxFunction().F(Uminus_));
//        DFminus_ = rpnumerics.RPNUMERICS.fluxFunction().DF(Uminus_);
//        hugoniotParams_.setDFMinus(rpnumerics.RPNUMERICS.fluxFunction().DF(Uminus_));
//        hugoniotParams_.setUMinus(Uminus_);
//         DFminus_ = rpnumerics.RPNUMERICS.fluxFunction().DF(Uminus_);
//        f_ = new GenericHugoniotFunction(hugoniotParams_);
    }

    /**
     *
     * @deprecated
     */
    public RealVector getFMinus() {
//        return new PhasePoint(hugoniotParams_.getFMinus());
        return null;

    }

    /**
     *
     * @deprecated
     */
    public RealMatrix2 getDFMinus() {
//        return hugoniotParams_.getDFMinus();

        return null;

    }

    public RpSolution calc() throws RpException {
       
        HugoniotCurve result;

        result = (HugoniotCurve) calc(((HugoniotParams) getParams()).getXZero(), RPNUMERICS.getConfiguration("hugoniotcurve"));

        result.setDirection(((HugoniotParams) getParams()).getDirection());

       
        
        return result;

    }

    @Override
    public RpSolution recalc() throws RpException {

        return calc();
    }

    @Override
    public RpSolution recalc(List<Area> areaList) throws RpException {

        System.out.println("Entrou neste recalc(area)");
        Area area = areaList.get(0);

        HugoniotCurve result;
        result = (HugoniotCurve) calc(((HugoniotParams) getParams()).getXZero(), (int) area.getResolution().getElement(0), (int) area.getResolution().getElement(1), area.getTopRight(), area.getDownLeft());

        return result;
    }

    public PhasePoint getUMinus() {
        return ((HugoniotParams) getParams()).getXZero();

    }

    public double[] getPrimitiveUMinus() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    
    @Override
    public void setReferencePoint(OrbitPoint referencePoint){
        HugoniotParams params  = (HugoniotParams)   getParams();
        
        params.setXZero(referencePoint);
    }
    

    private native RpSolution calc(PhasePoint initialpoint, Configuration configuration) throws RpException;

    //TODO : How to find the correct number of transition points after curve refinement??
    private native RpSolution calc(PhasePoint initialpoint, int xRes_, int yRes_, RealVector topR, RealVector dwnL) throws RpException;

    public enum HugoniotMethods {
        COREY,
        IMPLICIT,
        QUAD2,
        STONE,
        TESTE;

    }

}
