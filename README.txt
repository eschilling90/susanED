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
3)Removed all unused functions
4)Moved all functions into separate header/source files
5)Any variables that did not need to be passed into functions (only used within the function) were hard coded into the function itself.  
E.G. threshold, variables other than bp in setup_brightness
6) Removed all argument parsing from main since only -e will be used.


TODO:
Part A)
1)Remove all unnecessary communication/dependencies (?)
2)Simplify code -> maybe need to do a little more?
3)Remove "sys/file" header? It says to do this in susan.c, not sure how to go about it though...
4*)Make sure arguments passed between functions are not of type pointer *** -> explicitly pass arrays and convert pointers into arrays into array indices
Part B)
1)convert .h/.c -> .sc/.sir .  Combine setup_brightness_lut and susan_edges into detect edges
...


***I uploaded the flowchart for part 1a of the lab, you can view it by clicking the "Issues" tab