include $(RPNHOME)/var/make/defs

RPNUMERICSDIR = $(RPNHOME)/src/rpnumerics
WAVEDIR       = $(RPNHOME)/src/wave
include $(RPNUMERICSDIR)/defs
include $(WAVEDIR)/defs

CPPFLAGS += -I. -Wconversion
LDLIBS = $(SYSLIBS) -L../../lib/$(OBJDIR) -L/usr/lib -lrpnumerics -llapack
CLEANSRC += $(PROGEXE)

C++SRC = t.cc

DEPENDOBJ = $(C++SRC:%.cc=$(OBJDIR)/%.d)
PROGOBJ = $(C++SRC:%.cc=$(OBJDIR)/%.o)
PROGEXE = $(OBJDIR)/t

all: $(PROGEXE)

$(PROGEXE): $(PROGLIB) $(PROGOBJ)
	        $(LINK.cc) -o $@ $(PROGOBJ) $(LDLIBS)

.PRECIOUS: $(PROGEXE)

include $(RPNHOME)/var/make/rules
ifneq ($(RPHOSTTYPE),mingw32)
-include $(DEPENDOBJ)
endif

