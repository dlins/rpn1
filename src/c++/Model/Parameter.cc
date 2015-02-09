#include "Parameter.h"

void Parameter::copy(const Parameter *original){
    name_     = original->name_;
    tooltip_  = original->tooltip_;
    min_      = original->min_;
    max_      = original->max_;
    value_    = original->value_;
    type_     = original->type_;

    return;
}

Parameter::Parameter() : name_(std::string("")), 
                         tooltip_(std::string("")),
                         min_(0.0),
                         max_(0.0),
                         value_(0.0),
                         type_(PARAMETER_FLOAT){
}

Parameter::Parameter(const std::string &n, double v) : name_(n),
                                                       tooltip_(n),
                                                       min_(v),
                                                       max_(v),
                                                       value_(v),
                                                       type_(PARAMETER_FLOAT) {
}

Parameter::Parameter(const std::string &n, double mn, double mx, double v) : name_(n),
                                                                             tooltip_(n),
                                                                             min_(mn),
                                                                             max_(mx),
                                                                             value_(v),
                                                                             type_(PARAMETER_FLOAT) {
}

Parameter::Parameter(const Parameter &original){
    copy(&original);
}

Parameter::Parameter(const Parameter *original){
    copy(original);
}

Parameter::~Parameter(){
}

Parameter Parameter::operator=(const Parameter &original){
    if (this != &original) copy(&original);

    return *this;
}

