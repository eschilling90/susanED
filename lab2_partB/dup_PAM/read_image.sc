#include "susan.sh"

import "i_receive";
import "HWBus";

behavior ReadImage(i_receive start, 
		   in uchar image_buffer[IMAGE_SIZE], 
		   in  signal unsigned bit[ADDR_WIDTH-1:0] A,
		       signal unsigned bit[DATA_WIDTH-1:0] D,
		   in  signal unsigned bit[1]    ready,
		   out signal unsigned bit[1]    ack,
		   out signal unsigned bit[1] 	 intr)
{
    SlaveHardwareBus protocol(A, D, ready, ack);
    SlaveHardwareSyncGenerate sync(intr);
    SlaveHardwareBusLinkAccess mac(protocol);
    SlaveHardwareDriver driver(mac, sync);
    

    void main(void) {
        int i;
        uchar image_buffer_out[IMAGE_SIZE];

        while (true) {
            start.receive();
            for (i=0; i<IMAGE_SIZE; i++)
                image_buffer_out[i] = image_buffer[i];
            driver.send(INPUT_ADDR, image_buffer_out, sizeof(image_buffer_out));
        }
    }
         
}; 
