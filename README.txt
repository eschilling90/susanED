This is the SUSAN lab

SUSAN software flow


get_image() <-- char[] filename (from command line), unsigned char **in, int *x_size, int *y_size
    |
    |
    v
setup_brightness_lut() <-- uchar *bp, int thresh (hard coded: 20), int form (hard coded: 6)
    |
    |
    v
susan_edges() <-- uchar *in (from get_image), uchar *bp (from setup_brightness), uchar *mid, int *r (uses x/y_size from get_image), int max_no_edges (hard coded:2650) , int x_size, int y_size
    |
    |
    v
susan_thin() <-- uchar *mid, int *r, int x_size, int y_size (all from previous stage)
    |
    |
    v
edge_draw() <-- uchar *in, uchar *mid, int x_size, int y_size, int drawing_mode (drawing mode hardcoded:0 ,in from edges, else from thin)
    |
    |
    v  
put_image() <-- char[] filename (from command line), char *in , int x_size, int y_size (from previous step)




Code changes:
1)Set size: 
 -set x_size and y_size to hard coded values in main
 -pass x_size and y_size directly into get_image instead of their addresses
 -At the part where x_size and y_size are extracted from the bpm file using getint(), instead assign these to temp vars
	-> This needs to be done to ensure output and golden are the same, I think because getint() increments the file pointer
2)Remove dynamic memory allocation:
 -all malloc calls converted to using arrays instead


TODO:
1)Remove all unnecessary communication/dependencies (?)
2)Simplify code -> so fargot rid of susan_corners, susan_corners_quick, susan_principle, susan_principle_small,susan_smoothing,
  susan_edges_small ... need to clean up a bit further
3)Put each function in a separate header/source file


***I uploaded the flowchart for part 1a of the lab, you can view it by clicking the "Issues" tab