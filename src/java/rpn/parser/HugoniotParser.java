package rpn.parser;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import rpn.component.HugoniotCurveGeom;
import rpn.component.HugoniotCurveGeomFactory;
import rpn.component.HugoniotSegGeom;
import rpn.component.XZeroGeom;
import rpn.component.XZeroGeomFactory;
import rpn.controller.phasespace.NumConfigReadyImpl;
import rpnumerics.HugoniotCurve;
import rpnumerics.HugoniotCurveCalc;
import rpnumerics.HugoniotSegment;
import rpnumerics.RPNUMERICS;
import rpnumerics.StationaryPointCalc;
import wave.multid.CoordsArray;

public class HugoniotParser implements ActionListener {

    protected static HugoniotCurveGeom tempHugoniot;
    private HugoniotCurve hugoniotCurve_;
    private RPnDataModule dataModule_;

    public HugoniotParser(RPnDataModule dataModule) {
        dataModule_ = dataModule;
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("endHugoniotCurve")) {


            HugoniotCurveCalc curveCalc = RPNUMERICS.createHugoniotCalc();

            StationaryPointCalc stationaryPointCalc = RPNUMERICS.createStationaryPointCalc(RPnDataModule.XZERO);

            XZeroGeomFactory xZeroFactory = new XZeroGeomFactory(stationaryPointCalc);
            curveCalc.uMinusChangeNotify(RPnDataModule.XZERO);

            RPnDataModule.InputHandler.xZeroGeom_ = new XZeroGeom(new CoordsArray(RPnDataModule.XZERO.getCoords()), xZeroFactory);

            HugoniotCurve hCurve = new HugoniotCurve(RPnDataModule.XZERO, RPnDataModule.InputHandler.segmentList_);
            List hugoniotSegs = hCurve.segments();
            HugoniotSegGeom[] segmentsArray = new HugoniotSegGeom[hugoniotSegs.size()];
            for (int i = 0; i < segmentsArray.length; i++) {
                segmentsArray[i] = new HugoniotSegGeom((HugoniotSegment) hugoniotSegs.get(i));
            }
            HugoniotCurveGeomFactory factory = new HugoniotCurveGeomFactory(curveCalc);
            HugoniotParser.tempHugoniot = new HugoniotCurveGeom(segmentsArray,
                    factory);
            RPnDataModule.PHASESPACE.join(HugoniotParser.tempHugoniot);
            RPnDataModule.PHASESPACE.join(RPnDataModule.InputHandler.xZeroGeom_);
            RPnDataModule.PHASESPACE.changeState(new NumConfigReadyImpl(
                    HugoniotParser.tempHugoniot,
                    RPnDataModule.InputHandler.xZeroGeom_));


//            UIController.instance().setUserDialogInput(false);
//            StateInputController.SHOWDIALOG=true;
//            StateInputController controller = new StateInputController();
//            UIController.instance().setState(new SIGMA_CONFIG());
//            controller.propertyChange(new PropertyChangeEvent(this, "aplication state", UIController.instance().getState(),new SIGMA_CONFIG() ));



        //////////////////





//            XZeroGeomFactory xZeroFactory = new XZeroGeomFactory(RPNUMERICS.createStationaryPointCalc(RPnDataModule.XZERO));

//            HugoniotCurve hugoniotCurve = RPnDataModule.getHugoniotCurve();
//
//            HugoniotCurveGeomFactory factory = new HugoniotCurveGeomFactory(RPNUMERICS.createHugoniotCalc());
//
//            HugoniotCurveGeom hugoniotCurveGeom = new HugoniotCurveGeom(hugoniotCurve, factory);
//
//            RPnDataModule.PHASESPACE.join(hugoniotCurveGeom);

//            RPnDataModule.PHASESPACE.join(HugoniotParser.tempHugoniot);

//            RPnDataModule.InputHandler.xZeroGeom_ = new XZeroGeom(new CoordsArray(RPnDataModule.InputHandler.tempPhasePoint_.getCoords()), xZeroFactory);
//
//            HugoniotCurve hCurve = new HugoniotCurve(RPnDataModule.InputHandler.tempPhasePoint_,
//                    RPnDataModule.InputHandler.segmentList_);
//            List hugoniotSegs = hCurve.segments();
//            HugoniotSegGeom[] segmentsArray = new HugoniotSegGeom[hugoniotSegs.size()];
//            for (int i = 0; i < segmentsArray.length; i++) {
//                segmentsArray[i] = new HugoniotSegGeom((HugoniotSegment) hugoniotSegs.get(i));
//            }
//
//            HugoniotCurveGeomFactory factory = new HugoniotCurveGeomFactory(RPNUMERICS.createHugoniotCalc());
//            HugoniotParser.tempHugoniot = new HugoniotCurveGeom(segmentsArray,
//                    factory);
//            RPnDataModule.PHASESPACE.join(HugoniotParser.tempHugoniot);
//            RPnDataModule.PHASESPACE.join(RPnDataModule.InputHandler.xZeroGeom_);
//            RPnDataModule.PHASESPACE.changeState(new NumConfigReadyImpl(
//                    HugoniotParser.tempHugoniot,
//                    RPnDataModule.InputHandler.xZeroGeom_));
//            UIController.instance().setState(new GEOM_SELECTION());

//            System.out.println("Fechando Hugoniot");
        }

        if (e.getActionCommand().equals("endHugoniotCalc")) {

        }
    }
}
