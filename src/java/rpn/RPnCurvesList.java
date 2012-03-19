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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Vector;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.ScrollPaneConstants;
import javax.swing.WindowConstants;
import javax.swing.table.DefaultTableModel;
import rpn.component.BifurcationCurveGeom;
import rpn.component.RpCalcBasedGeomFactory;
import rpn.component.RpGeometry;
import rpn.controller.ui.UIController;
import rpnumerics.HugoniotCurve;
import rpnumerics.HugoniotCurveCalcND;
import rpnumerics.OrbitCalc;
import rpnumerics.PointLevelCalc;
import rpnumerics.RarefactionExtensionCalc;
import rpnumerics.RpCalculation;
import wave.multid.model.MultiGeometry;
import wave.util.RealVector;

public class RPnCurvesList extends JFrame implements ActionListener {

    private JScrollPane tablePanel_;
    private JToolBar toolBar_;
    private JTable curvesTable_;
    private JButton selectNoneButton_, selectAllButton_, invisibleButton_, visibleButton_, removeButton_;
    private DefaultTableModel tableModel_;
    private RPnPhaseSpaceAbstraction phaseSpace_;

    public RPnCurvesList(String title, RPnPhaseSpaceAbstraction phaseSpace) {
        super(title);

        phaseSpace_ = phaseSpace;


        this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

        setSize(new Dimension(600, 250));
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();

        int height = dim.height;
        int width = dim.width;

        this.setLocation((int) (width - (width * .55)), (int) (height - (height * .35)));

        tableModel_ = new RPnCurvesTableModel();


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

    public void removeGeometrySide(MultiGeometry geometry) {
        if (geometry instanceof BifurcationCurveGeom) {
            BifurcationCurveGeom bifurcationGeom = (BifurcationCurveGeom) geometry;
            phaseSpace_.remove(bifurcationGeom.getOtherSide());

        }

    }

    public void addGeometry(RpGeometry geometry) {

        RealVector userInput = new RealVector(2);


        RpCalcBasedGeomFactory factory = (RpCalcBasedGeomFactory) geometry.geomFactory();

        String geometryName = factory.geomSource().getClass().getSimpleName();


        RpCalculation calc = factory.rpCalc();


        if (calc instanceof HugoniotCurveCalcND) {
            HugoniotCurve curve = (HugoniotCurve) factory.geomSource();
            userInput = curve.getXZero().getCoords();
        }


        if (calc instanceof PointLevelCalc) {
            PointLevelCalc hCalc = (PointLevelCalc) calc;
            userInput = hCalc.getStartPoint();
        }

        if (calc instanceof OrbitCalc) {
            OrbitCalc orbitCalc = (OrbitCalc) calc;
            userInput = orbitCalc.getStart();

        }


        if (calc instanceof RarefactionExtensionCalc) {
            RarefactionExtensionCalc rarCalc = (RarefactionExtensionCalc) calc;
            userInput = rarCalc.getStart();
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

    public void clear() {
        int rowNumber = tableModel_.getRowCount();
        int i = 0;

        while (i < rowNumber) {
            tableModel_.removeRow(0);
            i++;
        }

    }

    public void actionPerformed(ActionEvent e) {

        if (e.getSource() instanceof JButton) {
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
                setGeometryVisible(false);
            }
            if (button.getName().equals("Visible")) {
                setGeometryVisible(true);
            }


            if (button.getName().equals("Remove")) {
                int index = 0;
                boolean selected;
                Iterator it = phaseSpace_.getGeomObjIterator();
                ArrayList<MultiGeometry> toBeRemoved = new ArrayList<MultiGeometry>();

                while (it.hasNext()) {
                    selected = (Boolean) tableModel_.getValueAt(index, 0);
                    MultiGeometry multiGeometry = (MultiGeometry) it.next();

                    if (selected) {
                        toBeRemoved.add(multiGeometry);
                    }
                    index++;
                }
                for (MultiGeometry multiGeometry : toBeRemoved) {
                    RPnPhaseSpaceManager.instance().remove(phaseSpace_, multiGeometry);
                }
            }

        }


    }

    public void update() {
        clear();
        Iterator iterator = phaseSpace_.getGeomObjIterator();
        while (iterator.hasNext()) {
            addGeometry((RpGeometry) iterator.next());
        }

    }

    private void setGeometryVisible(boolean visible) {
        int index = 0;
        boolean selected;
        Iterator it = phaseSpace_.getGeomObjIterator();
        while (it.hasNext()) {
            selected = (Boolean) curvesTable_.getValueAt(index, 0);
            MultiGeometry multiGeometry = (MultiGeometry) it.next();

            if (selected) {
                multiGeometry.viewingAttr().setVisible(visible);
                curvesTable_.setValueAt(visible, index, 3);
            }
            index++;
        }
        UIController.instance().panelsUpdate();
    }
}
