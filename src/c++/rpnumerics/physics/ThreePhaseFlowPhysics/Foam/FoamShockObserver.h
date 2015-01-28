#ifndef _FOAMSHOCKOBSERVER_
#define _FOAMSHOCKOBSERVER_

#include "Observer.h"

class FoamSubPhysics;

class FoamShockObserver: public Observer {
    private:
    protected:
        FoamSubPhysics *foam_;
    public:
        FoamShockObserver(FoamSubPhysics *f);
        virtual ~FoamShockObserver();

        void change();
};

#endif // _FOAMSHOCKOBSERVER_

