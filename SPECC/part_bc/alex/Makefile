CC = scc
CMP = diff -s
RM  = rm -rf

GOLDFILE = golden.pgm
INFILE =   input_small.pgm    
OUTFILE =  out.pgm



susan_edge_detector: susan_edge_detector.sc Makefile
	$(CC) c_image_queue -sc2sir -vv -ww
	$(CC) c_intarray_queue -sc2sir -vv -ww
	$(CC) c_bp_queue -sc2sir -vv -ww
	$(CC) c_image_thread_queue -sc2sir -vv -ww
	$(CC) stimulus -sc2sir -vv -ww
	$(CC) setup_brightness_lut -sc2sir -vv -ww
	$(CC) susan_edges -sc2sir -vv -ww
	$(CC) detect_edges -sc2sir -vv -ww
	$(CC) susan_thin -sc2sir -vv -ww
	$(CC) edge_draw -sc2sir -vv -ww
	$(CC) susan -sc2sir -vv -ww
	$(CC) monitor -sc2sir -vv -ww
	$(CC) read_image -sc2sir -vv -ww
	$(CC) write_image -sc2sir -vv -ww
	$(CC) design -sc2sir -vv -ww
	$(CC) susan_edge_detector -sc2sir -vv -ww
	$(CC) susan_edge_detector -vv -ww

test:
	./susan_edge_detector
	$(CMP) $(OUTFILE) $(GOLDFILE)

clean:
	$(RM) *.si  *.exe *.sir *.cc *.h *.o out.pgm susan_edge_detector $(OUTFILE)
