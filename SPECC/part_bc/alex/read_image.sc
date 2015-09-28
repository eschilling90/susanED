#include <stdio.h>

import "c_handshake";
import "c_double_handshake";
import "c_queue";
import "c_image_queue";

#define X_SIZE 76
#define Y_SIZE 95

behavior read_image(i_receive port_start,i_receiver port_in_stim,i_sender port_in1)
{

	unsigned char buf[X_SIZE*Y_SIZE];

	void main(void)
	{
	while(true){
	port_start.receive();
	port_in_stim.receive(buf,X_SIZE*Y_SIZE);
	port_in1.send(buf,X_SIZE*Y_SIZE);
	//port_in1.send(buf);
	}
	}
};

behavior read_image_fsm(i_receive port_start,i_receiver port_in_stim,i_sender port_in1)
{
	read_image	inst(port_start,port_in_stim,port_in1);	

	void main(void)
	{
	 fsm{inst: goto inst;}
	}
};

