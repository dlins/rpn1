#ifndef _CANVASMENU_
#define _CANVASMENU_

#include <FL/Fl.H>
#include <FL/Fl_Group.H>
#include <FL/Fl_Box.H>
#include <FL/Fl_Button.H>

#include "canvas.h"
#include "graphicobject.h"

#include <iostream>
using namespace std;

class CanvasMenuPack;

class CanvasMenu : public Fl_Group {
    private:
        Fl_Button *hidebtn, *showbtn, *deletebtn;
        Fl_Box *infobox;
        
        Canvas *canvas;
        GraphicObject *obj;

        std::vector<Canvas*> canvas_;
        std::vector<GraphicObject*> object_;

        CanvasMenuPack *cmp;

        int dim;

        void draw(void);
        void init(int x, int y, int w, int h, const char *l);
    protected:
        static void hidebtncb(Fl_Widget*, void*);
        static void showbtncb(Fl_Widget*, void*);
        static void deletebtncb(Fl_Widget*, void*);

    public:
        //CanvasMenu(int, int, int, int, const char*, Canvas*, GraphicObject*);
        CanvasMenu(int, int, int, int, const char*, Canvas*, GraphicObject*, CanvasMenuPack*);

        CanvasMenu(int, int, int, int, const char*, const std::vector<Canvas*>&, const std::vector<GraphicObject*>&, CanvasMenuPack*);

        ~CanvasMenu();
        void resize(int, int, int, int);
};

#endif // _CANVASMENU_

