/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package wave.util;

public class RectBoundary implements Boundary {

    private RealVector minimums_;
    private RealVector maximums_;
    private int size_;

    //
    // Constructors
    //
    public RectBoundary(RealVector minimums, RealVector maximums) {
        minimums_ = new RealVector(minimums);
        maximums_ = new RealVector(maximums);
        size_ = minimums.getSize();
    }

    public RectBoundary(String limits) {

        String[] limitsNumbers = limits.split(" ");
        int dimension = limitsNumbers.length / 2;

        minimums_ = new RealVector(dimension);
        maximums_ = new RealVector(dimension);

        int vectorIndex = 0;

        for (int i = 0; i < dimension; i++) {
            minimums_.setElement(vectorIndex, new Double(limitsNumbers[i]));
            vectorIndex++;

        }
        vectorIndex = 0;

        for (int i = dimension; i < limitsNumbers.length; i++) {
            maximums_.setElement(vectorIndex, new Double(limitsNumbers[i]));
            vectorIndex++;
        }


    }

    //
    // Accessors/Mutators
    //
    public double getCoordinateSpan(int i) {
        return maximums_.getElement(i) - minimums_.getElement(i);
    }

    public boolean inside(RealVector y) {
        // true iff y inside rectangular boundary
        boolean result = true;
        for (int i = 0; i < size_; i++) {
            if ((y.getElement(i) < minimums_.getElement(i)) || (y.getElement(i) > maximums_.getElement(i))) {
                result = false;
            }
        }
        return result;
    }

    public RealVector getMinimums() {
        return minimums_;
    }

    public RealVector getMaximums() {
        return maximums_;
    }

 
    //
    // Methods
    //
    public RealVector intersect(RealVector y1, RealVector y2) {
        // returns a point for intersection of [y1,y2] segment with the boundary
        double ratio;
        RealVector vec1 = new RealVector(size_);
        RealVector vec2 = new RealVector(size_);
        RealVector result = null;
        for (int i = 0; i < size_; i++) {
            if (y1.getElement(i) != y2.getElement(i)) {
                ratio = (minimums_.getElement(i) - y1.getElement(i)) / (y2.getElement(i) - y1.getElement(i));
                if ((ratio >= 0) && (ratio <= 1)) {
                    vec1.set(y1);
                    vec1.scale(1d - ratio);
                    vec2.set(y2);
                    vec2.scale(ratio);
                    vec2.add(vec1);
                    vec2.setElement(i, minimums_.getElement(i));
                    if (inside(vec2)) {
                        result = new RealVector(vec2);
                    }
                }
                ratio = (maximums_.getElement(i) - y1.getElement(i)) / (y2.getElement(i) - y1.getElement(i));
                if ((ratio >= 0) && (ratio <= 1)) {
                    vec1.set(y1);
                    vec1.scale(1d - ratio);
                    vec2.set(y2);
                    vec2.scale(ratio);
                    vec2.add(vec1);
                    vec2.setElement(i, maximums_.getElement(i));
                    if (inside(vec2)) {
                        result = new RealVector(vec2);
                    }
                }
            }
        }
        return result;
    }

    public String limits() {

        String limits = "";


        for (int i = 0; i < minimums_.getSize(); i++) {
            limits += minimums_.getElement(i);
            limits += " ";
        }

        for (int i = 0; i < maximums_.getSize(); i++) {
            limits += maximums_.getElement(i);
            limits += " ";

        }

        return limits;
    }
}
