#ifndef _HUGONIOTCURVE_
#define _HUGONIOTCURVE_

#include <vector>
#include "ColorCurve.h"
#include "Curve.h"
#include "ReferencePoint.h"

#define EXPLICIT_HUGONIOT 0
#define IMPLICIT_HUGONIOT 1

// Abstract Base Class for both implicit and explicit Hugoniot curves.
//
class HugoniotCurve {
    private:
    protected:
        ReferencePoint reference_point;

        const FluxFunction         *f;
        const AccumulationFunction *a;
        const Boundary             *boundary;

        int method_;
    public:
        HugoniotCurve(const FluxFunction *ff, const AccumulationFunction *aa, const Boundary *bb) : f(ff), a(aa), boundary(bb){
        }

        virtual ~HugoniotCurve(){
        }

        // Parameters:
        //
        //      ref: The reference point for this particular curve.
        //
        //     type: Identifies special cases of ref.point (generic, on a specific side of the domain, 
        //           on a secondary bifurcation, etc.).
        //
        //        c: Output.
        // 
        virtual void curve(const ReferencePoint &ref, int type, std::vector<Curve> &c) = 0;

        virtual void curve(const ReferencePoint &ref, int type, std::vector<HugoniotPolyLine> &classified_curve){
            std::vector<Curve> cc;

            curve(ref, type, cc);
            

            ColorCurve colorcurve(*f, *a, (const Viscosity_Matrix*)0);

            for (int i = 0; i < cc.size(); i++){
                if (cc[i].curve.size() > 0){

                    std::vector<RealVector> segmented_curve;
                    for (int j = 0; j < cc[i].curve.size() - 1; j++){
                        segmented_curve.push_back(cc[i].curve[j]);
                        segmented_curve.push_back(cc[i].curve[j + 1]);
                    }

                    std::vector<HugoniotPolyLine> temp_classified_curve;
                    std::vector<RealVector> transition_list;

                    colorcurve.classify_segmented_curve(segmented_curve, ref, temp_classified_curve, transition_list);

                    for (int j = 0; j < temp_classified_curve.size(); j++) classified_curve.push_back(temp_classified_curve[j]);
                }
            }

            return;
        }

        // Derived classes will fill these two vectors, name being the descriptor of type.
        // E.g.: For Stone these values could be used:
        //
        //     type[0]  = 0,  name[0]  = std::string("G vertex"),
        //     type[1]  = 1,  name[1]  = std::string("W vertex"),
        //     type[2]  = 2,  name[2]  = std::string("O vertex"),
        //     type[3]  = 3,  name[3]  = std::string("GW side"),
        //     type[4]  = 4,  name[4]  = std::string("WO side"),
        //     type[5]  = 5,  name[5]  = std::string("GO side"),
        //     type[6]  = 6,  name[6]  = std::string("G bifurcation"),
        //     type[7]  = 7,  name[7]  = std::string("W bifurcation"),
        //     type[8]  = 8,  name[8]  = std::string("O bifurcation"),
        //     type[9]  = 9,  name[9]  = std::string("Generic point"),
        //     type[10] = 10, name[10] = std::string("Umbilic point").
        //
        // The GUI could use this method to dynamically create a group of radio buttons.
        //
        virtual void types(std::vector<int> &type, std::vector<std::string> &name) const = 0;

        int implemented_method() const {return method_;}
};

#endif // _HUGONIOTCURVE_

