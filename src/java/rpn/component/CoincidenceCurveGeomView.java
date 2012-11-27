/*
* Instituto de Matematica Pura e Aplicada - IMPA
* Departamento de Dinamica dos Fluidos
*
*/


package rpn.component;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import wave.multid.DimMismatchEx;
import wave.multid.model.AbstractGeomObj;
import wave.multid.view.GeomObjView;
import wave.multid.view.ViewingAttr;
import wave.multid.view.ViewingTransform;


class CoincidenceCurveView extends GeomObjView{
    //
  // Members
  //


     public CoincidenceCurveView(CoincidenceCurveGeom abstractGeom,
                           ViewingTransform transf,
                           ViewingAttr viewAttr) throws DimMismatchEx {
         super(abstractGeom, transf, viewAttr);

  }



  //Original update method

  public void update() {


    viewList_.clear();
    Iterator geomListIterator = ( (CoincidenceCurveGeom) getAbstractGeom()).getBifurcationSegmentsIterator();
    while (geomListIterator.hasNext()) {
      RealSegGeom geomObj = (RealSegGeom) geomListIterator.next();
      geomObj.viewingAttr().setColor(CoincidenceCurveGeom.COLOR);
      try {
        viewList_.add(geomObj.createView(getViewingTransform()));
      }
      catch (DimMismatchEx dex) {
        dex.printStackTrace();
      }
    }
  }


   
}
