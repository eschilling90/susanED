#include <stdio.h>

import "c_double_handshake";
import "c_queue";
import "c_image_queue";

#define X_SIZE 76
#define Y_SIZE 95


behavior write_image(i_receiver port_in3,i_sender port_out_monitor)
{
	unsigned char buf[X_SIZE*Y_SIZE];

	void main(void)
	{
	while(true){
	port_in3.receive(buf,X_SIZE*Y_SIZE);
	//port_in3.receive(&buf);
	port_out_monitor.send(buf,X_SIZE*Y_SIZE);
	}
	}
};

behavior write_image_fsm(i_receiver port_in3,i_sender port_out_monitor)
{
	write_image	inst(port_in3,port_out_monitor);

	void main(void)
	{
	 fsm{inst: goto inst;}
	}
};

