/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rpn.component;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import rpn.RPnPhaseSpacePanel;
import rpn.controller.ui.UIController;

/**
 *
 * @author edsonlan
 */
public class CurveFlasher extends TimerTask {
    
    List<RpGeometry> geometryList_;
    Timer timer_;
    
    public CurveFlasher(Timer t) {
        
        
        geometryList_ = new Vector();
        
    }
    
    public void setList(List<RpGeometry> geometryList) {
        geometryList_ = new Vector(geometryList);
        
    }
    
    @Override
    public void run() {
        
        if (geometryList_.isEmpty()) {
            Iterator geomObjIterator = UIController.instance().getActivePhaseSpace().getGeomObjIterator();
            while (geomObjIterator.hasNext()) {
                Object object = geomObjIterator.next();
                RpGeometry geometry = (RpGeometry) object;
                if (geometry.isVisible()) {
                    geometry.setVisible(true);
                } else {
                    geometry.setVisible(false);
                }
            }
            
            UIController.instance().panelsUpdate();
            

            return;
        }
        
        
        
        for (int i = 0; i < 3; i++) {
            try {
                Iterator<RPnPhaseSpacePanel> installedPanelsIterator = UIController.instance().getInstalledPanelsIterator();
                for (RpGeometry rpGeometry : geometryList_) {
                    if (rpGeometry.isVisible()) {
                        rpGeometry.setVisible(false);
                    } else {
                        rpGeometry.setVisible(true);
                    }
                    Thread.sleep(500);
                    
                    while (installedPanelsIterator.hasNext()) {
                        RPnPhaseSpacePanel rPnPhaseSpacePanel = installedPanelsIterator.next();
                        rPnPhaseSpacePanel.scene().update();
                        
                    }
                    
                    UIController.instance().panelsUpdate();
                }
            } catch (InterruptedException ex) {
                Logger.getLogger(CurveFlasher.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            
        }
        
    }
}
