/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.component;

import rpnumerics.CompositeCalc;
import rpnumerics.CompositeCurve;
import rpnumerics.HugoniotSegment;

public class CompositeGeomFactory extends RpCalcBasedGeomFactory {
    //
    // Constants
    //
    //
    // Members
    //
    //
    // Constructors/Initializers
    //
    public CompositeGeomFactory(CompositeCalc calc) {
        super(calc);
    }
    //
    // Accessors/Mutators
    //
    //
    // Methods
    //
    protected RpGeometry createGeomFromSource() {

          CompositeCurve curve = (CompositeCurve) geomSource();

        // assuming a container with HugoniotSegment elements
        int resultSize = curve.segments().size();

        HugoniotSegGeom[] hugoniotArray = new HugoniotSegGeom[resultSize];
        for (int i = 0; i < resultSize; i++) {
            hugoniotArray[i] = new HugoniotSegGeom((HugoniotSegment) curve.segments().get(i));


        }

        return new CompositeGeom(hugoniotArray, this);

    }

    public String toXML() {
        StringBuffer str = new StringBuffer();
//        String tdir = "pos";
//        if (((OrbitCalc) rpCalc()).tDirection() == OrbitGeom.BACKWARD_DIR) {
//            tdir = "neg";
//        }
//        str.append("<RAREFACIONORBITCALC tdirection=\"" + tdir + "\" calcready=\"" + rpn.parser.RPnDataModule.RESULTS + "\">\n");
//        if (!rpn.parser.RPnDataModule.RESULTS) {
//            str.append(((Orbit) geomSource()).getPoints()[0].toXML());
//        }
//        str.append(((Orbit) geomSource()).toXML(rpn.parser.RPnDataModule.RESULTS));
//        str.append("</RAREFACTIONORBITCALC>\n");
        return str.toString();
    }

    public String toMatlab(int curveIndex) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
