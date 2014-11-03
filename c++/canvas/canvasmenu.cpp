#include "canvasmenu.h"
#include "canvasmenupack.h"

//void CanvasMenu::hidebtncb(Fl_Widget*, void *w){
//    CanvasMenu *cm = (CanvasMenu*)w;

//    cm->hidebtn->hide();
//    cm->showbtn->show(); cm->showbtn->take_focus(); 
//    cm->obj->hide();
//    cm->canvas->redraw();

//    return;
//}

//void CanvasMenu::showbtncb(Fl_Widget*, void *w){
//    CanvasMenu *cm = (CanvasMenu*)w;

//    cm->showbtn->hide();
//    cm->hidebtn->show(); cm->hidebtn->take_focus();
//    cm->obj->show();
//    cm->canvas->redraw();

//    return;
//}

void CanvasMenu::hidebtncb(Fl_Widget*, void *w){
    CanvasMenu *cm = (CanvasMenu*)w;

    cm->hidebtn->hide();
    cm->showbtn->show(); cm->showbtn->take_focus();

    for (int i = 0; i < cm->object_.size(); i++){
        cm->object_[i]->hide();
        cm->canvas_[i]->redraw();
    }

    return;
}

void CanvasMenu::showbtncb(Fl_Widget*, void *w){
    CanvasMenu *cm = (CanvasMenu*)w;

    cm->showbtn->hide();
    cm->hidebtn->show(); cm->hidebtn->take_focus();

    for (int i = 0; i < cm->object_.size(); i++){
        cm->object_[i]->show();
        cm->canvas_[i]->redraw();
    }

    return;
}

//void CanvasMenu::deletebtncb(Fl_Widget*, void *w){
//    CanvasMenu *cm = (CanvasMenu*)w;

//    // For some reason Fl_Pack won't get redrawn, so we must
//    // ask CanvasMenu root-parent to perform a mass-redraw (down there).
//    Fl_Widget *p = cm->window();

//    cm->parent()->remove((Fl_Widget*)w);
//    cm->canvas->erase(cm->obj);

//    if (cm->cmp != 0) cm->cmp->remove_menu(cm);

//    Fl::delete_widget((Fl_Widget*)w);

//    // Here, every widget gets redrawn in this window.
//    p->redraw();

//    return;
//}

void CanvasMenu::deletebtncb(Fl_Widget*, void *w){
    CanvasMenu *cm = (CanvasMenu*)w;

    // For some reason Fl_Pack won't get redrawn, so we must
    // ask CanvasMenu root-parent to perform a mass-redraw (down there).
    Fl_Widget *p = cm->window();

    cm->parent()->remove((Fl_Widget*)w);

    for (int i = 0; i < cm->canvas_.size(); i++) cm->canvas_[i]->erase(cm->object_[i]);

    if (cm->cmp != 0) cm->cmp->remove_menu(cm);

    Fl::delete_widget((Fl_Widget*)w);

    // Here, every widget gets redrawn in this window.
    p->redraw();

    return;
}

//CanvasMenu::CanvasMenu(int x, int y, int w, int h, const char *l, Canvas *c, GraphicObject *o, CanvasMenuPack *mp = 0) : Fl_Group(x, y, w, h){
//    dim = 20;
//    
//    hidebtn = new Fl_Button(0, 0, 0, 0, "Hide");
//    hidebtn->callback(hidebtncb, (void*)this);
//    hidebtn->tooltip("Hide this plot");

//    showbtn = new Fl_Button(0, 0, 0, 0, "Show");
//    showbtn->callback(showbtncb, (void*)this);
//    showbtn->hide();
//    showbtn->tooltip("Show this plot");

////    deletebtn =  new Fl_Button(0, 0, 0, 0, "@1+");
//    deletebtn =  new Fl_Button(0, 0, 0, 0, "X");
//    deletebtn->align(FL_ALIGN_CENTER | FL_ALIGN_INSIDE);
//    deletebtn->callback(deletebtncb, (void*)this);
//    deletebtn->labelcolor(FL_RED);
//    deletebtn->labelfont(FL_HELVETICA_BOLD);
//    deletebtn->tooltip("Delete this plot");

