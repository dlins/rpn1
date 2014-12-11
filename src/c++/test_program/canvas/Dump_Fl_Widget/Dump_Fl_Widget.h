#ifndef _DUMP_FL_WIDGET_
#define _DUMP_FL_WIDGET_

#include <FL/Fl_Widget.H>
#include <FL/Fl.H>
#include <FL/fl_draw.H>
#include <FL/x.H>
#include <stdio.h>
#include <stdlib.h>
#include <png.h>

#define DUMP_AS_EPS 1
#define DUMP_AS_PNG 2

#define DUMP_OK     10
#define DUMP_ERROR  20

class Dump_Fl_Widget {
    private:
    protected:
        static int save_png(const char *filename, int width, int height, unsigned char *rgb, unsigned char *alpha);
        static int save_eps(const char *filename, int w, int h, unsigned char *rgb);
    public:
        static int save(Fl_Widget *widget, const char *name, int type);
};

#endif // _DUMP_FL_WIDGET_

