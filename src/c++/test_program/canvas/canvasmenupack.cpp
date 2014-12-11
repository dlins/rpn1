#include "canvasmenupack.h"
#include "canvasmenu.h"

void CanvasMenuPack::setboxcolor(void){
   for (int i = 0; i < children(); i++){
        child(i)->color(boxcolor[i % 2]);
        child(i)->box(FL_FLAT_BOX);
    }

    return;
}

CanvasMenuPack::CanvasMenuPack(int x, int y, int w, int h) : Fl_Pack(x, y, w, h){
    boxcolor[0] = FL_DARK_CYAN;//FL_BACKGROUND_COLOR;
    boxcolor[1] = fl_lighter(boxcolor[0]);

    end();

    //spacing(1);
}

void CanvasMenuPack::add(CanvasMenu *w){
    Fl_Pack::add((Fl_Widget*)w);
    
    setboxcolor();

    return;
}

void CanvasMenuPack::remove_menu(CanvasMenu *w){
    Fl_Pack::remove((Fl_Widget*)w);
    
    setboxcolor();

    return;
}

void CanvasMenuPack::resize(int nx, int ny, int nw, int nh){
    
    Fl_Pack::resize(nx, ny, nw, nh);

    for (int i = 0; i < children(); i++){
        CanvasMenu *cm = (CanvasMenu*)child(i);
        cm->resize(cm->x(), cm->y(), nw, 20);
    }


    return;
}

