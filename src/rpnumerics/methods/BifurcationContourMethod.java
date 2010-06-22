package rpnumerics.methods;


import rpnumerics.*;
import rpnumerics.methods.contour.*;
import rpnumerics.methods.contour.support.CurveDomainManager;
import rpnumerics.methods.contour.support.NoContourMethodDefined;
import wave.util.*;

public class BifurcationContourMethod extends BifurcationMethod {

    private BifurcationParams params_;
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

  

    public BifurcationContourMethod(BifurcationParams params) {
        super();

        contourMethod_ = ContourFactory.createContourBifurcation(params);

        CurveDomainManager.instance().initializeInitialCurve(contourMethod_, boundaryToArea());

//        dimension = RPNUMERICS.domainDim();
//
//        resolution_ = new int[2 * dimension];
//
//        resolution_[0] = new Integer(RPNUMERICS.getContourConfiguration().getParamValue("xminus-resolution"));
//        resolution_[1] = new Integer(RPNUMERICS.getContourConfiguration().getParamValue("yminus-resolution"));
//
//        resolution_[2] = new Integer(RPNUMERICS.getContourConfiguration().getParamValue("xplus-resolution"));
//        resolution_[3] = new Integer(RPNUMERICS.getContourConfiguration().getParamValue("xplus-resolution"));
//
//        familyIndex = params.getFamilyIndex();
//
//        Boundary boundary = RPNUMERICS.boundary();
//
//        if (boundary instanceof RectBoundary) {
//
//            int dim = RPNUMERICS.domainDim();
//            boundaryArray_ = new double[2 * 2 * dim];
//
//            RealVector minimums = boundary.getMinimums();
//            RealVector maximums = boundary.getMaximums();
//
//            double[] minimumsArray = minimums.toDouble();
//            double[] maximumsArray = maximums.toDouble();
//
//            for (int pont_dim = 0; pont_dim < dim; pont_dim++) {
//
//                int first = 2 * pont_dim;
//                int second = 2 * pont_dim + 1;
//
//                boundaryArray_[first] = minimumsArray[pont_dim];
//                boundaryArray_[second] = maximumsArray[pont_dim];
//
//                boundaryArray_[first + (2 * dim)] = minimumsArray[pont_dim];
//                boundaryArray_[second + (2 * dim)] = maximumsArray[pont_dim];
//            }
//
//        } else {
//
//            System.out.println("Implementar para dominio triangular");
//
//        }
//
//        this.params_ = params;

    }

