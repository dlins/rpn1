#include "canvas.h"

void Canvas::draw(){
    fl_font(FL_HELVETICA, 12);

    // Draw the outer side
    fl_color(color());
    fl_rectf(x(), y(), w(), h());

    // Draw the canvas
    fl_color(FL_WHITE);
    fl_rectf(x() + sl, y() + su, w() - sl - sr, h() - su - sd);

    // Compute
    // TODO, take this calculations elsewhere.
    mx = (w() - sr - sl)/(xviewmax - xviewmin);
    bx = x() + sl - mx*xviewmin;

    my = (su + sd - h())/(yviewmax - yviewmin);
    by = y() + su - my*yviewmax;

    // Some variables for the horizontal ticks, the text and the grid
    double d_h = (double)(w() - sl - sr)/(double)(n_hor);
    double dx = (xviewmax - xviewmin)/((double)n_hor);

    // Some variables for the vertical ticks, the text and the grid
    double d_v = (double)(h() - su - sd)/(double)(n_ver);
    double dy = (yviewmax - yviewmin)/((double)n_ver);

    // Draw all the objects, and clip them if need be
    fl_push_clip(x() + sl, y() + su, w() - sl - sr, h() - su - sd);
        // Horizontal grid
        if (show_grid_){
            for (int i = 0; i <= n_ver; i++){
                int pos_ver = y() + su + i*(int)d_v;

                fl_color(FL_GRAY);
                fl_line_style(FL_DASHDOT);
                if (i > 0 && i < n_ver){
                    fl_line(x() + sl + 1, pos_ver, x() + w() - sr - 1, pos_ver);
                }
                fl_line_style(0);
            }
        }

        // Vertical grid
        if (show_grid_){
            for (int i = 0; i <= n_hor; i++){
                double pos_hor = (double)x() + (double)sl + (double)i*d_h;
    
                fl_color(FL_GRAY);
                fl_line_style(FL_DASHDOT);
                if (i > 0 && i < n_hor){
                    fl_line((int)pos_hor, y() + h() - sd - 1, (int)pos_hor, y() + su);
                }
                fl_line_style(0);
            }
        }

        // Prepare the transformation matrices
        fl_push_matrix();
            fl_translate(bx, by);
            fl_scale(mx, my);

            // Draw the objects proper
            for (int i = 0; i < vgr.size(); i++){
                if (vgr[i]->visible()) vgr[i]->draw();
            }
        fl_pop_matrix();

        // Draw zoom area
        if (draw_zoom){
            double alpha = .6;
            double beta = 1.0 - alpha;

            // Find the real area where the zoom is to be performed.
            int zbx = (int)min(pb.x, pe.x);
            int zby = (int)min(pb.y, pe.y);

            int zex = (int)max(pb.x, pe.x);
            int zey = (int)max(pb.y, pe.y);

            // Clamp. The extreme points of the zoom area are defined now.
            zbx = max(zbx, x() + sl);
            zby = max(zby, y() + su);

            zex = min(zex, x() + w() - sr);
            zey = min(zey, y() + h() - sd);

            // Width & height.
            int zw = zex - zbx;
            int zh = zey - zby;

            // Read the area where the zoom is to be applied.
            uchar *canvas_image = new uchar[zw*zh*3];
            fl_read_image(canvas_image, zbx, zby, zw, zh);

            // Draw inner zoom area
//            fl_color(fl_rgb_color(185, 213, 241));
//            fl_begin_polygon();
//                fl_vertex(pb.x, pb.y);
//                fl_vertex(pe.x, pb.y);
//                fl_vertex(pe.x, pe.y);
//                fl_vertex(pb.x, pe.y);
//            fl_end_polygon();

            // Perform manually the alpha-blending of the area under the zoom.
            // TODO: Parallelize.
            uchar blended_zoom_color[3];
            //uchar inner_zoom_color[3] = {241, 185, 213}; // Red
            //uchar inner_zoom_color[3] = {213, 241, 185}; // Green
            uchar inner_zoom_color[3] = {185, 213, 241}; // Blue
            //uchar inner_zoom_color[3] = {185, 185, 185}; // Gray

            for (int i = 0; i < zh; i++){
                for (int j = 0; j < zw; j++){
                    for (int k = 0; k < 3; k++){
                        blended_zoom_color[k] = alpha*canvas_image[3*(i*zw + j) + k] + beta*inner_zoom_color[k];
                    }
                    fl_color(fl_rgb_color(blended_zoom_color[0], blended_zoom_color[1], blended_zoom_color[2]));
                    fl_point(zbx + j, zby + i);
                }
            }

            // Draw outter zoom area. Should be alpha-blended (int the future).
            //fl_color(fl_rgb_color(255, 51, 153)); // Red
            //fl_color(fl_rgb_color(153, 255, 51)); // Green
            fl_color(fl_rgb_color(51, 153, 255)); // Blue
            //fl_color(fl_rgb_color(51, 51, 51)); // Gray

            fl_loop(zbx, zby,
                    zex, zby,
                    zex, zey,
                    zbx, zey);

            delete [] canvas_image;
        }

    fl_pop_clip();

    // Draw a frame around the canvas
    fl_color(FL_BLACK);
    fl_rect(x() + sl, y() + su, w() - sl - sr, h() - su - sd);

    // First and last horizontal text
    if (show_text_ && size() > 0){
        fl_color(labelcolor());
        char buf[20];
        int tw, th;
        int pos_hor;

        // First text
        sprintf(buf, "%.3g", xviewmin + 0);
        tw = 1; th = 1;
        fl_measure(buf, tw, th);

        pos_hor = x() + sl;
        fl_draw(buf, pos_hor, y() + h() - sd + th + 10);

        // Last text
        sprintf(buf, "%.3g", xviewmin + n_hor*dx);
        tw = 1; th = 1;
        fl_measure(buf, tw, th);

        pos_hor = x() + w() - sr - tw;
        fl_draw(buf, pos_hor, y() + h() - sd + th + 10);
    }

    // Draw the horizontal ticks and the text
    for (int i = 1; i < n_hor; i++){
        double pos_hor = (double)x() + (double)sl + (double)i*d_h;

        if (show_ticks_){
        fl_color(FL_BLACK);
            if (i > 0 && i < n_hor){
                fl_line((int)pos_hor, y() + h() - sd - 1, (int)pos_hor, y() + h() - sd - 10);
                fl_line((int)pos_hor, y() + su, (int)pos_hor, y() + su + 10);
            }
        }
    
        if (show_text_ ){
            fl_color(labelcolor());
            char buf[20];
            sprintf(buf, "%.3g", xviewmin + (double)i*dx);
            int tw = 1; int th = 1;
            fl_measure(buf, tw, th);
            fl_draw(buf, (int)pos_hor - tw/2, y() + h() - sd + th + 10);
        }
    }

    // First and last vertical text
    if (show_text_ && size() > 0){
        fl_color(labelcolor());
        char buf[20];
        int tw, th;
        int pos_ver;

        // First text
        sprintf(buf, "%.3g", yviewmax - n_ver*dy);
        tw = 1; th = 1;
        fl_measure(buf, tw, th);

        pos_ver = y() + h() - sd;
        fl_draw(buf, x() + sl - tw - 5, pos_ver);

        // Last text
        sprintf(buf, "%.3g", yviewmax - 0);
        tw = 1; th = 1;
        fl_measure(buf, tw, th);

        pos_ver = y() + su + th - fl_descent();
        fl_draw(buf, x() + sl - tw - 5, pos_ver);
    }

    // Draw the vertical ticks and the text
    for (int i = 1; i < n_ver; i++){
        int pos_ver = y() + su + i*(int)d_v;

        if (show_ticks_){
            fl_color(FL_BLACK);
            if (i > 0 && i < n_ver){
                fl_line(x() + sl + 1, pos_ver, x() + sl + 10, pos_ver);
                fl_line(x() + w() - sr - 1, pos_ver, x() + w() - sr - 10, pos_ver);
            }
        }

        if (show_text_ && size() > 0){
            fl_color(labelcolor());
            char buf[20];
            sprintf(buf, "%.3g", yviewmax - (double)i*dy);
            int tw = 1; int th = 1;
            fl_measure(buf, tw, th);
            fl_draw(buf, x() + sl - tw - 5, pos_ver - fl_descent() + th/2);
        }
    }

    // Draw the ylabel
    if (ylabel_ != NULL){
        fl_color(labelcolor());
        int ylabelw = 0, ylabelh = 0;
        fl_measure(ylabel_, ylabelw, ylabelh, 0);
        ylabelw *= 2;
        ylabelh += 4;

        // This code is from Fl_Vertical_Text::draw()
        // Begin
        if (ylabelbuf != NULL) free(ylabelbuf);
        ylabelbuf = (uchar*)malloc(ylabelw*ylabelh*3*sizeof(uchar));
        Fl_Offscreen off = fl_create_offscreen(ylabelw, ylabelh);

        fl_begin_offscreen(off); 
            // Box
            draw_box(FL_FLAT_BOX, 0, 0, ylabelw, ylabelh, color());

            // Text
            fl_color(labelcolor());
            fl_font(labelfont(), labelsize());

            fl_draw(ylabel_, 0, 0, ylabelw, ylabelh, Fl_Align(align()));

            fl_read_image(ylabelbuf, 0, 0, ylabelw, ylabelh);
        fl_end_offscreen();

        fl_delete_offscreen(off);
        // End

        for (int i = 0; i < ylabelh; i++){
            for (int j = 0; j < ylabelw; j++){
                fl_color(0, 0, 0);
                fl_color(ylabelbuf[3*(i*ylabelw + j)], 
                         ylabelbuf[3*(i*ylabelw + j) + 1], 
                         ylabelbuf[3*(i*ylabelw + j) + 2]);
                fl_point(x() + i + 10, y() + su + (h() - su - sd)/2 + ylabelw/2- j);
            }
        }
    }

    // Draw the xlabel
    if (xlabel_ != NULL){
        fl_color(labelcolor());
        int xlabelw = 0, xlabelh = 0;
        fl_measure(xlabel_, xlabelw, xlabelh, 0);
        fl_draw(xlabel_, x() + sl + (w() - sl - sr)/2 - xlabelw/2, y() + h() - sd/2 + 10);
    }


    // Draw the box
    draw_box();
    return;
}

