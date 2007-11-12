package rpn.parser;

import java.awt.event.ActionEvent;
import rpnumerics.RpNumerics;
import rpnumerics.HugoniotSegment;
import rpn.component.HugoniotCurveGeomFactory;
import rpn.component.XZeroGeomFactory;
import java.awt.event.ActionListener;
import rpn.component.XZeroGeom;
import rpn.component.HugoniotSegGeom;
import rpn.controller.phasespace.NumConfigReadyImpl;
import rpn.controller.ui.UIController;
import rpnumerics.HugoniotCurve;
import java.util.List;
import wave.multid.CoordsArray;
import rpn.component.HugoniotCurveGeom;
import rpn.controller.ui.GEOM_SELECTION;
import rpnumerics.StationaryPointCalc;


    public class HugoniotParser implements ActionListener{

        protected static HugoniotCurveGeom tempHugoniot;


        public void actionPerformed(ActionEvent e) {
            if (e.getActionCommand().equals("endHugoniot")) {
                XZeroGeomFactory xZeroFactory = new XZeroGeomFactory(new
                        StationaryPointCalc(RPnDataModule.InputHandler.
                                            tempPhasePoint_));
                RpNumerics.hugoniotCurveCalc().uMinusChangeNotify(RPnDataModule.
                        InputHandler.tempPhasePoint_);
                RPnDataModule.InputHandler.xZeroGeom_ = new XZeroGeom(new
                        CoordsArray(RPnDataModule.InputHandler.tempPhasePoint_.
                                    getCoords()), xZeroFactory);

//                HugoniotCurve hCurve = new HugoniotCurve(RPnDataModule.
//                        InputHandler.tempPhasePoint_,
//                        RPnDataModule.InputHandler.segmentList_);
//                List hugoniotSegs = hCurve.segments();
//                HugoniotSegGeom[] segmentsArray = new HugoniotSegGeom[
//                                                  hugoniotSegs.
//                                                  size()];
//                for (int i = 0; i < segmentsArray.length; i++) {
//                    segmentsArray[i] = new HugoniotSegGeom((HugoniotSegment)
//                            hugoniotSegs.get(i));
                }
                HugoniotCurveGeomFactory factory = new HugoniotCurveGeomFactory(
                        rpnumerics.RpNumerics.hugoniotCurveCalc());
//                HugoniotParser.tempHugoniot = new
//                        HugoniotCurveGeom(segmentsArray,
//                                          factory);
//                RPnDataModule.PHASESPACE.join(HugoniotParser.tempHugoniot);
//                RPnDataModule.PHASESPACE.join(RPnDataModule.InputHandler.
//                                              xZeroGeom_);
//                RPnDataModule.PHASESPACE.changeState(new NumConfigReadyImpl(
//                        HugoniotParser.tempHugoniot,
//                        RPnDataModule.InputHandler.xZeroGeom_));
//                UIController.instance().setState(new GEOM_SELECTION());


            }
        }


