#include <stdio.h>
#include <sim.sh>

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
	//sim_time_string		buf;
	//double 			time_start, time_end;

	void main(void)
	{
	par{
	    //time_start = now();
	    readImage.main();
	    susanBlock.main();
	    writeImage.main();
	    //time_end = now();
	    //printf("Elapsed time: %4s\n",time2str(buf,time_end-time_start));
           }
	}
};

