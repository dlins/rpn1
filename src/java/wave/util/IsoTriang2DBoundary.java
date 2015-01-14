/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package wave.util;

public class IsoTriang2DBoundary implements Boundary {

    private RealVector minimums_;
    private RealVector maximums_;
    private RealVector A_;
    private RealVector B_;
    private RealVector C_;

    //
    // Constructors
    //
    public IsoTriang2DBoundary(RealVector A, RealVector B, RealVector C) {

        A_ = A;
        B_ = B;
        C_ = C;

        double minAbs = A.getElement(0);
        double minOrd = A.getElement(1);
        double maxAbs = minAbs;
        double maxOrd = minOrd;
        // ABSSISSA
        // B
        if (B.getElement(0) < minAbs) {
            minAbs = B.getElement(0);
        }
        if (B.getElement(0) > maxAbs) {
            maxAbs = B.getElement(0);
        }
        // C
        if (C.getElement(0) < minAbs) {
            minAbs = C.getElement(0);
        }
        if (C.getElement(0) > maxAbs) {
            maxAbs = C.getElement(0);
        }
        // ORDENADE
        // B
        if (B.getElement(1) < minOrd) {
            minOrd = B.getElement(1);
        }
        if (B.getElement(1) > maxOrd) {
            maxOrd = B.getElement(1);
        }
        // C
        if (C.getElement(1) < minOrd) {
            minOrd = C.getElement(1);
        }
        if (C.getElement(1) > maxOrd) {
            maxOrd = C.getElement(1);
        }
        minimums_ = new RealVector(
                new double[]{minAbs, minOrd});
        maximums_ = new RealVector(
                new double[]{maxAbs, maxOrd});
    }

    public IsoTriang2DBoundary(String limits) {

        String[] limitsNumbers = limits.split(" ");
       
        RealVector A = new RealVector(2);

        int vectorIndex = 0;

        for (int i = 0; i < 2; i++) {
            A.setElement(i, new Double(limitsNumbers[i]));

        }

        RealVector B = new RealVector(2);

        for (int i = 2; i < 4; i++) {
            B.setElement(vectorIndex, new Double(limitsNumbers[i]));
            vectorIndex++;
        }

        RealVector C = new RealVector(2);
        vectorIndex = 0;
        for (int i = 4; i < 6; i++) {
            C.setElement(vectorIndex, new Double(limitsNumbers[i]));
            vectorIndex++;
        }

        A_ = A;
        B_ = B;
        C_ = C;

        double minAbs = A.getElement(0);
        double minOrd = A.getElement(1);
        double maxAbs = minAbs;
        double maxOrd = minOrd;
        // ABSSISSA
        // B
        if (B.getElement(0) < minAbs) {
            minAbs = B.getElement(0);
        }
        if (B.getElement(0) > maxAbs) {
            maxAbs = B.getElement(0);
        }
        // C
        if (C.getElement(0) < minAbs) {
            minAbs = C.getElement(0);
        }
        if (C.getElement(0) > maxAbs) {
            maxAbs = C.getElement(0);
        }
        // ORDENADE
        // B
        if (B.getElement(1) < minOrd) {
            minOrd = B.getElement(1);
        }
        if (B.getElement(1) > maxOrd) {
            maxOrd = B.getElement(1);
        }
        // C
        if (C.getElement(1) < minOrd) {
            minOrd = C.getElement(1);
        }
        if (C.getElement(1) > maxOrd) {
            maxOrd = C.getElement(1);
        }
        minimums_ = new RealVector(
                new double[]{minAbs, minOrd});
        maximums_ = new RealVector(
                new double[]{maxAbs, maxOrd});



    }

    //
    // Accessors/Mutators
    //
    public RealVector getMinimums() {
        return minimums_;
    }

    public RealVector getMaximums() {
        return maximums_;
    }

    public RealVector getA() {
        return A_;
    }

    public RealVector getB() {
        return B_;
    }

    public RealVector getC() {
        return C_;
    }

    //
    // Methods
    //
    public boolean inside(RealVector u) {

        // assuming a normalized boundary
        double x = u.getElement(0);
        double y = u.getElement(1);
        if ((x > 0.) && (y > 0.) && (x + y <= 1.)) {
            return true;
        }
        return false;
    }

    /*
     * This routine finds the intersection of the line segment from a to b
     * with the physical boundary.it is presumed that a is in the interior
     * of the physical domain and b is in the exterior.
     */
    public RealVector intersect(RealVector y1, RealVector y2) {
        /*            logical intseg
        real t

        // check whether it intersects the lower side
        if (intseg(a(1), a(2), b(1), b(2), t, 1.0))
        then c(1) = t c(2) = 0.0
        // check whether it intersects the left side
        else if (intseg(a(2), a(1), b(2), b(1), t, 1.0))
        then c(1) = 0. c(2) = t
        // check whether it intersects the hypotenuse
        else if (intseg(-a(1) + a(2) + 1.0, a(2) + a(1) - 1.0, 1 - b(1) + b(2) + 1.0,
        b(2) + b(1) - 1.0, t, 2.0))then c(2) = 0.5 * t c(1) = 1.0 - c(2)

        // error if segment does not intersect one of these
        else
        write(alterr, 9999) a, b 9999 format(' ERROR IN INTDOM:  A, B = ', 4f 12.4)
        c(1) = b(1)
        c(2) = b(2)

        endif

        return
        end*/

        return null;
    }

    public String limits() {
        String limits = "";

        for (int i = 0; i < A_.getSize(); i++) {
            limits += A_.getElement(i);
            limits += " ";
        }

        for (int i = 0; i < B_.getSize(); i++) {
            limits += B_.getElement(i);
            limits += " ";

        }

        for (int i = 0; i < C_.getSize(); i++) {
            limits += C_.getElement(i);
            limits += " ";

        }

        return limits;


    }
}
