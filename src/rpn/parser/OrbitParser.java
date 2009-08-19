package rpn.parser;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import wave.util.RealVector;
import rpnumerics.OrbitPoint;
import wave.multid.CoordsArray;
import rpnumerics.Orbit;
import rpn.component.OrbitGeom;
import rpn.component.OrbitGeomFactory;
import rpn.controller.ui.UIController;
import rpn.controller.ui.GEOM_SELECTION;
import rpnumerics.RPNUMERICS;

public class OrbitParser implements ActionListener {

    protected static int flag,  dir,  integrationFlag,  dimP,  dimN,  timeDirection;
    protected static double tempPTime;
    protected static boolean plotOrbit = true;

    public void actionPerformed(ActionEvent e) {

        if (e.getActionCommand().equals("endOrbit")) {

            try {

                OrbitPoint[] orbitPoints = new OrbitPoint[RPnDataModule.InputHandler.orbitPointsList_.size()];
                RPnDataModule.InputHandler.tempCoords_ = new CoordsArray[RPnDataModule.InputHandler.orbitPointsList_.size()];
                for (int i = 0;
                        i < RPnDataModule.InputHandler.tempCoords_.length; i++) {

                    OrbitPoint point = (OrbitPoint) RPnDataModule.InputHandler.orbitPointsList_.get(i);
                    orbitPoints[i] = point;

                    RPnDataModule.InputHandler.tempCoords_[i] = new CoordsArray(point.getCoords());
                }

                RealVector coordsVector = new RealVector(RPnDataModule.InputHandler.tempCoords_[0].getCoords());

                OrbitPoint oPoint = new OrbitPoint(coordsVector);
                int direction;
                if (OrbitParser.flag == 1) {
                    direction = OrbitGeom.FORWARD_DIR;
                } else {
                    direction = OrbitGeom.BACKWARD_DIR;
                }

                OrbitGeomFactory factory = new OrbitGeomFactory(RPNUMERICS.createOrbitCalc(oPoint));
                RPnDataModule.InputHandler.tempOrbit_ = new OrbitGeom(
                        RPnDataModule.InputHandler.tempCoords_, factory);
                RPnDataModule.ORBIT = new Orbit(orbitPoints,
                        OrbitParser.flag);
                RPnDataModule.InputHandler.orbitPointsList_.clear();

                if (OrbitParser.plotOrbit) {
                    RPnDataModule.PHASESPACE.join(RPnDataModule.InputHandler.tempOrbit_);
                }
                UIController.instance().setState(new GEOM_SELECTION());

            } catch (Exception ex) {
                ex.printStackTrace();
            }

        }

    }
}

