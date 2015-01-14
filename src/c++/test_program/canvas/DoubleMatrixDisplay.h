#ifndef _DOUBLEMATRIXDISPLAY_
#define _DOUBLEMATRIXDISPLAY_

#include "DoubleMatrix.h"
#include "graphicobject.h"
#include <limits>
#include <FL/Enumerations.H>

class DoubleMatrixDisplay : public GraphicObject {
	private:
	protected:
	    DoubleMatrix *dm;
	    
	    Fl_Color mincolor, zerocolor, maxcolor;
	    
	    void extremes(double &inferior, double &superior);
	public:
	    DoubleMatrixDisplay(DoubleMatrix *d);
	    virtual ~DoubleMatrixDisplay();
	    
	    void draw();
	    virtual void minmax(Point2D &, Point2D &);
        virtual void pstricks(ofstream *){}
};

#endif // _DOUBLEMATRIXDISPLAY_
