package rpn.parser;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import rpn.controller.ui.UIController;
import rpn.controller.ui.GEOM_SELECTION;
import rpnumerics.RpNumerics;
import rpnumerics.PhasePoint;
import rpn.controller.ui.UI_ACTION_SELECTED;
import rpn.usecase.PoincareSectionPlotAgent;
import wave.util.RealVector;
import wave.multid.CoordsArray;
import wave.util.SimplexPoincareSection;
import rpn.component.PoincareSectionGeomFactory;
import rpn.component.PoincareSectionGeom;
import java.util.ArrayList;

public class PoincareParser implements ActionListener{

    protected static  PoincareSectionGeom tempPoincareSection;

              public void actionPerformed(ActionEvent e){

                  if (e.getActionCommand().equals("endPoincare")) {


                      try {
                          UIController.instance().setState(new UI_ACTION_SELECTED(
               PoincareSectionPlotAgent.instance()));
               ArrayList poincareList = new ArrayList();
               // number of input points must match phys dim

               for (int i = 1; i <= RpNumerics.domainDim(); i++) {

                   PhasePoint last = (PhasePoint) RPnDataModule.InputHandler.pPointList_.get(RPnDataModule.InputHandler.pPointList_.size() -
                           i);

                   poincareList.add(last);
                   UIController.instance().userInputComplete(last.getCoords());
               }

               RealVector [] pPoints = new RealVector[poincareList.size()];
               CoordsArray [] poligonVertices = new CoordsArray [pPoints.length];
               for (int i =0 ;i < poincareList.size();i++){
                   pPoints[i]=((PhasePoint)poincareList.get(i)).getCoords();
                   poligonVertices [i]=new CoordsArray(pPoints[i]);


               }
               SimplexPoincareSection pSection = new SimplexPoincareSection(pPoints);
               PoincareSectionGeomFactory factory  = new PoincareSectionGeomFactory (pSection);
               PoincareParser.tempPoincareSection= new PoincareSectionGeom(poligonVertices,factory);
               UIController.instance().setState(new GEOM_SELECTION());

           }


           catch (ArrayIndexOutOfBoundsException ex) {
               ex.printStackTrace();
               //            throw new SAXException("POINCAREDATA does not match PHYS dim...");

           }




       }
   }
}












