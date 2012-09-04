/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.usecase;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.swing.JButton;
import rpn.RPnPhaseSpaceFrame;
import rpn.RPnUIFrame;
import rpn.component.*;
import rpn.controller.ui.UIController;
import rpn.controller.ui.UI_ACTION_SELECTED;
import rpn.parser.RPnDataModule;
import rpnumerics.*;
import wave.util.RealVector;

public class RiemannProfileAgent extends RpModelPlotAgent {
    //
    // Constants
    //

    static public final String DESC_TEXT = "Riemann Profile";
    //
    // Members
    //
    static private RiemannProfileAgent instance_ = null;

    //
    // Constructors/Initializers
    //
    protected RiemannProfileAgent() {
        super(DESC_TEXT, rpn.RPnConfig.HUGONIOT, new JButton());
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        System.out.println("Em action performed");
        UI_ACTION_SELECTED action = new UI_ACTION_SELECTED(this);
        action.userInputComplete(UIController.instance());// No input needed

    }

    public RpGeometry createRpGeometry(RealVector[] input) {
        
        return null;


    }

    @Override
    public void execute() {

        Iterator<RpGeometry> it = RPnDataModule.PHASESPACE.getGeomObjIterator();
        ArrayList<WaveCurve> waveCurveList = new ArrayList<WaveCurve>();

        while (it.hasNext()) {
            RpGeometry rpGeometry = it.next();

            if (rpGeometry instanceof WaveCurveGeom) {

                WaveCurve waveCurve = (WaveCurve) rpGeometry.geomFactory().geomSource();
                waveCurveList.add(waveCurve);

            }
        }





        WaveCurve waveCurveForward0;
        WaveCurve waveCurveBackward1;


        if (waveCurveList.get(0).getFamily() == 0 && waveCurveList.get(0).getDirection() == 10) {

            waveCurveForward0 = waveCurveList.get(0);
            waveCurveBackward1 = waveCurveList.get(1);

        } else {
            waveCurveForward0 = waveCurveList.get(1);
            waveCurveBackward1 = waveCurveList.get(0);

        }



        System.out.println("waveCurve forward direcao: "+waveCurveForward0.getDirection()+" "+" familia "+ waveCurveForward0.getFamily());

        System.out.println("waveCurve backward direcao: " + waveCurveBackward1.getDirection() + " " + " familia " + waveCurveBackward1.getFamily());



        List<Area> areaList = AreaSelectionAgent.instance().getListArea();


        Area firstArea = areaList.get(0);
        
        
//        
//        RealVector p1 = new RealVector("0.0075 0.1221");
//        RealVector p2 = new RealVector("0.2385 0.0231");
//        RealVector pres = new RealVector("10 10");
//        
//        Area firstArea = new Area(pres,p1,p2);
//        
        

        RiemannProfileCalc rc = new RiemannProfileCalc(firstArea, waveCurveForward0, waveCurveBackward1);






        try {
            RiemannProfile profile = (RiemannProfile)rc.calc();

            System.out.println(profile);

            RiemannProfileGeomFactory riemannProfileGeomFactory = new RiemannProfileGeomFactory(rc);


            RPnDataModule.RIEMANNPHASESPACE.join(riemannProfileGeomFactory.geom());



        } catch (RpException ex) {

        }
        
        
        
        for (RPnPhaseSpaceFrame frame : RPnUIFrame.getRiemannFrames()) {
            
            frame.setVisible(true);
            
        }
        
        
        
        
        

    }

    static public RiemannProfileAgent instance() {
        if (instance_ == null) {
            instance_ = new RiemannProfileAgent();
        }
        return instance_;
    }
}
