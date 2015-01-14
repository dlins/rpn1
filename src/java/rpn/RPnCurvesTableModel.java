/*
 *
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn;

import java.util.Iterator;
import java.util.Vector;
import javax.swing.table.DefaultTableModel;
import rpn.component.BifurcationCurveBranchGeom;

public class RPnCurvesTableModel extends DefaultTableModel {

   
    RPnPhaseSpaceAbstraction phaseSpace_;

    

    RPnCurvesTableModel(RPnPhaseSpaceAbstraction phaseSpace) {
        phaseSpace_=phaseSpace;
        columnIdentifiers.add("Curve");
        columnIdentifiers.add("User Input");
        columnIdentifiers.add("Visible");
        columnIdentifiers.add("Selected");
        columnIdentifiers.add("Correspondence");
        


    }

    @Override
    public Class getColumnClass(int c) {
        return getValueAt(0, c).getClass();
    }

    @Override
    public int getColumnCount() {
        return 5;
    }

    @Override
    public boolean isCellEditable(int row, int col) {

        if ((col == 2) || (col == 3)){
            return true;
        }
        
        if (col==4){
            int count =0;
            
            Iterator geomIterator = phaseSpace_.getGeomObjIterator();
            while (geomIterator.hasNext()) {
                Object object = geomIterator.next();
                if (object instanceof BifurcationCurveBranchGeom && count==row){
                    return true;
                }
                    
                count++;
            }
            
        }

        return false;
    }

    @Override
  public void setValueAt(Object aValue, int row , int col){
      super.setValueAt(aValue, row, col);
      
      int count =0;
            
            Iterator geomIterator = phaseSpace_.getGeomObjIterator();
            while (geomIterator.hasNext()) {
                Object object = geomIterator.next();
                if ((object instanceof BifurcationCurveBranchGeom && count==row) && (col==4)){
                    BifurcationCurveBranchGeom bifurcationCurveBranchGeom = (BifurcationCurveBranchGeom) object;
                    bifurcationCurveBranchGeom.setCorrespondenceDirection(BifurcationCurveBranchGeom.CorrespondenceDirection.valueOf((String)aValue).ordinal());
                }
                    
                count++;
                
            }
      
      
  }

}
