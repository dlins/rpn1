/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.component;

import java.awt.Color;
import wave.multid.view.ViewingAttr;

class InflectionCurveGeom extends BifurcationCurveGeom{

    public InflectionCurveGeom(BifurcationSegGeom[] hugoniotArray, InflectionCurveGeomFactory aThis) {
        super(hugoniotArray,aThis);

    }

      public ViewingAttr viewingAttr() {
        return new ViewingAttr(Color.yellow);
    }




}