int Canvas::handle(int e){
    int event_x = Fl::event_x();
    int event_y = Fl::event_y();
    if ((event_x < x() + sl || event_x > x() + w() - sr  ||
         event_y < y() + su || event_y > y() + h() - sd) && 
         !draw_zoom) return 0;

    if (e == FL_PUSH){
        if (Fl::event_button() == FL_LEFT_MOUSE){
            if (Fl::event_state(FL_CTRL)){
                if (extfunc != 0){
                    extfunc(extwidget, extvoid);
                    //ctrl = true;
                    return 1;
                }
            }
        }
    }

    // The external functions (this should be improved...)
    if (e == FL_ENTER){
//        std::cout << "Canvas: Enter." << std::endl;
        if (on_enter_function != 0) on_enter_function(this, 0);
        return 1;
    }

    if (e == FL_MOVE){
//        std::cout << "Canvas: Move." << std::endl;
        if (on_move_function != 0) on_move_function(this, 0);
        return 1;
    }

    if (e == FL_LEAVE){
//        std::cout << "Canvas: Leave." << std::endl;
        if (on_leave_function != 0) on_leave_function(this, 0);
        return 1;
    }

    if (Fl::event_button() == FL_RIGHT_MOUSE){
        switch (e){
            case FL_PUSH:
                pb.x = event_x; pb.y = event_y;
                pe.x = event_x; pe.y = event_y;

                fl_cursor(FL_CURSOR_CROSS);
                draw_zoom = true;
                redraw();

                return 1;
                break;
            case FL_RELEASE:
                pe.x = event_x; pe.y = event_y;
                if (pe.x != pb.x && pe.y != pb.y){
                    // Compute the new extremes
                    xviewmin = (min(pe.x, pb.x) - bx)/mx;
                    xviewmax = (max(pe.x, pb.x) - bx)/mx;
                    yviewmin = (max(pe.y, pb.y) - by)/my;
                    yviewmax = (min(pe.y, pb.y) - by)/my;
                }
                fl_cursor(FL_CURSOR_DEFAULT);
                draw_zoom = false;
                redraw();

                return 1;
                break;
            case FL_DRAG:
                pe.x = event_x; pe.y = event_y;

                redraw();

                return 1;
                break;
            default:
                return 0;
                break;
        }
    }
    else if (Fl::event_button() == FL_MIDDLE_MOUSE){
        switch (e){
            case FL_PUSH:
                nozoom();
                return 1;
                break;
            default:
                return 0;
                break;
        }
    }
    else if (Fl::event_button() == FL_LEFT_MOUSE){
        draw_zoom = false;
        switch (e){
            case FL_PUSH:
                pb.x = event_x; 
                pb.y = event_y;

                temp_x_factor = (xviewmin - xviewmax)/(w() - sl - sr);
                temp_y_factor = (yviewmax - yviewmin)/(h() - su - sd);

                xviewmax_old = xviewmax;
                xviewmin_old = xviewmin;
                yviewmax_old = yviewmax;
                yviewmin_old = yviewmin;

                fl_cursor(FL_CURSOR_MOVE);
                return 1;
                break;
            case FL_DRAG:
                pe.x = event_x;
                pe.y = event_y;    if (e == FL_ENTER && on_enter_function != 0){
        (*on_enter_function)(this, 0);
    }

                deltax = (pe.x - pb.x)*temp_x_factor;
                deltay = (pe.y - pb.y)*temp_y_factor;

                xviewmin = xviewmin_old + deltax;
                xviewmax = xviewmax_old + deltax;
                yviewmin = yviewmin_old + deltay;
                yviewmax = yviewmax_old + deltay;

                redraw();
                return 1;
                break;
            case FL_RELEASE:
                fl_cursor(FL_CURSOR_DEFAULT);
                redraw();
                return 1;
                break;
            default:
                return 0;
                break;
        }
    }
    else if (e == FL_MOUSEWHEEL){
        draw_zoom = false;
        double dx = xviewmax - xviewmin;
        double dy = yviewmax - yviewmin;

        double meanx = (xviewmax + xviewmin)/2.0;
        double meany = (yviewmax + yviewmin)/2.0;

        if (Fl::event_dy() < 0){
            xviewmax = meanx + dx*.9/2.0;
            xviewmin = meanx - dx*.9/2.0;
            yviewmax = meany + dy*.9/2.0;
            yviewmin = meany - dy*.9/2.0;
        }
        else if (Fl::event_dy() > 0){
            xviewmax = meanx + dx*(10.0/9.0)/2.0;
            xviewmin = meanx - dx*(10.0/9.0)/2.0;
            yviewmax = meany + dy*(10.0/9.0)/2.0;
            yviewmin = meany - dy*(10.0/9.0)/2.0;
        }
        redraw();

        return 1;
    }

    return 0;
}

