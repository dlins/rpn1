/*
 *
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpnumerics;

import java.util.List;
import wave.util.RealVector;

public class HysteresisCurveCalc extends ContourCurveCalc {

    private int family_;

    //
    // Constructors/Initializers
    //
    public HysteresisCurveCalc(ContourParams params, int family) {
        super(params);
        family_ = family;
        configuration_ = RPNUMERICS.getConfiguration("hysteresiscurve");
    }

    @Override
    public RpSolution calc() throws RpException {

        HysteresisCurve result = (HysteresisCurve) nativeCalc(family_);

        if (result == null) {
            throw new RpException("Error in native layer");
        }


        return result;
    }

    @Override
    public RpSolution recalc(List<Area> areaListToRefine) throws RpException {
        Area area = areaListToRefine.get(0);
        return nativeCalc(family_, (int) area.getResolution().getElement(0), (int) area.getResolution().getElement(1), area.getTopRight(), area.getDownLeft());

    }

    public int getFamily() {
        return family_;
    }

    private native RpSolution nativeCalc(int family) throws RpException;

    private native RpSolution nativeCalc(int family, int xRes_, int yRes_, RealVector topR, RealVector dwnL) throws RpException;
}
