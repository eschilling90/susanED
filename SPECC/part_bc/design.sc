#include <stdio.h>

import "susan";
import "read_image";
import "write_image";

import "c_queue";

#define QUEUE_SIZE 7220ul


behavior design(i_receive port_start, i_receiver port_in_stim, i_sender port_out_monitor)
{
	c_queue			com_read_susan(QUEUE_SIZE), com_susan_write(QUEUE_SIZE);		
	read_image		readImage(port_start,port_in_stim,com_read_susan);
	susan			susanBlock(com_read_susan,com_susan_write);
	write_image		writeImage(com_susan_write,port_out_monitor);

	void main(void)
	{
	par{
	    readImage.main();
	    susanBlock.main();
	    writeImage.main();
           }
	}
};

