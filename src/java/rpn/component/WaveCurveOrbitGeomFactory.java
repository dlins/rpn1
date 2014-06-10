/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.component;

import java.awt.Color;
import rpnumerics.FundamentalCurve;
import rpnumerics.Orbit;
import rpnumerics.OrbitPoint;
import rpnumerics.RPNUMERICS;
import rpnumerics.WaveCurve;
import rpnumerics.WaveCurveOrbitCalc;
import wave.multid.view.ViewingAttr;

public class WaveCurveOrbitGeomFactory extends OrbitGeomFactory {
    //
    // Constants
    //
    //
    // Members
    //
    //
    // Constructors/Initializers
    //

    public WaveCurveOrbitGeomFactory(WaveCurveOrbitCalc calc) {
        super(calc);
    }

    public WaveCurveOrbitGeomFactory(WaveCurveOrbitCalc calc, Orbit curve) {
        super(calc, curve);
    }
    //
    // Accessors/Mutators
    //
    //
    // Methods
    //

    public WaveCurveOrbitGeomFactory(WaveCurveOrbitCalc calc, WaveCurve curve) {
        super(calc, curve);
    }

    @Override
    protected ViewingAttr selectViewingAttr() {
        int family = (((FundamentalCurve) this.geomSource()).getFamilyIndex());//TODO REMOVE

        if (family == 1) {
            return new ViewingAttr(Color.red);
        }
        if (family == 0) {
            return new ViewingAttr(Color.blue);
        }

        return null;

    }

    @Override
    public String toXML() {

        StringBuilder buffer = new StringBuilder();

        FundamentalCurve geomSource = (FundamentalCurve) geomSource();
        OrbitPoint referencePoint = geomSource.getReferencePoint();

        String curve_name = '\"' + geomSource.getClass().getSimpleName() + '\"';
        String dimension = '\"' + Integer.toString(RPNUMERICS.domainDim()) + '\"';

        WaveCurveOrbitCalc orbitCalc = (WaveCurveOrbitCalc) rpCalc();

        //
        // PRINTS OUT THE CURVE ATTS
        //
        buffer.append("<").append(Orbit.XML_TAG).append(" curve_name=" + ' ').append(curve_name).append(' ' + " dimension=" + ' ').append(dimension).append(' ' + " startpoint=\"").append(referencePoint.getCoords()).append('\"'
                + " format_desc=\"1 segment per row\">" + "\n");

        //
        // PRINTS OUT THE CONFIGURATION INFORMATION
        //
        buffer.append(orbitCalc.getConfiguration().toXML());

        //
        // PRINTS OUT THE SEGMENTS COORDS
        //
        buffer.append(geomSource.toXML());

        buffer.append("</" + Orbit.XML_TAG + ">" + "\n");

        return buffer.toString();

    }

}