void Canvas::nozoom(void){
    DoubleMatrix m = get_transform_matrix();
    for (int i = 0; i < vgr.size(); i++) vgr[i]->canvas_transformation(m);

    Point2D pmin, pmax;
    if (vgr.size() > 0){
        vgr[0]->minmax(pmin, pmax);
        xmin = pmin.x; ymin = pmin.y;
        xmax = pmax.x; ymax = pmax.y;

        for (int i = 1; i < vgr.size(); i++){
            vgr[i]->minmax(pmin, pmax);
            if (xmin > pmin.x) xmin = pmin.x;
            if (xmax < pmax.x) xmax = pmax.x;
            if (ymin > pmin.y) ymin = pmin.y;
            if (ymax < pmax.y) ymax = pmax.y;
        }
    }
 
    xviewmax = xmax;
    xviewmin = xmin;
    yviewmax = ymax;
    yviewmin = ymin;

    deltax = deltay = 0;

    redraw();

    return;
}

Canvas::Canvas(int x, int y, int w, int h) : Fl_Widget(x, y, w, h){
    sl = 70 + 12 + 2*10; // Before: 70;
    sr = 20;
    su = 20;
    sd = 40 + 12 + 2*10; // Before: 40;

    n_hor = 6;
    n_ver = 6;
    fl_font(FL_HELVETICA, 12);

    draw_zoom = false;

    deltax = deltay = 0;

    show_text_ = true;
    show_ticks_ = true;

    extfunc = 0;

    xmax = xmin = ymax = ymin = 0;
    xviewmax = xviewmin = yviewmax = yviewmin = 0;

    // Labels
    xlabel_ = NULL;
    ylabel_ = NULL;
    ylabelbuf = NULL;

    // Grid
    show_grid_ = false;

    // Transformation matrix and its inverse
    tm = new double[9];
    itm = new double[9];

    for (int i = 0; i < 3; i++){
        for (int j = 0; j < 3; j++){
            tm[i*3 + j] = tm[j*3 + i] = itm[i*3 + j] = itm[j*3 + i] = 0.0;
        }
    }
    for (int i = 0; i < 3; i++) tm[i*3 + i] = itm[i*3 + i] = 1.0;

    on_move_function = 0;
    on_enter_function = 0;
    on_leave_function = 0;
}

