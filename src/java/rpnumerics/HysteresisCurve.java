/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpnumerics;

import java.util.List;

public class HysteresisCurve extends BifurcationCurve {
    //
    // Members
    //

    int family_;


    public HysteresisCurve(int family,List<HugoniotSegment> hSegments) {
        super(hSegments);
        family_=family;
      }

    public int getFamily() {
        return family_;
    }



   
}
