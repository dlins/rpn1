/*
 *
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpnumerics;

public class BoundaryExtensionCurveCalc extends BifurcationCurveCalc {

    //
    // Constructors/Initializers
    //
    int xResolution_;
    int yResolution_;
    int curveFamily_;
    int domainFamily_;
    int characteristicDomain_;
    int edge_;
    int edgeResolution_;


    public BoundaryExtensionCurveCalc(BifurcationParams params, int edgeResolution, int curveFamily, int domainFamily, int edge, int characteristicDomain) {
        super(params);
        curveFamily_ = curveFamily;
        domainFamily_ = domainFamily;
        edge_ = edge;
        edgeResolution_ = edgeResolution;
        characteristicDomain_ = characteristicDomain;
    }


    @Override
    public RpSolution calc() throws RpException {
        RpSolution result = null;

//        int resolution[] = getParams().getResolution();
//        result = (BoundaryExtensionCurve) nativeCalc(resolution, edgeResolution_, curveFamily_, domainFamily_, edge_, characteristicDomain_);

        result = (BoundaryExtensionCurve) nativeCalc(xResolution_, yResolution_, edgeResolution_, curveFamily_, domainFamily_, edge_, characteristicDomain_);

        if (result == null) {
            throw new RpException("Error in native layer");

        }


        return result;
    }

    public int getCharacteristicWhere() {
        return characteristicDomain_;
    }

    public int getCurveFamily() {
        return curveFamily_;
    }

    public int getDomainFamily() {
        return domainFamily_;
    }

    public int getEdgeResolution() {
        return edgeResolution_;
    }

    public int getEdge() {
        return edge_;
    }

    public int getxResolution() {
        return xResolution_;
    }

    public int getyResolution() {
        return yResolution_;
    }

    //private native RpSolution nativeCalc(int [] resolution, int edgeResolution, int leftFamily, int rightFamily, int edge, int characteristicDomain) throws RpException;

    private native RpSolution nativeCalc(int xResolution, int yResolution, int edgeResolution, int leftFamily, int rightFamily, int edge, int characteristicDomain) throws RpException;
}
