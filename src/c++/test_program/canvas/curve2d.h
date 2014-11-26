#ifndef _CURVE2D_
#define _CURVE2D_

#include <FL/Fl.H>
#include <FL/fl_draw.H>

#include "point2d.h"
#include "graphicobject.h"

#include <vector>
#include <string>
#include <limits>

#include "RealVector.h"

#define CURVE2D_SOLID_LINE 1
#define CURVE2D_MARKERS    2
#define CURVE2D_INDICES    4
#define CURVE2D_ARROWS     8

class Curve2D : public GraphicObject {
    private:
    protected:
        vector<Point2D> vp;
        std::vector<std::string> str;

        int type_;        
        int style_;
        void transform(const double *matrix);

        std::vector<int> arrow_tip;
        std::vector<int> arrow_base;
    public:
        Curve2D(const RealVector &, double, double, double, int = CURVE2D_MARKERS);
        Curve2D(const vector<RealVector> &, double, double, double, int = CURVE2D_SOLID_LINE);
        Curve2D(const vector<RealVector> &, double, double, double, const std::vector<std::string> &strorg, int = CURVE2D_SOLID_LINE);

        Curve2D(const vector<Point2D> &, double, double, double, int = CURVE2D_SOLID_LINE);
        Curve2D(const vector<Point2D> &, double, double, double, const std::vector<std::string> &strorg, int = CURVE2D_SOLID_LINE);
        virtual ~Curve2D();
        void draw();
        void minmax(Point2D &, Point2D &);
        void pstricks(ofstream *);

        // Set/get type.
        //
        void type(int t){type_ = t; draw(); return;}
        int type(void){return type_;}

        void style(int s){style_ = s;}

        // Set the arrowheads that will be drawn. It seems to be better to have
        // this as a separate method and not in the ctor.
        //
        void set_arrowheads(const std::vector<int> &aht, const std::vector<int> &ahb){
            clear_arrowheads();

            for (int i = 0; i < aht.size(); i++){
                arrow_tip.push_back(aht[i]);
                arrow_base.push_back(ahb[i]);
            }

            return;
        }
 
        // Clear the arrowheads.
        //
        void clear_arrowheads(){
            arrow_tip.clear();
            arrow_base.clear();

            return;
        }
};

#endif // _CURVE2D_

