/*
 *
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.plugininterface;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import javax.swing.JComponent;
import javax.swing.JTree;
import javax.swing.TransferHandler;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

public class TreeTransferHandler extends TransferHandler {

    @Override
    public int getSourceActions(JComponent c) {
        return COPY;
    }

    @Override
    public Transferable createTransferable(JComponent c) {

        JTree tree = (JTree) c;

        TreePath path = tree.getSelectionPath();

        String className;

        TreeModel treeModel = tree.getModel();

        if (path.getPathCount() > 3) {
            if (treeModel.isLeaf(path.getLastPathComponent())) {

                DefaultMutableTreeNode methodNode = (DefaultMutableTreeNode) path.getLastPathComponent();
                DefaultMutableTreeNode classNode = (DefaultMutableTreeNode) methodNode.getParent();
                DefaultMutableTreeNode pluginTypeNode = (DefaultMutableTreeNode) classNode.getParent();
                DefaultMutableTreeNode libNode = (DefaultMutableTreeNode) pluginTypeNode.getParent();
                className = libNode.toString()+" "+pluginTypeNode.toString() + " " + classNode.toString() + " " + methodNode.toString();

            } else {

                DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent();
                DefaultMutableTreeNode libNode = (DefaultMutableTreeNode) (node.getParent()).getParent();
                className = libNode.toString() + " " + node.getParent().toString() + " " + node.toString() + " " + node.getFirstChild().toString();
            
            }
            return new StringSelection(className);

        }

        return new StringSelection("");
    }


    @Override
    public boolean canImport(JComponent comp, DataFlavor[] transferFlavors) {
        return false;
    }
}

