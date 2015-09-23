#include <stdio.h>

import "detect_edges";
import "susan_thin";
import "edge_draw";

import "c_queue";
import "c_handshake";

#define QUEUE_SIZE 7220ul

behavior B(i_receive port_start)
{
  void main(void)
  {
    port_start.receive();
  }
};

behavior susan(i_receive port_start,i_receiver port_in1,i_sender port_in3)
{
	c_queue			com_r(28880ul);		// sizeof(int) * 7220ul
        c_queue 		com_mid1(7220ul);
	c_queue 		com_mid2(7220ul);
	c_queue 		com_in2(7220ul);
	detect_edges		detectEdges(port_in1,com_in2,com_r,com_mid1);
	susan_thin		susanThin(com_r,com_mid1,com_mid2);
	edge_draw		edgeDraw(com_mid2,com_in2,port_in3);
        B			b1(port_start);

	void main(void)
	{
	fsm{b1: goto detectEdges;
            detectEdges: goto susanThin;
            susanThin: goto edgeDraw;
            edgeDraw: goto b1;
           }
	}
};

