#easy make for test rtos

all: rtos.sc test_rtos.sc
	scc rtos -sc2sir -vv
	scc test_rtos -sc2out -vv
clean:
	rm -f *.si *.sir *.cc *.h *.o
	rm -f test_rtos
