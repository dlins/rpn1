package rpn.parser;

import java.awt.event.*;
import java.util.*;

import rpn.component.*;
import rpn.controller.ui.*;
import rpnumerics.*;
import wave.multid.*;
import wave.util.*;

public class StationaryPointParser implements ActionListener {

    protected static int integrationFlag;
    protected static double[] eigenvali,  eigenvalr;
    protected static StationaryPoint statPoint,  uMinus,  uPlus;
    protected static RealMatrix2 schurFormP,  schurVecP,  schurFormN,  schurVecN;
    protected static boolean plotStatPoint = false;

    public StationaryPointParser() {

//            plotStatPoint=true;

    }

    public static double[] parserEingenVal(String data) {

        StringTokenizer tokenizer = new StringTokenizer(data);
        double[] eigenval = new double[tokenizer.countTokens()];
        int i = 0;
        while (tokenizer.hasMoreTokens()) {
            eigenval[i++] = new Double(tokenizer.nextToken()).doubleValue();
        }


        return eigenval;
    }

    public void actionPerformed(ActionEvent e) {


        if (e.getActionCommand().equals("endStatPoint")) {
//            try {
//
//                PhasePoint initialPoint = new PhasePoint(RPnDataModule.InputHandler.tempPoint_);
//
//
//                System.out.println("Valor de temp Point " + RPnDataModule.InputHandler.tempPoint_);
//                System.out.println("Valor de temp Vector " + RPnDataModule.InputHandler.tempVector_);
//
//
//                StationaryPointParser.statPoint = new StationaryPoint(
//                        initialPoint, StationaryPointParser.eigenvalr,
//                        StationaryPointParser.eigenvali,
//                        RPnDataModule.InputHandler.vectorArray_,
//                        OrbitParser.dimP,
//                        StationaryPointParser.schurFormP,
//                        StationaryPointParser.schurVecP,
//                        OrbitParser.dimN,
//                        StationaryPointParser.schurFormN,
//                        StationaryPointParser.schurVecN,
//                        OrbitParser.integrationFlag);
//                StationaryPointCalc stationaryPointCalc = RPNUMERICS.createStationaryPointCalc(initialPoint);
//
//                StationaryPointGeomFactory factory = new StationaryPointGeomFactory(stationaryPointCalc);
//                plotStatPoint = true;
//                if (StationaryPointParser.plotStatPoint) {
//                    RPnDataModule.PHASESPACE.join(new StationaryPointGeom(new CoordsArray(
//                            StationaryPointParser.statPoint.getCoords()),
//                            factory));
//                }
////                UIController.instance().setUserDialogInput(false);
////                UIController.instance().setState(new GEOM_SELECTION());
//            } catch (Exception ex) {
//                ex.printStackTrace();
//            }
        }
    }
}
               









