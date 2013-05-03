# Project: test
# Makefile created by Dev-C++ 4.9.9.2

CPP  = g++
CC   = gcc

SRCDIR=/impa/home/g/cbevilac/workspace3/RPN301
OBJDIR = $(SRCDIR)/build/$(RPHOSTTYPE)
LIBDIR = $(SRCDIR)/lib/$(RPHOSTTYPE)

RPNUMERICSDIR = $(SRCDIR)/rpnumerics
WAVEDIR       = $(SRCDIR)/wave
JNIDIR        = $(SRCDIR)/JNI

UTILDIR = $(WAVEDIR)/util
MULTIDDIR = $(WAVEDIR)/multid

RES  = 
OBJ1  = MultipleLoop.o MultipleMatrix.o JetFunctionGridEvaluation.o FunctionGridEvaluation.o main.o $(RES)
LINKOBJ1  = MultipleLoop.o MultipleMatrix.o JetFunctionGridEvaluation.o FunctionGridEvaluation.o main.o $(RES)
LIBS =  -L /usr/lib -l lapack -l blas  -l g2c
INCS =  -I /usr/include -I . -I $(RPNUMERICSDIR) -I $(WAVEDIR) -I $(UTILDIR) -I $(MULTIDDIR) -I $(JNIDIR)
CFLAGS = $(INCS)  
RM = rm -f

clean: $(OBJ1)
	${RM} $(OBJ1) $(OBJ2) $(BIN)

all: $(OBJ1)
	$(CC) $(LINKOBJ1) /impa/home/g/cbevilac/workspace3/RPN301/build/linux_i686/*.o -o main $(LIBS)

main.o: main.cc
	$(CC) -c main.cc -o main.o $(CFLAGS)	

MultipleLoop.o: MultipleLoop.cc
	$(CC) -c MultipleLoop.cc -o MultipleLoop.o $(CFLAGS)
	
MultipleMatrix.o: MultipleMatrix.cc
	$(CC) -c MultipleMatrix.cc -o MultipleMatrix.o $(CFLAGS)
	
JetFunctionGridEvaluation.o: JetFunctionGridEvaluation.cc
	$(CC) -c JetFunctionGridEvaluation.cc -o JetFunctionGridEvaluation.o $(CFLAGS)
	
FunctionGridEvaluation.o: FunctionGridEvaluation.cc
	$(CC) -c FunctionGridEvaluation.cc -o FunctionGridEvaluation.o $(CFLAGS)


	
