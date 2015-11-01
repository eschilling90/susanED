#include "susan.sh"

import "c_uchar7220_queue";
import "c_int7220_queue";
import "setup_brightness_lut";
import "susan_edges";

import "full_rtos";

behavior DetectEdges(os_dr_receiver in_image,  i_int7220_sender out_r, i_uchar7220_sender out_mid, i_uchar7220_sender out_image, OS_API_TOP os_port)
{

    uchar bp[516];
        
    SetupBrightnessLut setup_brightness_lut(bp, os_port);
    SusanEdges susan_edges(in_image, out_r, out_mid, bp, out_image, os_port);
    
    void main(void) {
	os_port.os_register(0);	// fixed threadID, ID of DetectEdges is 0
	os_port.acquire_running_key(0);
        setup_brightness_lut.main(); 
        susan_edges.main();
	os_port.os_terminate(0);
    }

};

behavior Edges(os_dr_receiver in_image,  i_int7220_sender out_r, i_uchar7220_sender out_mid, i_uchar7220_sender out_image, OS_API_TOP os_port)
{

    DetectEdges detect_edges(in_image,  out_r, out_mid, out_image, os_port);
    
    void main(void) {
        fsm{
            detect_edges: {goto detect_edges;}
        }
    }
};

