#include "MultiColoredCurve.h"

// Initialize the color table: ATTENTION IT IS ALSO USEFUL TO PUT THIS +'s AND -'s.
//int ColorTable::color[16][3] = { 255, 255, 255, //  0 = Left transport     COLOR = white      ----
//                                 255, 255, 255, //  1                                         +---
//                                 255,   0,   0, //  2 = Choque 2 LAX.      COLOR = red        -+--
//                                 247, 151,  55, //  3 = SUPER_COMPRESSIVE  COLOR = orange     ++--
//                                 255, 255, 255, //  4                                         --+-
//                                 255, 255, 255, //  5                                         +-+-
//                                 255, 255, 255, //  6                                         -++-
//                                 255, 255, 255, //  7                                         +++-
//                                 255,   0, 255, //  8 = EXPANSIVE 2        COLOR = pink       ---+
//                                 255, 255, 255, //  9                                         +--+
//                                  18, 153,   1, // 10 = TRANSITIONAL       COLOR = green      -+-+
//                                   0,   0, 255, // 11 = Choque 1 LAX.      COLOR = dark blue  ++-+
//                                 255, 255, 255, // 12 = SUPER_EXPANSIVE    COLOR = white      --++
//                                 255, 255, 255, // 13                                         +-++
//                                   0, 255, 255, // 14 = EXPANSIVE 1        COLOR = cyan       -+++
//                                 255, 255, 255, // 15 = Right transport    COLOR = white      ++++
//                                };

MultiColoredCurve::MultiColoredCurve(const std::vector<HugoniotPolyLine> &w, int type) : GraphicObject(0, 0, 0){
//    double data[48] = {  0.0,   0.0,   0.0, //  0 = Left transport     COLOR = white      ----
//                         0.0,   0.0,   0.0, //  1                                         +---
//                       255.0,   0.0,   0.0, //  2 = Choque 2 LAX.      COLOR = red        -+--
//                       247.0, 151.0,  55.0, //  3 = SUPER_COMPRESSIVE  COLOR = orange     ++--
//                         0.0,   0.0,   0.0, //  4                                         --+-
//                         0.0,   0.0,   0.0, //  5                                         +-+-
//                         0.0,   0.0,   0.0, //  6                                         -++-
//                         0.0,   0.0,   0.0, //  7                                         +++-
//                       255.0,   0.0, 255.0, //  8 = EXPANSIVE 2        COLOR = pink       ---+
//                         0.0,   0.0,   0.0, //  9                                         +--+
//                        18.0, 153.0,   1.0, // 10 = TRANSITIONAL       COLOR = green      -+-+
//                         0.0,   0.0, 255.0, // 11 = Choque 1 LAX.      COLOR = dark blue  ++-+
//                         0.0,   0.0,   0.0, // 12 = SUPER_EXPANSIVE    COLOR = white      --++
//                         0.0,   0.0,   0.0, // 13                                         +-++
//                         0.0, 255.0, 255.0, // 14 = EXPANSIVE 1        COLOR = cyan       -+++
//                         0.0,   0.0,   0.0  // 15 = Right transport    COLOR = white      ++++
//                     };

    double data[48] = {  0.0,   0.0,   0.0, //  0 = Left transport     COLOR = white      ----
                         0.0, 255.0,   0.0, //  1                      COLOR = green      +--- // Was black
                       255.0,   0.0,   0.0, //  2 = Choque 2 LAX.      COLOR = red        -+--
                       247.0, 151.0,  55.0, //  3 = SUPER_COMPRESSIVE  COLOR = orange     ++--
                         0.0, 128.0, 128.0, //  4                      COLOR = turquoise  --+- // Was black
                       255.0, 255.0,   0.0, //  5                      COLOR = yellow     +-+- // Was black
                       128.0,   0.0, 128.0, //  6                      COLOR = purple     -++- // Was black
                         0.0,   0.0, 128.0, //  7                      COLOR = navy       +++- // Was black
                       255.0,   0.0, 255.0, //  8 = EXPANSIVE 2        COLOR = pink       ---+
                       128.0, 128.0, 128.0, //  9                      COLOR = Dark gray  +--+ // Was black
                        18.0, 153.0,   1.0, // 10 = TRANSITIONAL       COLOR = dark green -+-+
                         0.0,   0.0, 255.0, // 11 = Choque 1 LAX.      COLOR = dark blue  ++-+
                         0.0,   0.0,   0.0, // 12 = SUPER_EXPANSIVE    COLOR = white      --++
                       128.0, 128.0,   0.0, // 13                      COLOR = olive      +-++ // Was black
                         0.0, 255.0, 255.0, // 14 = EXPANSIVE 1        COLOR = cyan       -+++
                       128.0,   0.0,   0.0  // 15 = Right transport    COLOR = dark red   ++++ // Was black
                     };

    color_table = DoubleMatrix(16, 3);
    for (int i = 0; i < 16*3; i++) color_table(i) = data[i]/255.0;

    Curve2D *c;

    // Add curves
    for (int i = 0; i < w.size(); i++){
        std::vector<Point2D> p;
       
        for (int j = 0; j < w[i].point.size(); j++) p.push_back(Point2D(w[i].point[j].component(0), w[i].point[j].component(1)));

        c = new Curve2D(p, color_table(w[i].type[0], 0), color_table(w[i].type[0], 1), color_table(w[i].type[0], 2), type);
//        std::cout << "Size = " << w[i].point.size() << ", Type = " << w[i].type[0] << std::endl;

        sons.push_back(c);
    }
}

