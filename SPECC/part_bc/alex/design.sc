#include <stdio.h>

import "susan";
import "read_image";
import "write_image";

import "c_queue";
import "c_handshake";
import "c_image_queue";

#define QUEUE_SIZE 7220ul
#define const unsigned long SIZE 1


behavior design(i_receive port_start, i_receiver port_in_stim, i_sender port_out_monitor)
{
	c_queue			com_read_susan(QUEUE_SIZE), com_susan_write(QUEUE_SIZE);	
	//c_image_queue		com_read_susan((SIZE)), com_susan_write((SIZE));	
	read_image_fsm		readImage(port_start,port_in_stim,com_read_susan);
	susan			susanBlock(com_read_susan,com_susan_write);
	write_image_fsm		writeImage(com_susan_write,port_out_monitor);

	

	void main(void)
	{
	par{

	    readImage.main();
	    susanBlock.main();
	    writeImage.main();

           }
	}
};

