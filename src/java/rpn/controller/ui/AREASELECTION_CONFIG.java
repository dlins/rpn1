/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.controller.ui;

import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Iterator;
import rpn.RPnPhaseSpacePanel;
import rpn.command.DomainSelectionCommand;
import rpn.command.RpModelActionCommand;



public class AREASELECTION_CONFIG extends UI_ACTION_SELECTED {

    public AREASELECTION_CONFIG(RpModelActionCommand command) {
        super(DomainSelectionCommand.instance());

            Iterator<RPnPhaseSpacePanel> iterator = UIController.instance().getInstalledPanelsIterator();
            while (iterator.hasNext()) {
                RPnPhaseSpacePanel panel = iterator.next();
                
                
                MouseMotionListener[] mouseMotionArray = (MouseMotionListener[]) panel.getListeners(MouseMotionListener.class);
                MouseListener[] mouseListenerArray = (MouseListener[]) panel.getListeners(MouseListener.class);
                
                for (MouseListener mouseListener : mouseListenerArray) {
                    
                    if (mouseListener instanceof RPn2DMouseController) {
                        panel.removeMouseListener(mouseListener);
                        
                        
                    }
                }
                
                for (MouseMotionListener mouseMotionListener : mouseMotionArray) {
                    
                    if (mouseMotionListener instanceof RPn2DMouseController) {
                        panel.removeMouseMotionListener(mouseMotionListener);
                    }
                    
                }
                
            }
            
        }
        
        
        
        
       
        
        
        
        
        
        
        
        
        
        

    }



 