include make/env
include make/defs
include make/inc


SRCDIR = .
LIBDIR = $(SRCDIR)/../lib/$(RPHOSTTYPE)
OBJDIR = $(LIBDIR)
LIBNAME = librpn

LIBARC = $(LIBDIR)/$(LIBNAME).$(LIBEXT)
LIBOBJ = $(CCSRC:%.cc=$(OBJDIR)/%.o) \
	 $(CPPSRC:%.cpp=$(OBJDIR)/%.o) \
	 $(FSRC:%.F=$(OBJDIR)/%.o)

ifeq ($(RPHOSTTYPE),darwin_i386)
    LIBEXT   = dylib
    CPPFLAGS = $(INCLUDE) -I $(JAVA_INC)
    LDFLAG   = -fPIC
    LDLIB    = -L/Developer/SDKs/MacOSX10.5.sdk/System/Library/Frameworks/Accelerate.framework/Versions/A/Frameworks/vecLib.framework/Versions/A -lBLAS -lLAPACK -L/usr/local/lib -lgfortran
else # ifeq ($(RPHOSTTYPE),linux_*)
    LIBEXT   = so
    CPPFLAGS = $(INCLUDE) -g -pg -I $(JAVA_INC) -I $(JAVA_INC)/linux
    LDFLAG   = -Wl,-soname,$(LIBNAME).$(LIBEXT) -fPIC -pg 
    LDLIB    = -L/usr/lib -lblas -ldl -llapack 
endif


all: $(LIBARC)

$(LIBARC): $(LIBOBJ)
	$(CCC) -shared $(LDFLAG) -o $(LIBARC) $(LIBOBJ) $(LDLIB)


include make/rules

# TODO: Dependencies is not working!!!
#DEPENDOBJ = $(CPPSRC:%.cc=$(OBJDIR)/%.d) \
	    $(CPPSRC:%.cpp=$(OBJDIR)/%.d) \
	    $(FSRC:%.F=$(OBJDIR)/%.d) \
	    $(FSRC:%.f77=$(OBJDIR)/%.d)

#ifneq ($(RPHOSTTYPE),mingw32)
#-include $(DEPENDOBJ)
#endif

