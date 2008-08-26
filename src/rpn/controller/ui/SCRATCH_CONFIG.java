/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */

package rpn.controller.ui;

import java.awt.Point;
import javax.swing.JPanel;
import rpn.usecase.ChangeXZeroAgent;
import wave.multid.Coords2D;
import wave.multid.view.ViewingTransform;
import wave.util.RealVector;


public class SCRATCH_CONFIG extends UI_ACTION_SELECTED {
    

    
    private JPanel selectedPanel_;
    
    public SCRATCH_CONFIG() {
        
        super(ChangeXZeroAgent.instance());
        
    }
    
    /**
     * When two rpn instances communicates between the network pointmarks are added in the panel.
     *This points are sended as world coords by the master and transformed to dc coords by others .
     *PointMarks are not geometries so they are erased each time the scene is updated. 
     */
    
    public void userInputComplete(rpn.controller.ui.UIController ui,
            RealVector userInput) {
        
        
        if (!ui.getNetStatusHandler().isMaster()){
            ViewingTransform transf=  ui.getFocusPanel().scene().getViewingTransform();
            
            Coords2D wcCoords = new Coords2D(userInput.getElement(0),userInput.getElement(1));
            
            Coords2D dcCoords = new Coords2D();
            
            transf.viewPlaneTransform(wcCoords, dcCoords);
            
            Point point = new Point((int)dcCoords.getX(),(int)dcCoords.getY());
            
            ui.getFocusPanel().getCastedUI().pointMarkBuffer().add
                    (point); 

            
        }
        ui.getFocusPanel().repaint();
    }
    
}