MultiColoredCurve::MultiColoredCurve(const std::vector<HugoniotPolyLine> &w, double min_speed, double max_speed, int speed_steps, int type) : GraphicObject(0, 0, 0){
    double data[48] = {  0.0,   0.0,   0.0, //  0 = Left transport     COLOR = white      ----
                         0.0, 255.0,   0.0, //  1                      COLOR = green      +--- // Was black
                       255.0,   0.0,   0.0, //  2 = Choque 2 LAX.      COLOR = red        -+--
                       247.0, 151.0,  55.0, //  3 = SUPER_COMPRESSIVE  COLOR = orange     ++--
                         0.0, 128.0, 128.0, //  4                      COLOR = turquoise  --+- // Was black
                       255.0, 255.0,   0.0, //  5                      COLOR = yellow     +-+- // Was black
                       128.0,   0.0, 128.0, //  6                      COLOR = purple     -++- // Was black
                         0.0,   0.0, 128.0, //  7                      COLOR = navy       +++- // Was black
                       255.0,   0.0, 255.0, //  8 = EXPANSIVE 2        COLOR = pink       ---+
                       128.0, 128.0, 128.0, //  9                      COLOR = Dark gray  +--+ // Was black
                        18.0, 153.0,   1.0, // 10 = TRANSITIONAL       COLOR = dark green -+-+
                         0.0,   0.0, 255.0, // 11 = Choque 1 LAX.      COLOR = dark blue  ++-+
                       192.0, 192.0, 192.0, // 12 = SUPER_EXPANSIVE    COLOR = silver     --++ // Was black
                       128.0, 128.0,   0.0, // 13                      COLOR = olive      +-++ // Was black
                         0.0, 255.0, 255.0, // 14 = EXPANSIVE 1        COLOR = cyan       -+++
                       128.0,   0.0,   0.0  // 15 = Right transport    COLOR = dark red   ++++ // Was black
                     };

    color_table = DoubleMatrix(16, 3);
    for (int i = 0; i < 16*3; i++) color_table(i) = data[i]/255.0;

    std::vector<double> speeds;
    double delta = (max_speed - min_speed)/(double)(speed_steps - 1);
    for (int i = 0; i < speed_steps; i++) speeds.push_back(min_speed + delta*(double)i);

    Curve2D *c;

    // Add curves
    for (int i = 0; i < w.size(); i++){
//        std::cout << "HPL[" << i << "]" << std::endl;
        std::vector<Point2D> p;
       
        for (int j = 0; j < w[i].point.size(); j++) p.push_back(Point2D(w[i].point[j].component(0), w[i].point[j].component(1)));

        c = new Curve2D(p, color_table(w[i].type[0], 0), color_table(w[i].type[0], 1), color_table(w[i].type[0], 2), type | CURVE2D_ARROWS);

        std::vector<int> arrow_tip;
        std::vector<int> arrow_base;

        const std::vector<double> &current_curve_speed = w[i].speed;
//        for (int j = 0; j < current_curve_speed.size(); j++) std::cout << "    sigma[" << j << "] = " << current_curve_speed[j] << std::endl;

        for (int j = 0; j < current_curve_speed.size() - 1; j++){
            for (int k = 0; k < speeds.size(); k++){
               if ((current_curve_speed[j] - speeds[k])*(current_curve_speed[j + 1] - speeds[k]) < 0.0){ 
                    if (current_curve_speed[j] < current_curve_speed[j + 1]){
                        // arrow_tip.push_back(j);
                        // arrow_base.push_back(j - 1);
                        if (j > 0){
                            arrow_tip.push_back(j);
                            arrow_base.push_back(j - 1);
                        }
                        else {
                            arrow_tip.push_back(j + 1);
                            arrow_base.push_back(j);
                        }
                    }
                    else {
                        // arrow_tip.push_back(j);
                        // arrow_base.push_back(j + 1);
                        if (j == current_curve_speed.size() - 2){
                            arrow_tip.push_back(j);
                            arrow_base.push_back(j + 1);
                        }
                        else {
                            arrow_tip.push_back(j);
                            arrow_base.push_back(j - 1);
                        }
                    }
                }
            }
        }

        c->set_arrowheads(arrow_tip, arrow_base);

        sons.push_back(c);
    }
}

MultiColoredCurve::~MultiColoredCurve(){
    for (int i = 0; i < sons.size(); i++) delete sons[i];
}

void MultiColoredCurve::transform(const double *matrix){
    for (int i = 0; i < sons.size(); i++) sons[i]->transform(matrix);
    return;
}

void MultiColoredCurve::draw(){
    for (int i = 0; i < sons.size(); i++) sons[i]->draw();
    return;
}

void MultiColoredCurve::minmax(Point2D &, Point2D &){
    return;
}

