#include "susan.sh"

import "c_uchar7220_queue";
import "c_int7220_queue";
import "c_uchar7220left_queue";
import "c_uchar7220right_queue";

import "detect_edges";
import "susan_thin";
import "edge_draw";
import "full_rtos";

     
behavior Susan(i_uchar7220left_receiver in_image, i_uchar7220right_sender out_image, OS_API_TOP api_port) 
{

    c_int7220_queue r(1ul, api_port);
    c_uchar7220_queue mid(1ul, api_port);
    c_uchar7220_queue mid_edge_draw(1ul, api_port);
    c_uchar7220_queue image_edge_draw(1ul, api_port);

//    Edges edges(in_image, r, mid, image_edge_draw);
//    Thin thin(r, mid, mid_edge_draw);
//    Draw draw(image_edge_draw, mid_edge_draw, out_image);
 //   template for next step
    Edges_wrapper edges(in_image, r, mid, image_edge_draw, 0, api_port);
    Thin_wrapper thin(r, mid, mid_edge_draw, 1, api_port);
    Draw_wrapper draw(image_edge_draw, mid_edge_draw, out_image, 2, api_port);

        
    void main(void)
    {
/*        fsm {
            edges : goto thin;
            thin  : goto draw;
            draw  : goto edges;
        }*/  
//  template for next step    
	api_port.os_init();
	edges.os_register(0);
	thin.os_register(1);
	draw.os_register(2);
	api_port.par_start();
	par {
	    edges;
	    thin;
	    draw;
	} 
	api_port.par_end();
    }
   
};   

behavior SusanFSM(i_uchar7220left_receiver in_image, i_uchar7220right_sender out_image, OS_API_TOP api_port)
{
  Susan inst(in_image, out_image, api_port);

  void main(void)
  {
    fsm{inst: goto inst;}
  }
};
