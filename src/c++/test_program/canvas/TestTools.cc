#include "TestTools.h"

bool TestTools::pause_used = false;
int TestTools::pausex, TestTools::pausey;

void TestTools::closecb(Fl_Widget *btn, void*){
    Fl_Double_Window *win = (Fl_Double_Window*)btn->window();

    win->hide();
    Fl::check();

    return;
}

void TestTools::copy_to_clipboardcb(Fl_Widget*, void *data){
    std::string *s = (std::string*)data;

    int clipboard = 1; // Main clipboard
    Fl::copy(s->c_str(), s->length(), clipboard);

    return;
}

void TestTools::pause(){
//    int w = 200, h = 100;

//    Fl_Double_Window pause((Fl::w() - w)/2, (Fl::h() - h)/2, w, h, "Pause on");
//        Fl_Button btn(10, 10, w - 20, h -  20, "Continue");
//        btn.callback(closecb);
//    pause.end();
//    pause.show();

//    while (pause.shown()) Fl::wait();

    pause("");

    return;
}

void TestTools::pause(const char *s, int type){
    // To restore later...
    //
    int current_font = fl_font();
    int current_size = fl_size();

    Fl_Group *previously_current_group = Fl_Group::current();
    Fl_Group::current(0);

    // Prepare the dialog proper.
    //
    fl_font(FL_HELVETICA, 14);

    int w = 0, h = 0;

    fl_measure(s, w, h, 0);

    w = std::max(w, 200);

    int winw = w + 20;

    int winh, btny;
    if (h == 0){
        winh = 20 + 35;

        btny = 10;
    }
    else {
        winh = h + 30 + 35;

        btny = 10 + h + 10;
    }

    if (type == TESTTOOLS_COPY_TO_CLIPBOARD_YES){
        winh += 45;
    }

    if (!pause_used){
        pausex = (Fl::w() - winw)/2;
        pausey = (Fl::h() - winh)/2;
        pause_used = true;
    }

    Fl_Double_Window pause(pausex, pausey, winw, winh, "Pause on");
        Fl_Box box(10, 10, w, h, s);
        box.labelfont(FL_HELVETICA);
        box.labelsize(14);

        Fl_Button btn(10, btny, w, 35, "Continue");
        btn.callback(closecb);

        Fl_Button btn_clip(10, btn.y() + btn.h() + 10, w, 35, "Copy to clipboard");
        std::string label_string(s);
        btn_clip.callback(copy_to_clipboardcb, (void*)(&label_string));
        if (type == TESTTOOLS_COPY_TO_CLIPBOARD_NO) btn_clip.hide();
    pause.end();

    // Restore...
    //
    Fl_Group::current(previously_current_group);
    fl_font(current_font, current_size);


    // Show, wait and disappear.
    //    
    pause.show();

    while (pause.shown()) Fl::wait();

    // Remember the position of the window!
    //
    pausex = pause.x();
    pausey = pause.y();

    return;
}

