/*
 * Instituto de Matematica Pura e Aplicada - IMPA
 * Departamento de Dinamica dos Fluidos
 *
 */
package rpnumerics.methods;

import rpnumerics.methods.contour.ContourND;
import rpnumerics.methods.contour.functionsobjects.CubeFunction;
import rpnumerics.methods.contour.functionsobjects.MDVectorFunction;
import wave.util.VectorFunction;

public class ContourParams {

    private CubeFunction[] functionsArray_;
    private VectorFunction vFunction_;
    private MDVectorFunction mFunction_;
    private ContourND contour_;
    double[] rectl_;
    int[] res_;

    public ContourParams(ContourND contour, CubeFunction[] functions, double[] boundary, int[] resolution) {

        functionsArray_ = functions;
        rectl_ = boundary;
        res_ = resolution;
        contour_ = contour;
    }

    public ContourParams(ContourND contour, VectorFunction function, double[] boundary, int[] resolution) {

        vFunction_ = function;
        rectl_ = boundary;
        res_ = resolution;
        contour_ = contour;

    }

    public ContourParams(ContourND contour, MDVectorFunction function, double[] boundary, int[] resolution) {

        mFunction_ = function;
        rectl_ = boundary;
        res_ = resolution;
        contour_ = contour;
    }

    public CubeFunction[] getFunctions() {
        return functionsArray_;
    }

    public VectorFunction getFunction() {
        return vFunction_;
    }

    public MDVectorFunction getMDVectorFunction() {
        return mFunction_;
    }

    public int[] getResolution() {
        return res_;
    }

    public void setResolution(int[] resolution) {
    }

    public double[] getBoundary() {
        return rectl_;
    }

    public void setBoundary(double[] boundary) {
    }

    public ContourND getContour() {
        return contour_;
    }
}
