/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.command;

import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.Iterator;
import javax.swing.JButton;
import rpn.RPnMenuCommand;
import rpn.ui.diagram.RPnDiagramFrame;
import rpn.RPnPhaseSpacePanel;
import rpn.component.*;
import rpn.controller.phasespace.riemannprofile.RiemannProfileReady;
import rpn.controller.phasespace.riemannprofile.RiemannProfileState;
import rpn.controller.phasespace.riemannprofile.RiemannProfileWaitState;
import rpn.controller.ui.UIController;
import rpn.controller.ui.UI_ACTION_SELECTED;
import rpn.parser.RPnDataModule;
import rpnumerics.*;
import wave.multid.view.ViewingTransform;
import wave.util.RealVector;

public class RiemannProfileCommand extends RpModelPlotCommand implements RPnMenuCommand, WindowListener {
    //
    // Constants
    //

    static public final String DESC_TEXT = "Riemann Profile";
    //
    // Members
    //
    static private RiemannProfileCommand instance_ = null;

    private RiemannProfileState state_;

    private RPnDiagramFrame riemannFrame_;

    //
    // Constructors/Initializers
    //
    protected RiemannProfileCommand() {
        super(DESC_TEXT, null, new JButton());
        state_ = new RiemannProfileWaitState();
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        UI_ACTION_SELECTED action = new UI_ACTION_SELECTED(this);
        action.userInputComplete(UIController.instance());// No input needed
        setEnabled(false);

    }

    public RpGeometry createRpGeometry(RealVector[] input) {
        return null;
    }

    static public RiemannProfileCommand instance() {
        if (instance_ == null) {
            instance_ = new RiemannProfileCommand();

        }
        return instance_;
    }

    public RiemannProfileState getState() {
        return state_;

    }

   

   public void updateRiemannFrame(){
    
       riemannFrame_.phaseSpacePanel().repaint();
       
   }

    

    @Override
    public void execute() {

        RiemannProfileReady state = (RiemannProfileReady) state_;

        String Xlimits[] = RPNUMERICS.getParamValue("riemannprofile", "speedrange").split(" ");

        String Ylimits[] = RPNUMERICS.getParamValue("riemannprofile", "Yrange").split(" ");

        RealVector min = new RealVector(Xlimits[0] + " " + Ylimits[0]);
        RealVector max = new RealVector(Xlimits[1] + " " + Ylimits[1]);
        
        
        
        DiagramGeom diagram = state.calcProfile();
        
        
        Iterator<RPnPhaseSpacePanel> installedPanelsIterator = UIController.instance().getInstalledPanelsIterator();
        
        ViewingTransform transform =null;
        
        while (installedPanelsIterator.hasNext()) {
            RPnPhaseSpacePanel rPnPhaseSpacePanel = installedPanelsIterator.next();
            transform = rPnPhaseSpacePanel.scene().getViewingTransform();
            
        }
        
        
        
        
        diagram.setRelater(new RiemannDiagramRelater(transform));

        RPnDataModule.RIEMANNPHASESPACE.join(diagram);
        
        String[] fieldNames = new String[RPNUMERICS.domainDim()];
        
        for (int i = 0; i < fieldNames.length; i++) {
            fieldNames[i]=String.valueOf(i);
            
        }
          
        riemannFrame_= new RPnDiagramFrame(RPnDataModule.RIEMANNPHASESPACE,"speed",fieldNames,this);
        
        riemannFrame_.updateScene(min,max);
        
        riemannFrame_.setVisible(true);
 
    }


    

    @Override
    public void finalizeApplication() {
        DomainSelectionCommand.instance().getContainer().setSelected(false);
        DomainSelectionCommand.instance().setEnabled(false);
        RiemannProfileCommand.instance().setEnabled(false);
        state_ = new RiemannProfileWaitState();
        UIController.instance().resetCursor();
        UIController.instance().globalInputTable().reset();

        riemannFrame_.dispose();

    }

    @Override
    public void networkCommand() {

    }

    @Override
    public void windowOpened(WindowEvent e) {

    }

    @Override
    public void windowClosing(WindowEvent e) {

        RPnDataModule.RIEMANNPHASESPACE.clear();

        RiemannResetCommand.instance().execute();

    }

    @Override
    public void windowClosed(WindowEvent e) {

    }

    @Override
    public void windowIconified(WindowEvent e) {

    }

    @Override
    public void windowDeiconified(WindowEvent e) {

    }

    @Override
    public void windowActivated(WindowEvent e) {

    }

    @Override
    public void windowDeactivated(WindowEvent e) {

    }

    public void changeState(RiemannProfileState newState) {

        state_ = newState;

    }

}
