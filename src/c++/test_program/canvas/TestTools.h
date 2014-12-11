#ifndef _TESTTOOLS_
#define _TESTTOOLS_

#include <FL/Fl.H>
#include <FL/Fl_Double_Window.H>
#include <FL/Fl_Button.H>
#include <FL/Fl_Multiline_Output.H>
#include <FL/Fl_Box.H>
#include <FL/fl_draw.H>

#include <iostream>
#include <sstream>

#define TESTTOOLS_COPY_TO_CLIPBOARD_YES 0
#define TESTTOOLS_COPY_TO_CLIPBOARD_NO  1

class TestTools {
    private:
    protected:
        static bool pause_used;
        static int pausex, pausey;


        static void closecb(Fl_Widget *win, void*);
        static void copy_to_clipboardcb(Fl_Widget *, void *data);
    public:
        static void pause();

        static void pause(const char *s, int type = TESTTOOLS_COPY_TO_CLIPBOARD_NO);
        static void pause_clipboard(const char *s){
            pause(s, TESTTOOLS_COPY_TO_CLIPBOARD_YES);

            return;
        }

        static void pause(const std::string &s, int type = TESTTOOLS_COPY_TO_CLIPBOARD_NO){
            pause(s.c_str(), type); 
            return;
        }
        static void pause_clipboard(const std::string &s){
            pause(s, TESTTOOLS_COPY_TO_CLIPBOARD_YES);

            return;
        }

        static void pause(const std::stringstream &s, int type = TESTTOOLS_COPY_TO_CLIPBOARD_NO){
            pause(s.str(), type);
            return;
        }
        static void pause_clipboard(const std::stringstream &s){
            pause(s, TESTTOOLS_COPY_TO_CLIPBOARD_YES);

            return;
        }

};

#endif // _TESTTOOLS_

