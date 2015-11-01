#include "susan.sh"

import "i_sender";
import "i_send";
import "HWBus";

behavior WriteImage(i_sender out_image,
		    i_send sync,
		    ISlaveHardwareBusLinkAccess mac)
{
    SlaveHardwareDriver driver(mac, sync);

    void main(void) {
        
        uchar image_buffer[IMAGE_SIZE];
        
        while (true) {
            driver.receive(OUTPUT_ADDR, &image_buffer, sizeof(image_buffer));
            out_image.send(image_buffer, sizeof(image_buffer));       
        }
    }
         
}; 
