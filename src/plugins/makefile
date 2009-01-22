include make/defs

vpath %.cc ./plugins

SRCDIR = .
OBJDIR = $(SRCDIR)/build/$(RPHOSTTYPE)
LIBDIR = $(SRCDIR)/../lib/$(RPHOSTTYPE)

RPNUMERICSDIR = $(SRCDIR)/rpnumerics
WAVEDIR       = $(SRCDIR)/wave
include $(RPNUMERICSDIR)/defs
include $(WAVEDIR)/defs

PLUG_SRC = ConservationShockFlow.cc RarefactionFlowPlugin.cc



PLUG_LIBNAME = RPnDefaultPlugins.so
PLUG_LIBARC = $(LIBDIR)/plugins/$(PLUG_LIBNAME)
PLUG_LIBOBJ = $(PLUG_SRC:%.cc=$(OBJDIR)/%.o)
PLUG_DEPENDOBJ = $(PLUG_SRC:%.cc=$(OBJDIR)/%.d)

SHARELIBFLAG += -fPIC
CPPFLAGS += $(SHARELIBFLAG) -I $(RPNUMERICSDIR) -I $(RPNUMERICSDIR)/plugin -I $(RPNUMERICSDIR)/physics/quad
LIBRPN = -L $(LIBDIR) -lwave -lrpnumerics

all: $(PLUG_LIBARC)

$(PLUG_LIBARC): $(PLUG_LIBOBJ)
	$(CCC) -shared $(SHARELIBFLAG) -Wl,-soname,$(PLUG_LIBNAME) -o $(PLUG_LIBARC) $(PLUG_LIBOBJ) $(LIBRPN)


.PRECIOUS: $(LIBARC)

include make/rules
ifneq ($(RPHOSTTYPE),mingw32)
-include $(DEPENDOBJ)
endif

CLEANSRC += $(LIBARC)

