/*
 *
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.plugininterface;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.TransferHandler;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

public class TableTransferHandler extends TransferHandler {

    @Override
    public boolean importData(TransferHandler.TransferSupport info) {//Put a plugin in use
        if (!info.isDrop()) {
            return false;
        }

        JTable table = (JTable) info.getComponent();

        JTable.DropLocation dropLocation = table.getDropLocation();

        TableModel tModel = table.getModel();

        Transferable data = info.getTransferable();
        String dataString;
        try {
            dataString = (String) data.getTransferData(DataFlavor.stringFlavor);

            String[] infos = dataString.split(" ");

            tModel.setValueAt(infos[0], dropLocation.getRow(), 1);
            tModel.setValueAt(infos[2], dropLocation.getRow(), 2);

            tModel.setValueAt(getMethodName(infos[3]), dropLocation.getRow(), 3);
            tModel.setValueAt(getMethodName(infos[4]), dropLocation.getRow(), 4);


        } catch (Exception e) {
            return false;
        }

        DefaultTableModel defaultModel = (DefaultTableModel) tModel;
        defaultModel.fireTableCellUpdated(dropLocation.getRow(), 1);
        defaultModel.fireTableCellUpdated(dropLocation.getRow(), 2);
        defaultModel.fireTableCellUpdated(dropLocation.getRow(), 3);
        defaultModel.fireTableCellUpdated(dropLocation.getRow(), 4);

        return true;


    }

    @Override
    public boolean canImport(TransferHandler.TransferSupport support) {//Verifiy the plugin type

        JComponent targetComponent = (JComponent) support.getComponent();

        if (!(targetComponent instanceof JTable)) {
            return false;
        }

        JTable pluginsTable = (JTable) targetComponent;

        Transferable data = support.getTransferable();

        JTable.DropLocation dropLocation = (JTable.DropLocation) support.getDropLocation();

        TableModel tModel = pluginsTable.getModel();

        String dataString;

        try {
            dataString = (String) data.getTransferData(DataFlavor.stringFlavor);

            String[] infos = dataString.split(" ");

            if ((tModel.getValueAt(dropLocation.getRow(), 0)).toString().equals(infos[1])) {
                return true;
            }

            return false;

        } catch (Exception e) {

            return false;
        }
    }

    private String getMethodName(String inputString) {
        String[] data = inputString.split("\"");
        return data[1];
    }
}
