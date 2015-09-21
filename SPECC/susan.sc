import "detect_edges";
import "susan_thin";
import "edge_draw";

import "c_double_handshake";

behavior susan(i_receive port_start,i_receiver port_in1,i_sender port_in3)
{
	c_double_handshake	com_r,com_mid1,com_mid2,com_in2;
	detect_edges		detectEdges(port_in1,com_in2,com_r,com_mid1);
	susan_thin		susanThin(com_r,com_mid1,com_mid2);
	edge_draw		edgeDraw(com_mid2,com_in2,port_in3;

	void main(void)
	{
	    port_start.receive;
		detectEdges.main();
		susanThin.main();
		edgeDraw.main();
	}
};

