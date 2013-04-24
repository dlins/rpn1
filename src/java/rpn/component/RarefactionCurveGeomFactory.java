/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.component;

import rpnumerics.Orbit;
import rpnumerics.RPNUMERICS;
import rpnumerics.RarefactionCurve;
import rpnumerics.RarefactionCurveCalc;

public class RarefactionCurveGeomFactory extends WaveCurveOrbitGeomFactory {
    //
    // Constants
    //
    //
    // Members
    //
    //
    // Constructors/Initializers
    //

    public RarefactionCurveGeomFactory(RarefactionCurveCalc calc) {
        super(calc);
    }

    public RarefactionCurveGeomFactory(RarefactionCurveCalc calc,RarefactionCurve curve) {
        super(calc,curve);
    }
    //
    // Accessors/Mutators
    //
    //
    // Methods
    //
    @Override
    public RpGeometry createGeomFromSource() {

        RarefactionCurve orbit = (RarefactionCurve) geomSource();


        return new RarefactionCurveGeom(MultidAdapter.converseOrbitPointsToCoordsArray(orbit.getPoints()), this);

    }

    public String toXML() {


        StringBuilder buffer = new StringBuilder();

        Orbit geomSource = (Orbit) geomSource();

        String curve_name = '\"' + geomSource.getClass().getSimpleName() + '\"';
        String dimension = '\"' + Integer.toString(RPNUMERICS.domainDim())  + '\"';

        //
        // PRINTS OUT THE CURVE ATTS
        //
        buffer.append("<" + Orbit.XML_TAG +
                        " curve_name=" + ' ' + curve_name + ' ' +
                        " dimension=" + ' ' +  dimension + ' ' +
                        " startpoint=\"" + geomSource.getPoints()[0].getCoords() + '\"' +
                        " format_desc=\"1 segment per row\">" + "\n");

        //
        // PRINTS OUT THE CONFIGURATION INFORMATION
        //
        buffer.append(rpCalc().getConfiguration().toXML());

        //
        // PRINTS OUT THE SEGMENTS COORDS
        //
        buffer.append(geomSource.toXML());

        buffer.append("</" + Orbit.XML_TAG + ">" + "\n");

        return buffer.toString();

    }
}