Canvas::~Canvas(){
    delete [] itm;
    delete [] tm;

    if (xlabel_ != NULL) free(xlabel_);
    if (ylabel_ != NULL) free(ylabel_);
    if (ylabelbuf != NULL) free(ylabelbuf);

    clear();
}

void Canvas::add(GraphicObject *gr){
//    gr->transform(tm);
    vgr.push_back(gr); vgr[vgr.size() - 1]->transform(tm);

    // Update Canva's xmin, xmax, ymin, ymax
    Point2D pmin, pmax;
    gr->minmax(pmin, pmax);

    if (vgr.size() == 1){
        xmin = pmin.x; ymin = pmin.y;
        xmax = pmax.x; ymax = pmax.y;
        //nozoom();
    }
    else {
        if (xmin > pmin.x) xmin = pmin.x;
        if (xmax < pmax.x) xmax = pmax.x;
        if (ymin > pmin.y) ymin = pmin.y;
        if (ymax < pmax.y) ymax = pmax.y;
    }

    redraw();
    return;
}

void Canvas::clear(){
    for (int i = 0; i < vgr.size(); i++) delete vgr[i];
    vgr.clear();
    redraw();
    return;
}

void Canvas::erase(int n){
    if (n >= 0 && n < vgr.size()){
        delete vgr[n];
        vgr.erase(vgr.begin() + n);
        redraw();
    }
    return;
}

