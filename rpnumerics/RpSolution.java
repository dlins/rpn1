/*
*
* Instituto de Matematica Pura e Aplicada - IMPA
* Departamento de Dinamica dos Fluidos
*
*/

package rpnumerics;

public interface RpSolution {
    public static final int DEFAULT_NULL_FLAG = -1;
    public static final int OUT_OF_THE_BOUNDARY = 0;
    public static final int INTERSECTION_WITH_POINCARE = 1;
    public static final int MAX_NUM_OF_ITERATION_REACHED = 2;
    // Stationary point calculations
    public static final int NO_CONVERGENCE_IN_STATIONARY_POINT_COMPUTATION = 3;
    public static final int CONVERGENCE_IN_STATIONARY_POINT_COMPUTATION = 4;
    public static final int FOUND_OUT_OF_BOUNDARY = 5;
    // Manifold calculations
    public static final int MANIFOLD_DOES_NOT_EXIST = 6;
    public static final int MANIFOLD_FINISHES_ON_POINCARE_SECTION = 7;
    public static final int MANIFOLD_FINISHES_ON_BOUNDARY = 8;
    public static final int MANIFOLD_FINISHES_DUE_TO_MANY_STEPS = 9;
    public static final int STATIONARY_POINT_IS_TOO_CLOSE_TO_POINCARE_SECTION = 10;
    public static final int STATIONARY_POINT_IS_TOO_CLOSE_TO_BOUNDARY = 11;
    // Connections calculations
    public static int CONNECTION_NOT_FINISHED = 12;
    public static int DEVIATION_INCREASED = 13;
    public static int NO_POINCARE_SECTION = 14;
    public static int CONNECTED = 15;



    }
