/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rpn.component;


import rpnumerics.BifurcationCurve;
import rpnumerics.ContourCurveCalc;
import rpnumerics.CoincidenceCurve;
import rpnumerics.CoincidenceExtensionCurve;
import rpnumerics.HugoniotSegment;

public class CoincidenceExtensionCurveGeomFactory extends BifurcationCurveGeomFactory {

    public CoincidenceExtensionCurveGeomFactory(ContourCurveCalc calc) {
        super(calc);
    }

    // Methods
    //
    @Override
    public RpGeometry createGeomFromSource() {

        CoincidenceExtensionCurve curve = (CoincidenceExtensionCurve) geomSource();

        // assuming a container with HugoniotSegment elements
        int resultSize = curve.segments().size();
        //ViewingAttr viewingAttr = new ViewingAttr(Color.MAGENTA);
        RealSegGeom[] hugoniotArray = new RealSegGeom[resultSize];
        for (int i = 0; i < resultSize; i++) {
            hugoniotArray[i] = new RealSegGeom((HugoniotSegment) curve.segments().get(i));

        }
        return new CoincidenceExtensionCurveGeom(hugoniotArray, this);

    }


}
