/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package wave.util;

/**
 *
 * @author edsonlan
 */

public class BoxND {
    
    
public RealVector pmin, pmax;
    
  
public BoxND(RealVector ppmin, RealVector ppmax){
//BoxND::BoxND(PointND &ppmin, PointND &ppmax){
    int n = ppmin.getSize();

    pmin = new RealVector(n);//ppmin;
    pmax = new RealVector(n);//ppmax;

    for (int i = 0; i < n; i++){
        pmin.setElement(i,Math.min(ppmin.getElement(i), ppmax.getElement(i)));
        pmax.setElement(i, Math.max(ppmin.getElement(i), ppmax.getElement(i)));
    }
}






// Box-box collision test, very similar to circle-circle collision test.
// Verify that the distance between the centers of the boxes is less
// than the sum of the "radii" for each dimension.
//
public boolean intersect(BoxND b){
    int n = pmin.getSize();

    RealVector bpmin = b.pmin, bpmax = b.pmax;

    double []this_radius = new double[n];
    double  []b_radius=new double[n];

    for (int i = 0; i < n; i++){
        this_radius[i] = (pmax.getElement(i)  - pmin.getElement(i))/2.0;
        b_radius[i]    = (bpmax.getElement(i) - bpmin.getElement(i))/2.0;
    }

    double []this_center = new double[n];
    double []b_center=new double[n];

    for (int i = 0; i < n; i++){
        this_center[i] = pmin.getElement(i) + this_radius[i];
        b_center[i]    = bpmin.getElement(i) + b_radius[i];
    }

    boolean collision = true; int pos = 0;

    while (collision && pos < n){
        if (Math.abs(this_center[pos] - b_center[pos]) > (this_radius[pos] + b_radius[pos])) collision = false;
        pos++;
    }

    return collision;
}


    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
}
