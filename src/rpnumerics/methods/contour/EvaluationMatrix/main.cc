
#include <iostream.h>
#include "MultipleMatrix.h"

class ObjectTest {
	
public:
	ObjectTest(int value);   
    ~ObjectTest(void);
    
    int valueObj;
  	  
};

ObjectTest::ObjectTest(int value) { valueObj = value;}
ObjectTest::~ObjectTest() {}


main(void) {
	int limits[2];
	
	limits[0] = 4;
	limits[1] = 6;
	
	MultipleMatrix testMatrix(limits, 2); 	
	
	int coordinate1[2];
	coordinate1[0] = 2;
	coordinate1[1] = 3;
	
	int coordinate2[2];
	coordinate2[0] = 3;
	coordinate2[1] = 4;
	
	ObjectTest object(6);
	
	testMatrix.setElement(coordinate1, &object);
	
	int _coordinate1[2];
	_coordinate1[0] = 2;
	_coordinate1[1] = 3;
	ObjectTest *pont;
	
	pont = (ObjectTest) testMatrix.getElement(_coordinate1);
	
	cout << "Valor guardado...\n";
	cout << pont->valueObj;
		
}