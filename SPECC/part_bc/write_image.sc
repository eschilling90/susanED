#include <stdio.h>

#define X_SIZE 76
#define Y_SIZE 95

import "c_queue";
import "c_handshake";


behavior write_image(i_receiver port_in3,i_sender port_out_monitor)
{
	char in3_buffer[X_SIZE*Y_SIZE];
	void main(void)
	{
	//how do you connect this? transceiver?
	port_in3.receive(in3_buffer, X_SIZE*Y_SIZE*sizeof(char));
	port_out_monitor.send(in3_buffer, X_SIZE*Y_SIZE*sizeof(char));
	}
};

