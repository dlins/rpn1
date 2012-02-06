/*
 *
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpnumerics;

public class HysteresisCurveCalc extends BifurcationCurveCalc {

    static private int contCC = 0;      //** declarei isso (Leandro)
    private int domainFamily_;
    private int curveFamily_;
    private int xResolution_;
    private int yResolution_;
    private int characteristic_where_;
    private int singular_;

    //
    // Constructors/Initializers
    //
    public HysteresisCurveCalc(BifurcationParams params, int domainFamily, int curveFamily,
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


        //** acrescentei isso (Leandro)

        if (contCC == 0) {
            System.out.println("Entrando em InflectionCalc...");

            RPnCurve.lista.add(result);
            System.out.println("Tamanho da lista: " + RPnCurve.lista.size());

            contCC += 1;
        }

        //*********************************************

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

    public int getxResolution() {
        return xResolution_;
    }

    public int getyResolution() {
        return yResolution_;
    }

    private native RpSolution nativeCalc(int domainFamily, int curveFamily,
            int [] resolution,
            int singular, int characteristicDomain) throws RpException;
}
