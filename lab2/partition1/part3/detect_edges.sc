#include "susan.sh"

import "c_uchar7220_queue";
import "c_int7220_queue";
import "setup_brightness_lut";
import "susan_edges";
import "rtos";

behavior DetectEdges(i_uchar7220_receiver in_image,  i_int7220_sender out_r, i_uchar7220_sender out_mid, i_uchar7220_sender out_image, OS_API api_port)
{

    uchar bp[516];
        
    SetupBrightnessLut setup_brightness_lut(bp, api_port);
    SusanEdges susan_edges(in_image, out_r, out_mid, bp, out_image, api_port);
    
    void main(void) {
        setup_brightness_lut.main(); 
        susan_edges.main();
    }

};

behavior Edges(i_uchar7220_receiver in_image,  i_int7220_sender out_r, i_uchar7220_sender out_mid, i_uchar7220_sender out_image, OS_API api_port)
{
    DetectEdges detect_edges(in_image,  out_r, out_mid, out_image, api_port);
    
    void main(void) {
	//printf("detect edges start\n");
        //fsm{
	par{
            //detect_edges: {goto detect_edges;}
	    detect_edges;
        }
	//printf("detect edges end\n");
    }
};

