/*
 *
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package wave.multid.graphs;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Point;

public class dcViewport extends Rectangle {
    //
    // Members
    //
    //
    // Constructors
    //

    public dcViewport(int w, int h) {
        super(new Point(), new Dimension(w, h));
    }

    public dcViewport(Point origin, int w, int h,int margin) {
        super(new Point(origin.x + margin, origin.y + margin), new java.awt.Dimension(w - 2*margin, h - 2*margin));
    }
    
    
    
//      public dcViewport(Point origin, int w, int h,double margin) {
//        super(new Point((int) (origin.x-(origin.x * margin)), (int) (origin.y - (origin.y * margin))),
//                new java.awt.Dimension((int) (w - (2*origin.x * margin)), (int) (h - (2*origin.y * margin))));
//    }
    
    

//    private static Point calculateMargin(Point initialOrigin){
//        
//        
//        
//        
//        
//    }
    //
    // Accessors/Mutators
    //
    public Point getOriginPosition() {
        return this.getLocation();
    }
    //
    // Methods
    //
}
