/*
 *
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.plugininterface;

import java.awt.BorderLayout;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Vector;
import javax.swing.DropMode;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.ListSelectionModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

public class PluginInfoPanel extends JPanel {

    private JTree pluginTree_;
    private static JTable configPluginTable_;
    private DefaultMutableTreeNode rootNode_;
    private BorderLayout layout = new BorderLayout();
    private JSplitPane splitPane;
    private JScrollPane pluginTreePanel, tableConfigPanel;

    public PluginInfoPanel() {

        this.setLayout(layout);

        rootNode_ = new DefaultMutableTreeNode("Libraries");
        pluginTree_ = new JTree(rootNode_);

        pluginTree_.setDragEnabled(true);

        pluginTree_.setTransferHandler(new TreeTransferHandler());
        pluginTree_.setDropMode(DropMode.ON);
        pluginTree_.addMouseListener(new TreeMouseListener());


        configPluginTable_ = new JTable(PluginTableModel.instance());
        configPluginTable_.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        configPluginTable_.getTableHeader().setReorderingAllowed(false);

        tableConfigPanel = new JScrollPane(configPluginTable_);

        pluginTreePanel = new JScrollPane(pluginTree_);
        configPluginTable_.setFillsViewportHeight(true);

        TableColumn tableColumn = null;

        TableModel configTableModel = configPluginTable_.getModel();
        for (int i = 0; i < configTableModel.getColumnCount(); i++) {
            tableColumn = configPluginTable_.getColumnModel().getColumn(i);
            String headerValue = (String) tableColumn.getHeaderValue();
            tableColumn.setPreferredWidth(headerValue.length() + 1);
            tableColumn.sizeWidthToFit();
        }

        configPluginTable_.setDragEnabled(true);
        configPluginTable_.setTransferHandler(new TableTransferHandler());
        configPluginTable_.setDropMode(DropMode.ON);
        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, tableConfigPanel, pluginTreePanel);

        this.add(splitPane, BorderLayout.CENTER);

    }

    public void fillPluginTree(HashMap<String, HashMap<String, Vector<HashMap<String, String>>>> library) {

        rootNode_.removeAllChildren();

        Set<Entry<String, HashMap<String, Vector<HashMap<String, String>>>>> libraryContentSet = library.entrySet();

        Set<String> libraryName = library.keySet();

        Iterator<String> libraryNameString = libraryName.iterator();

        Iterator<Entry<String, HashMap<String, Vector<HashMap<String, String>>>>> libraryIterator = libraryContentSet.iterator();


        while (libraryIterator.hasNext()) {

            DefaultMutableTreeNode libraryNameNode = new DefaultMutableTreeNode(libraryNameString.next());

            Entry<String, HashMap<String, Vector<HashMap<String, String>>>> libraryEntry = libraryIterator.next();

            HashMap<String, Vector<HashMap<String, String>>> pluginTypeClasses = libraryEntry.getValue();

            Set<Entry<String, Vector<HashMap<String, String>>>> classesSet = pluginTypeClasses.entrySet();

            Iterator<Entry<String, Vector<HashMap<String, String>>>> classesIterator = classesSet.iterator();

            while (classesIterator.hasNext()) {

                Entry<String, Vector<HashMap<String, String>>> pluginTypeEntry = classesIterator.next();

                DefaultMutableTreeNode pluginTypeNode = new DefaultMutableTreeNode(pluginTypeEntry.getKey());

                Vector<HashMap<String, String>> classesVector = pluginTypeEntry.getValue();

                for (int i = 0; i < classesVector.size(); i++) {

                    HashMap<String, String> classData = classesVector.get(i);

                    Set<Entry<String, String>> classEntry = classData.entrySet();

                    Iterator<Entry<String, String>> classIterator = classEntry.iterator();

                    while (classIterator.hasNext()) {

                        Entry<String, String> classEnt = classIterator.next();

                        DefaultMutableTreeNode classNode = new DefaultMutableTreeNode(classEnt.getKey().toString());
                        DefaultMutableTreeNode constructMethodNode = new DefaultMutableTreeNode(classEnt.getValue().toString());
                        classNode.add(constructMethodNode);
                        pluginTypeNode.add(classNode);

                    }

                }
                libraryNameNode.add(pluginTypeNode);

            }
            rootNode_.add(libraryNameNode);
        }

        rootNode_.setUserObject(PluginTableModel.getPluginDir());
        DefaultTreeModel newModel = (DefaultTreeModel) pluginTree_.getModel();
        newModel.setRoot(rootNode_);


        pluginTree_.setModel(newModel);
        pluginTree_.expandRow(0);
    }

    public static void transferTreeToTable(JTree tree) {

        TreePath path = tree.getSelectionPath();

        String className;

        TreeModel treeModel = tree.getModel();

        if (path.getPathCount() > 3) {
            if (treeModel.isLeaf(path.getLastPathComponent())) {

                DefaultMutableTreeNode methodNode = (DefaultMutableTreeNode) path.getLastPathComponent();
                DefaultMutableTreeNode classNode = (DefaultMutableTreeNode) methodNode.getParent();
                DefaultMutableTreeNode pluginTypeNode = (DefaultMutableTreeNode) classNode.getParent();
                DefaultMutableTreeNode libNode = (DefaultMutableTreeNode) pluginTypeNode.getParent();
                className = libNode.toString() + " " + pluginTypeNode.toString() + " " + classNode.toString() + " " + methodNode.toString();

            } else {

                DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent();
                DefaultMutableTreeNode libNode = (DefaultMutableTreeNode) (node.getParent()).getParent();
                className = libNode.toString() + " " + node.getParent().toString() + " " + node.toString() + " " + node.getFirstChild().toString();

            }


            TableModel configModel = configPluginTable_.getModel();

            String[] infos = className.split(" ");


            for (int i = 0; i < configPluginTable_.getRowCount(); i++) {

                if (configModel.getValueAt(i, 0).equals(infos[1])) {

                    configModel.setValueAt(infos[0], i, 1);
                    configModel.setValueAt(infos[2], i, 2);
                    configModel.setValueAt(infos[3], i, 3);

                }

            }

        }

    }

    public JTable getConfigTable() {
        return configPluginTable_;
    }
}
