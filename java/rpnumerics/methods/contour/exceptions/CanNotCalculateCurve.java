/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rpnumerics.methods.contour.exceptions;

/**
 *
 * @author cbevilac
 */

import java.util.ArrayList;
import rpnumerics.*;

public class CanNotCalculateCurve extends BifurcationCurve {
    public CanNotCalculateCurve() {
//        super(0, null);
        super(new ArrayList(), null);
    }

}
