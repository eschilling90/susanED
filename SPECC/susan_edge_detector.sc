#include <stdlib.h>
import "stimulus";
import "susan";
import "monitor";

import "c_handshake";
import "c_double_handshake";
import "c_queue";

behavior Main(void)
{
	c_double_handshake	image_buffer_in;
	c_queue			image_buffer_out(1ul);
	c_handshake		susan_start;
	stimulus		stim(susan_start,image_buffer_in);
	susan			sus(susan_start,image_buffer_in,image_buffer_out);
	monitor			mon(image_buffer_out);

	int main(void)
	{
		par{
			stim.main();
			sus.main();
			mon.main();
		}
		return 0;
	} 
};

