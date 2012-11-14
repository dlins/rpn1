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

    public RpGeometry createGeomFromSource() {
        StationaryPoint point = (StationaryPoint) geomSource();
        CoordsArray coords = new CoordsArray(point.getPoint().getCoords());
        return new StationaryPointGeom(coords, this);
    }
}
