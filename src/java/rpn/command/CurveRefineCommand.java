/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.command;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.HeadlessException;
import java.awt.Insets;
import java.awt.Polygon;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;
import rpn.RPnDialog;
import rpn.RPnPhaseSpaceAbstraction;
import rpn.RPnPhaseSpacePanel;
import rpn.component.RpCalcBasedGeomFactory;
import rpn.component.RpGeometry;
import rpn.component.util.AreaSelected;
import rpn.controller.ui.UIController;
import rpnumerics.Area;
import wave.multid.view.GeomObjView;
import wave.util.RealVector;

public class CurveRefineCommand extends RpModelConfigChangeCommand {
    //
    // Constants
    //

    static public final String DESC_TEXT = "Curve Refine";
    //
    // Members
    //
    private static CurveRefineCommand instance_ = null;
    private RealVector resolution_;
    private RpGeometry curveToRefine_ = null;
    private RPnPhaseSpacePanel panelToRefine_ = null;

    //
    // Constructors
    //
    protected CurveRefineCommand() {
        super(DESC_TEXT);
        resolution_ = new RealVector(2);
    }

    public void execute() {

        if (curveToRefine_ != null && panelToRefine_ != null) {
            processGeometry(curveToRefine_, panelToRefine_);
            RPnPhaseSpaceAbstraction phaseSpace = (RPnPhaseSpaceAbstraction) panelToRefine_.scene().getAbstractGeom();
            phaseSpace.update();
            panelToRefine_.clearAreaSelection();
        }


    }

    public void unexecute() {
        Double oldValue = (Double) log().getNewValue();
        Double newValue = (Double) log().getOldValue();
        applyChange(new PropertyChangeEvent(this, "level", oldValue, newValue));
    }

    static public CurveRefineCommand instance() {
        if (instance_ == null) {
            instance_ = new CurveRefineCommand();
        }
        return instance_;
    }

    public void setResolution(RealVector resolution) {
        resolution_ = resolution;
    }

    @Override
    public void actionPerformed(ActionEvent event) {

        ResolutionDialog resolutionDialog = new ResolutionDialog("Resolution");
        resolutionDialog.setVisible(true);
    }

    private void processGeometry(RpGeometry selectedGeometry, RPnPhaseSpacePanel phaseSpacePanel) {

        List<Integer> indexToRemove = new ArrayList<Integer>();
        List<Area> areasToRefine = new ArrayList<Area>();

        List<AreaSelected> graphicsArea = phaseSpacePanel.getSelectedAreas();


    



        for (AreaSelected polygon : graphicsArea) {

            Iterator geomIterator = phaseSpacePanel.scene().geometries();
            while (geomIterator.hasNext()) {
                GeomObjView geomObjView = (GeomObjView) geomIterator.next();
                if (((RpGeometry) geomObjView.getAbstractGeom()) == selectedGeometry) {
                    List<Integer> segmentIndex = geomObjView.contains((Polygon)polygon.getShape());
                    if (!segmentIndex.isEmpty()) {
                        indexToRemove.addAll(segmentIndex);
                        
                        Area principalArea = new Area(resolution_, polygon);
                        
                        areasToRefine.add(principalArea);
                        
                        System.out.println("Resolucao: "+resolution_);
                        System.out.println("Principal area: "+ principalArea);
                        
                        

                    }

                }

            }

        }
        
        
//            // ------ Preenche uma lista de areas correspondentes, caso existam
//        List<AreaSelected> correspondentAreas = new ArrayList<AreaSelected>();
//        Iterator<RPnPhaseSpacePanel> phaseSpacePanelIterator = UIController.instance().getInstalledPanelsIterator();
//        while (phaseSpacePanelIterator.hasNext()) {
//            RPnPhaseSpacePanel panel = phaseSpacePanelIterator.next();
//            if (panel!=phaseSpacePanel  &&  panel.getSelectedAreas().size()>0) {
//                correspondentAreas.addAll(panel.getSelectedAreas());
//                // ??? Necessario instanciar objeto Area ???
//                Area correspondentArea = new Area(resolution_, correspondentAreas.get(0));
//                System.out.println("Correspondent area: "+correspondentArea);
//                areasToRefine.add(correspondentArea);
//            }
//        }
//        // -------------------

        
        RpCalcBasedGeomFactory factory = (RpCalcBasedGeomFactory) selectedGeometry.geomFactory();

        factory.updateGeom(areasToRefine, indexToRemove);

    }

    void setRefineGeometryAndPanel(RpGeometry phasSpaceGeometry, RPnPhaseSpacePanel panel) {
        curveToRefine_ = phasSpaceGeometry;
        panelToRefine_ = panel;

    }

    private class ResolutionDialog extends RPnDialog {

        JTextField xresolution_;
        JTextField yresolution_;
        JButton okButton_;

        public ResolutionDialog(String title) throws HeadlessException {

            super(false, true);
            setSize(200, 200);
            setTitle(title);

            xresolution_ = new JTextField(String.valueOf((int) resolution_.getElement(0)));
            yresolution_ = new JTextField(String.valueOf((int) resolution_.getElement(1)));

            GridBagLayout gridBagLayout = new GridBagLayout();
            JPanel resolutionPanel = new JPanel(gridBagLayout);

            GridBagConstraints constraints = new GridBagConstraints();
            constraints.gridx = 0;
            constraints.gridy = 0;

            constraints.ipadx = 10;
            constraints.ipady = 5;

            resolutionPanel.add(xresolution_, constraints);

            constraints.anchor = GridBagConstraints.EAST;

            constraints.insets = new Insets(5, 5, 5, 5);

            constraints.gridx = 1;

            constraints.gridy = 0;

            resolutionPanel.add(yresolution_, constraints);


            getContentPane().add(resolutionPanel, BorderLayout.CENTER);


            Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();

            int height = dim.height;
            int width = dim.width;
            this.setLocation((int) (width - (width * .55)), (int) (height - (height * .9)));
            this.setLocation((int) (width - (width * .55)), 100);

        }

        @Override
        protected void apply() {
            resolution_ = new RealVector(xresolution_.getText() + " " + yresolution_.getText());
            execute();
            dispose();

        }

        @Override
        protected void begin() {
        }
    }
}
