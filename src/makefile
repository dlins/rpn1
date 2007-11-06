include $(RPNDIR)/var/make/defs

C++SRC += 
RPNUMERICSDIR = $(RPNDIR)/src/rpnumerics
WAVEDIR       = $(RPNDIR)/src/wave
include $(RPNUMERICSDIR)/defs
include $(WAVEDIR)/defs

LIBOBJ = $(C++SRC:%.cc=$(OBJDIR)/%.o)
DEPENDOBJ = $(C++SRC:%.cc=$(OBJDIR)/%.d)
LIBNAME = librpnumerics.so
LIBARC = $(RPNDIR)/src/$(OBJDIR)/$(LIBNAME)
CLEANSRC += $(LIBARC)


all: $(LIBARC)

$(LIBARC): $(LIBOBJ)
	$(CC) -shared -fPIC -Wl,-soname,$(LIBNAME) -o $(LIBARC) $(LIBOBJ)

# Static library target.
#$(LIBARC): $(LIBOBJ)
#	ar -ruv $(LIBARC) $(LIBOBJ)
#	$(RANLIB) $(LIBARC)


.PRECIOUS: $(LIBARC)

include $(RPNDIR)/var/make/rules
ifneq ($(RPHOSTTYPE),mingw32)
-include $(DEPENDOBJ)
endif
