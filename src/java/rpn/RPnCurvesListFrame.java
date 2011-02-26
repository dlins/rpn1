/*
 *
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.DisplayMode;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.text.NumberFormat;
import java.util.Vector;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.ScrollPaneConstants;
import javax.swing.WindowConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import rpn.controller.ui.UIController;
import rpn.parser.RPnDataModule;
import wave.util.RealVector;

public class RPnCurvesListFrame extends JFrame implements ListSelectionListener, ActionListener, TableModelListener, MouseListener {

    private JScrollPane tablePanel_;
    private JToolBar toolBar_;
    private JTable curvesTable_;
    private JButton resetSelectionButton_;
    private static DefaultTableModel tableModel_ = new RPnCurvesTableModel();
    private JPopupMenu popUpTableMenu_;
    private JMenuItem removeMenuItem_;

    public RPnCurvesListFrame() {
        super("Curves");

        this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

        setSize(new Dimension(360, 200));
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice[] gs = ge.getScreenDevices();
        DisplayMode displayMode = gs[0].getDisplayMode();
        int height = displayMode.getHeight();
        int width = displayMode.getWidth();
        this.setLocation((int) (width - (width * .55)), (int) (height - (height * .45)));


        toolBar_ = new JToolBar();
        tableModel_.addTableModelListener(this);
        curvesTable_ = new JTable(tableModel_);

        curvesTable_.addMouseListener(this);

        popUpTableMenu_ = new JPopupMenu("Curves Menu");



        removeMenuItem_ = new JMenuItem("Remove selected curves");
        removeMenuItem_.addMouseListener(this);
        popUpTableMenu_.add(removeMenuItem_);



        tablePanel_ = new JScrollPane(curvesTable_);
        resetSelectionButton_ = new JButton("Select None");
        resetSelectionButton_.addActionListener(this);


        toolBar_.add(resetSelectionButton_);
        toolBar_.setFloatable(false);

        curvesTable_.getSelectionModel().addListSelectionListener(this);
        curvesTable_.getColumnModel().getColumn(0).setPreferredWidth(20);
        curvesTable_.getColumnModel().getColumn(1).setPreferredWidth(80);


        tablePanel_.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        this.getContentPane().setLayout(new BorderLayout());
        this.getContentPane().add(tablePanel_, BorderLayout.CENTER);
        this.getContentPane().add(toolBar_, BorderLayout.NORTH);

    }

    public static void addGeometry(String geometryIndex, String geometryName) {

        Vector<Object> data = new Vector<Object>();
        RealVector userInput = UIController.instance().globalInputTable().values();

        NumberFormat formatter = NumberFormat.getInstance();
        formatter.setMaximumFractionDigits(4);

//        data.add(geometryIndex);
        data.add(geometryName);
        String userInputString = "";
        for (int i = 0; i < userInput.getSize(); i++) {
            userInputString = userInputString.concat(formatter.format(userInput.getElement(i)) + " ");

        }




        data.add(userInputString);
        data.add(new Boolean(true));

        tableModel_.addRow(data);

    }

    public static void removeLastEntry() {

        if (tableModel_.getRowCount() >= 2) {
            tableModel_.removeRow(tableModel_.getRowCount() - 2);
        }

    }

    public void valueChanged(ListSelectionEvent e) {

        if (!e.getValueIsAdjusting()) {

            int rowsNumber = curvesTable_.getRowCount();
            for (int i = 0; i < rowsNumber; i++) {
                if (curvesTable_.isRowSelected(i)) {
                    RPnDataModule.PHASESPACE.highlightGeometry(i);
                } else {
                    RPnDataModule.PHASESPACE.lowlightGeometry(i);
                }

            }
        }

    }

    public void actionPerformed(ActionEvent e) {
        curvesTable_.clearSelection();
        RPnDataModule.PHASESPACE.clearGeometrySelection();

    }

    public void tableChanged(TableModelEvent e) {
        curvesTable_.clearSelection();

//        if (e.getType() == TableModelEvent.INSERT) {
//            RPnDataModule.PHASESPACE.clearGeometrySelection();
//        }

        if (e.getType() == TableModelEvent.UPDATE) {

            if (e.getColumn() == 2) {
                Boolean visible = (Boolean) curvesTable_.getValueAt(e.getFirstRow(), 2);
                if (visible) {
//                    RPnDataModule.PHASESPACE.displayGeometry(new Integer((String) curvesTable_.getValueAt(e.getFirstRow(), 0)));

                    RPnDataModule.PHASESPACE.displayGeometry(e.getFirstRow());

                } else {
//                    RPnDataModule.PHASESPACE.hideGeometry(new Integer((String) curvesTable_.getValueAt(e.getFirstRow(),0)));                }
                    RPnDataModule.PHASESPACE.hideGeometry(e.getFirstRow());
                }
//                System.out.println("Trocando valor" + curvesTable_.getValueAt(e.getFirstRow(), 3) + " " + curvesTable_.getValueAt(e.getFirstRow(), 0));
            }

        }

        RPnDataModule.PHASESPACE.clearGeometrySelection();

    }

    private void geometryVisibleSelection(int geomIndex) {
    }

    public void mouseClicked(MouseEvent e) {
    }

    public void mousePressed(MouseEvent e) {

        if (e.isPopupTrigger()) {
            popUpTableMenu_.show(curvesTable_, e.getX(), e.getY());
        }


        if (e.getSource() instanceof JMenuItem) {
            int[] selectedRows = curvesTable_.getSelectedRows();

            for (int i = 0; i < selectedRows.length; i++) {
                int j = selectedRows[i];
                RPnDataModule.PHASESPACE.remove(j);

                System.out.println("Geometria: " + j);
                tableModel_.removeRow(j);

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
