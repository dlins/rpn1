#include <FL/Fl.H>
#include <FL/Fl_Double_Window.H>
#include <fstream>

#include "canvas.h"
#include "curve2d.h"
#include "spline1d.h"
#include "MolarDensity.h"
#include "Thermodynamics.h"

Fl_Double_Window *win;
    Canvas *canvasf, *canvasdf, *canvasd2f;


void diff(double delta, const std::vector<RealVector> &f, int component, std::vector<RealVector> &df){
    df.clear();

    if (f.size() > 2){
        for (int i = 1; i < f.size() - 1; i++){
            RealVector p(f[i]);

            p(component) = (f[i + 1](component) - f[i - 1](component))/(2.0*delta);
            df.push_back(p);
        }
    }

    return;
}

//int main2(){
//    std::vector<RealVector> f;

//    ap::real_1d_array x, y;
//    int n_base_func;
//    int n;

//    ifstream in("./c++/rpnumerics/physics/CompositionalPhysics/TPCW/hsigmaC_spline.txt");
//    {
//        std::string name;
//        in >> name;

//        double P;
//        in >> P;

//        in >> n_base_func;

//        in >> n;

//        x.setlength(n);
//        y.setlength(n);

//        for (int i = 0; i < n; i++){
//            RealVector p(2);
//            in >> p(0) >> p(1);

//            f.push_back(p);

//            x(i) = p(0);
//            y(i) = p(1);
//        }
//        

//    }

//    in.close();

//    // Generate the spline
//    spline1dinterpolant spline;
//    int spline_info;
//    spline1dfitreport report;
//    spline1dfitcubic(x, y, n, n_base_func,
//            spline_info,
//            spline,
//            report);

//    double delta = f[1](0) - f[0](0);
//    double max = f.back()[0];
//    double min = f.front()[0];

//    std::vector<RealVector> df;

//    diff(delta, f, 1, df);

//    win = new Fl_Double_Window(10, 10, 800, 800, "f, df, d2f");
//    {
//        canvasf = new Canvas(0, 0, win->w(), win->h());
//        canvasf->xlabel("x");
//        canvasf->ylabel("f (red), df (blue), d2f (green)");

//        {
//            Curve2D *c = new Curve2D(f, 1.0, 0.0, 0.0);
//            canvas->add(c);

//            {
//                double b = -8.75e5;
//                double m = 2274.54;
//                std::vector<RealVector> line;

//                double sum = 0.0;
//                for (int i = 0; i < f.size(); i++){
//                    RealVector p(f[i]);
//                    
//                    p(1) = b + m*p(0);

//                    line.push_back(p);

//                    sum += std::abs(f[i](1) - p(1));
//                }

//                Curve2D *l = new Curve2D(line, 0.0, 0.0, 0.0);
//                canvas->add(l);
//                std::cout << "sum/n = " << sum/f.size() << std::endl;
//            }


//            {
//                int new_n = 10*n;
//                double new_delta = (max - min)/(double)(new_n - 1);
//                std::vector<RealVector> splinevec;
//                for (int i = 0; i < new_n; i++){
//                    RealVector p(2);
//                    p(0) = min + new_delta*(double)i;

//                    double sf, sdf, sd2f;
//                    spline1ddiff(spline, p(0), sf, sdf, sd2f);

//                    p(1) = sd2f;

//                    splinevec.push_back(p);
//                }

//                Curve2D *s = new Curve2D(splinevec, 0.5, 0.5, 0.0, /*CURVE2D_MARKERS | */CURVE2D_SOLID_LINE);
////                canvas->add(s);


//                std::vector<RealVector> d2f;
//                diff(new_delta, splinevec, 1, d2f);

//                Curve2D *c = new Curve2D(d2f, 1.0, 0.0, 0.0, CURVE2D_MARKERS | CURVE2D_SOLID_LINE);
////                canvas->add(c);
//           }
//        }

//        {
//            Curve2D *c = new Curve2D(df, 0.0, 0.0, 1.0);
//            canvas->add(c);

//            double sum = 0.0;
//            for (int i = 0; i < df.size(); i++) sum += df[i](1);

//            std::cout << sum/df.size() << std::endl;
//        }

////        {
////            std::vector<RealVector> d2f;
////            diff(delta, df, 1, d2f);

////            Curve2D *c = new Curve2D(d2f, 1.0, 0.0, 0.0, CURVE2D_MARKERS);
////            canvas->add(c);
////        }

//        canvas->nozoom();
//    }
//    win->end();
//    win->resizable(win);

//    win->show();

