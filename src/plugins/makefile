include make/env
include make/defs
include make/inc
vpath %.cc ./plugins

SRCDIR = .
LIBDIR = $(SRCDIR)/../lib/$(RPHOSTTYPE)
OBJDIR = $(LIBDIR)
LIBNAME = RPnDefaultPlugins

PLUGINSRC = ShockFlowPlugin.cc \
	RarefactionFlowPlugin.cc

LIBARC = $(LIBDIR)/plugins/$(LIBNAME).$(LIBEXT)
LIBOBJ = $(PLUGINSRC:%.cc=$(OBJDIR)/%.o)

CPPFLAGS = $(INCLUDE)
LDLIB    = -L$(LIBDIR) -lrpn

ifeq ($(RPHOSTTYPE),darwin_i386)
    LIBEXT = dylib
    LDFLAG = -fPIC 
    LDLIB += -L/Developer/SDKs/MacOSX10.5.sdk/System/Library/Frameworks/Accelerate.framework/Versions/A/Frameworks/vecLib.framework/Versions/A -lBLAS -lLAPACK -L/usr/local/lib -lgfortran

else # ifeq ($(RPHOSTTYPE),linux_*)
    LIBEXT = so
    LDFLAG = -Wl,-soname,$(LIBNAME).$(LIBEXT) -fPIC
    LDLIB += -L/usr/lib -lblas -ldl -llapack
endif


all: $(LIBARC)

$(LIBARC): $(LIBOBJ)
	$(CCC) -shared $(LDFLAG) -o $(LIBARC) $(LIBOBJ) $(LDLIB)


include make/rules

# TODO: Dependencies is not working!!!
#DEPENDOBJ = $(CPPSRC:%.cc=$(OBJDIR)/%.d)

#ifneq ($(RPHOSTTYPE),mingw32)
#-include $(DEPENDOBJ)
#endif


