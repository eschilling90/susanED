#include <stdio.h>

import "c_queue";
import "setup_brightness_lut";
import "susan_edges";

behavior detect_edges(i_receiver port_in1, i_sender port_in2, i_sender port_r, i_sender port_mid1)
{
	c_queue		      com_bp(516ul);
        brt_lut_fsm	      setupBrightnessLut(com_bp);
	susan_edges_fsm	      susanEdges(port_in1,port_in2,port_r,port_mid1,com_bp);
	
	void main(void)
	{
	  par{
		setupBrightnessLut.main();
		susanEdges.main();
	  }
	}
};

