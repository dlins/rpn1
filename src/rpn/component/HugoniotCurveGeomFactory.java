/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */

package rpn.component;

import rpnumerics.*;
import rpn.controller.HugoniotController;
import rpn.controller.RpController;


public class HugoniotCurveGeomFactory extends RpCalcBasedGeomFactory {
    public HugoniotCurveGeomFactory(HugoniotCurveCalc calc) {
        super(calc);
    }

    protected RpController createUI() {
        return new HugoniotController();
    }

    //
    // Methods
    //

    protected RpGeometry createGeomFromSource() {

      HugoniotCurve curve = (HugoniotCurve)geomSource();

        // assuming a container with HugoniotSegment elements
//        int resultSize = curve.segments().size();

//        HugoniotSegGeom[] hugoniotArray = new HugoniotSegGeom[resultSize];
//        for (int i = 0; i < resultSize; i++)
//          hugoniotArray[i] = new HugoniotSegGeom( (HugoniotSegment) curve.segments().get(i));

      
      return null;
//      return new HugoniotCurveGeom(hugoniotArray, this); 

    }

    public String toXML() {

      StringBuffer buffer = new StringBuffer();

//      buffer.append(((HugoniotCurve)geomSource()).toXML(rpn.parser.RPnDataModule.RESULTS));

      return  buffer.toString();

    }
}