    public BifurcationCurve curve() {
        try {
            return (BifurcationCurve) CurveDomainManager.instance().calculateInitialCurve();
        } catch (NoContourMethodDefined ex) {
            ex.printStackTrace();
            return null;
        }

        /*resolution_[0] = new Integer(RPNUMERICS.getContourConfiguration().getParamValue("xminus-resolution"));
        resolution_[1] = new Integer(RPNUMERICS.getContourConfiguration().getParamValue("yminus-resolution"));
        resolution_[2] = new Integer(RPNUMERICS.getContourConfiguration().getParamValue("xplus-resolution"));
        resolution_[3] = new Integer(RPNUMERICS.getContourConfiguration().getParamValue("xplus-resolution"));
        Boundary boundary = RPNUMERICS.boundary();
        if (boundary instanceof RectBoundary) {
        int dim = RPNUMERICS.domainDim();
        boundaryArray_ = new double[2*2*dim];
        RealVector minimums = boundary.getMinimums();
        RealVector maximums = boundary.getMaximums();
        double[] minimumsArray = minimums.toDouble();
        double[] maximumsArray = maximums.toDouble();
        for(int pont_dim = 0; pont_dim < dim; pont_dim++) {
        int first  = 2*pont_dim;
        int second = 2*pont_dim + 1;
        boundaryArray_[first] = minimumsArray[pont_dim];
        boundaryArray_[second] = maximumsArray[pont_dim];
        boundaryArray_[first + (2*dim)] = minimumsArray[pont_dim];
        boundaryArray_[second + (2*dim)] = maximumsArray[pont_dim];
        }
        } else {
        System.out.println("Implementar para dominio triangular");
        } */
        //            boundaryArray_[0] = new Double (RPNUMERICS.getContourConfiguration().getParamValue("x0minus-boundary"));
        //            boundaryArray_[1] = new Double (RPNUMERICS.getContourConfiguration().getParamValue("xFminus-boundary"));
        //            boundaryArray_[2] = new Double (RPNUMERICS.getContourConfiguration().getParamValue("y0minus-boundary"));
        //            boundaryArray_[3] = new Double (RPNUMERICS.getContourConfiguration().getParamValue("yFminus-boundary"));
        //            boundaryArray_[4] = new Double (RPNUMERICS.getContourConfiguration().getParamValue("x0plus-boundary"));
        //            boundaryArray_[5] = new Double (RPNUMERICS.getContourConfiguration().getParamValue("xFplus-boundary"));
        //            boundaryArray_[6] = new Double (RPNUMERICS.getContourConfiguration().getParamValue("y0plus-boundary"));
        //            boundaryArray_[7] = new Double (RPNUMERICS.getContourConfiguration().getParamValue("yFplus-boundary"));
        //
        //            numberOfSubDomains = new Integer(RPNUMERICS.getContourConfiguration().getParamValue("subdomains"));
        //            subDomainsResolutions = new int[numberOfSubDomains][4];
        //            subDomainsBoundaryArray = new double[numberOfSubDomains][8];
        //
        //
        //            for (int pont_subdomain = 0; pont_subdomain < numberOfSubDomains; pont_subdomain++) {
        //
        //                 // verificar se estah dentro do dominio    total
        //
        //
        //                String tmp =  X_Minus_Resolution + "-" + (pont_subdomain + 1);
        //                subDomainsResolutions[pont_subdomain][0] = new Integer(RPNUMERICS.getContourConfiguration().getParamValue(tmp));
        //                tmp =  Y_Minus_Resolution + "-" + (pont_subdomain + 1);
        //                subDomainsResolutions[pont_subdomain][1] = new Integer(RPNUMERICS.getContourConfiguration().getParamValue(tmp));
        //                tmp =  X_Plus_Resolution + "-" + (pont_subdomain + 1);
        //                subDomainsResolutions[pont_subdomain][2] = new Integer(RPNUMERICS.getContourConfiguration().getParamValue(tmp));
        //                tmp =  Y_Plus_Resolution + "-" + (pont_subdomain + 1);
        //                subDomainsResolutions[pont_subdomain][3] = new Integer(RPNUMERICS.getContourConfiguration().getParamValue(tmp));
        //
        //                tmp = X_0_Minus_Boundary + "-" + (pont_subdomain + 1);
        //                subDomainsBoundaryArray[pont_subdomain][0] = new Double(RPNUMERICS.getContourConfiguration().getParamValue(tmp));
        //                subDomainsBoundaryArray[pont_subdomain][0] = calculateNextLowerPoint( boundaryArray_[0], boundaryArray_[1], resolution_[0], subDomainsBoundaryArray[pont_subdomain][0]);
        //                tmp = Y_0_Minus_Boundary + "-" + (pont_subdomain + 1);
        //                subDomainsBoundaryArray[pont_subdomain][2] = new Double(RPNUMERICS.getContourConfiguration().getParamValue(tmp));
        //                subDomainsBoundaryArray[pont_subdomain][2] = calculateNextLowerPoint( boundaryArray_[2], boundaryArray_[3], resolution_[1], subDomainsBoundaryArray[pont_subdomain][2]);
        //
        //                tmp = X_F_Minus_Boundary + "-" + (pont_subdomain + 1);
        //                subDomainsBoundaryArray[pont_subdomain][1] = new Double(RPNUMERICS.getContourConfiguration().getParamValue(tmp));
        //                subDomainsBoundaryArray[pont_subdomain][1] = calculateNextUpperPoint( boundaryArray_[0], boundaryArray_[1], resolution_[0], subDomainsBoundaryArray[pont_subdomain][1]);
        //                tmp = Y_F_Minus_Boundary + "-" + (pont_subdomain + 1);
        //                subDomainsBoundaryArray[pont_subdomain][3] = new Double(RPNUMERICS.getContourConfiguration().getParamValue(tmp));
        //                subDomainsBoundaryArray[pont_subdomain][3] = calculateNextUpperPoint( boundaryArray_[2], boundaryArray_[3], resolution_[1], subDomainsBoundaryArray[pont_subdomain][3]);
        //
        //                tmp = X_0_Plus_Boundary + "-" + (pont_subdomain + 1);
        //                subDomainsBoundaryArray[pont_subdomain][4] = new Double(RPNUMERICS.getContourConfiguration().getParamValue(tmp));
        //                subDomainsBoundaryArray[pont_subdomain][4] = calculateNextLowerPoint( boundaryArray_[4], boundaryArray_[5], resolution_[2], subDomainsBoundaryArray[pont_subdomain][4]);
        //                tmp = Y_0_Plus_Boundary + "-" + (pont_subdomain + 1);
        //                subDomainsBoundaryArray[pont_subdomain][6] = new Double(RPNUMERICS.getContourConfiguration().getParamValue(tmp));
        //                subDomainsBoundaryArray[pont_subdomain][6] = calculateNextLowerPoint( boundaryArray_[6], boundaryArray_[7], resolution_[3], subDomainsBoundaryArray[pont_subdomain][6]);
        //                tmp = X_F_Plus_Boundary + "-" + (pont_subdomain + 1);
        //                subDomainsBoundaryArray[pont_subdomain][5] = new Double(RPNUMERICS.getContourConfiguration().getParamValue(tmp));
        //                subDomainsBoundaryArray[pont_subdomain][5] = calculateNextUpperPoint( boundaryArray_[4], boundaryArray_[5], resolution_[2], subDomainsBoundaryArray[pont_subdomain][5]);
        //                tmp = Y_F_Plus_Boundary + "-" + (pont_subdomain + 1);
        //                subDomainsBoundaryArray[pont_subdomain][7] = new Double(RPNUMERICS.getContourConfiguration().getParamValue(tmp));
        //                subDomainsBoundaryArray[pont_subdomain][7] = calculateNextUpperPoint( boundaryArray_[6], boundaryArray_[7], resolution_[3], subDomainsBoundaryArray[pont_subdomain][7]);
        //
        //                if (!((subDomainsBoundaryArray[pont_subdomain][0] < subDomainsBoundaryArray[pont_subdomain][1]) &&
        //                      (subDomainsBoundaryArray[pont_subdomain][2] < subDomainsBoundaryArray[pont_subdomain][3]) &&
        //                      (subDomainsBoundaryArray[pont_subdomain][4] < subDomainsBoundaryArray[pont_subdomain][5]) &&
        //                      (subDomainsBoundaryArray[pont_subdomain][6] < subDomainsBoundaryArray[pont_subdomain][7]))){
        //                    // test if points are given on the right format
        //                    return new CanNotCalculateCurve();
        //                }
        //
        //                for (int pont_subdomain_comp = 0; pont_subdomain_comp < pont_subdomain; pont_subdomain_comp++) {
        //
        //                    double UL1x, UL1y, LR1x, LR1y;
        //                    double UL2x, UL2y, LR2x, LR2y;
        //
        //                    // Minus Side
        //                    UL1x = subDomainsBoundaryArray[pont_subdomain_comp][0];
        //                    UL1y = subDomainsBoundaryArray[pont_subdomain_comp][3];
        //
        //                    LR1x = subDomainsBoundaryArray[pont_subdomain_comp][1];
        //                    LR1y = subDomainsBoundaryArray[pont_subdomain_comp][2];
        //
        //                    UL2x = subDomainsBoundaryArray[pont_subdomain][0];
        //                    UL2y = subDomainsBoundaryArray[pont_subdomain][3];
        //
        //                    LR2x = subDomainsBoundaryArray[pont_subdomain][1];
        //                    LR2y = subDomainsBoundaryArray[pont_subdomain][2];
        //
        //                    if((LR1y < UL2y) &&
        //                       (LR1x > UL2x) &&
        //                       (UL1x < LR2x) &&
        //                       (UL1y > LR2y)) {
        //                        return new CanNotCalculateCurve();
        //                    }
        //
        //                     // Plus Side
        //                    UL1x = subDomainsBoundaryArray[pont_subdomain][4];
        //                    UL1y = subDomainsBoundaryArray[pont_subdomain][7];
        //
        //                    LR1x = subDomainsBoundaryArray[pont_subdomain][5];
        //                    LR1y = subDomainsBoundaryArray[pont_subdomain][6];
        //
        //                    UL2x = subDomainsBoundaryArray[pont_subdomain_comp][4];
        //                    UL2y = subDomainsBoundaryArray[pont_subdomain_comp][7];
        //
        //                    LR2x = subDomainsBoundaryArray[pont_subdomain_comp][5];
        //                    LR2y = subDomainsBoundaryArray[pont_subdomain_comp][6];
        //
        //                    if((LR1y < UL2y) &&
        //                       (LR1x > UL2x) &&
        //                       (UL1x < LR2x) &&
        //                       (UL1y > LR2y)) {
        //                        return new CanNotCalculateCurve();
        //                    }
        //
        //                 } // for subdomain_comp
        //
        //                } // for subdomain
        //
        //                ArrayList realSegments = new ArrayList();
        //
        //	        RealVector p1 = null;
        //	        RealVector p2 = null;
        //
        //                RealVector p3 = null;
        //                RealVector p4 = null;
        //
        //		BifurcationCurve bifurcationCurve = null;
        //
        //		try {
        //
        //			RPnCurve curve = contourMethod_.curvND(boundaryArray_, resolution_);
        //
        //                        //filtrar
        //
        //	        PointNDimension[][] polyline = curve.getPolylines();
        //
        //	        for (int polyLineIndex = 0; polyLineIndex < polyline.length; polyLineIndex++) {
        //
        //	            int size = polyline[polyLineIndex].length;
        //
        //	            CoordsArray[] coords = new CoordsArray[size];
        //
        //	            for (int polyPoint = 0; polyPoint < size; polyPoint++) {
        //	            	coords[polyPoint] = polyline[polyLineIndex][polyPoint].toCoordsArray();
        //	            }
        //
        //	            for (int i = 0; i < coords.length - 2; i++) {
        //	            	double[] firstPoint = coords[i].getCoords();
        //                        double[] secondPoint = coords[i + 1].getCoords();
        //
        //	                p1 = new RealVector(minusSide(firstPoint));
        //	                p2 = new RealVector(minusSide(secondPoint));
        //
        //                        p3 = new RealVector(plusSide(firstPoint));
        //	                p4 = new RealVector(plusSide(secondPoint));
        //
        //                        if (!(isInRestrictions(p1, p2, p3, p4, subDomainsBoundaryArray))) {
        //                            realSegments.add(new RealSegment(new RealVector(firstPoint), new RealVector(secondPoint)));
        //                        }
        //	            } // for coordiantes
        //
        //                    double[] firstPoint = coords[coords.length - 2].getCoords();
        //                    double[] secondPoint = coords[coords.length - 1].getCoords();
        //
        //                    p1 = new RealVector(minusSide(firstPoint));
        //                    p2 = new RealVector(minusSide(secondPoint));
        //
        //                    p3 = new RealVector(plusSide(firstPoint));
        //                    p4 = new RealVector(plusSide(secondPoint));
        //
        //                    if (!(isInRestrictions(p1, p2, p3, p4, subDomainsBoundaryArray))) {
        //                            realSegments.add(new RealSegment(new RealVector(firstPoint), new RealVector(secondPoint)));
        //                    }
        //                }// for polilineIndex
        //
        //	        } catch (Exception e1) {
        //                    e1.printStackTrace();
        //                    return new CanNotCalculateCurve();
        //                } //try fo primeiro ContourND
        //
        //                for (int pont_subdomain = 0; pont_subdomain < numberOfSubDomains; pont_subdomain++) {
        //                   try {
        //
        //                        String tmp = "operation-" + (pont_subdomain + 1);
        //                        int operation = new Integer(RPNUMERICS.getContourConfiguration().getParamValue(tmp));
        //
        //                        if (operation == 1) {
        //                            RPnCurve curveSubDomain = contourMethod_.curvND(subDomainsBoundaryArray[pont_subdomain], subDomainsResolutions[pont_subdomain]);
        //
        //                            if (curveSubDomain != null) {
        //                                PointNDimension[][] polyline1 = curveSubDomain.getPolylines();
        //
        //                                RealVector p11 = null;
        //                                RealVector p21 = null;
        //
        //                                RealVector p31 = null;
        //                                RealVector p41 = null;
        //
        //                                for (int polyLineIndex = 0; polyLineIndex < polyline1.length; polyLineIndex++) {
        //
        //                                    int size = polyline1[polyLineIndex].length;
        //
        //                                    CoordsArray[] coords1 = new CoordsArray[size];
        //
        //                                    for (int polyPoint = 0; polyPoint < size; polyPoint++) {
        //                                        coords1[polyPoint] = polyline1[polyLineIndex][polyPoint].toCoordsArray();
        //                                    }
        //
        //                                    for (int i = 0; i < coords1.length - 2; i++) {
        //                                        double[] firstPoint = coords1[i].getCoords();
        //                                        double[] secondPoint = coords1[i + 1].getCoords();
        //
        //                                        realSegments.add(new RealSegment(new RealVector(firstPoint), new RealVector(secondPoint)));
        //                                    }
        //
        //                                    double[] firstPoint = coords1[coords1.length - 2].getCoords();
        //                                    double[] secondPoint = coords1[coords1.length - 1].getCoords();
        //
        //                                    realSegments.add(new RealSegment(new RealVector(firstPoint), new RealVector(secondPoint)));
        //
        //                            } //for polulineIndex
        //                        bifurcationCurve = new BifurcationCurve(familyIndex, realSegments);
        //                    }
        //                } //if
        //
        //
        //
        //	    } catch (CanNotPerformCalculations ex) {
        //	        ex.printStackTrace();
        //                return new CanNotCalculateCurve();
        //	    } // try
        //
        //
        //	} // for subdomain
        //        return bifurcationCurve;

    }

    public BifurcationParams getParams() {
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

        Boundary boundary = RPNUMERICS.boundary();
        double resolution[] = {30, 30};
        RealVector testeResolution = new RealVector(resolution);
        Area resultArea = new Area(testeResolution, boundary.getMaximums(), boundary.getMinimums());

        return resultArea;
    }
}