void Canvas::erase(GraphicObject *obj){
    if (vgr.size() > 0){
        int n = 0;
        while (n < vgr.size() && vgr[n] != obj){
            n++;
        }
        if (n < vgr.size()) erase(n);
    }
    
    return;
}

int Canvas::size(){
    return vgr.size();
}

string Canvas::num2str(double n){
    stringstream s;
    s.precision(5);
    s << fixed << n;
    return s.str();
}

void Canvas::pstricks(const char *name){
    ofstream out(name);
    out.precision(5);
    out << fixed;

    out << "\\documentclass{article}\n";
    out << "\\usepackage{color}\n";
    out << "\\usepackage{pstricks}\n";
    out << "\\pagestyle{empty}\n\n";
    out << "\\begin{document}\n";
    out << "\\begin{center}\n";
    out << "\\fboxsep=1mm\n";
    out << "\\fboxrule=0mm\n";
    out << "\\fbox{\n";

    out << "\\psset{linewidth=0.001, xunit=" << num2str(0.01*(w() - sl - sr)/(xviewmax - xviewmin)) 
                          << ", yunit=" << num2str(0.01*(h() - su - sd)/(yviewmax - yviewmin)) << "}\n";

    double psxviewmin = (x() - bx)/mx;
    double psxviewmax = (x() + w() - bx)/mx;
    double psyviewmin = (y() - by)/my;
    double psyviewmax = (y() + h() - by)/my;
    out << "\\begin{pspicture}(" << num2str(psxviewmin) << ", " << num2str(psyviewmin) << ")(" 
                               << num2str(psxviewmax) << ", " << num2str(psyviewmax) << ")\n";
    out << "\\psclip{\\psframe[linestyle=none](" 
                               << num2str(xviewmin) << ", " << num2str(yviewmin) << ")(" 
                               << num2str(xviewmax) << ", " << num2str(yviewmax) << ")}\n\n";

    for (int i = 0; i < vgr.size(); i++){
        if (vgr[i]->visible()) vgr[i]->pstricks(&out);
    }
    out << "\n";

    out << "\\endpsclip\n\n";

    // Draw the horizontal ticks and the text
    out << "%Draw the horizontal ticks and the text\n";
    double d_h = (double)(w() - sl - sr)/(double)(n_hor);
    for (int i = 0; i <= n_hor; i++){
        int pos_hor = x() + sl + i*(int)d_h;
        double dx = (xviewmax - xviewmin)/((double)n_hor);

        if (show_ticks_){
            if (i > 0 && i < n_hor){
                out << "\\psline(" << num2str((pos_hor - bx)/mx) << ", " << num2str((y() + h() - sd - by)/my) << ")(" 
                                   << num2str((pos_hor - bx)/mx) << ", " << num2str((y() + h() - sd - 10 - by)/my) << ")\n";
                out << "\\psline(" << num2str((pos_hor - bx)/mx) << ", " << num2str((y() + su - by)/my) << ")(" 
                                   << num2str((pos_hor - bx)/mx) << ", " << num2str((y() + su + 10 - by)/my) << ")\n";
            }
        }

        if (show_text_){
            char buf[20];
            sprintf(buf, "%.3f", xviewmin + (double)i*dx);
            int tw = 1; int th = 1;
            fl_measure(buf, tw, th);
            // Since rput is used (and the text within it is centered), the text will be automatically
            // centered in the given box. The horizontal coordinate is, therefor, that of the tick.
            //out << "\\rput(" << num2str((pos_hor - bx)/mx) << ", " << num2str((y() + h() - sd + th + 10 - by)/my)
            out << "\\rput(" << num2str((pos_hor - bx)/mx) << ", " << num2str((y() + h() - sd + 20 - by)/my)
                             << "){\\psscalebox{.5 .5}{$" << buf << "$}}\n\n";
        }
    }

    // Draw the vertical ticks and the text
    out << "\n%Draw the vertical ticks and the text\n";
    double d_v = (double)(h() - su - sd)/(double)(n_ver);
    for (int i = 0; i <= n_ver; i++){
        int pos_ver = y() + su + i*(int)d_v;
        double dy = (yviewmax - yviewmin)/((double)n_ver);

        if (show_ticks_){
            if (i > 0 && i < n_ver){
                out << "\\psline(" << num2str((x() + sl + 00 - bx)/mx) << ", " << num2str((pos_ver - by)/my) << ")(" 
                                   << num2str((x() + sl + 10 - bx)/mx) << ", " << num2str((pos_ver - by)/my) << ")\n";
                out << "\\psline(" << num2str((x() + w() - sr - bx)/mx) << ", " << num2str((pos_ver - by)/my) << ")(" 
                                   << num2str((x() + w() - sr - 10 - bx)/mx) << ", " << num2str((pos_ver - by)/my) << ")\n";
            }
        }

        if (show_text_){
            char buf[20];
            sprintf(buf, "%.3f", yviewmax - (double)i*dy);
            int tw = 1; int th = 1;
            fl_measure(buf, tw, th);
            // Since rput is used (and the text within it is centered), the text will be automatically
            // centered in the given box. The vertical coordinate is, therefor, that of the tick.
            //out << "\\rput[r](" << num2str((x() + sl - tw - 5 - bx)/mx) << ", " << num2str((pos_ver - by)/my)
            out << "\\rput[r](" << num2str((x() + sl - 10 - bx)/mx) << ", " << num2str((pos_ver - by)/my)    
                             << "){\\psscalebox{.5 .5}{$" << buf << "$}}\n\n";
        }
    }

    out << "\\psframe[linecolor=black](" 
                               << num2str(xviewmin) << ", " << num2str(yviewmin) << ")(" 
                               << num2str(xviewmax) << ", " << num2str(yviewmax) << ")\n";

    out << "\\end{pspicture}\n}\n";
    out << "\\end{center}\n";
    out << "\\end{document}\n\n";

    out.close();
    return;
}

