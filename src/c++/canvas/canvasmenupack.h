#ifndef _CANVASMENUPACK_
#define _CANVASMENUPACK_

#include <FL/Fl.H>
#include <FL/Fl_Scroll.H>
#include <FL/Fl_Pack.H>
#include <FL/fl_draw.H>

class CanvasMenu;
class CanvasMenuScroll;

class CanvasMenuPack : public Fl_Pack {
    private:
        Fl_Color boxcolor[2];

        CanvasMenuPack(int, int, int, int);
        void setboxcolor(void);

        friend class CanvasMenuScroll;
    protected:
    public:
        void add(CanvasMenu*);
        void remove_menu(CanvasMenu*);
        void resize(int, int, int, int);
};

#endif // _CANVASMENUPACK_

