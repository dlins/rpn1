#ifndef _TESTABLEJET_
#define _TESTABLEJET_

#include "RealVector.h"
#include "JetMatrix.h"

#include <vector>
#include <string>

class TestableJet {
    private:
    protected:
    public:
        TestableJet();
        virtual ~TestableJet();

        virtual void list_of_functions(std::vector<int (*)(void*, const RealVector&, int degree, JetMatrix&)> &list,
                                       std::vector<std::string> &name) = 0;
};

#endif // _TESTABLEJET_

