#include <stdlib.h>
import "stimulus";
import "design";
import "monitor";

import "c_handshake";
import "c_double_handshake";
//import "c_queue";

behavior Main(void)
{
	//c_queue			image_buffer_in(7220ul);
	//c_queue			image_buffer_out(7220ul);
	c_double_handshake 	image_in,image_out;
	c_handshake		susan_start;
	stimulus		stim(susan_start,image_in);
	//susan			sus(susan_start,image_buffer_in,image_buffer_out);
	design			des(susan_start,image_in,image_out);
	monitor			mon(image_out);

	int main(void)
	{
		par{
			stim.main();
			//sus.main();
			des.main();
			mon.main();
		}
		return 0;
	} 
};

