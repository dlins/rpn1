/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rpnumerics.methods.contour.support;

import rpnumerics.methods.contour.*;
import rpnumerics.*;
import rpnumerics.methods.contour.exceptions.*;

import java.util.*;
import java.util.List;
import java.awt.*;
import wave.util.*;
import wave.multid.*;
import wave.multid.view.*;

public class CurveDomainManager {

    private static CurveDomainManager instance_ = null;
    private ContourND contourMethod = null;
    private Area area = null;
    
    private double[] boundaryArray;
    private int[] resolution;
    private int dimension;

    private CurveDomainManager() {

    }

    public static CurveDomainManager instance() {

        if (instance_ == null) {
            instance_ = new CurveDomainManager();
            return instance_;
        }

        return instance_;
    }

    public void initializeInitialCurve(ContourND contourMethod, Area area) {
        this.contourMethod = contourMethod;
        this.area = area;

        // set boundary and resolution

        RealVector downleft = area.getDownLeft();
        RealVector topright = area.getTopRight();
        RealVector resolutionArea = area.getResolution();

        dimension = (downleft.getSize() / 2);

        boundaryArray = new double[dimension * 2];
        resolution = new int[dimension];

        for (int pont_dimension = 0; pont_dimension < dimension; pont_dimension++) {
            resolution[pont_dimension] = (int) resolutionArea.getElement(pont_dimension);
            boundaryArray[2*pont_dimension] = downleft.getElement(pont_dimension);
            boundaryArray[2*pont_dimension + 1] = topright.getElement(pont_dimension);
        }

    }

    public ContourND getContourMethod() throws NoContourMethodDefined {

        if (contourMethod == null) {
            throw new NoContourMethodDefined();
        }

        return this.contourMethod;
    }

    public Area getArea() {
        return this.area;
    }

    public RPnCurve calculateInitialCurve() throws NoContourMethodDefined {
        /*
         * it returns a curve based on the area set on RPNProfile
         */

        if ((contourMethod == null) || (area == null))  {
            throw new NoContourMethodDefined();
        }

        ArrayList realSegments = new ArrayList();

        RealVector p1 = null;
        RealVector p2 = null;

        RPnCurve curve = null;

        try {

            curve = contourMethod.curvND(boundaryArray, resolution);

            PointNDimension[][] polyline = curve.getPolylines();

            for (int polyLineIndex = 0; polyLineIndex < polyline.length; polyLineIndex++) {

                int size = polyline[polyLineIndex].length;

                CoordsArray[] coords = new CoordsArray[size];

                for (int polyPoint = 0; polyPoint < size; polyPoint++) {
                    coords[polyPoint] = polyline[polyLineIndex][polyPoint].toCoordsArray();
                }

                for (int i = 0; i < coords.length - 2; i++) {
                    double[] firstPoint = coords[i].getCoords();
                    double[] secondPoint = coords[i + 1].getCoords();

                    p1 = new RealVector(firstPoint);
                    p2 = new RealVector(secondPoint);

                    realSegments.add(new RealSegment(new RealVector(firstPoint), new RealVector(secondPoint)));

                } // for coordiantes

                double[] firstPoint = coords[coords.length - 2].getCoords();
                double[] secondPoint = coords[coords.length - 1].getCoords();

                p1 = new RealVector(firstPoint);
                p2 = new RealVector(secondPoint);

                realSegments.add(new RealSegment(new RealVector(firstPoint), new RealVector(secondPoint)));

            }// for polilineIndex

        } catch (Exception e1) {
            e1.printStackTrace();
            return new CanNotCalculateCurve();
        } //try fo primeiro ContourND

        return new RPnCurve(coordsArrayFromRealSegments(realSegments), new ViewingAttr(Color.white));

    }

    public void repositionAreas(List areasList) throws DimensionDoenstMatch {

        Area lastArea = (Area) areasList.get((areasList.size() - 1));

        RealVector downleft = lastArea.getDownLeft();
        RealVector topright = lastArea.getTopRight();
        RealVector resolutionArea = lastArea.getResolution();

        int dimensionArea  = (downleft.getSize() / 2);

        if (dimensionArea == dimension) {

            double[] boundaryNewArea = new double[dimension * 2];
            
            for (int pont_dimension = 0; pont_dimension < dimension; pont_dimension++) {
                boundaryNewArea[pont_dimension] = calculateNextLowerPoint(boundaryArray[2*pont_dimension] , boundaryArray[2*pont_dimension + 1], resolution[pont_dimension], downleft.getElement(pont_dimension));
            }

            for (int pont_dimension = 0; pont_dimension < dimension; pont_dimension++) {
                boundaryNewArea[pont_dimension + dimension] = calculateNextUpperPoint(boundaryArray[2*pont_dimension] , boundaryArray[2*pont_dimension + 1], resolution[pont_dimension], topright.getElement(pont_dimension));
            }

            RealVector newDownLeft = new RealVector(dimension);
            RealVector newTopRight = new RealVector(dimension);
            
            areasList.remove((areasList.size() - 1));
            areasList.add(new Area(resolutionArea, newTopRight, newDownLeft));
            
        } else {
            throw new DimensionDoenstMatch();
        }
        
    }

