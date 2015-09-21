#include <stdio.h>

import "c_double_handshake";
import "setup_brightness_lut";
import "susan_edges";

behavior detect_edges(i_receiver port_in1, i_sender port_in2, i_sender port_r, i_sender port_mid1)
{
	c_double_handshake    com_bp;
        setup_brightness_lut  setupBrightnessLut(com_bp);
	susan_edges	      susanEdges(port_in1,port_r,port_mid1,com_bp);
	
	void main(void)
	{
		setupBrightnessLut.main();
		susanEdges.main();
	}
};

