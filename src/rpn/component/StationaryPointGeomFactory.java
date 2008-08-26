/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.component;

import rpnumerics.StationaryPointCalc;
import wave.multid.CoordsArray;
import rpnumerics.StationaryPoint;

public class StationaryPointGeomFactory extends RpCalcBasedGeomFactory {
    //
    // Constants
    //
    public static boolean STATPOINTWRITE = true;

    public StationaryPointGeomFactory(StationaryPointCalc calc) {
        super(calc);
    }

    protected RpGeometry createGeomFromSource() {
        StationaryPoint point = (StationaryPoint) geomSource();
        CoordsArray coords = new CoordsArray(point.getPoint().getCoords());
        return new StationaryPointGeom(coords, this);
    }

    public String toXML() {

        StringBuffer str = new StringBuffer();
        str.append("<STATPOINTCALC coordinates=\"" + ((StationaryPointCalc) rpCalc()).getInitPoint() + "\"" + " calcready=\"" + rpn.parser.RPnDataModule.RESULTS + "\"" + " flowname=\""+((StationaryPointCalc) rpCalc()).getFlow().getName()+"\""+" methodname=\"" + ((StationaryPointCalc) rpCalc()).getCalcMethodName() + "\"" + ">\n");

        str.append(((StationaryPoint) geomSource()).toXML(rpn.parser.RPnDataModule.RESULTS));
        str.append("</STATPOINTCALC>\n");
        return str.toString();
    }
}
