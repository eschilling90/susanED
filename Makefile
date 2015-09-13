CC = gcc
CMP = diff -s
RM  = rm -rf

GOLDFILE = golden.pgm
INFILE =   input_small.pgm    
OUTFILE =  output_edge.pgm



susan: susan.c Makefile
	$(CC) -static -O4 -o susan susan.c get_image.c put_image.c setup_brightness_lut.c edge_draw.c susan_thin.c susan_edges.c -lm 

test:
	./susan $(INFILE) $(OUTFILE) -e
	$(CMP) $(OUTFILE) $(GOLDFILE)

clean:
	$(RM) susan get_image put_image setup_brightness_lut edge_draw susan_thin susan_edges $(OUTFILE)
