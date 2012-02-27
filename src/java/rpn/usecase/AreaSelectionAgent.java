/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.usecase;

import java.util.List;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JToggleButton;
import rpn.RPnDesktopPlotter;
import rpn.RPnMenuCommand;
import rpn.RPnPhaseSpaceAbstraction;
import rpn.RPnPhaseSpaceFrame;
import rpn.RPnPhaseSpacePanel;
import rpn.RPnSelectedAreaDialog;
import rpn.RPnUIFrame;
import rpn.component.HugoniotCurveGeomFactory;
import rpn.component.LevelCurveGeomFactory;
import rpn.component.util.ControlClick;
import rpn.component.util.GeometryGraph;
import rpn.component.util.GeometryGraph3D;
import rpn.component.util.GeometryGraphND;
import rpn.component.util.GeometryUtil;
import rpn.controller.phasespace.NumConfigImpl;
import rpn.controller.ui.AREASELECTION_CONFIG;
import rpn.controller.ui.BIFURCATIONREFINE_CONFIG;
import rpn.controller.ui.UIController;
import rpn.parser.RPnDataModule;
import rpnumerics.Area;
import rpnumerics.BifurcationProfile;
import rpnumerics.HugoniotCurve;
import rpnumerics.HugoniotCurveCalcND;
import rpnumerics.RPNUMERICS;
import rpnumerics.RPnCurve;
import rpnumerics.RpException;
import rpnumerics.SegmentedCurve;
import wave.multid.CoordsArray;
import wave.multid.model.BoundingBox;
import wave.util.Boundary;
import wave.util.RealVector;
import wave.util.RectBoundary;


public class AreaSelectionAgent extends RpModelActionAgent {

    static public final String DESC_TEXT = "Select Area";
    static private AreaSelectionAgent instance_ = null;
    private JToggleButton button_;
    private RealVector resolution_;
    private boolean validResolution_;
    private List<Area> listArea_;

    
    private AreaSelectionAgent() {
        super(DESC_TEXT, null);

        button_ = new JToggleButton(this);
        button_.setToolTipText(DESC_TEXT);
        button_.setFont(rpn.RPnConfigReader.MODELPLOT_BUTTON_FONT);
        listArea_ = new ArrayList<Area>();
        setEnabled(true);
    }

    //** Edson / Leandro
    @Override
    public void actionPerformed(ActionEvent event) {

       UIController.instance().setState(new AREASELECTION_CONFIG());
       
    }
    //***

    public static AreaSelectionAgent instance() {
        if (instance_ == null) {
            instance_ = new AreaSelectionAgent();
        }
        return instance_;
    }

    
    @Override
    public void unexecute() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void execute() {

        //**********************************************************************

        if (ControlClick.ind % 2 == 0  &&  GeometryUtil.closestCurve_ instanceof SegmentedCurve) {
            
            Object[] options = {"Zoom", "Refine"};
            int n = JOptionPane.showOptionDialog(new JFrame(),
                    "Selected area to: ",
                    "Selected Area",
                    JOptionPane.YES_NO_CANCEL_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    options,
                    options[1]);

            if (n == 0  &&  GeometryGraphND.mapToEqui == 0) {
                RectBoundary rectBdry = new RectBoundary(GeometryGraph.downLeft, GeometryGraph.topRight);
                Boundary bdry = (Boundary) rectBdry;
                RPnDesktopPlotter.getUIFrame().phaseSpaceFramesInit(bdry);
            }

            if (n == 1) {
                Area area = null;
                String Re1 = JOptionPane.showInputDialog(null, "Resolucao horizontal", "Resolucao", JOptionPane.QUESTION_MESSAGE);
                String Re2 = JOptionPane.showInputDialog(null, "Resolucao vertical", "Resolucao", JOptionPane.QUESTION_MESSAGE);

                try {
                    RealVector resolution = new RealVector(RPNUMERICS.domainDim());
                    resolution.setElement(0, Integer.parseInt(Re1));
                    resolution.setElement(1, Integer.parseInt(Re2));

                    if (RPNUMERICS.domainDim() == 2) {
                        area = new Area(resolution, GeometryGraph.topRight, GeometryGraph.downLeft);
                        System.out.println(area);
                        listArea_.add(area);
                        System.out.println("listArea.size() : " + listArea_.size());

                    } else if (RPNUMERICS.domainDim() == 3) {
                        area = new Area(resolution, GeometryGraph3D.topRight, GeometryGraph3D.downLeft);
                        System.out.println(area);
                        listArea_.add(area);
                    }


                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, "Resolucao nao foi informada", "ATENCAO", JOptionPane.INFORMATION_MESSAGE);
                }
            }


        }


        //**********************************************************************
        

    }

    public JToggleButton getContainer() {
        return button_;
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

}


