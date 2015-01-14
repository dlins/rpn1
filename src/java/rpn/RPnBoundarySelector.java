/*
 *
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn;

import java.awt.Color;
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
import rpn.component.PhysicalBoundaryFactory;
import rpn.component.PhysicalBoundaryGeom;
import rpn.component.RpGeometry;
import rpn.component.util.LinePlotted;
import rpnumerics.PhysicalBoundaryCalc;
import wave.multid.Coords2D;
import wave.multid.CoordsArray;
import wave.multid.Space;
import wave.multid.view.ViewingAttr;
import wave.util.RealVector;

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
        phaseSpacePanel_.getCastedUI().pointMarkBuffer().clear();
        

        
        ArrayList<JCheckBox> selectedCheckBox = new ArrayList<JCheckBox>();
        
        for (JCheckBox jCheckBox : verticesArray_) {
            
            if (jCheckBox.isSelected()){
                selectedCheckBox.add(jCheckBox);                
            }

            
        }

        
        createEdgeSelection(selectedCheckBox);
        
//        for (int i = 0; i < selectedCheckBox.size(); i++) {
//            JCheckBox jCheckBox = selectedCheckBox.get(i);
//            
//            String [] pointCoords = jCheckBox.getName().split(" ");
//            
//            int xCoord = Integer.parseInt(pointCoords[0]);
//            int yCoord = Integer.parseInt(pointCoords[1]);
//            
//            
//            Point point = new Point(xCoord, yCoord);
//            
//            
//            phaseSpacePanel_.getCastedUI().pointMarkBuffer().add(point);
//            
//            
//            
//            
//            
//        }
//        
//        phaseSpacePanel_.repaint();
        


    }

    
    
    private void createEdgeSelection(List<JCheckBox> selectedVertices){
        
        if (selectedVertices.size()==1){
            JCheckBox jCheckBox = selectedVertices.get(0);
            String [] pointCoords = jCheckBox.getName().split(" ");
            
            int xCoord = Integer.parseInt(pointCoords[0]);
            int yCoord = Integer.parseInt(pointCoords[1]);
            
            Point point = new Point(xCoord, yCoord);

            phaseSpacePanel_.getCastedUI().pointMarkBuffer().add(point);
            
        }
        
        if (selectedVertices.size()==2){
            
            List<Object> lineEdges = new ArrayList<Object>();

            for (int i = 0; i < selectedVertices.size(); i++) {
            JCheckBox jCheckBox = selectedVertices.get(i);
            
            String [] pointCoords = jCheckBox.getName().split(" ");
            
            int xCoord = Integer.parseInt(pointCoords[0]);
            int yCoord = Integer.parseInt(pointCoords[1]);
            
                        
            
            Coords2D dcSelectionPoint = new Coords2D(xCoord, yCoord);
            CoordsArray wcSelectionPoint = new CoordsArray(new Space("", 2));
            
            phaseSpacePanel_.scene().getViewingTransform().dcInverseTransform(dcSelectionPoint, wcSelectionPoint);

            lineEdges.add(wcSelectionPoint);
            
            
        }
            
            PhysicalBoundaryGeom  boundaryGeom =null;
            
            PhysicalBoundaryFactory factory  = new PhysicalBoundaryFactory(new PhysicalBoundaryCalc());
            
            boundaryGeom= (PhysicalBoundaryGeom) factory.geom();
            

            boundaryGeom.edgeSelection((CoordsArray)lineEdges.get(0), (CoordsArray)lineEdges.get(1));
            
//            lineEdges.add("");
//            
//            LinePlotted edge = new LinePlotted(lineEdges, phaseSpacePanel_.scene().getViewingTransform(), new ViewingAttr(Color.yellow));            
//            
//            phaseSpacePanel_.addGraphicUtil(edge);
//            phaseSpacePanel_.repaint();
            
        }
        
        
    }
    

    
    
    
   
}