//    infobox = new Fl_Box(0, 0, 0, 0, l);
//    infobox->align(FL_ALIGN_LEFT | FL_ALIGN_INSIDE);
//    infobox->color(color());
//    infobox->labelcolor(fl_contrast(labelcolor(), color()));
//    infobox->box(FL_FLAT_BOX);
//    infobox->copy_label(l);             // These two lines make sure that the label
//    infobox->tooltip(infobox->label()); // and the tooltip are rendered correctly

//    canvas = c;

//    cmp = mp;

//    obj = o;

//    end();
//    //box(FL_FLAT_BOX);
//    box(FL_BORDER_BOX);

//    resize(x, y, w, h);
//}

CanvasMenu::CanvasMenu(int x, int y, int w, int h, const char *l, Canvas *c, GraphicObject *o, CanvasMenuPack *mp = 0) : Fl_Group(x, y, w, h){
//    canvas = c;
//    obj = o;

    canvas_.push_back(c);
    object_.push_back(o);

    cmp = mp;

    init(x, y, w, h, l);
}

CanvasMenu::CanvasMenu(int x, int y, int w, int h, const char *l, const std::vector<Canvas*> &c, const std::vector<GraphicObject*> &o, CanvasMenuPack *mp = 0) : Fl_Group(x, y, w, h){
    canvas_ = c;
    object_ = o;
    cmp = mp;

    init(x, y, w, h, l);
}

void CanvasMenu::init(int x, int y, int w, int h, const char *l){
    dim = 20;
    
    hidebtn = new Fl_Button(0, 0, 0, 0, "Hide");
    hidebtn->callback(hidebtncb, (void*)this);
    hidebtn->tooltip("Hide this plot");

    showbtn = new Fl_Button(0, 0, 0, 0, "Show");
    showbtn->callback(showbtncb, (void*)this);
    showbtn->hide();
    showbtn->tooltip("Show this plot");

//    deletebtn =  new Fl_Button(0, 0, 0, 0, "@1+");
    deletebtn =  new Fl_Button(0, 0, 0, 0, "X");
    deletebtn->align(FL_ALIGN_CENTER | FL_ALIGN_INSIDE);
    deletebtn->callback(deletebtncb, (void*)this);
    deletebtn->labelcolor(FL_RED);
    deletebtn->labelfont(FL_HELVETICA_BOLD);
    deletebtn->tooltip("Delete this plot");

    infobox = new Fl_Box(0, 0, 0, 0, l);
    infobox->align(FL_ALIGN_LEFT | FL_ALIGN_INSIDE);
    infobox->color(color());
    infobox->labelcolor(fl_contrast(labelcolor(), color()));
    infobox->box(FL_FLAT_BOX);
    infobox->copy_label(l);             // These two lines make sure that the label
    infobox->tooltip(infobox->label()); // and the tooltip are rendered correctly

    end();
    //box(FL_FLAT_BOX);
    box(FL_BORDER_BOX);

    resize(x, y, w, h);

    return;
}

CanvasMenu::~CanvasMenu(){
    delete hidebtn;
    delete showbtn;
    delete deletebtn;
    delete infobox;
}

void CanvasMenu::resize(int nx, int ny, int nw, int nh){
    Fl_Group::resize(nx, ny, nw, nh);

    hidebtn->resize(nx + 5, ny + 1, 50, nh - 2);
    showbtn->resize(hidebtn->x(), hidebtn->y(), hidebtn->w(), hidebtn->h());
    deletebtn->resize(hidebtn->x() + hidebtn->w() + 5, hidebtn->y(), hidebtn->w(), hidebtn->h());
    infobox->resize(deletebtn->x() + deletebtn->w() + 5, 
                    deletebtn->y(), 
                    nw - hidebtn->w() - deletebtn->w() - 4*5, 
                    deletebtn->h());

    redraw();
    return;
}

void CanvasMenu::draw(){
    labelcolor(fl_contrast(FL_WHITE, color()));

    infobox->color(color());
    infobox->labelcolor(labelcolor());

    Fl_Group::draw();

    return;
}

