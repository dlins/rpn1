/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */

package wave.util;

public class NewtonSolver {
    public static boolean Newton(VectorFunction f, RealVector guess) {
    /*  Classical Newton method for solving equation f(x) = 0:

        input:
          VectorFunction f has two methods: value(RealVector),
deriv(RealVector)
          guess is initial guess

        output:
          guess is a computed solution
          boolean is true (successful computation) or false (no convergence)
*/

        int iterationNumber = 0;
        int MaximumIterationNumber = 30;
        int n = guess.getSize();
        double minimumDeltaXnorm = 1.0e-14 * Math.max(guess.norm(), 1);
        RealMatrix2 A = f.deriv(guess);
        RealVector f0 = f.value(guess);
        f0.negate();
        RealVector deltaX = A.solve(f0);
        guess.add(deltaX);
        while ((deltaX.norm() > minimumDeltaXnorm) && (iterationNumber < MaximumIterationNumber)) {
            A = f.deriv(guess);
            f0 = f.value(guess);
            f0.negate();
            deltaX = A.solve(f0);
            guess.add(deltaX);
        }
        if (deltaX.norm() > minimumDeltaXnorm) return false; // no convergence
        else
            return true; // convergence
    }

    public static boolean intersectionPoint(int n, RealVector[] points, RealVector[] values, RealVector result) {
    /*  Finds zero of a linear function on a simplex
        given by points points[i], i = 0,...,n-1

        input:
          points is a vector of n points defining a simplex (dimension n)
          values is a vector of function values at points (dimension n-1)

        output:
          result is the intersection point
          boolean is true (point is inside the simples) or
            false (point is outsede the simples) */

        RealVector deltaF = new RealVector(n - 1);
        RealMatrix2 A = new RealMatrix2(n - 1, n - 1);
        for (int i = 0; i < n - 1; i++) {
            deltaF.sub(values[i], values[n - 1]);
            A.setColumn(i, deltaF);
        }
        deltaF.set(values[n - 1]);
        deltaF.negate();
        RealVector coeffX = A.solve(deltaF);
        boolean success = true;
        RealVector deltaX = new RealVector(n);
        result.set(points[n - 1]);
        double sum = 0;
        for (int j = 0; j < n - 1; j++) {
            if (coeffX.getElement(j) < 0)
                success = false;
            sum = sum + coeffX.getElement(j);
            deltaX.sub(points[j], points[n - 1]);
            deltaX.scale(coeffX.getElement(j));
            result.add(deltaX);
        }
        if ((sum < 0) || (sum > 1))
            success = false;
        return success;
    }

    public static int NewtonInPlane(VectorFunction f, RealVector guess, int m, RealVector[] points) {
    /*  Newton method for solving equation f(x) = 0 on a plane given
        by points :

        input:
          VectorFunction f has two methods: value(RealVector),
deriv(RealVector)
          guess is initial guess
          m is dimension of the plane (number of points)
          points are points determining plane (n+1)

        output:
          guess is a computed solution
          return: 1   successful computation,
                        and result is inside the simplex given by points
                  -1  successful computation,
                        but result is out of simplex given by points
                  0   no convergence */

        int success;
        int iterationNumber = 0;
        int MaximumIterationNumber = 30;
        int n = guess.getSize();
        double minimumDeltaXnorm = 1.0e-14 * Math.max(guess.norm(), 1);
        // find a basis on a plane
        RealVector[] basisVec = new RealVector[m];
        for (int i = 0; i < m; i++) {
            basisVec[i] = new RealVector(points[i]);
            basisVec[i].sub(points[m]);
        }
        RealMatrix2 basis = new RealMatrix2(n, m);
        for (int i = 0; i < m; i++)
            basis.setColumn(i, basisVec[i]);
        // find a dual basis (for normal projection of guess to a plane)
        RealMatrix2 transposeB = new RealMatrix2(m, n);
        transposeB.transpose(basis);
        RealMatrix2 coef = new RealMatrix2(m, m);
        coef.mul(transposeB, basis);
        coef.invert();
        RealMatrix2 dual = new RealMatrix2(n, m);
        dual.mul(basis, coef);
        RealMatrix2 transposeDual = new RealMatrix2(m, n);
        transposeDual.transpose(dual);
        // iniatial guess in basis
        RealVector guessB = new RealVector(m);
        guessB.mul(transposeDual, guess);
        // first step in Newton method
        RealMatrix2 A = f.deriv(guess);
        RealMatrix2 AinBasis = new RealMatrix2(m, m);
        AinBasis.mul(A, basis);
        RealVector f0 = f.value(guess);
        f0.negate();
        RealVector deltaXinBasis = AinBasis.solve(f0);
        guessB.add(deltaXinBasis);
        guess.mul(basis, guessB);
        guess.add(points[m]);
        RealVector deltaX = new RealVector(n);
        deltaX.mul(basis, deltaXinBasis);
        // Newton method
        while ((deltaX.norm() > minimumDeltaXnorm) && (iterationNumber < MaximumIterationNumber)) {
            A = f.deriv(guess);
            AinBasis.mul(A, basis);
            f0 = f.value(guess);
            f0.negate();
            deltaXinBasis = AinBasis.solve(f0);
            guessB.add(deltaXinBasis);
            guess.mul(basis, guessB);
            guess.add(points[m]);
            deltaX.mul(basis, deltaXinBasis);
        }
        if (deltaX.norm() > minimumDeltaXnorm) success = 0; // no convergence
        else {
            success = 1;
            deltaX.sub(guess, points[m]);
            deltaXinBasis.mul(transposeDual, deltaX);
            double sum = 0;
            for (int i = 0; i < m; i++) {
                if (deltaXinBasis.getElement(i) < 0)
                    success = -1;
                sum = sum + deltaXinBasis.getElement(i);
            }
            if ((sum < 0) || (sum > 1))
                success = -1;
        }
        return success;
    }
}