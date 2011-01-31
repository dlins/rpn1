/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rpn.component;

import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import wave.multid.DimMismatchEx;
import wave.multid.model.AbstractGeomObj;
import wave.multid.view.GeomObjView;
import wave.multid.view.ViewingAttr;
import wave.multid.view.ViewingTransform;

/**
 *
 * @author edsonlan
 */
class CoincidenceCurveView implements GeomObjView{
    //
  // Members
  //
  private List viewList_;
  private ViewingTransform viewingTransf_;
  private AbstractGeomObj abstractGeom_;
  private ViewingAttr viewAttr_;

     public CoincidenceCurveView(CoincidenceCurveGeom abstractGeom,
                           ViewingTransform transf,
                           ViewingAttr viewAttr) throws DimMismatchEx {
    setAbstractGeom(abstractGeom);
    setViewingTransform(transf);
    setViewingAttr(viewAttr);
    viewList_ = new ArrayList();
    update();
  }

  //
  // Accessors/Mutators
  //
  public AbstractGeomObj getAbstractGeom() {
    return abstractGeom_;
  }

  public void setAbstractGeom(AbstractGeomObj abstractGeom) {
    abstractGeom_ = abstractGeom;
  }

  public ViewingTransform getViewingTransform() {
    return viewingTransf_;
  }

  public void setViewingTransform(ViewingTransform transf) {
    viewingTransf_ = transf;
  }

  public ViewingAttr getViewingAttr() {
    return viewAttr_;
  }

  public void setViewingAttr(ViewingAttr viewAttr) {
    viewAttr_ = viewAttr;
  }

  //
  // Methods
  //
  public void draw(Graphics2D g) {

    for (int i = 0; i < viewList_.size(); i++) {
      ( (GeomObjView) viewList_.get(i)).draw(g);
    }
  }

  //Original update method

  public void update() {


    viewList_.clear();
    Iterator geomListIterator = ( (CoincidenceCurveGeom) abstractGeom_).getHugoniotSegIterator();
    while (geomListIterator.hasNext()) {
      HugoniotSegGeom geomObj = (HugoniotSegGeom) geomListIterator.next();
      try {
        viewList_.add(geomObj.createView(getViewingTransform()));
      }
      catch (DimMismatchEx dex) {
        dex.printStackTrace();
      }
    }
  }


   
}