//void Canvas::canvas2pnm(const char *name){
//    
//    uchar *p = (uchar*)malloc(3*w()*h()*sizeof(uchar));
//    draw();
//    fl_read_image(p, x(), y(), w(), h());

//    save_pnm(name, p, w(), h(), 3);
//    free(p);
//    return;
//}

void Canvas::show_text(void){
    show_text_ = true;
    sl = sr + 50;
    sd = su + 20;
    redraw();
    return;
}

void Canvas::hide_text(void){
    show_text_ = false;
    sl = sr;
    sd = su;
    redraw();
    return;
}

void Canvas::show_ticks(void){
    show_ticks_ = true;
    redraw();
    return;
}

void Canvas::hide_ticks(void){
    show_ticks_ = false;
    redraw();
    return;
}

void Canvas::setextfunc(void(*f)(Fl_Widget*, void*), Fl_Widget *w, void *v){
    extfunc = f;
    extwidget = w;
    extvoid = v;
    return;
}

void Canvas::getxy(double &x, double &y){
//    x = (Fl::event_x() - bx)/mx;
//    y = (Fl::event_y() - by)/my;

    double xtemp = (Fl::event_x() - bx)/mx;
    double ytemp = (Fl::event_y() - by)/my;

    // Apply the inverse transformation1
    x = xtemp*itm[0] + ytemp*itm[1] + itm[2];
    y = ytemp*itm[3] + ytemp*itm[4] + itm[5];

    return;
}

