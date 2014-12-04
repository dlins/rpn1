##################################################################################
#
# General Makefile - IMPA - daniel@impa.br
#
##################################################################################
#
#	User customized definitions
#
##################################################################################

#
#	Define executable and/or library name
PROGNAME = 
LIBNAME = 

#
#	Compilers and linker
CC = gcc
CXX = g++
FC = gcc
LD = $(CXX)

#
#	Include, C-preprocessor, C, C++ and Fortran compilation flags
INCFLAGS = -I . -I .. -I ../JNI/include -I ../wave/ode/ -I ../rpnumerics/methods/Coincidence -I  ../wave/util/MultiArray -I /usr/lib/jvm/java-8-oracle/include -I /usr/lib/jvm/java-8-oracle/include/linux -I /opt/jdk1.6.0_45/include -I /opt/jdk1.6.0_45/include/linux

CPPFLAGS = -g -fPIC
CXXFLAGS =
CFLAGS =
FFLAGS =

#
#	Libraries to link and special flags. Ex: LDLIBS = -L/lib -lm, LDFLAGS = -fPIC
LDLIBS =  -L/usr/lib -lblas -ldl -llapack -lgfortran -lfltk -lX11 -lpng
LDFLAGS = -g -fPIC

#
#	Flags used to create a library. Only needed if LIBNAME was defined.
LIBEXT =
LIBFLAGS =

#
#	Source code files
SOURCE_DIR = ..
SOURCE_EXT = c cc cpp F
SOURCE_AUTO = no
SOURCE_LIST = source_files.mk




##################################################################################
#
#
#
#	WARNING: change the code below at youe ow: risk!
#
#
#
##################################################################################



##################################################################################
#
#	External source code files definition
#
##################################################################################

EXISTS = $(shell if [ -f $(SOURCE_LIST) ]; then echo yes; fi )

ifeq ($(EXISTS),yes)
-include $(SOURCE_LIST)
else
abort_1:
	@echo Error: No $(SOURCE_LIST) found!
	@echo You can use \'make sourcedef\' to create one.
endif




##################################################################################
#
#	General Definitions
#
##################################################################################

#
#	Source and include file paths
#
VPATH := $(shell echo $(dir $(cSRC) $(ccSRC) $(cppSRC) $(FSRC)) | sed "s, ,$'\n,g" | sort | uniq )
INCLUDE = $(patsubst %,-I %,$(subst :, ,$(VPATH))) $(INCFLAGS)

