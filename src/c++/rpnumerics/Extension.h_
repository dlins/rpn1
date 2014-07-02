#ifndef _EXTENSION_
#define _EXTENSION_

#define EXTENSION_OK    0
#define EXTENSION_ERROR 1

#include <vector>
#include <string>
#include "RealVector.h"
#include "Curve.h"

class Extension {
    private:
    protected:
    public:
        Extension(){
        }

        virtual ~Extension(){
        }

        virtual int extension(const RealVector &p, RealVector &ext_p) = 0;

        virtual void extension_curve(const std::vector<RealVector> &curve, const std::vector<RealVector> &domain_polygon, const std::vector<RealVector> &image_polygon, std::vector<RealVector> &ext_curve){
            RealVector ext_p;

            for (int i = 0; i < curve.size(); i++){
                if (extension(curve[i], ext_p) == EXTENSION_OK) ext_curve.push_back(ext_p);
            }                

            return;
        }

        virtual void extension_curve(const Curve &curve, const std::vector<RealVector> &domain_polygon, const std::vector<RealVector> &image_polygon, Curve &ext_curve){
            extension_curve(curve.curve, domain_polygon, image_polygon, ext_curve.curve);

            for (int i = 0; i < curve.speed.size(); i++) ext_curve.speed.push_back(curve.speed[i]);

            ext_curve.type = COMPOSITE_CURVE;

            return;
        }

        virtual std::string name() const = 0;
};

#endif // _EXTENSION_

