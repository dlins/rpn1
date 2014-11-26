#ifndef _CANVASMENUSCROLL_
#define _CANVASMENUSCROLL_

#include <FL/Fl.H>
#include <FL/Fl_Scroll.H>
#include <FL/Fl_Pack.H>

#include "graphicobject.h"
#include "canvas.h"
#include "canvasmenu.h"
#include "canvasmenupack.h"

class CanvasMenuScroll : public Fl_Scroll {
    private:
        CanvasMenuPack *pack;

        std::vector<GraphicObject*> object_;
        std::vector<Canvas*>        canvas_;
    protected:
    public:
        CanvasMenuScroll(int, int, int, int, const char*);
        ~CanvasMenuScroll();
        void add(const char*, Canvas*, GraphicObject*);
        void resize(int, int, int, int);

        void clear_all_graphics();
};

#endif // _CANVASMENUSCROLL_

