#include "marchingsquares.h"

// Powers of 2
int MarchingSquares::twopower[4] = {1, 2, 4, 8};

// Marching Squares Look-up Table
//
// Legend:
//          *: positive vertex,
//          o: negative vertex,
//          +: intersection at this edge,
//     - or |: no intersection at this edge.
//
// Case 0:  o-o    Case 1:  o-o    Case 2:  o-o    Case 3:  o-o
//          | |             + |             | +             + +
//          o-o             *+o             o+*             *-*
// 
// Case 4:  o+*    Case 5:  o+*    Case 6:  o+*    Case 7:  o+*
//          | +             + +             | |             + |
//          o-o             *+o             o+*             *-*
//
// Case 8:  *+o    Case 9:  *+o    Case 10: *+o    Case 11: *+o
//          + |             | |             + +             | +
//          o-o             *+o             o+*             *-*
//
// Case 12: *-*    Case 13: *-*    Case 14: *-*    Case 15: *-*
//          + +             | +             + |             | |
//          o-o             *+o             o+*             *-*
//
// The numbers in the look-up table stand for the edges of the rectangle,
// and must be interpreted as couples. So in Case 5, for example, two segments
// will be computed: Between segments 0 and 1 and between segments 2 and 3. Where
// no intersection segments should be computed a -1 is used instead. So, in Case 0
// and Case 15 no intersections are to be computed.
//
int MarchingSquares::lut[16][4] = {-1, -1, -1, -1, // Case 0
                                    3,  0, -1, -1, // Case 1
                                    0,  1, -1, -1, // Case 2
                                    3,  1, -1, -1, // Case 3
                                    1,  2, -1, -1, // Case 4
                                    1,  2,  3,  0, // Case 5
                                    0,  2, -1, -1, // Case 6
                                    3,  2, -1, -1, // Case 7
                                    2,  3, -1, -1, // Case 8
                                    2,  0, -1, -1, // Case 9
                                    0,  1,  2,  3, // Case 10
                                    2,  1, -1, -1, // Case 11
                                    1,  3, -1, -1, // Case 12
                                    1,  0, -1, -1, // Case 13
                                    0,  3, -1, -1, // Case 14
                                   -1, -1, -1, -1, // Case 15
                                   };

// Find the point where a function is zero.
// It is assumed that the function was already evaluated at the extremes of 
// the edge.
Point2D MarchingSquares::zero(const int *e, int pos, const double *v, Point2D *vp){
    double alpha = (v[e[pos*2 + 1]])/(v[e[pos*2 + 1]] - v[e[pos*2 + 0]]);

    return Point2D(alpha*vp[e[pos*2 + 0]].x + (1 - alpha)*vp[e[pos*2 + 1]].x, 
                   alpha*vp[e[pos*2 + 0]].y + (1 - alpha)*vp[e[pos*2 + 1]].y);
}

// Marching Squares proper. This is the public interface.
//
// Parameters:
//         f: Pointer to a function that receives a Point2D and returns a double.
//            This is the implicit function whose contours will be computed.
//     level: A vector of levels.
//      pmin: The lower-left extreme of the domain.
//      pmax: The upper-right extreme of the domain.
//      m, n: Number of rows and columns into which the domain will be divided.
//        sc: A vector of pointers to objects of class SegmentedCurve: this will store
//            the resulting curves.
//
void MarchingSquares::marching_squares(double (*f)(Point2D &), const vector<double> &level, 
                                       const Point2D &pmin, const Point2D &pmax,
                                       int m, int n,
                                       vector<SegmentedCurve*> &sc){

    // Create the grid
    double xmin = min(pmin.x, pmax.x), xmax = max(pmin.x, pmax.x);
    double ymin = min(pmin.y, pmax.y), ymax = max(pmin.y, pmax.y);
    
    double dx = (xmax - xmin)/n, dy = (ymax - ymin)/m;

    // Create the mesh of points 
    Point2D *vp = (Point2D*)malloc((m + 1)*(n + 1)*sizeof(Point2D));
    for (int j = 0; j <= m; j++){
        for (int i = 0; i <= n; i++){
            vp[j*(n + 1) + i] = Point2D(xmin + i*dx, ymin + j*dy);
        }
    }

    // Create the mesh of edges
    int *edges = (int*)malloc(2*(m*(n + 1) + n*(m + 1))*sizeof(int));
    // Horizontal edges
    for (int i = 0; i <= m; i++){
        for (int j = 0; j < n; j++){
            edges[2*(2*n*i + i + j) + 0] = i*(n + 1) + j;
            edges[2*(2*n*i + i + j) + 1] = i*(n + 1) + j + 1;
        }
    }
    // Vertical edges
    for (int i = 0; i < m; i++){
        for (int j = 0; j <= n; j++){
            edges[2*(2*n*i + n + i + j) + 0] = i*(n + 1) + j; 
            edges[2*(2*n*i + n + i + j) + 1] = (i + 1)*(n + 1) + j;
        }
    }

    // Create the mesh of rectangles
    int *rects = (int*)malloc(8*m*n*sizeof(int));
    for (int i = 0; i < m; i++){
        for (int j = 0; j < n; j++){
            // Edges
            rects[8*(i*n + j) + 0] = (i + 1)*(n + 1) + (i + 1)*n + j;
            rects[8*(i*n + j) + 1] = i*(n + 1) + i*n + j + n + 1;
            rects[8*(i*n + j) + 2] = 2*i*n + i + j;
            rects[8*(i*n + j) + 3] = i*(n + 1) + i*n + j + n;

            // Points
            rects[8*(i*n + j) + 4] = (i + 1)*(n + 1) + j;
            rects[8*(i*n + j) + 5] = (i + 1)*(n + 1) + j + 1;
            rects[8*(i*n + j) + 6] = i*(n + 1) + j + 1;
            rects[8*(i*n + j) + 7] = i*(n + 1) + j;
        }
    }

    // Evaluate the function at the points
    double *v = (double*)malloc((m + 1)*(n + 1)*sizeof(double));
    double *vtemp = (double*)malloc((m + 1)*(n + 1)*sizeof(double));
    for (int i = 0; i < (m + 1)*(n + 1); i++) v[i] = f(vp[i]);

    // Marching Squares proper
    vector<Point2D> curve;

    for (int k = 0; k < level.size(); k++){
        curve.clear();
        for (int i = 0; i < m*n; i++){
            // Detect the case
            int lutcase = 0;
            for (int j = 0; j < 4; j++){
                lutcase += (v[rects[8*i + 4 + j]] - level[k] >= 0 ? 1 : 0)*twopower[j];
            }

            // Set the level to be found
            for (int j = 0; j < (m + 1)*(n + 1); j++) vtemp[j] = v[j] - level[k];

            // Find the points in the edges where the function becomes zero.
            for (int j = 0; j < 2; j++){
                if (lut[lutcase][2*j] != -1){
                    curve.push_back(zero(edges, rects[8*i + lut[lutcase][2*j] ], vtemp, vp));
                    curve.push_back(zero(edges, rects[8*i + lut[lutcase][2*j + 1] ], vtemp, vp));
                }
            }
        }

        SegmentedCurve *s = new SegmentedCurve(curve, 0, 0, 0);
        sc.push_back(s);
    }
 
    // Release the memory that was used
    free(v);
    free(rects);
    free(edges);
    free(vp);

    return;
}

