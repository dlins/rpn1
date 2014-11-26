/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rpn.component;

import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import rpn.controller.ui.UIController;

/**
 *
 * @author edsonlan
 */
public class CurveFlasher extends TimerTask {

   private RpGeometry geometry_;


    public CurveFlasher(RpGeometry rpGeometry) {
        geometry_ = rpGeometry;
//        visibleOrNot_ = geometry_.isVisible();
    }

    
    @Override
    public void run() {

        if (UIController.instance().getSelectedGeometriesList().contains(geometry_)) {
            try {
                if (geometry_.isVisible()) {
                    geometry_.setVisible(false);
                } else {
                    geometry_.setVisible(true);
                }
                Thread.sleep(500);

            } 
            catch (InterruptedException ex) {
                Logger.getLogger(CurveFlasher.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        } else {
            cancel();

            geometry_.setVisible(true);



        }
        
        UIController.instance().panelsUpdate();

    }
    
}
