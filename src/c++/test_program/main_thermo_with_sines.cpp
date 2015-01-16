#include "Thermodynamics.h"
#include "VLE_Flash_TPCW.h"
#include "canvas.h"
#include "curve2d.h"
#include <FL/Fl_Double_Window.H>

Fl_Double_Window *winf;
    Canvas *canvasf;

Fl_Double_Window *windf;
    Canvas *canvasdf;

Fl_Double_Window *wind2f;
    Canvas *canvasd2f;


//    a1*sin(b1*x+c1) + a2*sin(b2*x+c2) + a3*sin(b3*x+c3) + 
//                    a4*sin(b4*x+c4) + a5*sin(b5*x+c5) + a6*sin(b6*x+c6) + 
//                    a7*sin(b7*x+c7) + a8*sin(b8*x+c8)

void fit(double T, double &f, double &df, double &d2f){
    std::vector<double> a, b, c;

a.push_back(   0.629322462003440);
b.push_back(   0.000000416290019);
c.push_back(  -0.000005998986346);
a.push_back(   0.153151918580917);
b.push_back(   0.000000840380178);
c.push_back(  -0.000014935753596);
a.push_back(   0.062846217409066);
b.push_back(   0.000001283458418);
c.push_back(   0.000032711230000);
a.push_back(   1.790942413366250);
b.push_back(   0.000000180094400);
c.push_back(  -0.000006354716100);
a.push_back(   0.030314673629487);
b.push_back(   0.000001736477024);
c.push_back(   0.000014107037042);
a.push_back(   0.017789951511209);
b.push_back(   0.000002235648644);
c.push_back(  -0.000022200704479);
a.push_back(   0.035865232407301);
b.push_back(   0.000002748627385);
c.push_back(  -0.000064136464048);
a.push_back(   0.029673333036949);
b.push_back(   0.000002835331653);
c.push_back(   0.000059147771265);

for (int i = 0; i < 8; i++){
    a[i] *= 1.0e5;
    b[i] *= 1.0e5;
    c[i] *= 1.0e5;
}

    f = df = d2f = 0.0;

    for (int i = 0; i < 8; i++){
        f   += a[i]*sin(b[i]*T + c[i]);
        df  += a[i]*b[i]*cos(b[i]*T + c[i]);
        d2f += -a[i]*b[i]*b[i]*sin(b[i]*T + c[i]);
    }

    return;

}

