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

public class AreaSelectionAgent extends RpModelPlotAgent {

    public int ind = 0;
    static public final String DESC_TEXT = "Select Area";
    static private AreaSelectionAgent instance_ = null;
    private JToggleButton button_;
    private RealVector resolution_;
    private boolean validResolution_;
    private List<Area> listArea_;

    private AreaSelectionAgent() {
        super(DESC_TEXT, null, new JToggleButton());
        listArea_ = new ArrayList<Area>();
        setEnabled(true);
    }

    //** Edson / Leandro
    @Override
    public void actionPerformed(ActionEvent event) {

        UIController.instance().setState(new AREASELECTION_CONFIG());


        Iterator<RPnPhaseSpacePanel> iterator = UIController.instance().getInstalledPanelsIterator();

        while (iterator.hasNext()) {
            RPnPhaseSpacePanel button = iterator.next();

            RPnPhasePanelBoxPlotter boxPlotter = new RPnPhasePanelBoxPlotter();
            button.addMouseListener(boxPlotter);
            button.addMouseMotionListener(boxPlotter);



        }




    }

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

    public void execute_() {



        UserInputTable userInputList = UIController.instance().globalInputTable();
        RealVector newValue = userInputList.values();

        RPnPhaseSpaceAbstraction phaseSpace = RPnDataModule.PHASESPACE;
        RpGeometry geom = phaseSpace.findClosestGeometry(newValue);
        RPnCurve curve = (RPnCurve) (geom.geomFactory().geomSource());

//        if (curve instanceof SegmentedCurve) {

        Object[] options = {"Zoom", "Refine"};
        int n = JOptionPane.showOptionDialog(new JFrame(),
                "Selected area to: ",
                "Selected Area",
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[1]);

        if (n == 0 && GeometryGraphND.mapToEqui == 0) {
            RectBoundary rectBdry = new RectBoundary(GeometryGraph.downLeft, GeometryGraph.topRight);
            Boundary bdry = (Boundary) rectBdry;
            RPnDesktopPlotter.getUIFrame().phaseSpaceFrameZoom(bdry);
        }

        if (n == 1) {
            Area area = null;
//                String Re1 = JOptionPane.showInputDialog(null, "Resolucao horizontal", "Resolucao", JOptionPane.QUESTION_MESSAGE);
//                String Re2 = JOptionPane.showInputDialog(null, "Resolucao vertical", "Resolucao", JOptionPane.QUESTION_MESSAGE);

            try {
                RealVector resolution = new RealVector(RPNUMERICS.domainDim());
//                    resolution.setElement(0, Integer.parseInt(Re1));
//                    resolution.setElement(1, Integer.parseInt(Re2));

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


//        }
    }

    @Override
    public void execute() {
//        UserInputTable userInputList = UIController.instance().globalInputTable();
//        RealVector newValue = userInputList.values();
//
//        //*** ATENCAO: qualquer coisa relacionada a ControlClick deve ser removida
//        if ((GeometryGraph.count % 2) == 0) {
//
//            for (int i = 0; i < newValue.getSize(); i++) {
//                GeometryGraphND.targetPoint.setElement(i, newValue.getElement(i));
//            }
//
//            RPnPhaseSpaceAbstraction phaseSpace = RPnDataModule.PHASESPACE;
//            RpGeometry geom = phaseSpace.findClosestGeometry(newValue);
//            RPnCurve curve = (RPnCurve) (geom.geomFactory().geomSource());
//
//            GeometryGraphND.pMarca = curve.findClosestPoint(newValue);
//
//            GeometryGraph.count++;
//            return;
//
//        }
//
//
//        if ((GeometryGraph.count % 2) == 1) {
//
//            for (int i = 0; i < newValue.getSize(); i++) {
//                GeometryGraphND.cornerRet.setElement(i, newValue.getElement(i));
//            }
//
//            GeometryGraphND.zContido.clear();
//            GeometryGraphND.wContido.clear();
//
//            GeometryGraph.count++;
//            execute_();
//
//        }
//        //******
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
