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
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JToggleButton;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import rpn.RPnDesktopPlotter;
import rpn.RPnPhaseSpaceAbstraction;
import rpn.RPnPhaseSpacePanel;
import rpn.RPnUIFrame;
import rpn.component.RpGeometry;
import rpn.component.util.GeometryGraph;
import rpn.component.util.GeometryGraph3D;
import rpn.component.util.GeometryGraphND;
import rpn.controller.RPnAdjustedSelectionPlotter;
import rpn.controller.RPnPhasePanelBoxPlotter;
import rpn.controller.ui.AREASELECTION_CONFIG;
import rpn.controller.ui.UIController;
import rpn.controller.ui.UserInputTable;
import rpn.parser.RPnDataModule;
import rpnumerics.Area;
import rpnumerics.RPNUMERICS;
import rpnumerics.RPnCurve;
import wave.util.Boundary;
import wave.util.RealVector;
import wave.util.RectBoundary;

public class CurveRefineAgent extends RpModelPlotAgent {

    public int ind = 0;
    static public final String DESC_TEXT = "Refine Curve";
    static private CurveRefineAgent instance_ = null;
    private JToggleButton button_;
    private RealVector resolution_;
    private boolean validResolution_;
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
            RPnPhaseSpacePanel button = iterator.next();

            RPnAdjustedSelectionPlotter  boxPlotter = new RPnAdjustedSelectionPlotter();
            button.addMouseListener(boxPlotter);
            button.addMouseMotionListener(boxPlotter);
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

    public void setResolution(RealVector resolution) {
        resolution_ = resolution;
    }

    public void setValidResolution(boolean validResolution) {
        validResolution_ = validResolution;
    }

    public List<Area> getListArea() {
        return listArea_;
    }

    @Override
    public RpGeometry createRpGeometry(RealVector[] coords) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
