/**
 * IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) RpNumerics.h
 **/

#ifndef _RpNumerics_H
#define	_RpNumerics_H

#include "Physics.h"
#include "GridValuesFactory.h"
#include "StationaryPoint.h"
#include "WaveCurve.h"
#include "eigen.h"

class RpNumerics {
private:

    static Physics * physics_;
    static GridValuesFactory * gridValuesFactory_;
    static vector<StationaryPoint* > * stationaryPointVector_;
    static map<int, WaveCurve *> *waveCurveMap_;
    
    static double sigma;

    static int curveCounter;




public:

    static Physics & getPhysics();

    static GridValuesFactory & getGridFactory();

    static WaveCurve * getWaveCurve(int);

    static void addWaveCurve(WaveCurve *);

    static int getCurrentCurveID();

    static void increaseCurveID();

    static vector<StationaryPoint *> & getStationaryPointVector();

    static const FluxFunction & getFlux();

    static const AccumulationFunction & getAccumulation();

    static void setSigma(double);

    static double getSigma();

    static void setPhysics(const Physics &);

    static void clean();

    static void clearCurveMap();

    static void removeCurve(int);
    
    static void setEigenOrderFunction (const string & );
    
    static map<string,bool (*)(const eigenpair&, const eigenpair&)> *orderFunctionMap_;
 
    
    



};

inline Physics & RpNumerics::getPhysics() {
    return *physics_;
}

inline GridValuesFactory & RpNumerics::getGridFactory() {
    return *gridValuesFactory_;
}

inline WaveCurve * RpNumerics::getWaveCurve(int n) {
    return waveCurveMap_->at(n);
}

inline void RpNumerics::addWaveCurve(WaveCurve * wc) {
    waveCurveMap_->insert(std::pair<int, WaveCurve *>(curveCounter, wc));

}

inline vector<StationaryPoint *> & RpNumerics::getStationaryPointVector() {
    return *stationaryPointVector_;
}

inline int RpNumerics::getCurrentCurveID() {
    return curveCounter;
}

inline void RpNumerics::increaseCurveID() {
    curveCounter++;
}

inline const FluxFunction & RpNumerics::getFlux() {
    return physics_->fluxFunction();
}

inline const AccumulationFunction & RpNumerics::getAccumulation() {
    return physics_->accumulation();
}

inline void RpNumerics::setPhysics(const Physics & physics) {
    delete physics_;
    physics_ = physics.clone();
    delete gridValuesFactory_;
    gridValuesFactory_ = new GridValuesFactory(physics_);
    stationaryPointVector_ = new vector<StationaryPoint *>();
    waveCurveMap_ = new map<int, WaveCurve *>();
    
    
    orderFunctionMap_ = new  map<string,bool (*)(const eigenpair&, const eigenpair&)> ();

    vector<bool (*)(const eigenpair&, const eigenpair&) > order_function;
    
    vector<string> eigenSortFunctionNames;
      
    
    Eigen::list_order_eigenpairs(order_function, eigenSortFunctionNames);
    
    for (int i = 0; i < eigenSortFunctionNames.size(); i++) {
         orderFunctionMap_->insert(std::pair<string, bool (*)(const eigenpair&, const eigenpair&)>(eigenSortFunctionNames.at(i), order_function.at(i)));

    }
      
//    
//    for (std::map<string, bool (*)(const eigenpair&, const eigenpair&)>::iterator it = orderFunctionMap_->begin(); it != orderFunctionMap_->end(); ++it) {
//        cout<< "Primeiro: "<< it->first<<" Segundo: "<<it->second<<endl;
//    }
    
  
}


inline void RpNumerics::setEigenOrderFunction (const string & functionName){
    
    Eigen::set_order_eigenpairs(orderFunctionMap_->at(functionName));
    
}

inline void RpNumerics::clearCurveMap() {


    for (std::map<int, WaveCurve *>::iterator it = waveCurveMap_->begin(); it != waveCurveMap_->end(); ++it) {
        delete it->second;
    }

    waveCurveMap_->clear();

}

inline void RpNumerics::removeCurve(int curveID) {


    if (waveCurveMap_->count(curveID) == 1) {
        delete waveCurveMap_->at(curveID);
        waveCurveMap_->erase(curveID);
    }


}

inline void RpNumerics::setSigma(double s) {
    sigma = s;
}

inline double RpNumerics::getSigma() {
    return sigma;
}






#endif	/* _JNIDEFS_H */

