/*
* Instituto de Matematica Pura e Aplicada - IMPA
* Departamento de Dinamica dos Fluidos
*
*/

package rpnumerics.ode;

import wave.util.SimplexPoincareSection;


public class ODESolverProfile {
    
    public native SimplexPoincareSection getPoincareSection();
    
    public native void setPoincareSection(SimplexPoincareSection section);
    
    public native void setPoincareSectionFlag(boolean flag);
    
}
