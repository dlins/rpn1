/*
 *
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpn.parser;

import java.util.HashMap;

public class VisualizationProfile {

    
    private int [] projectionAxis_;
    private int [] viewPort_;
    private boolean isoEquiTransform_;

    public int[] getProjectionAxis() {
        return projectionAxis_;
    }

    public void setProjectionAxis(int[] projectionAxis_) {
        this.projectionAxis_ = projectionAxis_;
    }

    public int[] getViewPort() {
        return viewPort_;
    }

    public void setViewPort(int[] viewPort_) {
        this.viewPort_ = viewPort_;
    }

    public boolean isIsoEquiTransform() {
        return isoEquiTransform_;
    }

    public void setIsoEquiTransform(boolean isoEquiTransform_) {
        this.isoEquiTransform_ = isoEquiTransform_;
    }
    

    
    
    
   
}