//    return Fl::run();
//}

double Tref_water = 274.3775;
double T_typical_   = 304.63;

double Theta2T(double Theta){return Theta*T_typical_ + Tref_water;}

int main(){
    win = new Fl_Double_Window(0, 0, Fl::w()*.8, Fl::h()*.8, "f, df, d2f");
    {
        canvasf = new Canvas(0, 0, win->w()/3, win->h());
        canvasf->xlabel("Theta");
        canvasf->ylabel("f");
        
        canvasdf = new Canvas(canvasf->x() + canvasf->w(), canvasf->y(), canvasf->w(), canvasf->h());
        canvasdf->xlabel("Theta");
        canvasdf->ylabel("df");

        canvasd2f = new Canvas(canvasdf->x() + canvasdf->w(), canvasdf->y(), canvasdf->w(), canvasdf->h());
        canvasd2f->xlabel("Theta");
        canvasd2f->ylabel("d2f");
    }
    win->end();
    win->resizable(win);

    win->show();

    int type = MOLAR_DENSITY_VAPOR;
        type = MOLAR_DENSITY_LIQUID;

    double mc = 0.044;
    double mw = 0.018;
     
    MolarDensity mdv(MOLAR_DENSITY_VAPOR,  100.900000e5);
    MolarDensity mdl(MOLAR_DENSITY_LIQUID, 100.900000e5);

    MolarDensity *m = &mdl;
     
    VLE_Flash_TPCW *flash = new VLE_Flash_TPCW(&mdl, &mdv);
     
    Thermodynamics *tc = new Thermodynamics(mc, mw, "./c++/rpnumerics/physics/CompositionalPhysics/TPCW/hsigmaC_spline.txt");
    tc->set_flash(flash);

    double Theta_min = 0.099309;
    double Theta_max = 0.576511;
    double T_min = Theta2T(Theta_min);
    double T_max = Theta2T(Theta_max);

    int n = 50;
    double delta = (T_max - T_min)/(double)(n - 1);

    std::vector<RealVector> fa, dfa, d2fa;

    for (int i = 0; i < n; i++){
        RealVector p(2);
        p(0) = T_min + delta*(double)i;

//        JetMatrix ecj(1);
//        mdv.epsilon_c_jet(p(0), 2, ecj);
//        mdv.epsilon_w_jet(p(0), 2, ecj);
//        mdv.G12_jet(p(0), 2, ecj);
//        mdv.G21_jet(p(0), 2, ecj);
//        mdv.tau12_jet(p(0), 2, ecj);
//        mdv.tau21_jet(p(0), 2, ecj);
//        p(1) = ecj.get(0);
//        fa.push_back(p);

//        p(1) = ecj.get(0, 0);
//        dfa.push_back(p);

//        p(1) = ecj.get(0, 0, 0);
//        d2fa.push_back(p);   

        {
            JetMatrix xcj(1), ywj(1);
            flash->molarfractions_jet(p(0), xcj, ywj, 2);

            JetMatrix var = xcj;
//                      var = ywj;

            p(1) = var.get(0);
            fa.push_back(p);

            p(1) = var.get(0, 0);
            dfa.push_back(p);

            p(1) = var.get(0, 0, 0);
            d2fa.push_back(p);
        }

//GE
//gamma_1
//gamma_2
//Gamma_1
//Gamma_2
//vapor_L

      
    }    

    // F.
    //
    {
        Curve2D *c = new Curve2D(fa, 1.0, 0.0, 0.0);
        canvasf->add(c);

        canvasf->nozoom();
    }
    
    // DF.
    //
    {
        Curve2D *c;

        c = new Curve2D(dfa, 1.0, 0.0, 0.0);
        canvasdf->add(c);

        std::vector<RealVector> num;
        diff(delta, fa, 1, num);
        for (int i = 0; i < num.size(); i++) num[i](1);

        c = new Curve2D(num, 0.0, 0.0, 1.0, CURVE2D_MARKERS);
        canvasdf->add(c);

        canvasdf->nozoom();
    }

    // D2F.
    //
    {
        Curve2D *c;

        c = new Curve2D(d2fa, 1.0, 0.0, 0.0);
        canvasd2f->add(c);

        std::vector<RealVector> num;
        diff(delta, dfa, 1, num);
        for (int i = 0; i < num.size(); i++) num[i](1);

        c = new Curve2D(num, 0.0, 0.0, 1.0, CURVE2D_MARKERS);
        canvasd2f->add(c);

        canvasd2f->nozoom();
    }

    return Fl::run();
}

