/*
 *
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */

package rpn.component;

import java.util.List;
import wave.multid.CoordsArray;
import wave.multid.view.ViewingTransform;

public abstract class BifurcationCurveBranchGeom implements RpGeometry{
    
    public final static int LEFTRIGHT =0;
    public final static int RIGHTLEFT =1;
    public final static int NONE =2;
    
    private int correspondenceDirection_;

    public int getCorrespondenceDirection() {
        return correspondenceDirection_;
    }

    public void setCorrespondenceDirection(int correspondenceDirection) {
        correspondenceDirection_ = correspondenceDirection;
    }
    
    

    abstract List<BifurcationCurveBranchGeom> getBifurcationListGeom();

    public abstract void showCorrespondentPoint(CoordsArray coordsWC, ViewingTransform viewingTransform);
    

}
