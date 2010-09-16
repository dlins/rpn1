/*
 * IMPA - Fluid Dynamics Laboratory
 *
 * RPn Project
 *
 * @(#) WaveFlowFactory.h
 */

#ifndef _WaveFlowFactory_H
#define _WaveFlowFactory_H

/*
 * ---------------------------------------------------------------
 * Includes:
 */
#include "WaveFlow.h"
#include "RarefactionFlow.h"
#include "ConservationShockFlow.h"
#include "PhasePoint.h"
#include "ShockFlowParams.h"

/*
 * ---------------------------------------------------------------
 * Definitions:
 */

class WaveFlowFactory {
private:

    int static familyIndex_; //TODO Make acessors and mutators
    int static timeDirection_;


public:

    static WaveFlow * createWaveFlow(const char *, int, int, const FluxFunction &);
    static WaveFlow * createWaveFlow(const char *, const FluxFunction &);
    static void setFamilyIndex(int familyIndex);
    static void setTimeDirection(int timeDirection);
};

inline void WaveFlowFactory::setFamilyIndex(int familyIndex) {
    familyIndex_ = familyIndex;

}

inline void WaveFlowFactory::setTimeDirection(int timeDirection) {
    timeDirection_ = timeDirection;

}

inline WaveFlow * WaveFlowFactory::createWaveFlow(const char * flowName, int familyIndex, int timeDirection, const FluxFunction & flux) {


    if (!strcmp(flowName, "RarefactionFlow")) {

        return new RarefactionFlow(familyIndex, timeDirection, flux);

    }


    if (!strcmp(flowName, "ConservationShockFlow")) {

        RealVector realVector(2);
        PhasePoint phasePoint(realVector);
        ShockFlowParams params(phasePoint, 0);

        return new ConservationShockFlow(params, flux);



    }


}

inline WaveFlow * WaveFlowFactory::createWaveFlow(const char * flowName, const FluxFunction & flux) {

    WaveFlowFactory::timeDirection_ = 1; //TODO Hardcoded !!
    WaveFlowFactory::familyIndex_ = 0;

    if (!strcmp(flowName, "RarefactionFlow")) {


        return new RarefactionFlow(familyIndex_, timeDirection_, flux);

    }

    if (!strcmp(flowName, "ConservationShockFlow")) {
       
        RealVector realVector(2);
        PhasePoint phasePoint(realVector);
        ShockFlowParams params(phasePoint, 0);

        return new ConservationShockFlow(params, flux);

        


    }





}


#endif //! _WaveFlowFactory_H
