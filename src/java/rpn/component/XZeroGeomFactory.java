/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */

package rpn.component;

import rpn.controller.RpController;
import rpn.controller.XZeroController;
import rpnumerics.StationaryPointCalc;
import rpnumerics.StationaryPoint;
import wave.multid.CoordsArray;

public class XZeroGeomFactory extends StationaryPointGeomFactory {
    //
    // Members
    //
    //
    // Constructors/Initializers
    //
    public XZeroGeomFactory(StationaryPointCalc calc) {
        super(calc);
    }

    @Override
    protected RpController createUI() {
        return new XZeroController();
    }

    @Override
    protected RpGeometry createGeomFromSource() {
        StationaryPoint point = (StationaryPoint)geomSource();
        CoordsArray coords = new CoordsArray(point.getPoint().getCoords());
        return new XZeroGeom(coords, this);
    }
    //
    // Accessors/Mutators
    //
}
