/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */

package rpn.component;

import rpnumerics.BifurcationCurve;
import rpnumerics.ContourCurveCalc;
import rpnumerics.CoincidenceCurve;
import rpnumerics.HugoniotSegment;

public class CoincidenceCurveGeomFactory extends BifurcationCurveGeomFactory{

    public CoincidenceCurveGeomFactory(ContourCurveCalc calc) {
        super(calc);
    }

  
    // Methods
    //
    public RpGeometry createGeomFromSource() {

        CoincidenceCurve curve = (CoincidenceCurve) geomSource();

        // assuming a container with HugoniotSegment elements
        int resultSize = curve.segments().size();
        RealSegGeom[] hugoniotArray = new RealSegGeom[resultSize];
        for (int i = 0; i < resultSize; i++) {
            hugoniotArray[i] = new RealSegGeom((HugoniotSegment) curve.segments().get(i));

        }
        return new CoincidenceCurveGeom(hugoniotArray, this);

    }

}
