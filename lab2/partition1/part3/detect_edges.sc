#include "susan.sh"

import "c_uchar7220_queue";
import "c_int7220_queue";
import "setup_brightness_lut";
import "susan_edges";
import "rtos";
import "full_rtos";



behavior DetectEdges(i_uchar7220_receiver in_image,  i_int7220_sender out_r, i_uchar7220_sender out_mid, i_uchar7220_sender out_image, OS_API api_port) implements OS_REG
{

void os_register(int threadID)
  {
    api_port.os_register(threadID);
  }

    uchar bp[516];
        
    SetupBrightnessLut setup_brightness_lut(bp, api_port);
    SusanEdges susan_edges(in_image, out_r, out_mid, bp, out_image, api_port);
    
    void main(void) {
        setup_brightness_lut.main(); 
        susan_edges.main();
    }

};

behavior Edges(i_uchar7220_receiver in_image,  i_int7220_sender out_r, i_uchar7220_sender out_mid, i_uchar7220_sender out_image)
{
    RTOS edges_rtos;
    DetectEdges detect_edges(in_image,  out_r, out_mid, out_image, edges_rtos);

    
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

behavior Edges_wrapper(i_uchar7220_receiver in_image,  i_int7220_sender out_r, i_uchar7220_sender out_mid, i_uchar7220_sender out_image, in int thID, OS_API_TOP api_port_top)
{
    
void os_register(int threadID)
  {
    api_port_top.os_register(threadID);
  }   

    Edges detect_edges(in_image,  out_r, out_mid, out_image);

    
    void main(void) {
	
	api_port_top.acquire_running_key(thID);
	detect_edges;
	api_port_top.os_terminate(thID);

    }
};
