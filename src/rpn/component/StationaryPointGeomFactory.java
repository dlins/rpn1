/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */

package rpn.component;

import rpnumerics.StationaryPointCalc;
import wave.multid.model.MultiGeometryImpl;
import wave.multid.model.MultiPoint;
import wave.multid.view.ViewingTransform;
import wave.multid.CoordsArray;
import wave.multid.view.ViewingAttr;
import wave.multid.view.GeomObjView;
import wave.multid.DimMismatchEx;
import rpnumerics.StationaryPoint;
import java.awt.Color;

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
          str.append("<STATPOINTCALC calcready=\"" + rpn.parser.RPnDataModule.RESULTS +
                 "\">\n");
      str.append( ( (StationaryPointCalc) rpCalc()).getInitPoint().toXML());
      str.append( ( (StationaryPoint) geomSource()).toXML(rpn.parser.RPnDataModule.
          RESULTS));
      str.append("</STATPOINTCALC>\n");
    return str.toString();
  }


}
