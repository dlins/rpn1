/*
 *
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn;

import java.util.Vector;
import javax.swing.table.DefaultTableModel;

public class RPnCurvesTableModel extends DefaultTableModel {

    Vector<Integer> selectedRowVector_;

    public RPnCurvesTableModel() {

        columnIdentifiers.add("Curve");
        columnIdentifiers.add("User Input");
        columnIdentifiers.add("Visible");
        columnIdentifiers.add("Selected");


    }

    @Override
    public Class getColumnClass(int c) {
        return getValueAt(0, c).getClass();
    }

    @Override
    public int getColumnCount() {
        return 4;
    }

    @Override
    public boolean isCellEditable(int row, int col) {
        if ((col==2) || (col==3)) {
            return true;
            
        }
       
        return false;
    }

    

   
}