int main(){
    // Thermo
    double mc = 0.044;
    double mw = 0.018;

    MolarDensity mdv(MOLAR_DENSITY_VAPOR,  100.900000e5);
    MolarDensity mdl(MOLAR_DENSITY_LIQUID, 100.900000e5);

    VLE_Flash_TPCW *flash = new VLE_Flash_TPCW(&mdl, &mdv);

    Thermodynamics *tc = new Thermodynamics(mc, mw, "./c++/rpnumerics/physics/tpcw/hsigmaC_spline.txt");
    tc->set_flash(flash);

    winf = new Fl_Double_Window(10, 10, 800, 800, "New hsigmaC");
    {
        canvasf = new Canvas(0, 0, winf->w(), winf->h());

        // Sines
        std::vector<RealVector> f;

        double Theta = 0.099309;
        double DeltaTheta = .001;
        while (Theta < 0.576511){
            JetMatrix hsigmaC_jet(1);
            tc->hsigmaC_jet(Theta, 2, hsigmaC_jet);

            RealVector point(2);
            point(0) = Theta;

            point(1) = hsigmaC_jet.get(0);
            f.push_back(point);

            Theta += DeltaTheta;
        }

        Curve2D *curve = new Curve2D(f, 0.0, 0.0, 0.0, CURVE2D_SOLID_LINE);
        canvasf->add(curve);

        // Splines
        std::vector<RealVector> fsp;

        Theta = 0.099309;
//        double DeltaTheta = .001;
        while (Theta < 0.576511){
            JetMatrix hsigmaC_jet(1);
            tc->hsigmaC_jet_splines(Theta, 2, hsigmaC_jet);

            RealVector point(2);
            point(0) = Theta;

            point(1) = hsigmaC_jet.get(0);
            fsp.push_back(point);

            Theta += DeltaTheta;
        }

        Curve2D *curvesp = new Curve2D(fsp, 0.0, 0.0, 0.0, CURVE2D_MARKERS);
        canvasf->add(curvesp);

    }
    winf->end();

    winf->resizable(winf);

    winf->show();

    // 1
    windf = new Fl_Double_Window(10, 10, 800, 800, "New hsigmaC");
    {
        canvasdf = new Canvas(0, 0, windf->w(), windf->h());

        std::vector<RealVector> df;

        double Theta = 0.099309;
        double DeltaTheta = .001;
        while (Theta < 0.576511){
            JetMatrix hsigmaC_jet(1);
            tc->hsigmaC_jet(Theta, 2, hsigmaC_jet);

            RealVector point(2);
            point(0) = Theta;
            point(1) = hsigmaC_jet.get(0, 0);
            df.push_back(point);

            Theta += DeltaTheta;
        }


//        double T = 304.0;
//        double dT = 0.1;
//        while (T < 450.0){
//            double fs, dfs, d2fs;
//            fit(T, fs, dfs, d2fs);

//            RealVector point(2);
//            point(0) = T;
//            point(1) = d2fs;

//            f.push_back(point);

//            T += dT;
//        }


        Curve2D *curve = new Curve2D(df, 1.0, 0.0, 0.0, CURVE2D_SOLID_LINE);
        canvasdf->add(curve);

        // Splines
        std::vector<RealVector> dfsp;

        Theta = 0.099309;
//        double DeltaTheta = .001;
        while (Theta < 0.576511){
            JetMatrix hsigmaC_jet(1);
            tc->hsigmaC_jet_splines(Theta, 2, hsigmaC_jet);

            RealVector point(2);
            point(0) = Theta;

            point(1) = hsigmaC_jet.get(0, 0);
            dfsp.push_back(point);

            Theta += DeltaTheta;
        }

        Curve2D *curvesp = new Curve2D(dfsp, 1.0, 0.0, 0.0, CURVE2D_MARKERS);
        canvasdf->add(curvesp);

    }
    windf->end();

    windf->resizable(windf);

    windf->show();

    // 2
    wind2f = new Fl_Double_Window(10, 10, 800, 800, "New hsigmaC");
    {
        canvasd2f = new Canvas(0, 0, wind2f->w(), wind2f->h());

        std::vector<RealVector> d2f;

        double Theta = 0.099309;
        double DeltaTheta = .001;
        while (Theta < 0.576511){
            JetMatrix hsigmaC_jet(1);
            tc->hsigmaC_jet(Theta, 2, hsigmaC_jet);

            RealVector point(2);
            point(0) = Theta;
            point(1) = hsigmaC_jet.get(0, 0, 0);
            d2f.push_back(point);

            Theta += DeltaTheta;
        }

        Curve2D *curve = new Curve2D(d2f, 0.0, 0.0, 1.0, CURVE2D_SOLID_LINE);
        canvasd2f->add(curve);

        // Splines
        std::vector<RealVector> d2fsp;

        Theta = 0.099309;
//        double DeltaTheta = .001;
        while (Theta < 0.576511){
            JetMatrix hsigmaC_jet(1);
            tc->hsigmaC_jet_splines(Theta, 2, hsigmaC_jet);

            RealVector point(2);
            point(0) = Theta;

            point(1) = hsigmaC_jet.get(0, 0, 0);
            d2fsp.push_back(point);

            Theta += DeltaTheta;
        }

        Curve2D *curvesp = new Curve2D(d2fsp, 0.0, 0.0, 1.0, CURVE2D_MARKERS);
        canvasd2f->add(curvesp);
    }
    wind2f->end();

    wind2f->resizable(wind2f);

    wind2f->show();

    return Fl::run();
}

