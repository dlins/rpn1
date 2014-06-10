#ifndef _COREYQUADHUGONIOTCURVE_
#define _COREYQUADHUGONIOTCURVE_

#include "HugoniotCurve.h"
#include "CoreyQuad.h"
#include "Stone_Explicit_Bifurcation_Curves.h"

#define COREYQUADHUGONIOTCURVE_GENERIC_POINT 0
#define COREYQUADHUGONIOTCURVE_G_VERTEX      1
#define COREYQUADHUGONIOTCURVE_W_VERTEX      2
#define COREYQUADHUGONIOTCURVE_O_VERTEX      3
#define COREYQUADHUGONIOTCURVE_GW_SIDE       4
#define COREYQUADHUGONIOTCURVE_WO_SIDE       5
#define COREYQUADHUGONIOTCURVE_GO_SIDE       6
#define COREYQUADHUGONIOTCURVE_G_BIFURCATION 7
#define COREYQUADHUGONIOTCURVE_W_BIFURCATION 8
#define COREYQUADHUGONIOTCURVE_O_BIFURCATION 9
#define COREYQUADHUGONIOTCURVE_UMBILIC_POINT 10

class CoreyQuadHugoniotCurve : public HugoniotCurve {
    private:
    protected:
        const CoreyQuad *flux;
        Stone_Explicit_Bifurcation_Curves *sebc;

        RealVector G_vertex, W_vertex, O_vertex;
    public:
        CoreyQuadHugoniotCurve(const CoreyQuad *ff, const AccumulationFunction *aa, Stone_Explicit_Bifurcation_Curves *s, const Boundary *b);
        virtual ~CoreyQuadHugoniotCurve();

        void types(std::vector<int> &type, std::vector<std::string> &name) const {
            type.clear();
            name.clear();

            type.push_back(COREYQUADHUGONIOTCURVE_GENERIC_POINT);
            name.push_back(std::string("Generic point"));

            type.push_back(COREYQUADHUGONIOTCURVE_G_VERTEX);
            name.push_back(std::string("G vertex"));

            type.push_back(COREYQUADHUGONIOTCURVE_W_VERTEX);
            name.push_back(std::string("W vertex"));

            type.push_back(COREYQUADHUGONIOTCURVE_O_VERTEX);
            name.push_back(std::string("O vertex"));

            type.push_back(COREYQUADHUGONIOTCURVE_GW_SIDE);
            name.push_back(std::string("GW side"));

            type.push_back(COREYQUADHUGONIOTCURVE_WO_SIDE);
            name.push_back(std::string("WO side"));

            type.push_back(COREYQUADHUGONIOTCURVE_GO_SIDE);
            name.push_back(std::string("GO side"));

            type.push_back(COREYQUADHUGONIOTCURVE_G_BIFURCATION);
            name.push_back(std::string("G bifurcation"));

            type.push_back(COREYQUADHUGONIOTCURVE_W_BIFURCATION);
            name.push_back(std::string("W bifurcation"));

            type.push_back(COREYQUADHUGONIOTCURVE_O_BIFURCATION);
            name.push_back(std::string("O bifurcation"));

            type.push_back(COREYQUADHUGONIOTCURVE_UMBILIC_POINT);
            name.push_back(std::string("Umbilic point"));            

            return;
        }

        virtual void curve(const ReferencePoint &ref, int type, std::vector<HugoniotPolyLine> &classified_curve){
            HugoniotCurve::curve(ref, type, classified_curve);

            return;
        }

        // This method will move upwards to ThreePhaseFlowPhysics.
        //
        RealVector project(const RealVector &p, int type);
};

#endif // _COREYQUADHUGONIOTCURVE_

