/*
*
* Instituto de Matematica Pura e Aplicada - IMPA
* Departamento de Dinamica dos Fluidos
*
*/

package wave.multid.graphs;

import wave.multid.map.HomogeneousMap;

public class ViewPlane {
    private dcViewport viewport_;
    private wcWindow window_;

    public ViewPlane(dcViewport viewport, wcWindow window) {

        viewport_ = viewport;
        window_ = new wcWindow(window);
    }

    //
    // Accessors/Mutators
    //
    public dcViewport getViewport() { return viewport_; }

    public wcWindow getWindow() { return window_; }

    //
    // Methods
    //
    public void update(HomogeneousMap map) {

	getWindow().update(map);

    }
    
   

}
