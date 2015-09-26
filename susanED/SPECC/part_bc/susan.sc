#include <stdio.h>

import "detect_edges";
import "susan_thin";
import "edge_draw";

import "c_queue";
import "c_handshake";

#define QUEUE_SIZE 7220ul
#define QUEUE_SIZE_INT 28880ul //sizeof(int) * 7220ul



behavior susan(i_receiver port_in1,i_sender port_in3)
{
	c_queue			com_r(QUEUE_SIZE_INT);		
        c_queue 		com_mid1(QUEUE_SIZE);
	c_queue 		com_mid2(QUEUE_SIZE);
	c_queue 		com_in2(QUEUE_SIZE);
	detect_edges		detectEdges(port_in1,com_in2,com_r,com_mid1);
	susan_thin		susanThin(com_r,com_mid1,com_mid2);
	edge_draw		edgeDraw(com_mid2,com_in2,port_in3);

	void main(void)
	{
	fsm{
            detectEdges: goto susanThin;
            susanThin: goto edgeDraw;
            edgeDraw: goto detectEdges;
           }
	}
};

