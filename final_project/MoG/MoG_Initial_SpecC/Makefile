#
# Exploration specific makefile for task 
#

TARGETS=$(USER_SPEC)

#input specification 
USER_SPEC = Design

#base name of the generated file 
GEN_NAME  = BGE
PE_NAME   = BGE

TESTS=frame-based.py


# files of local dir to include in distribution
DISTFILES   = frame-based.py Design.sc Makefile

# include all the default rules
include ../Makefile.rules

#------------------------------------------------------------------------------
# testcase special rules
# rule to execute the test cases

# for now just exec arch exploration
test_exec: arch
	./arch
