/*
 *
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Observable;
import java.util.Vector;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.WindowConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import rpn.command.CurveSelectionCommand;
import rpn.component.BifurcationCurveBranchGeom;
import rpn.component.BifurcationCurveGeom;
import rpn.component.BifurcationCurveGeomSide;
import rpn.component.RpCalcBasedGeomFactory;
import rpn.component.RpGeometry;
import rpn.controller.ui.UIController;
import rpn.parser.RPnDataModule;
import rpnumerics.HugoniotCurve;
import rpnumerics.HugoniotCurveCalcND;
import rpnumerics.OrbitCalc;
import rpnumerics.EigenValuePointLevelCalc;
import rpnumerics.RPnCurve;
import rpnumerics.RarefactionExtensionCalc;
import rpnumerics.RpCalculation;
import rpnumerics.StationaryPoint;
import rpnumerics.StationaryPointCalc;
import wave.multid.model.MultiGeometry;
import wave.util.RealVector;

public class RPnCurvesList extends Observable implements ActionListener, ListSelectionListener, TableModelListener, MouseListener {

    private JScrollPane tablePanel_;
    private JToolBar toolBar_;
    private JTable curvesTable_;
    private JButton selectNoneButton_, selectAllButton_, removeButton_,
            removeAreasButton_, setVisibleButton_;
    private DefaultTableModel tableModel_;
    private RPnPhaseSpaceAbstraction phaseSpace_;
    private JFrame frame_;
    private List<RpGeometry> selectedGeometries_;
    private List indexGeometries;
    private List<RpGeometry> geometryList_;
    private ArrayList<RpGeometry> geometriesToRemove_;

    public RPnCurvesList(String title, RPnPhaseSpaceAbstraction phaseSpace) {

        frame_ = new JFrame(title);

        phaseSpace_ = phaseSpace;

        selectedGeometries_ = new ArrayList();
        geometryList_ = new ArrayList();

        frame_.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

        frame_.setSize(new Dimension(600, 250));
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();

        int height = dim.height;
        int width = dim.width;

        frame_.setLocation((int) (width - (width * .55)), (int) (height - (height * .35)));

        tableModel_ = new RPnCurvesTableModel(phaseSpace);
        tableModel_.addTableModelListener(this);

        toolBar_ = new JToolBar();

        curvesTable_ = new JTable(tableModel_);

        curvesTable_.setRowSelectionAllowed(true);
        curvesTable_.setSelectionMode(javax.swing.ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        curvesTable_.getSelectionModel().addListSelectionListener(this);

        tablePanel_ = new JScrollPane(curvesTable_);

        selectNoneButton_ = new JButton("Select None");
        selectNoneButton_.setName("SelectNone");

        selectAllButton_ = new JButton("Select All");
        selectAllButton_.setName("SelectAll");

        removeButton_ = new JButton("Remove");
        removeButton_.setName("Remove");

        removeAreasButton_ = new JButton("Remove All Areas");
        removeAreasButton_.setName("RemoveAreas");

        setVisibleButton_ = new JButton("Visible");
        setVisibleButton_.setName("Visible");

        setVisibleButton_.addActionListener(this);

        selectNoneButton_.addActionListener(this);
        selectAllButton_.addActionListener(this);

        removeButton_.addActionListener(this);
        removeAreasButton_.addActionListener(this);

        toolBar_.add(selectNoneButton_);
        toolBar_.add(selectAllButton_);

        toolBar_.add(removeButton_);
        toolBar_.add(removeAreasButton_);
        toolBar_.add(setVisibleButton_);

        toolBar_.setFloatable(false);

        curvesTable_.getColumnModel().getColumn(0).setPreferredWidth(20);
        curvesTable_.getColumnModel().getColumn(1).setPreferredWidth(80);
        curvesTable_.addMouseListener(this);

        curvesTable_.getTableHeader().setReorderingAllowed(false);

        tablePanel_.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        frame_.getContentPane().setLayout(new BorderLayout());
        frame_.getContentPane().add(tablePanel_, BorderLayout.CENTER);
        frame_.getContentPane().add(toolBar_, BorderLayout.NORTH);

        curvesTable_.getColumnModel().getColumn(2).setCellEditor(new DefaultCellEditor(new JCheckBox()));
        curvesTable_.getColumnModel().getColumn(3).setCellEditor(new DefaultCellEditor(new JCheckBox()));

        JComboBox comboBox = new JComboBox();

        comboBox.addItem("LEFTRIGHT");
        comboBox.addItem("RIGHTLEFT");
        comboBox.addItem("NONE");
        comboBox.setSelectedIndex(2);

        curvesTable_.getColumnModel().getColumn(4).setCellEditor(new DefaultCellEditor(comboBox));

    }

    public void removeGeometrySide(MultiGeometry geometry) {

        if (geometry instanceof BifurcationCurveGeomSide) {
            BifurcationCurveGeomSide bifurcationGeom = (BifurcationCurveGeomSide) geometry;
            phaseSpace_.remove(bifurcationGeom.getOtherSide());

        }

        if (geometry instanceof BifurcationCurveGeom) {
            BifurcationCurveGeom branch = (BifurcationCurveGeom) geometry;
            for (BifurcationCurveBranchGeom rpGeometry : branch.getBifurcationListGeom()) {
                removeGeometrySide(rpGeometry);
            }

        }

    }

    //**** alteracao do metodo original para testar (Leandro)
    public void addGeometry(RpGeometry geometry) {

        RealVector userInput = new RealVector(2);
        String geometryName = "Poincare";

        int curveID = 1000;
        if (geometry.geomFactory() instanceof RpCalcBasedGeomFactory) {
            RpCalcBasedGeomFactory factory = (RpCalcBasedGeomFactory) geometry.geomFactory();

            geometryName = factory.geomSource().getClass().getSimpleName();
            RpCalculation calc = factory.rpCalc();

            RPnCurve curveToID = (RPnCurve) factory.geomSource();

            if (calc instanceof HugoniotCurveCalcND) {
                HugoniotCurve curve = (HugoniotCurve) factory.geomSource();
                curveID = curveToID.getId();
                userInput = curve.getXZero().getCoords();
            }

            if (calc instanceof EigenValuePointLevelCalc) {
                EigenValuePointLevelCalc hCalc = (EigenValuePointLevelCalc) calc;
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

            if (calc instanceof StationaryPointCalc) {
                StationaryPoint point = (StationaryPoint) factory.geomSource();
                userInput = point.getCoords();
            }

        }

        Vector<Object> data = new Vector<Object>();

        NumberFormat formatter = NumberFormat.getInstance();
        formatter.setMaximumFractionDigits(4);

        data.add(geometryName + " curve ID: " + curveID);
        String userInputString = "";
        for (int i = 0; i < userInput.getSize(); i++) {
            userInputString = userInputString.concat(formatter.format(userInput.getElement(i)) + " ");

        }

        data.add(userInputString);
        data.add(geometry.isVisible());
        data.add(geometry.isSelected());

        data.add("NONE");

        tableModel_.addRow(data);

    }

    //**************************************************************************
    public void clear() {
        int rowNumber = tableModel_.getRowCount();
        int i = 0;

        while (i < rowNumber) {
            tableModel_.removeRow(0);
            i++;
        }

    }

    public void removeStrings() {

        List claStringToRemove = new ArrayList();
        List velStringToRemove = new ArrayList();
        List claToRemove = new ArrayList();
        List velToRemove = new ArrayList();

        for (MultiGeometry multiGeometry : selectedGeometries_) {
            RpGeometry geometry = ((RpGeometry) multiGeometry).geomFactory().geom();
            RPnCurve curve = (RPnCurve) geometry.geomFactory().geomSource();

            System.out.println("VERIFICANDO curve.claStringToRemove.size() ::: " + curve.claStringToRemove.size());

            claStringToRemove.addAll(curve.claStringToRemove);
            velStringToRemove.addAll(curve.velStringToRemove);
            claToRemove.addAll(curve.claToRemove);
            velToRemove.addAll(curve.velToRemove);

            curve.claToRemove.clear();
            curve.velToRemove.clear();
            curve.claStringToRemove.clear();
            curve.velStringToRemove.clear();
        }

        System.out.println("RPnCurvesList : linha string para remover --- " + claToRemove.size());
        System.out.println("RPnCurvesList : linha vel para remover    --- " + velToRemove.size());
        System.out.println("RPnCurvesList : claString para remover    --- " + claStringToRemove.size());
        System.out.println("RPnCurvesList : velString para remover    --- " + velStringToRemove.size());

        Iterator<RPnPhaseSpacePanel> phaseSpacePanelIterator = UIController.instance().getInstalledPanelsIterator();
        while (phaseSpacePanelIterator.hasNext()) {
            RPnPhaseSpacePanel panel = phaseSpacePanelIterator.next();

            if (panel.getName().equals("Phase Space")) {
                // *** sem fazer estas remocoes, o programa erra quando volta a classificar (depois de alguma remocao de strings)
                //panel.getCastedUI().getTypeString().removeAll(claStringToRemove);
                //panel.getCastedUI().getVelocityString().removeAll(velStringToRemove);
                // ***
                panel.getGraphicsUtil().removeAll(claToRemove);
                panel.getGraphicsUtil().removeAll(velToRemove);

                System.out.println("getTypeString().size() antes de remover : " + panel.getCastedUI().getTypeString().size());

                panel.getCastedUI().getTypeString().removeAll(claStringToRemove);

                System.out.println("getTypeString().size() depois de remover : " + panel.getCastedUI().getTypeString().size());

                panel.getCastedUI().getVelocityString().removeAll(velStringToRemove);
                panel.repaint();
            }

        }

    }

    @Override
    public void actionPerformed(ActionEvent e) {

        System.out.println("Comando: " + e.paramString());

        if (e.getSource() instanceof JButton) {
            JButton button = (JButton) e.getSource();

            if (button.getName().equals("SelectAll")) {
                curvesTable_.selectAll();
            }

            if (button.getName().equals("SelectNone")) {
                curvesTable_.clearSelection();
            }

            if (button.getName().equals("RemoveAreas")) {
                UIController.instance().clearAllAreas();
            }

            if (button.getName().equals("Visible")) {

                for (Object selectedIndex : indexGeometries) {
                    Integer index = (Integer) selectedIndex;
                    curvesTable_.getModel().setValueAt(true, index, 2);
                }

            }

            if (button.getName().equals("Remove")) {

                // --- Por enquanto, remove todas as strings do painel onde está a curva selecionada
                String panelName = panelName(frame_.getTitle());
                Iterator<RPnPhaseSpacePanel> phaseSpacePanelIterator = UIController.instance().getInstalledPanelsIterator();

                while (phaseSpacePanelIterator.hasNext()) {
                    RPnPhaseSpacePanel rPnPhaseSpacePanel = phaseSpacePanelIterator.next();
                    if (rPnPhaseSpacePanel.getName().equals(panelName)) {
                        rPnPhaseSpacePanel.clearAllStrings();
                    }
                }
                // ---------------------------------------------------------------------------------                

                for (RpGeometry geometry : geometriesToRemove_) {
                    UIController.instance().getSelectedGeometriesList().remove(geometry);
                    RPnPhaseSpaceManager.instance().remove(phaseSpace_, geometry);

                }

            }

        }

    }

    private String panelName(String frameName) {
        String panelName = "";

        if (frameName.equals("Main")) {
            panelName = RPnDataModule.PHASESPACE.getName();
        }
        if (frameName.equals("Right")) {
            panelName = RPnDataModule.RIGHTPHASESPACE.getName();
        }
        if (frameName.equals("Left")) {
            panelName = RPnDataModule.LEFTPHASESPACE.getName();
        }

        return panelName;
    }

    //*** metodo original
    public void update() {
        clear();
        geometryList_.clear();
        Iterator iterator = phaseSpace_.getGeomObjIterator();

        while (iterator.hasNext()) {
            RpGeometry geometry = (RpGeometry) iterator.next();
            addGeometry(geometry);
            geometryList_.add(geometry);
        }

        setChanged();
        notifyObservers();

    }

    public RPnPhaseSpaceAbstraction getPhaseSpace() {
        return phaseSpace_;
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {

        geometriesToRemove_ = new ArrayList();
        indexGeometries = new ArrayList();

        ListSelectionModel listSelectionModel = (ListSelectionModel) e.getSource();

        if (!e.getValueIsAdjusting()) {

            int minIndex = listSelectionModel.getMinSelectionIndex();
            int maxIndex = listSelectionModel.getMaxSelectionIndex();

            for (int i = minIndex; i <= maxIndex; i++) {
                if (listSelectionModel.isSelectedIndex(i)) {
                    int index = 0;
                    Iterator it = phaseSpace_.getGeomObjIterator();
                    while (it.hasNext()) {
                        RpGeometry geometry = (RpGeometry) it.next();
                        if (index == i) {
                            geometriesToRemove_.add(geometry);
                            indexGeometries.add(index);
                        }
                        index++;
                    }

                }

            }

        }

    }

    void setVisible(boolean show) {
        frame_.setVisible(show);
    }

    @Override
    public void tableChanged(TableModelEvent e) {

        int row = e.getLastRow();
        int column = e.getColumn();
        TableModel model = (TableModel) e.getSource();
        if (e.getType() == TableModelEvent.UPDATE) {
            Object data = model.getValueAt(row, column);
            RpGeometry geometry = geometryList_.get(row);

            switch (column) {
                case 2:

                    Boolean visible = (Boolean) data;
                    geometry.setVisible(visible);
                    setChanged();
                    notifyObservers();

                    break;
                case 3:
                    Boolean selected = (Boolean) data;
                    geometry.setSelected(selected);

                    if (geometry.isSelected()) {
                        UIController.instance().getSelectedGeometriesList().add(geometry);

                    } else {
                        UIController.instance().getSelectedGeometriesList().remove(geometry);
                        curvesTable_.setValueAt(true, row, 2);

                    }

                    RPnCurve curve = (RPnCurve) geometry.geomFactory().geomSource();

                    CurveSelectionCommand.instance().setCurveToSelect(curve.getId());

                    CurveSelectionCommand.instance().execute();

                    setChanged();
                    notifyObservers();

                    break;
                default:

            }

        }

    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

}
