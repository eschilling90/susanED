#include "susan.sh"

import "i_sender";
import "HWBus";

behavior WriteImage(i_sender out_image,
		    in  signal unsigned bit[ADDR_WIDTH-1:0] A,
		        signal unsigned bit[DATA_WIDTH-1:0] D,
		    in  signal unsigned bit[1]    ready,
		    out signal unsigned bit[1]    ack,
		    out signal unsigned bit[1] 	  intr)
{
    SlaveHardwareBus protocol(A, D, ready, ack);
    SlaveHardwareSyncGenerate sync(intr);
    SlaveHardwareBusLinkAccess mac(protocol);
    SlaveHardwareDriver driver(mac, sync);

    void main(void) {
        
        uchar image_buffer[IMAGE_SIZE];
        
        while (true) {
            driver.receive(OUTPUT_ADDR, &image_buffer, sizeof(image_buffer));
            out_image.send(image_buffer, sizeof(image_buffer));       
        }
    }
         
}; 
