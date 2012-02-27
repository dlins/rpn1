/*
 *
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
import java.util.Vector;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.ScrollPaneConstants;
import javax.swing.WindowConstants;
import javax.swing.table.DefaultTableModel;
import rpn.component.RpCalcBasedGeomFactory;
import rpn.component.RpGeometry;
import rpn.controller.ui.UIController;
import rpn.parser.RPnDataModule;
import rpnumerics.HugoniotCurveCalcND;
import rpnumerics.LevelCurveCalc;
import rpnumerics.OrbitCalc;
import rpnumerics.PointLevelCalc;
import rpnumerics.RpCalculation;
import wave.util.RealVector;

public class RPnCurvesListFrame extends JFrame implements ActionListener {

    private JScrollPane tablePanel_;
    private JToolBar toolBar_;
    private JTable curvesTable_;
    private JButton selectNoneButton_, selectAllButton_, invisibleButton_, visibleButton_, removeButton_;
    private static DefaultTableModel tableModel_ = new RPnCurvesTableModel();

    public RPnCurvesListFrame() {
        super("Curves");

        this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

        setSize(new Dimension(600, 250));
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();

        int height = dim.height;
        int width = dim.width;

        this.setLocation((int) (width - (width * .55)), (int) (height - (height * .35)));


        toolBar_ = new JToolBar();

        curvesTable_ = new JTable(tableModel_);

        curvesTable_.setRowSelectionAllowed(false);

        tablePanel_ = new JScrollPane(curvesTable_);

        selectNoneButton_ = new JButton("Select None");
        selectNoneButton_.setName("SelectNone");

        selectAllButton_ = new JButton("Select All");
        selectAllButton_.setName("SelectAll");

        invisibleButton_ = new JButton("Invisible");
        invisibleButton_.setName("Invisible");

        visibleButton_ = new JButton("Visible");
        visibleButton_.setName("Visible");

        removeButton_ = new JButton("Remove");
        removeButton_.setName("Remove");

        selectNoneButton_.addActionListener(this);
        selectAllButton_.addActionListener(this);
        invisibleButton_.addActionListener(this);
        visibleButton_.addActionListener(this);
        removeButton_.addActionListener(this);

        toolBar_.add(selectNoneButton_);
        toolBar_.add(selectAllButton_);
        toolBar_.add(invisibleButton_);
        toolBar_.add(visibleButton_);
        toolBar_.add(removeButton_);


        toolBar_.setFloatable(false);


        curvesTable_.getColumnModel().getColumn(0).setPreferredWidth(20);
        curvesTable_.getColumnModel().getColumn(1).setPreferredWidth(80);

        curvesTable_.getTableHeader().setReorderingAllowed(false);


        tablePanel_.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        this.getContentPane().setLayout(new BorderLayout());
        this.getContentPane().add(tablePanel_, BorderLayout.CENTER);
        this.getContentPane().add(toolBar_, BorderLayout.NORTH);


    }

    public static void removeGeometry(Integer geometryIndex) {
        tableModel_.removeRow(geometryIndex);
    }

    public static void addGeometry(RpGeometry geometry) {


        String geometryName = geometry.getClass().getSimpleName();

        RealVector userInput =new RealVector(2);


        RpCalcBasedGeomFactory factory = (RpCalcBasedGeomFactory) geometry.geomFactory();

        RpCalculation calc = factory.rpCalc();


        if (calc instanceof HugoniotCurveCalcND) {
            HugoniotCurveCalcND hCalc = (HugoniotCurveCalcND) calc;
            userInput = hCalc.getParams().getXZero();
        }


        if (calc instanceof PointLevelCalc) {
            PointLevelCalc hCalc = (PointLevelCalc) calc;
            userInput = hCalc.getStartPoint();

        }

        if (calc instanceof OrbitCalc) {

            OrbitCalc orbitCalc = (OrbitCalc) calc;

            userInput = orbitCalc.getStart();


        }


        Vector<Object> data = new Vector<Object>();

        NumberFormat formatter = NumberFormat.getInstance();
        formatter.setMaximumFractionDigits(4);
        data.add(true);
        data.add(geometryName);
        String userInputString = "";
        for (int i = 0; i < userInput.getSize(); i++) {
            userInputString = userInputString.concat(formatter.format(userInput.getElement(i)) + " ");

        }

        data.add(userInputString);
        data.add(geometry.viewingAttr().isVisible());

        tableModel_.addRow(data);

    }

    public static void clear() {
        int rowNumber = tableModel_.getRowCount();
        int i = 0;

        while (i < rowNumber) {
            tableModel_.removeRow(0);
            i++;
        }

    }

    public static void removeLastEntry() {

        if (tableModel_.getRowCount() >= 2) {
            tableModel_.removeRow(tableModel_.getRowCount() - 2);
        }

    }

    public void actionPerformed(ActionEvent e) {

        JButton button = (JButton) e.getSource();

        if (button.getName().equals("SelectAll")) {


            for (int i = 0; i < curvesTable_.getModel().getRowCount(); i++) {

                curvesTable_.setValueAt(new Boolean(true), i, 0);

            }


        }

        if (button.getName().equals("SelectNone")) {

            for (int i = 0; i < curvesTable_.getModel().getRowCount(); i++) {

                curvesTable_.setValueAt(new Boolean(false), i, 0);

            }
        }



        if (button.getName().equals("Invisible")) {

            for (int i = 0; i < curvesTable_.getModel().getRowCount(); i++) {

                Boolean selected = (Boolean) curvesTable_.getValueAt(i, 0);

                if (selected) {
                    //RPnDataModule.PHASESPACE.displayGeometry(i,false);
                    RPnDataModule.PHASESPACE.hideGeometry(i);
                    curvesTable_.setValueAt(false, i, 3);
                }


            }

        }

        if (button.getName().equals("Visible")) {


            for (int i = 0; i < curvesTable_.getModel().getRowCount(); i++) {

                Boolean selected = (Boolean) curvesTable_.getValueAt(i, 0);

                if (selected) {
                    //RPnDataModule.PHASESPACE.displayGeometry(i,true);
                    RPnDataModule.PHASESPACE.displayGeometry(i);
                    curvesTable_.setValueAt(true, i, 3);
                }


            }

        }


        if (button.getName().equals("Remove")) {
            int count = tableModel_.getRowCount();
            int index = 0;

            while (index < count) {
                boolean selected = (Boolean) tableModel_.getValueAt(index, 0);
                if (selected) {
                    tableModel_.removeRow(index);
                    RPnDataModule.PHASESPACE.remove(index);
                    index = 0;
                    count = tableModel_.getRowCount();
                } else {
                    index++;
                }

            }

        }

    }
}
