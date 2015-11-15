SCC = scc
RM = rm -f
SCFILES = typed_queue.sc trainlayer.sc testlayer.sc stimulus.sc monitor.sc mnist.sc neural_network.sc
SIRFILES = typed_queue.sir trainlayer.sir testlayer.sir stimulus.sir monitor.sir mnist.sir neural_network.sir
SCCOPTS =       -vv -ww -g $(DEFINES)
EXEFILE =       neural_network

#--- rules

.SUFFIXES: .sc .sir

.sc.sir:
	$(SCC) $* -sc2sir $(SCCOPTS)

#--- build precess

all: $(EXEFILE)

$(EXEFILE): $(SIRS) 
	$(SCC) $(EXEFILE) -sc2out $(SCCOPTS)

clean:
	$(RM) *~ *.si *.o *.h *.cc
	$(RM) $(SIRFILES) 
	$(RM) $(EXEFILE)

