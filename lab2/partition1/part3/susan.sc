#include "susan.sh"

import "c_uchar7220_queue";
import "c_int7220_queue";

import "detect_edges";
import "susan_thin";
import "edge_draw";
import "full_rtos";

     
behavior Susan(i_uchar7220_receiver in_image, i_uchar7220_sender out_image) 
{

    c_int7220_queue r(1ul);
    c_uchar7220_queue mid(1ul);
    c_uchar7220_queue mid_edge_draw(1ul);
    c_uchar7220_queue image_edge_draw(1ul);

    RTOS_TOP rtos_top;
    Edges edges(in_image, r, mid, image_edge_draw);
    Thin thin(r, mid, mid_edge_draw);
    Draw draw(image_edge_draw, mid_edge_draw, out_image);
 //   template for next step
 //   Edges_wrapper edges(in_image, r, mid, image_edge_draw);
 //   Thin_wrapper thin(r, mid, mid_edge_draw);
 //   Draw_wrapper draw(image_edge_draw, mid_edge_draw, out_image);

        
    void main(void)
    {
        fsm {
            edges : goto thin;
            thin  : goto draw;
            draw  : goto edges;
        }  
/*  template for next step    
	rtos_top.os_init();
	edges.os_register(0);
	thin.os_register(1);
	draw.os_register(2);
	rtos_top.par_start();
	par {
	    edges;
	    thin;
	    draw;
	} 
	rtos_top.par_end()
*/
    }
   
};   


