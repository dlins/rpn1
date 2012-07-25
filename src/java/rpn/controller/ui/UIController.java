/*
 *
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.controller.ui;

import rpn.RPnPhaseSpaceAbstraction;
import rpn.usecase.*;
import rpn.RPnPhaseSpacePanel;
import wave.multid.Coords2D;
import wave.multid.CoordsArray;
import wave.util.RealVector;
import java.util.ArrayList;
import java.util.List;
import java.awt.Point;
import javax.swing.plaf.ComponentUI;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseEvent;
import java.awt.Cursor;
import java.beans.PropertyChangeEvent;
import rpn.controller.*;
import java.net.*;
import java.util.Iterator;
import rpn.RPnDesktopPlotter;
import rpn.RPnUIFrame;
import rpn.component.RpGeometry;
import rpn.component.util.GeometryGraph;
import rpn.component.util.GeometryGraphND;
import rpn.message.*;
import rpn.parser.RPnDataModule;
import rpnumerics.RPnCurve;

/** This class implements a general controller to the application. With the UIController class, the state of the application is changed, the controllers of each panel are installed or removed and the user inputs are stored in a global table. */
public class UIController extends ComponentUI {
    //
    // Constants
    //

    static public final Cursor WAIT_CURSOR = Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR);
    static public final Cursor DEFAULT_CURSOR = Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR);
    //
    // Members
    //
    private rpn.controller.ui.UserInputHandler handler_;
    private List installedPanels_;
    private MouseController mouseController_;
    private MouseMotionController mouseMotionController_;
    private rpn.controller.ui.UserInputTable globalInputTable_;
    private static UIController instance_ = null;
    private RPnNetworkStatus netStatus_ = null;
    private String clientID_;
    private RPnPhaseSpacePanel focusPanel_;
    private StateInputController stateController_;
    public static UI_ACTION_SELECTED INITSTATE = null;
    private ArrayList<Command> commandArray_;
    private boolean auxPanelsEnabled_;

    //
    // Constructors
    //
    protected UIController() {

        mouseMotionController_ = new MouseMotionController();
        stateController_ = new StateInputController(RPnDesktopPlotter.getUIFrame());

        installedPanels_ = new ArrayList();
        mouseController_ = new MouseController();
        globalInputTable_ = new UserInputTable(rpnumerics.RPNUMERICS.domainDim());

        commandArray_ = new ArrayList<Command>();
        handler_ = new RAREFACTION_CONFIG();
        auxPanelsEnabled_ = true;

        initNetStatus();

    }

    private void initNetStatus() {

        try {
            InetAddress ip_ = InetAddress.getLocalHost();
            String from2_ = ip_.getHostAddress();
            clientID_ = from2_.replace('.', '_');
            netStatus_ = new RPnNetworkStatus(clientID_);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void toggleCursorLines() {

        if (RPnPhaseSpacePanel.isShowCursor()) {
            RPnPhaseSpacePanel.setShowCursor(false);

        } else {
            RPnPhaseSpacePanel.setShowCursor(true);
        }
        Iterator it = installedPanels_.iterator();

        while (it.hasNext()) {
            RPnPhaseSpacePanel panel = (RPnPhaseSpacePanel) it.next();
            panel.repaint();
        }

    }

    public void showCursorLines(boolean showCursor) {
        RPnPhaseSpacePanel.setCursorLineVisible(showCursor);
        Iterator it = installedPanels_.iterator();

        while (it.hasNext()) {
            RPnPhaseSpacePanel panel = (RPnPhaseSpacePanel) it.next();
            panel.repaint();
        }
    }

    public static UIController instance() {
        if (instance_ == null) {
            instance_ = new UIController();
            return instance_;
        }

        return instance_;
    }

  

    public void removeLastCommand() {
        commandArray_.remove(commandArray_.size() - 1);
    }

    public Iterator<Command> getCommandIterator() {
        return commandArray_.iterator();

    }

    public void setAuxPanels(boolean selected) {
        auxPanelsEnabled_ = selected;
    }

    public boolean isAuxPanelsEnabled() {
        return auxPanelsEnabled_;
    }

    //
    // Inner Classes
    //
    class MouseMotionController extends MouseMotionAdapter {


       

        @Override
        public void mouseDragged(MouseEvent event) {
            RPnUIFrame.clearStatusMessage();

            if (event.getComponent() instanceof RPnPhaseSpacePanel) {
                RPnPhaseSpacePanel panel = (RPnPhaseSpacePanel) event.getComponent();

                //***  Permite que o input point de uma curva seja exatamente um ponto sobre outra curva
                if (GeometryGraphND.onCurve == 1) {
                    UserInputTable userInputList = UIController.instance().globalInputTable();
                    RealVector newValue = userInputList.values();
                    RPnPhaseSpaceAbstraction phaseSpace = RPnDataModule.PHASESPACE;
                    RpGeometry geom = phaseSpace.findClosestGeometry(newValue);


                    //RpGeometry geom = RPnPhaseSpaceAbstraction.findClosestGeometry(newValue);
                    RPnCurve curve = (RPnCurve)(geom.geomFactory().geomSource());
                    GeometryGraphND.pMarca = curve.findClosestPoint(newValue);

                    panel.repaint();
                }
                //***-----------------------------------------------------------------------------------

                // this will automatically work only for 2D(isComplete())
                updateUserInputTable(panel, event.getPoint());

                if (globalInputTable().isComplete()) {

                    globalInputTable().reset();
                    resetPanelsCursorCoords();


                    if (event.isShiftDown() && event.isControlDown()) {
                        userInputComplete(globalInputTable().values());
                    }

                    else
                    
                    if (event.isShiftDown()) {
                        GeometryGraph.count = 0;
                        userInputComplete(globalInputTable().values());
                        GeometryGraph.count = 0;
                    }
                    else {

                        if (handler_ instanceof UI_ACTION_SELECTED) {
                            UI_ACTION_SELECTED actionSelected = (UI_ACTION_SELECTED) handler_;
                            RpModelActionAgent action = (RpModelActionAgent) actionSelected.getAction();
                            action.setPhaseSpace((RPnPhaseSpaceAbstraction) panel.scene().getAbstractGeom());
                            DragPlotAgent.instance().setPhaseSpace((RPnPhaseSpaceAbstraction) panel.scene().getAbstractGeom());
                        }

                        DragPlotAgent.instance().execute();
                        
                    }
                }

            }
        }
    }

    class MouseController extends MouseAdapter {

        public MouseController() {
        }


       

        @Override
        public void mousePressed(MouseEvent event) {
            RPnUIFrame.clearStatusMessage();
            RPnUIFrame.disableSliders();

            if (event.getComponent() instanceof RPnPhaseSpacePanel) {
                
                RPnPhaseSpacePanel panel = (RPnPhaseSpacePanel) event.getComponent();

                RPnPhaseSpaceAbstraction.namePhaseSpace = ((RPnPhaseSpaceAbstraction) panel.scene().getAbstractGeom()).getName();   //** acrescentei isso (Leandro)
                panel.setName(RPnPhaseSpaceAbstraction.namePhaseSpace);

                if (netStatus_.isMaster() || !(netStatus_.isOnline())) {


                    int sceneDim = panel.scene().getViewingTransform().projectionMap().getDomain().getDim();
                    if (sceneDim == globalInputTable_.flags().length) {

                        updateUserInputTable(panel, event.getPoint());
                        evaluatePanelsCursorCoords(panel, event.getPoint());
                        // execute
                        if (globalInputTable().isComplete()) {
                            userInputComplete(globalInputTable().values());
                            globalInputTable().reset();
                            resetPanelsCursorCoords();
                            RPnUIFrame.enableSliders();
                        }

                    }

                }
            }

        }

        @Override
        public void mouseEntered(MouseEvent event) {

            if (event.getSource() instanceof RPnPhaseSpacePanel) {
                toggleCursorLines();
                RPnPhaseSpacePanel panel = (RPnPhaseSpacePanel) event.getComponent();

                RPnPhaseSpaceAbstraction.namePhaseSpace = ((RPnPhaseSpaceAbstraction) panel.scene().getAbstractGeom()).getName();   //** acrescentei isso (Leandro)
                panel.setName(RPnPhaseSpaceAbstraction.namePhaseSpace);

                if (handler_ instanceof UI_ACTION_SELECTED) {
                    UI_ACTION_SELECTED actionSelected = (UI_ACTION_SELECTED) handler_;
                    RpModelActionAgent action = (RpModelActionAgent) actionSelected.getAction();
                    action.setPhaseSpace((RPnPhaseSpaceAbstraction) panel.scene().getAbstractGeom());
                    DragPlotAgent.instance().setPhaseSpace((RPnPhaseSpaceAbstraction) panel.scene().getAbstractGeom());

                }

            }
        }

        @Override
        public void mouseExited(MouseEvent event) {
            if (event.getSource() instanceof RPnPhaseSpacePanel) {

                toggleCursorLines();
            }
        }
    }

    //
    // Accessors/Mutators
    //
    /** Returns the values entered by the user for a specific action. */
    public RealVector[] userInputList() {
        return handler_.userInputList(this);
    }

    /** Returns a table with all the points entered by the user. The application holds a table with all points entered by the user. This points are taked by mouse clicks in all panels .*/
    public rpn.controller.ui.UserInputTable globalInputTable() {
        return globalInputTable_;
    }

    //
    // Methods
    //
    /** This method installs a listener into a panel of application.*/
    public void install(RPnPhaseSpacePanel panel) {
        installedPanels_.add(panel);
        panel.addMouseListener(mouseController_);
        panel.addMouseMotionListener(mouseMotionController_);

    }

    /** This method removes  a listener of a  panel.*/
    public void uninstall(RPnPhaseSpacePanel panel) {
        installedPanels_.remove(panel);
        panel.removeMouseListener(mouseController_);
        panel.removeMouseMotionListener(mouseMotionController_);

    }

    /** This method removes all listeners .*/
    public void uninstallPanels() {
        for (int i = 0; i < installedPanels_.size(); i++) {
            RPnPhaseSpacePanel panel = (RPnPhaseSpacePanel) installedPanels_.get(i);
            panel.removeMouseListener(mouseController_);
            panel.removeMouseMotionListener(mouseMotionController_);
        }

    }

    public void installPanels() {
        for (int i = 0; i < installedPanels_.size(); i++) {
            RPnPhaseSpacePanel panel = (RPnPhaseSpacePanel) installedPanels_.get(i);
            panel.addMouseListener(mouseController_);
            panel.addMouseMotionListener(mouseMotionController_);
        }

    }

    /** Takes the coordinates of a clicked point in a panel and adds this coordinates in a buffer . Each panel has a buffer to store points entered by the user , this method add points in this buffer. The variables absComplete_ and ordComplete_ controls if the pair X/Y are taked correctely.*/
    protected void evaluatePanelsCursorCoords(RPnPhaseSpacePanel clickedPanel, Point point) {
        for (int i = 0; i < installedPanels_.size(); i++) {
            RPnPhaseSpacePanel panel = (RPnPhaseSpacePanel) installedPanels_.get(i);
            panel.getCastedUI().evaluateCursorCoords(clickedPanel, point);
        }
    }

    /** Sets the wait cursor to all panels .*/
    public void setWaitCursor() {
        for (int i = 0; i < installedPanels_.size(); i++) {
            RPnPhaseSpacePanel panel = (RPnPhaseSpacePanel) installedPanels_.get(i);
            panel.setCursor(WAIT_CURSOR);

        }
    }

    /** Sets de default cursor to all panels. */
    public void resetCursor() {
        for (int i = 0; i < installedPanels_.size(); i++) {
            RPnPhaseSpacePanel panel = (RPnPhaseSpacePanel) installedPanels_.get(i);
            panel.setCursor(DEFAULT_CURSOR);
        }
    }

    /** Updates the panels. This method is invoked when any atualization in the visuals objects shown by a panel is necessary. */
    public void panelsUpdate() {
        for (int i = 0; i < installedPanels_.size(); i++) {
            RPnPhaseSpacePanel panel = (RPnPhaseSpacePanel) installedPanels_.get(i);
            panel.invalidate();
            panel.repaint();
        }
    }

    /** This method sets the absComplete_ and  ordComplete_ variables to false. This forces a new data input. */
    protected void resetPanelsCursorCoords() {
        for (int i = 0; i < installedPanels_.size(); i++) {
            RPnPhaseSpacePanel panel = (RPnPhaseSpacePanel) installedPanels_.get(i);
            panel.getCastedUI().resetCursorCoords();
        }
    }

    /** Clear the list that holds the points entered by the user with mouse clicks .*/
    public void panelsBufferClear() {
        for (int i = 0; i < installedPanels_.size(); i++) {
            RPnPhaseSpacePanel panel = (RPnPhaseSpacePanel) installedPanels_.get(i);
            panel.getCastedUI().pointMarkBuffer().clear();
            panel.invalidate();
            panel.repaint();
        }
    }

    /** Do a specific action when all user inputs has been made. */
    public void userInputComplete(RealVector userInput) {
        // state dependent

        handler_.userInputComplete(this, userInput);

        if (netStatus_.isOnline()) {
            RPnActionMediator.instance().sendMessage(userInput);
        }

    }

    public void addCommand(Command command) {
        RPnUIFrame.clearStatusMessage();
        commandArray_.add(command);
    }

    /** Sets the state of the application. The application works as a state machine and this method changes the actual state.*/
    public void setState(rpn.controller.ui.UserInputHandler newAction) {
        stateController_.propertyChange(new PropertyChangeEvent(this, "aplication state", handler_, newAction));
        System.out.println(newAction.toString());

        if (handler_ instanceof UI_ACTION_SELECTED) {

            UI_ACTION_SELECTED currentSelection = (UI_ACTION_SELECTED) handler_;

            globalInputTable_ = new UserInputTable(currentSelection.actionDimension());

            if (newAction instanceof UI_ACTION_SELECTED) {
                UI_ACTION_SELECTED selectedAction = (UI_ACTION_SELECTED) newAction;
                // either unselect or new selection
                if (currentSelection.getAction() instanceof RpModelPlotAgent) {

                    ((RpModelPlotAgent) currentSelection.getAction()).getContainer().setSelected(false);

                }
                // Singletons !
                if (currentSelection.getAction() == selectedAction.getAction()) // unselect
                {
//                    setState(new GEOM_SELECTION());
                } else {
                    handler_ = newAction;
                }
            } else {
                handler_ = newAction;
                // TODO we should have a checkbox for menu actions
            }
        } else {
            handler_ = newAction;
        }


    }

    /** Updates the user input table. If the user input table is not completed yet this method adds a point to this table . */
    public void updateUserInputTable(RPnPhaseSpacePanel clickedPanel, Point point) {
        Coords2D dcPoint = new Coords2D(point.getX(), point.getY());
        CoordsArray wcProjectedPoint = rpn.component.MultidAdapter.createCoords();
        clickedPanel.scene().getViewingTransform().dcInverseTransform(dcPoint, wcProjectedPoint);
        // checks abs index
        if (!globalInputTable().isComplete(clickedPanel.getCastedUI().getAbsIndex())) {
            globalInputTable().setElement(clickedPanel.getCastedUI().getAbsIndex(),
                    wcProjectedPoint.getElement(clickedPanel.getCastedUI().getAbsIndex()));
        }
        // checks ord index
        if (!globalInputTable().isComplete(clickedPanel.getCastedUI().getOrdIndex())) {
            globalInputTable().setElement(clickedPanel.getCastedUI().getOrdIndex(),
                    wcProjectedPoint.getElement(clickedPanel.getCastedUI().getOrdIndex()));
        }
        if (clickedPanel.getCastedUI() instanceof PhaseSpacePanel3DController) {
            PhaseSpacePanel3DController panel3DUI = (PhaseSpacePanel3DController) clickedPanel.getCastedUI();
            if (!(globalInputTable().isComplete(panel3DUI.getDepthIndex()))) {
                globalInputTable().setElement(panel3DUI.getDepthIndex(), PhaseSpacePanel3DController.userDepthInput());
            }
        }
    }

    /** This method converts all user inputs to a RealVector array. */
    static public RealVector[] inputConvertion(List userInputList) {
        // coords type convertion
        RealVector[] coords = new RealVector[userInputList.size()];
        for (int i = 0; i < userInputList.size(); i++) {
            coords[i] = (RealVector) userInputList.get(i);
        }
        return coords;
    }

    public UserInputHandler getState() {
        return handler_;
    }

    public RPnNetworkStatus getNetStatusHandler() {
        return netStatus_;
    }

    /**
     * @deprecated
     *
     *
     */
    public void setFocusPanel(RPnPhaseSpacePanel phaseSpacePanel) {
        focusPanel_ = phaseSpacePanel;

    }

    /**
     * @deprecated
     *
     */
    public RPnPhaseSpacePanel getFocusPanel() {
        return focusPanel_;
    }

    public void setStateController(StateInputController stateController) {
        stateController_ = stateController;
    }
}
