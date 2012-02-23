/*
 *
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpnumerics;

public class HysteresisCurveCalc extends ContourCurveCalc {

    private int domainFamily_;
    private int curveFamily_;
    private int characteristic_where_;
    private int singular_;

    //
    // Constructors/Initializers
    //
    public HysteresisCurveCalc(ContourParams params, int domainFamily, int curveFamily,
            int characteristic_where, int singular) {
        super(params);
        domainFamily_ = domainFamily;
        curveFamily_ = curveFamily;
        characteristic_where_ = characteristic_where;
        singular_ = singular;
    }

    @Override
    public RpSolution calc() throws RpException {

        int resolution[] = getParams().getResolution();
        HysteresisCurve result = (HysteresisCurve) nativeCalc(domainFamily_, curveFamily_, resolution, singular_, characteristic_where_);

        if (result == null) {
            throw new RpException("Error in native layer");
        }


        return result;
    }

    public int getCharacteristicWhere() {
        return characteristic_where_;
    }

    public int getCurveFamily() {
        return curveFamily_;
    }

    public int getDomainFamily() {
        return domainFamily_;
    }

    public int getSingular() {
        return singular_;
    }

    private native RpSolution nativeCalc(int domainFamily, int curveFamily,
            int[] resolution,
            int singular, int characteristicDomain) throws RpException;
}
