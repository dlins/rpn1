/*
 *
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;

public class RPnCurvesTableModel extends DefaultTableModel  {

    public RPnCurvesTableModel() {
        columnIdentifiers.add(new String("Index"));
        columnIdentifiers.add(new String("Geometry"));
        columnIdentifiers.add(new String("Visible"));


    }

    @Override
    public Class getColumnClass(int c) {
        return getValueAt(0, c).getClass();
    }

    @Override
    public int getColumnCount() {
        return 3;
    }

   
    @Override
    public boolean isCellEditable(int row, int col) {
        if (col == 2) {
            return true;
        }
        return false;
    }
}
