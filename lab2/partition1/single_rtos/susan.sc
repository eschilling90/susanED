#include "susan.sh"

import "c_uchar7220_queue";
import "c_int7220_queue";

import "detect_edges";
import "susan_thin";
import "edge_draw";

import "full_rtos";

     
behavior TASK_PE1(i_uchar7220_left_receiver in_image, i_uchar7220_right_sender out_image, OS_API_TOP os_port) 
{

    c_int7220_queue r(1ul);
    c_uchar7220_queue mid(1ul);
    c_uchar7220_queue mid_edge_draw(1ul);
    c_uchar7220_queue image_edge_draw(1ul);

    Edges edges(in_image, r, mid, image_edge_draw, os_port);
    ThinWapper thin(r, mid, mid_edge_draw, os_port);
    Draw draw(image_edge_draw, mid_edge_draw, out_image, os_port);
        
    void main(void)
    {
	os_port.os_init();
	os_port.par_start();
        par {
            edges;
            thin;
            draw;
        }      
	os_port.par_end();
    }
   
};   

behavior PE1(i_uchar7220_left_receiver in_image, i_uchar7220_right_sender out_image)
{
  RTOS_TOP rtos;
  TASK_PE1 susan(in_image, out_image, rtos);

  void main(void)
  {
    susan;
  }
};
