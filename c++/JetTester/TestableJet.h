#ifndef _TESTABLEJET_
#define _TESTABLEJET_

#include "JetTester.h"

class TestableJet {
    private:
    protected:
    public:
        TestableJet();
        virtual ~TestableJet();

        virtual void list_of_functions(std::vector<int (*)(void*, const RealVector&, JetMatrix&)>) = 0;
};

#endif // _TESTABLEJET_

