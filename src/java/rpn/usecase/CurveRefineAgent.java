/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.usecase;

import java.util.List;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.JToggleButton;
import rpn.RPnLeftPhaseSpaceAbstraction;
import rpn.RPnPhaseSpaceAbstraction;
import rpn.RPnPhaseSpacePanel;
import rpn.component.RpCalcBasedGeomFactory;
import rpn.component.RpGeometry;
import rpn.component.SegmentedCurveGeom;
import rpn.controller.RPnAdjustedSelectionPlotter;
import rpn.controller.ui.AREASELECTION_CONFIG;
import rpn.controller.ui.UIController;
import rpnumerics.Area;
import rpnumerics.ContourCurveCalc;
import wave.multid.view.ViewingAttr;
import wave.util.RealVector;

public class CurveRefineAgent extends RpModelPlotAgent {

    public int ind = 0;
    static public final String DESC_TEXT = "Refine Curve";
    static private CurveRefineAgent instance_ = null;
    private List<Area> listArea_;

    private CurveRefineAgent() {
        super(DESC_TEXT, null, new JToggleButton());
        listArea_ = new ArrayList<Area>();
        setEnabled(true);
    }

    @Override
    public void actionPerformed(ActionEvent event) {


        UIController.instance().setState(new AREASELECTION_CONFIG());
        Iterator<RPnPhaseSpacePanel> iterator = UIController.instance().getInstalledPanelsIterator();

        while (iterator.hasNext()) {
            RPnPhaseSpacePanel panel = iterator.next();
            RPnPhaseSpaceAbstraction phaseSpace = (RPnPhaseSpaceAbstraction) panel.scene().getAbstractGeom();
            
            Iterator phaseSpaceIterator = phaseSpace.getGeomObjIterator();
            while (phaseSpaceIterator.hasNext()) {
                RpGeometry phasSpaceGeometry =(RpGeometry) phaseSpaceIterator.next();
                
                if (phasSpaceGeometry.viewingAttr().isSelected()) {

                RpCalcBasedGeomFactory factory = (RpCalcBasedGeomFactory) phasSpaceGeometry.geomFactory();

                if (factory.rpCalc() instanceof ContourCurveCalc) {
                    ContourCurveCalc calc = (ContourCurveCalc) factory.rpCalc();

                    int x = calc.getParams().getResolution()[0];
                    int y = calc.getParams().getResolution()[1];

                    CurveRefine.instance().setResolution(new RealVector(x + " " + y));
                    CurveRefine.instance().setRefineGeometry(phasSpaceGeometry, panel);
                    

                    RPnAdjustedSelectionPlotter boxPlotter = new RPnAdjustedSelectionPlotter(x, y);
                    panel.addMouseListener(boxPlotter);
                    panel.addMouseMotionListener(boxPlotter);
                }


            }
                
            }
            
            

        }



    }

    public static CurveRefineAgent instance() {
        if (instance_ == null) {
            instance_ = new CurveRefineAgent();
        }
        return instance_;
    }

    @Override
    public void unexecute() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void execute() {



        System.out.println("Resolution de Curve Refine");

    }

    public List<Area> getListArea() {
        return listArea_;
    }

    @Override
    public RpGeometry createRpGeometry(RealVector[] coords) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
