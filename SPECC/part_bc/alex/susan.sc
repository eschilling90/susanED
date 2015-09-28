#include <stdio.h>

import "detect_edges";
import "susan_thin";
import "edge_draw";

import "c_queue";
import "c_handshake";
import "c_intarray_queue";
import "c_image_queue";

#define QUEUE_SIZE 7220ul
#define QUEUE_SIZE_INT 28880ul //sizeof(int) * 7220ul
#define const unsigned long SIZE 1



behavior susan(i_receiver port_in1,i_sender port_in3)
{
        c_intarray_queue	com_r((SIZE));	
//        c_queue 		com_mid1(QUEUE_SIZE);
        c_image_queue 		com_mid1((SIZE));
//	c_queue 		com_mid2(QUEUE_SIZE);
	c_image_queue 		com_mid2((SIZE));
//	c_queue 		com_in2(QUEUE_SIZE);
	c_image_queue 		com_in2((SIZE));
	detect_edges_fsm	detectEdges(port_in1,com_in2,com_r,com_mid1);
	susan_thin_fsm		susanThin(com_r,com_mid1,com_mid2);
	edge_draw		edgeDraw(com_mid2,com_in2,port_in3);

	void main(void)
	{
	par{
            susanThin.main();
            edgeDraw.main();
            detectEdges.main();
           }
	}
};

