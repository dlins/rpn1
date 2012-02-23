package rpnumerics.methods;


import rpnumerics.*;
import rpnumerics.methods.contour.*;
import rpnumerics.methods.contour.support.CurveDomainManager;
import rpnumerics.methods.contour.support.NoContourMethodDefined;
import wave.util.*;

public class BifurcationContourMethod extends BifurcationMethod {

    private ContourParams params_;
    private ContourBifurcation contourMethod_;
    private double[] boundaryArray_;
    private int[] resolution_;
    private int familyIndex;
    private int dimension;
    private int numberOfSubDomains;
    private int[][] subDomainsResolutions;
    private double[][] subDomainsBoundaryArray;
    private String X_Minus_Resolution = "xminus-resolution";
    private String Y_Minus_Resolution = "yminus-resolution";
    private String X_0_Minus_Boundary = "x0minus-boundary";
    private String Y_0_Minus_Boundary = "y0minus-boundary";
    private String X_F_Minus_Boundary = "xFminus-boundary";
    private String Y_F_Minus_Boundary = "yFminus-boundary";
    private String X_Plus_Resolution = "xplus-resolution";
    private String Y_Plus_Resolution = "yplus-resolution";
    private String X_0_Plus_Boundary = "x0plus-boundary";
    private String Y_0_Plus_Boundary = "y0plus-boundary";
    private String X_F_Plus_Boundary = "xFplus-boundary";
    private String Y_F_Plus_Boundary = "yFplus-boundary";

  

    public BifurcationContourMethod(ContourParams params) {
        super();

        contourMethod_ = ContourFactory.createContourBifurcation(params);
        
        dimension = RPNUMERICS.domainDim();

        CurveDomainManager.instance().initializeInitialCurve(contourMethod_, boundaryToArea());

    }

