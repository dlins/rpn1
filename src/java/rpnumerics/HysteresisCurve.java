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


    private  int domainFamily_;
    private  int curveFamily_;


    public HysteresisCurve(int domainFamily,int curveFamily,List<HugoniotSegment> curveSegments,List<HugoniotSegment> domainSegments) {
        super(curveSegments, domainSegments);
        domainFamily_=domainFamily;
        curveFamily_=curveFamily;

      }

  


   
}
