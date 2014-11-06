#ifndef _TRIVIALTESTSUBPHYSICS_
#define _TRIVIALTESTSUBPHYSICS_

#include "SubPhysics.h"
#include "RectBoundary.h"
#include "TrivialTestFluxFunction.h"
#include "StoneAccumulation.h"

class TrivialTestSubPhysics : public SubPhysics {
    private:
    protected:
    public:
        TrivialTestSubPhysics();
        virtual ~TrivialTestSubPhysics();
        
        void shock_cases(std::vector<int> &type, std::vector<std::string> &name) const;
};

#endif // _TRIVIALTESTSUBPHYSICS_