void Canvas::replace(GraphicObject *oldobj, GraphicObject *newobj){
    for (int i = 0; i < vgr.size(); i++){
        if (vgr[i] == oldobj){
            vgr[i] = newobj; printf("found!\n");
            vgr[i]->transform(tm);
            delete oldobj;   printf("replaced!\n");
            redraw();
            break;
        }
    }
    return;
}

void Canvas::xlabel(const char *l){
    if (xlabel_ != NULL) free(xlabel_);
    xlabel_ = (char*)malloc((strlen(l) + 1)*sizeof(char));
    strcpy(xlabel_, l);

    redraw();
    return;
}

void Canvas::ylabel(const char *l){
    if (ylabel_ != NULL) free(ylabel_);
    ylabel_ = (char*)malloc((strlen(l) + 1)*sizeof(char));
    strcpy(ylabel_, l);

    redraw();
    return;
}

void Canvas::show_grid(void){
    show_grid_ = true;
    redraw();
    return;
}

void Canvas::hide_grid(void){
    show_grid_ = false;
    redraw();
    return;
}

void Canvas::number_of_horizontal_ticks(int n){
    n_hor = n;
    redraw();
    return;
}

int Canvas::number_of_horizontal_ticks(void){
    return n_hor;
}

void Canvas::number_of_vertical_ticks(int n){
    n_ver = n;
    redraw();
    return;
}

int Canvas::number_of_vertical_ticks(void){
    return n_ver;
}

void Canvas::axis(double minx, double maxx, double miny, double maxy){
    xviewmin = minx;
    xviewmax = maxx;

    yviewmin = miny;
    yviewmax = maxy;

    redraw();

    return;
}

int Canvas::set_transform_matrix(const double *m){
    double Delta = (m[0]*m[5]*m[7] - m[1]*m[5]*m[6] - m[2]*m[3]*m[7] + m[2]*m[4]*m[6] - m[0]*m[4]*m[8] + m[1]*m[3]*m[8]);

    if (fabs(Delta) < 1e-8) return -1;

    for (int i = 0; i < 9; i++) tm[i] = m[i];

    itm[0] =  (m[5]*m[7] - m[4]*m[8])/Delta;
    itm[1] = -(m[2]*m[7] - m[1]*m[8])/Delta;
    itm[2] = -(m[1]*m[5] - m[2]*m[4])/Delta;
    itm[3] = -(m[5]*m[6] - m[3]*m[8])/Delta;
    itm[4] =  (m[2]*m[6] - m[0]*m[8])/Delta;
    itm[5] =  (m[0]*m[5] - m[2]*m[3])/Delta;
    itm[6] = -(m[3]*m[7] - m[4]*m[6])/Delta;
    itm[7] =  (m[0]*m[7] - m[1]*m[6])/Delta;
    itm[8] = -(m[0]*m[4] - m[1]*m[3])/Delta;

    for (int i = 0; i < vgr.size(); i++) vgr[i]->transform(tm);

    return 0;
}

