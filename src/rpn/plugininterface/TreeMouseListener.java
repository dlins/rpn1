/*
 *
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.plugininterface;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTree;
import javax.swing.tree.TreePath;

public class TreeMouseListener implements MouseListener {

    public void mouseClicked(MouseEvent e) {
    }

    public void mousePressed(MouseEvent e) {

        JTree tree = (JTree) e.getSource();
        if (tree.getSelectionCount() != 0) {

            TreePath path = tree.getSelectionPath();
            if (path.getPathCount() == 1 && e.isPopupTrigger()) {

                JPopupMenu popMenu = new JPopupMenu();

                JMenuItem menuItem = new JMenuItem("Change plugin Directory ...");

                menuItem.addActionListener(
                        new ActionListener() {

                            public void actionPerformed(ActionEvent e) {
                                PluginDirectoryDialog dialog = new PluginDirectoryDialog();
                                dialog.setVisible(true);
                            }
                        });

                popMenu.add(menuItem);
                popMenu.show(e.getComponent(), e.getX(), e.getY());
            }


            if (path.getPathCount() > 3 && e.isPopupTrigger()) {
                JPopupMenu popMenu = new JPopupMenu();
                JMenuItem installItem = new JMenuItem("Use this plugin");
                installItem.addActionListener(new PluginTreeClickController(tree));
                popMenu.add(installItem);
                popMenu.show(e.getComponent(), e.getX(), e.getY());

            }
        }


    }

    public void mouseReleased(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }
}
