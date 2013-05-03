/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */

package wave.util;

public class GridProfile {
    private double min_;
    private double max_;
    private double delta_;
    private int numOfNodes_;

    public GridProfile(double min, double max, int numOfNodes) {
        min_ = min;
        max_ = max;
        numOfNodes_ = numOfNodes;
        delta_ = (max_ - min_) / numOfNodes_;
    }

    public double minimum() { return min_; }

    public double maximum() { return max_; }

    public double delta() { return delta_; }

    public int numOfNodes() { return numOfNodes_; }
}
