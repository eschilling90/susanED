#include <stdio.h>

import "c_queue";
import "setup_brightness_lut";
import "susan_edges";
import "c_bp_queue";
import "c_intarray_queue";
import "c_image_queue";

const unsigned long SIZE = 1; //number of spaces available on queue

behavior detect_edges(i_receiver port_in1, i_image_sender port_in2, i_intarray_sender port_r, i_image_sender port_mid1)
{
	//c_queue		      com_bp(516ul);
	c_bp_queue		  com_bp((SIZE));
        setup_brightness_lut_fsm  setupBrightnessLut(com_bp);
	susan_edges_fsm	      	  susanEdges(port_in1,port_in2,port_r,port_mid1,com_bp);
	
	void main(void)
	{
		par{
		setupBrightnessLut.main();
		susanEdges.main();
		}
	}
};

behavior detect_edges_fsm(i_receiver port_in1, i_image_sender port_in2, i_intarray_sender port_r, i_image_sender port_mid1)
{
	detect_edges	inst(port_in1,port_in2,port_r,port_mid1);
	
	void main(void)
	{
	 fsm{inst: goto inst;}
	}
};