    public RPnCurve fillSubDomain(RPnCurve curve, Area subdomain) throws NoContourMethodDefined{

        RPnCurve newCurve = null;

        double[] boundaryArrayTemp = new double[dimension * 2];
        int[] resolutionTemp = new int[dimension];

        for (int pont_dimension = 0; pont_dimension < dimension; pont_dimension++) {
            resolutionTemp[pont_dimension] = resolution[pont_dimension];
            boundaryArrayTemp[2*pont_dimension] = boundaryArray[2*pont_dimension];
            boundaryArrayTemp[2*pont_dimension + 1] = boundaryArray[2*pont_dimension + 1];
        }
        
        // pegar area e sudomain

        RealVector downleft = area.getDownLeft();
        RealVector topright = area.getTopRight();
        RealVector resolutionArea = area.getResolution();

        try {
            for (int pont_dimension = 0; pont_dimension < dimension; pont_dimension++) {
                    boundaryArray[2*pont_dimension] = calculateNextLowerPoint(boundaryArray[2*pont_dimension] , boundaryArray[2*pont_dimension + 1], resolution[pont_dimension], downleft.getElement(2*pont_dimension));
                    this.resolution[2*pont_dimension] = (int) resolutionArea.getElement(2*pont_dimension);
            }

            for (int pont_dimension = 0; pont_dimension < dimension; pont_dimension++) {
                boundaryArray[2*pont_dimension + 1] = calculateNextUpperPoint(boundaryArray[2*pont_dimension] , boundaryArray[2*pont_dimension + 1], resolution[pont_dimension], topright.getElement(pont_dimension));
                this.resolution[2*pont_dimension + 1] = (int) resolutionArea.getElement(2*pont_dimension + 1);
            }
        } catch (Exception e) {

        }

        // code goes here
        PointNDimension[][] polyline = curve.getPolylines();

        PointNDimension[][] cleanedPolyline = new PointNDimension[polyline.length][];

        Vector[] tempPolylines = new Vector[polyline.length];

        for (int pont_polyline = 0; pont_polyline < polyline.length; pont_polyline++) {

            int numberOfPoints = polyline[pont_polyline].length;

            for (int pont_point = 0; pont_point < numberOfPoints; pont_point++) {

                boolean InDomain = true;
                PointNDimension point = polyline[pont_polyline][pont_point];

                for (int pont_dimension = 0; (pont_dimension < dimension) && InDomain; pont_dimension++) {
                    try {
                        if ((boundaryArray[2*pont_dimension] < point.getCoordinate(pont_dimension + 1)) || (boundaryArray[2*pont_dimension + 1] > point.getCoordinate(pont_dimension + 1))) {
                            InDomain = false;
                        }
                    } catch (Exception e) {

                    }
                }

                if (!InDomain) {
                    tempPolylines[pont_polyline].add(area);
                }
            }
        }

        for (int pont_polyline = 0; pont_polyline < polyline.length; pont_polyline++) {
            Object[] points = tempPolylines[pont_polyline].toArray();
            cleanedPolyline[pont_polyline] = (PointNDimension []) points[pont_polyline];
        }

        // limpar
        newCurve = new RPnCurve(cleanedPolyline, curve.viewingAttr());
        // set domain
        RPnCurve tempCurve = calculateInitialCurve();

        PointNDimension[][] tempPolyline = tempCurve.getPolylines();

        for (int pont_polyline = 0; pont_polyline < polyline.length; pont_polyline++) {
            try {
                newCurve.append(tempPolyline[pont_polyline]);
            } catch (Exception e) {

            }
        }
        
        for (int pont_dimension = 0; pont_dimension < dimension; pont_dimension++) {
            resolution[pont_dimension] = resolutionTemp[pont_dimension];
            boundaryArray[2*pont_dimension] = boundaryArrayTemp[2*pont_dimension];
            boundaryArray[2*pont_dimension + 1] = boundaryArrayTemp[2*pont_dimension + 1];
        }

        return newCurve;

    }

    private CoordsArray[] coordsArrayFromRealSegments(List segments) {

        ArrayList tempCoords = new ArrayList(segments.size());
        for (int i = 0; i < segments.size(); i++) {
            RealSegment segment = (RealSegment) segments.get(i);
            tempCoords.add(new CoordsArray(segment.p1()));
            tempCoords.add(new CoordsArray(segment.p2()));

        }

        CoordsArray[] coords = new CoordsArray[tempCoords.size()];
        for (int i = 0; i < tempCoords.size(); i++) {
            coords[i] = (CoordsArray) tempCoords.get(i);
        }
        tempCoords = null;
        return coords;

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

    private boolean isInRestrictions(RealVector p1, RealVector p2, RealVector p3, RealVector p4, double [][]boundaries) {

       /* double[] point1Minus = p1.toDouble();
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

            if ((((yp1Minus >= yl1Minus) && (yp1Minus <= yl2Minus)) &&
                 ((xp1Minus >= xl1Minus) && (xp1Minus <= xl2Minus))) ||
                (((xp2Minus >= xl1Minus) && (xp2Minus <= xl2Minus)) &&
                 ((yp2Minus >= yl1Minus) && (yp2Minus <= yl2Minus)))) {
               return true;
            }

        // Plus

           double xl1Plus = boundaries[pont_subdomain][4];
           double yl1Plus = boundaries[pont_subdomain][6];

           double xl2Plus = boundaries[pont_subdomain][5];
           double yl2Plus = boundaries[pont_subdomain][7];

            if ((((yp1Plus >= yl1Plus) && (yp1Plus <= yl2Plus)) &&
                 ((xp1Plus >= xl1Plus) && (xp1Plus <= xl2Plus))) ||
                (((xp2Plus >= xl1Plus) && (xp2Plus <= xl2Plus)) &&
                 ((yp2Plus >= yl1Plus) && (yp2Plus <= yl2Plus)))) {


               return true;
            }
       } */

        return false;
    }
    
}
