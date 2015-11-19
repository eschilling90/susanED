SCC = scc
RM = rm -f
SCFILES = typed_queue.sc network.sc loader.sc stimulus.sc monitor.sc mnist.sc ANN.sc
SIRFILES = typed_queue.sir network.sir loader.sir stimulus.sir monitor.sir mnist.sir ANN.sir
SCCOPTS =       -vv -ww -g $(DEFINES)
EXEFILE =       ANN

#--- rules

.SUFFIXES: .sc .sir

.sc.sir:
	$(SCC) $* -sc2sir $(SCCOPTS)

#--- build precess

all: $(EXEFILE)

$(EXEFILE): $(SIRFILES) $(SCFILES) 
	$(SCC) $(EXEFILE) -sc2out $(SCCOPTS)

clean:
	$(RM) *~ *.si *.o *.h *.cc
	$(RM) $(SIRFILES) 
	$(RM) $(EXEFILE)

