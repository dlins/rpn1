/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpnumerics;

public class LevelCurveCalc extends ContourCurveCalc {

    private int family_;
    private double level_;
 


    public LevelCurveCalc(int family, double level, ContourParams params) {
        super(params);
        family_ = family;
        level_ = level;

        configuration_=RPNUMERICS.getConfiguration("levelcurve");

    }


    protected LevelCurveCalc(int family, ContourParams params) {
        super(params);
        family_ = family;
    }


    public RpSolution calc() throws RpException {

        LevelCurve result = (LevelCurve) calcNative(family_, level_,getParams().getResolution());

        if (result == null) {
            throw new RpException("Error in native layer");
        }

        return result;

    }

    public int getFamily() {
        return family_;
    }

   

    private native RpSolution calcNative(int family, double level, int[] resolution);

   
}
