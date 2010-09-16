package rpn.parser;

import rpnumerics.ManifoldOrbit;
import rpn.component.ManifoldGeom;
import rpn.component.ManifoldGeomFactory;
import rpnumerics.PhasePoint;
import java.awt.event.ActionEvent;
import rpn.controller.ui.UIController;
import rpn.controller.phasespace.fwdProfileReadyImpl;
import wave.multid.CoordsArray;
import java.awt.event.ActionListener;
import rpnumerics.OrbitPoint;
import rpn.controller.phasespace.bwdProfileReadyImpl;
import rpn.controller.ui.GEOM_SELECTION;
import rpnumerics.ManifoldOrbitCalc;
import rpnumerics.RPNUMERICS;

public class ManifoldParser implements ActionListener {


    protected static ManifoldGeom manifoldGeomA, manifoldGeomB;

    protected static ManifoldOrbit manifoldOrbitA, manifoldOrbitB;

    protected static int dir, manifoldNumber = 0;


    public void actionPerformed(ActionEvent e) {

        if (e.getActionCommand().equals("endManifold")) {

            try {

                OrbitPoint[] points;
                CoordsArray[] coords;

                ManifoldOrbitCalc manifoldCalc = RPNUMERICS.createManifoldCalc(StationaryPointParser.statPoint,  new PhasePoint(RPnDataModule.InputHandler.tempVector_), dir);
                        
//                        new ManifoldOrbitCalc(
//                        StationaryPointParser.statPoint,
//                        new PhasePoint(RPnDataModule.InputHandler.tempVector_),
//                        OrbitParser.timeDirection);
                ManifoldGeomFactory factory = new ManifoldGeomFactory(
                        manifoldCalc);

                switch (OrbitParser.timeDirection) {

                case 1:

                    ManifoldParser.manifoldOrbitA = new
                            ManifoldOrbit(StationaryPointParser.statPoint,
                                          new
                                          PhasePoint(RPnDataModule.InputHandler.
                            tempVector_), RPnDataModule.ORBIT,
                                          OrbitParser.timeDirection);
                    points = (ManifoldParser.manifoldOrbitA.getOrbit()).
                             getPoints();
                    coords = new CoordsArray[points.length];

                    for (int i = 0; i < points.length; i++) {

                        coords[i] = new CoordsArray((points[i]).getCoords());

                    }
                    ManifoldParser.manifoldGeomA = new
                            ManifoldGeom(coords, factory);
                    RPnDataModule.PHASESPACE.join(ManifoldParser.manifoldGeomA);
                    RPnDataModule.PHASESPACE.changeState(new
                            fwdProfileReadyImpl(
                                    HugoniotParser.tempHugoniot,
                                    RPnDataModule.InputHandler.xZeroGeom_,
                                   PoincareParser.tempPoincareSection,
                                    ManifoldParser.manifoldGeomA));
                    break;

                case -1:

                    ManifoldParser.manifoldOrbitB = new
                            ManifoldOrbit(StationaryPointParser.statPoint,
                                          new
                                          PhasePoint(RPnDataModule.InputHandler.
                            tempVector_), RPnDataModule.ORBIT,
                                          OrbitParser.timeDirection);
                    points = (ManifoldParser.manifoldOrbitB.getOrbit()).
                             getPoints();
                    coords = new CoordsArray[points.length];

                    for (int i = 0; i < points.length; i++) {

                        coords[i] = new CoordsArray((points[i]).getCoords());

                    }
                    ManifoldParser.manifoldGeomB = new
                            ManifoldGeom(coords, factory);
                    RPnDataModule.PHASESPACE.plot(ManifoldParser.manifoldGeomB);
                    RPnDataModule.PHASESPACE.changeState(new
                                                         bwdProfileReadyImpl(HugoniotParser.tempHugoniot,
                                                RPnDataModule.InputHandler.
                                                xZeroGeom_,
                                               PoincareParser.tempPoincareSection,
                                                ManifoldParser.manifoldGeomB));
                    break;
                }

                StationaryPointParser.plotStatPoint = true;
                OrbitParser.plotOrbit = true;
                UIController.instance().setState(new GEOM_SELECTION());
            } catch (Exception ex) {
                ex.printStackTrace();
            }

        }
    }

}
