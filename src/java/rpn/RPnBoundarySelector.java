/*
 *
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn;

import java.awt.Point;
import java.awt.Polygon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.table.DefaultTableModel;
import rpn.component.RpGeometry;
import rpn.parser.RPnDataModule;
import wave.multid.view.PointMark;

public class RPnBoundarySelector extends Observable implements ActionListener, ItemListener {

    private JScrollPane tablePanel_;
    private JToolBar toolBar_;
    private JTable curvesTable_;
    private JButton selectNoneButton_, selectAllButton_, removeButton_,
            removeAreasButton_, setVisibleButton_;
    private DefaultTableModel tableModel_;
    private RPnPhaseSpaceAbstraction phaseSpace_;
    private JFrame frame_;
    private List<RpGeometry> selectedGeometries_;
    private List indexGeometries;
    private List<RpGeometry> geometryList_;
    private ArrayList<JCheckBox> verticesArray_;
    private Polygon boundary_;
    private final RPnPhaseSpacePanel phaseSpacePanel_;

    public RPnBoundarySelector(RPnPhaseSpacePanel phaseSpacePanel) {

        boundary_ = phaseSpacePanel.getPhysicalBoundaryPolygon();
        phaseSpacePanel_= phaseSpacePanel;

        createVerticesArray();

    }

    private void createVerticesArray() {

        int[] xVerticesCoords = boundary_.xpoints;
        int[] yVerticesCoords = boundary_.ypoints;

        verticesArray_ = new ArrayList<JCheckBox>();

        for (int i = 0; i < boundary_.npoints; i++) {
            JCheckBox checkBox = new JCheckBox(String.valueOf(i));
            StringBuilder checkNameBuilder = new StringBuilder();
            checkNameBuilder.append(String.valueOf(xVerticesCoords[i])).append(" ").append(String.valueOf(yVerticesCoords[i]));
            checkBox.setName(checkNameBuilder.toString());
            checkBox.addItemListener(this);
            verticesArray_.add(checkBox);
        }

    }

    public ArrayList<JCheckBox> getVerticesArray() {
        return verticesArray_;
    }

   
    @Override
    public void actionPerformed(ActionEvent e) {

    }


    public RPnPhaseSpaceAbstraction getPhaseSpace() {
        return phaseSpace_;
    }

   

    void setVisible(boolean show) {
        frame_.setVisible(show);
    }

    

    @Override
    public void itemStateChanged(ItemEvent e) {

        int checkBoxIndex = verticesArray_.indexOf(e.getSource());
        JCheckBox checkBoxEvent = verticesArray_.get(checkBoxIndex);
        System.out.println("Clicando no check: " + checkBoxEvent.getName());
        


    }

    
    
    
    

    
    
    
   
}
