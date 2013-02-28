package wave.util;

import wave.util.RealVector;
import org.netlib.util.intW;
import org.netlib.lapack.DGESV;

public class SimplexPoincareSection implements PoincareSection {
    //
    // Members
    //
    private double b_;
    private RealVector point_;
    private RealVector normal_;
    private RealVector[] points_;

    //
    // Constructors
    //
    public SimplexPoincareSection(RealVector[] points) {
        points_ = points;
        int m = points[0].getSize();
        // calculate normal (bed method but working well)
        RealVector p = new RealVector(m);
        normal_ = new RealVector(m);
        double sqrt2 = Math.sqrt(2.0);
        double rand = 0.5;
        while (normal_.norm() == 0) {
            for (int i = 0; i < m; i++) {
                p.setElement(i, rand);
                rand = rand + sqrt2;
                rand = rand - Math.floor(rand);
            }
            normal_ = getNormal(p);
        }
        normal_ = getNormal(normal_);
        point_ = new RealVector(points_[0]);
    }

    //
    // Methods
    //
    public void shift(RealVector center, RealVector normal) {
        int m = points_[0].getSize();
        RealVector oldCenter = new RealVector(m);
        for (int i = 0; i < m; i++)
            oldCenter.add(points_[i]);
        oldCenter.scale(1d / m);
        normal.scale(1.0 / normal.norm());
        RealVector v = new RealVector(normal);
        v.sub(normal_);
        v.scale(1.0 / v.norm());
        RealMatrix2 transform = new RealMatrix2(m, m);
        for (int i = 0; i < m; i++)
            for (int j = 0; j < m; j++)
                transform.setElement(i, j, transform.getElement(i, j) - 2.0 * v.getElement(i) * v.getElement(j));
        for (int i = 0; i < m; i++) {
            points_[i].sub(oldCenter);
            points_[i].mul(transform, points_[i]);
            points_[i].add(center);
        }
        normal_.set(normal);
        point_.set(points_[0]);
    }

    public RealVector getNormal(RealVector x) {
        int m = x.getSize();
        RealVector[] p = new RealVector[m];
        for (int i = 0; i < m - 1; i++) {
            p[i] = new RealVector(points_[0]);
            p[i].sub(points_[i + 1]);
            p[i].scale(1.0 / p[i].norm());
        }
        p[m - 1] = new RealVector(x);
        double c;
        RealVector tmp = new RealVector(m);
        for (int i = 0; i < m - 1; i++)
            for (int j = i + 1; j < m; j++) {
                c = p[j].dot(p[i]) / p[i].dot(p[i]);
                tmp.set(p[i]);
                tmp.scale(c);
                p[j].sub(tmp);
                p[j].scale(1.0 / p[j].norm());
            }
        RealVector normal = new RealVector(p[m - 1]);
        c = normal.norm();
        if (c != 0)
            normal.scale(1 / c);
        else
            normal.scale(0);
        return normal;
    }

    public RealVector getNormal() { return normal_; }

    public RealVector[] getPoints() { return points_; }

    public boolean intersectPlane(RealVector P1, RealVector P2) {
        RealVector p = new RealVector(P1);
        p.sub(point_);
        double proj = p.dot(normal_);
        p.set(P2);
        p.sub(point_);
        if (proj * p.dot(normal_) <= 0) return true;
        else
            return false;
    }

    public RealVector intersectionPoint(RealVector P1, RealVector P2) {
        RealVector p = new RealVector(P1);
        p.sub(P2);
        double proj1 = p.dot(normal_);
        p.set(P2);
        p.sub(point_);
        double proj2 = p.dot(normal_);
        RealVector result = new RealVector(P1);
        if (proj1 == 0) return result;
        result.scale(-proj2 / proj1);
        p.set(P2);
        p.scale(1 + proj2 / proj1);
        result.add(result, p);
        return result;
    }

    public boolean intersect(RealVector P1, RealVector P2) throws Exception{
        RealVector p1 = new RealVector(P1);
        RealVector p2 = new RealVector(P2);
        if (p1.getSize() != p2.getSize()) throw new Exception ( "p1.size() != p2.size()");
        boolean result = true;
        int m = p1.getSize();
        RealVector p = new RealVector(m);
        RealMatrix2 V = new RealMatrix2(m, m);
        p.sub(p2, p1);
        V.setColumn(m - 1, p);
        for (int i = 1; i < m; i++) {
            p.set(points_[i]);
            p.sub(points_[0]);
            V.setColumn(i - 1, p);
        }
        double[] [] cd = new double[m] [1];
        double[] [] Vd = new double[m] [m];
        for (int i = 0; i < m; i++) {
            cd[i] [0] = p2.getElement(i) - points_[0].getElement(i);
            for (int j = 0; j < m; j++)
                Vd[i] [j] = V.getElement(i, j);
        }
        int[] intArray = new int[m];
        intW info = new intW(1);
        DGESV.DGESV(m, 1, Vd, intArray, cd, info);
        if (info.val != 0)
            result = false;
        else if ((cd[m - 1] [0] < 0) || (cd[m - 1] [0] > 1))
            result = false;
        else {
            double sum = 0;
            for (int i = 0; i < m - 1; i++) {
                sum = sum + cd[i] [0];
                if (cd[i] [0] < 0)
                    result = false;
            }
            if (sum > 1)
                result = false;
        }
        return result;
    }
}
