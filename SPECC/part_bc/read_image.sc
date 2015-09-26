#include <stdio.h>

#define X_SIZE 76
#define Y_SIZE 95

import "c_queue";
import "c_handshake";
import "c_double_handshake";

behavior read_image(i_receive port_start,i_receiver port_in_stim,i_sender port_in1)
{
	char in_stim[X_SIZE*Y_SIZE];
	void main(void)
	{
	port_start.receive();
	//How do I connect port_in_stim to port_in1?
        port_in_stim.receive(in_stim, X_SIZE*Y_SIZE*sizeof(char));
	port_in1.send(in_stim, X_SIZE*Y_SIZE*sizeof(char));
	}
};

