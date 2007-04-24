/*
*
* Instituto de Matematica Pura e Aplicada - IMPA
* Departamento de Dinamica dos Fluidos
*
*/

package wave.util.graphs;

public class ViewVolume {
    //
    // Members
    //
    private double width_;
    private double height_;
    private double depth_;
    private double[] center_;

    //
    // Constructors
    //
    public ViewVolume(double[] center, double width, double height, double depth) {
        center_ = (double[]) center.clone();
        width_ = width;
        height_ = height;
        depth_ = depth;
    }

    public ViewVolume(double width, double height, double depth) {
        this(
            new double[] { 0., 0., 0. }, width, height, depth);
    }

    //
    // Accessors/Mutators
    //
    public double[] getCenter() { return center_; }

    public double getWidth() { return width_; }

    public double getHeight() { return height_; }

    public double getDepth() { return depth_; }
}
