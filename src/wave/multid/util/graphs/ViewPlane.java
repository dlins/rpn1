/*
*
* Instituto de Matematica Pura e Aplicada - IMPA
* Departamento de Dinamica dos Fluidos
*
*/

package wave.util.graphs;

import wave.util.RealMatrix2;
import wave.util.RealVector;

public class ViewPlane {
    private dcViewport viewport_;
    private wcWindow window_;

    public ViewPlane(dcViewport viewport, wcWindow window) {
        viewport_ = viewport;
        window_ = window;
    }

    //
    // Accessors/Mutators
    //
    public dcViewport getViewport() { return viewport_; }

    public wcWindow getWindow() { return window_; }
}