#
#	Definitions for cleaning
#
CLEAN = rm -f
CLEANSRC = $(OBJDIR)/*.[ado] $(PROGNAME) $(LIBNAME).$(LIBEXT)

#
#	Definitions for printing
#
PRINT = lpr -p
PRINTSRC = makefile *.[hcF]*

#
#	Tar definitions
#
TAR = tar
TARDIR = .
TARFLAGS = cf
TARSRC = makefile* *.[hcfF]*

#
#	Archiver definitions
#
AR = ar
ARFLAGS = r
RANLIB = ranlib

#
#	Miscellaneous definitions
#
MAKE = make
RMDIR = rmdir --ignore-fail-on-non-empty

#
#	Location for object, archive, and dependency files
#
MAKEDIR = $(dir $(PWD)/$(strip $(MAKEFILE_LIST)))
RPHOSTTYPE = $(shell uname -s | tr '[:upper:]' '[:lower:]')_$(shell uname -m)
#LOCAL = -$(notdir $(CURDIR))
OBJDIR = $(RPHOSTTYPE)$(LOCAL)

#
#	List of objects and dependency files
#
cSRC_ = $(notdir $(cSRC))
ccSRC_ = $(notdir $(ccSRC))
cppSRC_ = $(notdir $(cppSRC))
FSRC_ = $(notdir $(FSRC))

OBJECTS = $(cSRC_:%.c=$(OBJDIR)/%.o) \
	$(ccSRC_:%.cc=$(OBJDIR)/%.o) \
	$(cppSRC_:%.cpp=$(OBJDIR)/%.o) \
	$(FSRC_:%.F=$(OBJDIR)/%.o)

DEPENDS = $(cSRC_:%.c=$(OBJDIR)/%.d) \
	$(ccSRC_:%.cc=$(OBJDIR)/%.d) \
	$(cppSRC_:%.cpp=$(OBJDIR)/%.d) \
	$(FSRC_:%.F=$(OBJDIR)/%.d)

ifeq ($(strip $(OBJECTS)),)
abort_3:
	@echo Error: No source code defined!
endif


.PRECIOUS: 





##################################################################################
#
#	Main Targets
#
##################################################################################


subphysics: $(OBJECTS)
	$(LINK) -o $@ $(INCLUDE) main_subphysics.cpp $^ $(LDLIBS) $(LDFLAGS)

permgui: $(OBJECTS)
	$(LINK) -o $@ $(INCLUDE) main_permgui.cpp $^ $(LDLIBS) $(LDFLAGS)

eigen: $(OBJECTS)
	$(LINK) -o $@ $(INCLUDE) main_eigen.cpp $^ $(LDLIBS) $(LDFLAGS)

##################################################################################
#
#	Compilation Rules
#
##################################################################################

SUFFIXES = .o .d .c .cc .cpp .F
.SUFFIXES:
.SUFFIXES: $(SUFFIXES)


CPPFLAGS := $(CPPFLAGS) $(INCLUDE)
DEPEND = $(CC) -M $(CPPFLAGS)
LINK = $(LD)


#
#	C definitions & rules
#
COMPILE.c = $(CC) $(CFLAGS) $(CPPFLAGS) -c

$(OBJDIR)/%.o: %.c
	$(COMPILE.c) -o $@ $<

$(OBJDIR)/%.d: %.c
	@echo Creating dependency file $@ from $<
	@set -e; rm -f $@; $(DEPEND) $< | sed 's,\($*\)\.o[ :]*,$(OBJDIR)/\1.o $@ : ,g' > $@;


#
#	C++ definitions & rules
#
COMPILE.cc = $(CXX) $(CXXFLAGS) $(CPPFLAGS) -c

$(OBJDIR)/%.o: %.cc
	$(COMPILE.cc) -o $@ $<

$(OBJDIR)/%.o: %.cpp
	$(COMPILE.cc) -o $@ $<

$(OBJDIR)/%.d: %.cc
	@echo Creating dependency file $@ from $<
	@set -e; rm -f $@; $(DEPEND) $< | sed 's,\($*\)\.o[ :]*,$(OBJDIR)/\1.o $@ : ,g' > $@;

$(OBJDIR)/%.d: %.cpp
	@echo Creating dependency file $@ from $<
	@set -e; rm -f $@; $(DEPEND) $< | sed 's,\($*\)\.o[ :]*,$(OBJDIR)/\1.o $@ : ,g' > $@;


#
#	Fortran definitions & rules
#
COMPILE.F = $(FC) $(FFLAGS) $(CPPFLAGS) -c

$(OBJDIR)/%.o: %.F
	$(COMPILE.F) -o $@ $<

$(OBJDIR)/%.d: %.F
	@echo Creating dependency file $@ from $<
	@set -e; rm -f $@; $(DEPEND) $< | sed 's,\($*\)\.o[ :]*,$(OBJDIR)/\1.o $@ : ,g' > $@;




##################################################################################
#
#	General Rules
#
##################################################################################

$(OBJECTS) $(DEPENDS): | $(OBJDIR)

$(OBJDIR):
	@echo Creating object repository directory \"$(OBJDIR)\".
	@mkdir -p $(OBJDIR)


clean: FORCE
	$(CLEAN) $(CLEANSRC)


distclean: FORCE clean
	@$(RMDIR) $(OBJDIR)
	$(CLEAN) $(SOURCE_LIST)


print: FORCE
	$(PRINT) $(PRINTSRC)


$(TAGSFILE): $(TAGSSRC)
	$(TAGS) $(TAGSFLAGS) $(TAGSSRC) > $(TAGSFILE)


tar: FORCE
	pwd=`pwd`; name=`basename $$pwd`.tar;\
	$(TAR) $(TARFLAGS) $(TARDIR)/$$name $(TARSRC)


sourcedef: FORCE
	@set -e; > $(SOURCE_LIST) ; \
	for ext in $(SOURCE_EXT); do \
	    for source in `find $(SOURCE_DIR) canvas -name \*.$$ext | grep -v test_program | sed -e 's,^\./,,g'`; do \
	        src="$$src \\\\\n\\t$$source"; \
	    done; \
	    echo "\n"$$ext"SRC += $$src\n" >> $(SOURCE_LIST); src=; \
	done


debug:
	$(info Debug Makefile:)
	$(info )
	$(info SOURCEFILES = $(cSRC) $(ccSRC) $(cppSRC) $(FSRC))
	$(info OBJECTS     = $(OBJECTS))
	$(info DEPENDS     = $(DEPENDS))
	$(info VPATH       = $(VPATH))
	$(info INCLUDE     = $(INCLUDE))
	$(info OBJDIR      = $(OBJDIR))
	@echo
	
FORCE:


-include $(DEPENDS)



##################################################################################

