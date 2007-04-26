#include <iostream>
#include "ADTADV.h"

class TestADT
	: public ADT
{
public:
	int i;
};

class TestADV
	: public ADV
{
public:
	TestADV(TestADT &test_adt__);

	TestADT &test_adt(void) const;
	inline void update(void);

	void set_int(int i_);
	void print(void);

private:
	TestADT &test_adt_;
	int i;
};

inline
TestADV::TestADV(TestADT &test_adt__)
	: ADV(test_adt__),
	  test_adt_(test_adt__)
{
	update();
}

inline TestADT &
TestADV::test_adt(void) const
{
	return test_adt_;
}

inline void
TestADV::update(void)
{
	i = test_adt().i;
}

inline void
TestADV::set_int(int i_)
{
	test_adt().i = i_;
	test_adt().synchronize();
}

inline void
TestADV::print(void)
{
	cerr << "i = " << i << endl;
}

int
main()
{
	TestADT adt;
	adt.i = 1;

	TestADV adv1(adt);
	TestADV adv2(adt);

	cerr << "adv1: "; adv1.print();
	cerr << "adv2: "; adv2.print();

	adv1.set_int(2);

	cerr << "adv1: "; adv1.print();
	cerr << "adv2: "; adv2.print();

	return 0;
}
