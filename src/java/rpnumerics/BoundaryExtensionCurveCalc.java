/*
 *
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpnumerics;

import java.util.List;

public class BoundaryExtensionCurveCalc extends ContourCurveCalc {

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


    public BoundaryExtensionCurveCalc(ContourParams params, int edgeResolution, int domainFamily,int edge, int characteristicWhere) {
        super(params);

        domainFamily_ = domainFamily;
        edge_ = edge;
        edgeResolution_ = edgeResolution;
        characteristicDomain_ = characteristicWhere;
        configuration_=RPNUMERICS.getConfiguration("boundaryextensioncurve");

    }


    @Override
    public RpSolution calc() throws RpException {
        RpSolution result = null;

        result = (BoundaryExtensionCurve) nativeCalc(edgeResolution_,domainFamily_, edge_, characteristicDomain_);

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

   

    private native RpSolution nativeCalc(int edgeResolution,  int rightFamily, int edge, int characteristicDomain) throws RpException;

    public RpSolution recalc(List<Area> area) throws RpException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    
}
