#include "canvasmenuscroll.h"

CanvasMenuScroll::CanvasMenuScroll(int x, int y, int w, int h, const char *l) : Fl_Scroll(x, y, w, h, l){
    pack = new CanvasMenuPack(x + 1, y + 3, w - 2 - Fl::scrollbar_size(), h - 6);
    pack->type(Fl_Pack::VERTICAL);

    end();

    box(FL_EMBOSSED_BOX);

    type(Fl_Scroll::BOTH_ALWAYS);
}

CanvasMenuScroll::~CanvasMenuScroll(){
    pack->clear();
    delete pack;
}

void CanvasMenuScroll::add(const char *info, Canvas *canvas, GraphicObject *obj){
    scrollbar.do_callback();
    CanvasMenu *cm = new CanvasMenu(pack->x(), pack->y(), pack->w(), 20, info, canvas, obj, pack);
    
    pack->add(cm);

    object_.push_back(obj);
    canvas_.push_back(canvas);

    redraw();
    return;
}

void CanvasMenuScroll::resize(int nx, int ny, int nw, int nh){
    Fl_Scroll::resize(nx, ny, nw, nh);
    pack->resize(nx + 1, ny + 3, nw - 2 - Fl::scrollbar_size(), nh - 6);

//    redraw();

    return;
}

void CanvasMenuScroll::clear_all_graphics(){
    pack->clear();

    for (int i = 0; i < object_.size(); i++){
        canvas_[i]->erase(object_[i]);
    }

    object_.clear();
    canvas_.clear();

    window()->redraw();
    Fl::check();

    return;
}

