CC = scc
CMP = diff -s
RM  = rm -rf

GOLDFILE = golden.pgm
INFILE =   input_small.pgm    
OUTFILE =  out.pgm



susan_edge_detector: susan_edge_detector.sc Makefile
	$(CC) stimulus -sc2sir -vv -ww
	$(CC) setup_brightness_lut -sc2sir -vv -ww
	$(CC) susan_edges -sc2sir -vv -ww
	$(CC) detect_edges -sc2sir -vv -ww
	$(CC) susan_thin -sc2sir -vv -ww
	$(CC) edge_draw -sc2sir -vv -ww
	$(CC) susan -sc2sir -vv -ww
	$(CC) monitor -sc2sir -vv -ww
	$(CC) susan_edge_detector -vv -ww

test:
	./susan 
	$(CMP) $(OUTFILE) $(GOLDFILE)

clean:
	$(RM) *.si *.sir $(OUTFILE)