    public BifurcationCurve curve() {
        try {
            return (BifurcationCurve) CurveDomainManager.instance().calculateInitialCurve();
        } catch (NoContourMethodDefined ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public ContourParams getParams() {
        return params_;
    }

    public double[] minusSide(double[] coordinates) {

        int dim = coordinates.length;
        int reducedDimension = dim / 2;

        double[] filteredResult = new double[reducedDimension];

        for (int pont_dimension = 0; pont_dimension < reducedDimension; pont_dimension++) {
            filteredResult[pont_dimension] = coordinates[pont_dimension];
        }

        return filteredResult;
    }

    public double[] plusSide(double[] coordinates) {
        int dim = coordinates.length;
        int reducedDimension = dim / 2;

        double[] filteredResult = new double[reducedDimension];

        for (int pont_dimension = 0; pont_dimension < reducedDimension; pont_dimension++) {
            filteredResult[pont_dimension] = coordinates[pont_dimension + reducedDimension];
        }

        return filteredResult;
    }

    private double calculateNextLowerPoint(double b1, double b2, int resolution, double coordinate) {

        if (b1 > b2) {
            double temp = b2;
            b2 = b1;
            b1 = temp;
        }

        double Dparam = Math.abs(b2 - b1) / resolution;
        int Z = (int) (coordinate / Dparam);
        double answer = Z * Dparam;

        if (answer < b1) {
            return b1;
        }

        return answer;

    }

    private double calculateNextUpperPoint(double b1, double b2, int resolution, double coordinate) {

        if (b1 > b2) {
            double temp = b2;
            b2 = b1;
            b1 = temp;
        }

        double Dparam = Math.abs(b2 - b1) / resolution;
        int Z = (int) (coordinate / Dparam);

        double answer = (Z + 1) * Dparam;
        if (answer >= b2) {
            return b2;
        }

        return answer;
    }

    private boolean isInRestrictions(RealVector p1, RealVector p2, RealVector p3, RealVector p4, double[][] boundaries) {

        double[] point1Minus = p1.toDouble();
        double xp1Minus = point1Minus[0];
        double yp1Minus = point1Minus[1];

        double[] point2Minus = p2.toDouble();
        double xp2Minus = point2Minus[0];
        double yp2Minus = point2Minus[1];

        double[] point1Plus = p3.toDouble();
        double xp1Plus = point1Plus[0];
        double yp1Plus = point1Plus[1];

        double[] point2Plus = p4.toDouble();

        double xp2Plus = point2Plus[0];
        double yp2Plus = point2Plus[1];

        for (int pont_subdomain = 0; pont_subdomain < numberOfSubDomains; pont_subdomain++) {

            // Minus

            double xl1Minus = boundaries[pont_subdomain][0];
            double yl1Minus = boundaries[pont_subdomain][2];

            double xl2Minus = boundaries[pont_subdomain][1];
            double yl2Minus = boundaries[pont_subdomain][3];

            if ((((yp1Minus >= yl1Minus) && (yp1Minus <= yl2Minus))
                    && ((xp1Minus >= xl1Minus) && (xp1Minus <= xl2Minus)))
                    || (((xp2Minus >= xl1Minus) && (xp2Minus <= xl2Minus))
                    && ((yp2Minus >= yl1Minus) && (yp2Minus <= yl2Minus)))) {
                return true;
            }

            // Plus

            double xl1Plus = boundaries[pont_subdomain][4];
            double yl1Plus = boundaries[pont_subdomain][6];

            double xl2Plus = boundaries[pont_subdomain][5];
            double yl2Plus = boundaries[pont_subdomain][7];

            if ((((yp1Plus >= yl1Plus) && (yp1Plus <= yl2Plus))
                    && ((xp1Plus >= xl1Plus) && (xp1Plus <= xl2Plus)))
                    || (((xp2Plus >= xl1Plus) && (xp2Plus <= xl2Plus))
                    && ((yp2Plus >= yl1Plus) && (yp2Plus <= yl2Plus)))) {


                return true;
            }
        }

        return false;
    }

      private Area boundaryToArea() {

        Boundary originalBoundary = RPNUMERICS.boundary();
        
        RealVector maximums = originalBoundary.getMaximums();
        RealVector minimums = originalBoundary.getMinimums();
        
        double[] maximumsArray = maximums.toDouble();
        double[] minimumsArray = minimums.toDouble();
        
        double[] extendedMaximumsArray = new double[maximumsArray.length * 2];
        double[] extendedMinimumsArray = new double[minimumsArray.length * 2];
        
        for (int pont_array = 0; pont_array < maximumsArray.length; pont_array++) {
        	extendedMaximumsArray[pont_array] = maximumsArray[pont_array];
        	extendedMinimumsArray[pont_array] = minimumsArray[pont_array];
        	
        	extendedMaximumsArray[pont_array + maximumsArray.length] = maximumsArray[pont_array];
        	extendedMinimumsArray[pont_array + maximumsArray.length] = minimumsArray[pont_array];
        }
        
        RealVector extendedMaximums = new RealVector(extendedMaximumsArray);
        RealVector extendedMinimums = new RealVector(extendedMinimumsArray);
        
        RectBoundary extendedBoundary = new RectBoundary(extendedMinimums, extendedMaximums);
        
        double resolutionArray[] = {30, 30, 30, 30};
        
        /*double resolutionArray[] = new double[dimension * 2];
        
        resolutionArray[0] = new Integer(RPNUMERICS.getContourConfiguration().getParamValue("xminus-resolution"));
        resolutionArray[1] = new Integer(RPNUMERICS.getContourConfiguration().getParamValue("yminus-resolution"));
        resolutionArray[2] = new Integer(RPNUMERICS.getContourConfiguration().getParamValue("xplus-resolution"));
        resolutionArray[3] = new Integer(RPNUMERICS.getContourConfiguration().getParamValue("xplus-resolution"));
        
        */
        
        RealVector resolution = new RealVector(resolutionArray);
                
        Area resultArea = new Area(resolution, extendedBoundary.getMaximums(), extendedBoundary.getMinimums());

        return resultArea;
    }
}
