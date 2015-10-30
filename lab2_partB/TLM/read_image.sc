#include "susan.sh"

import "i_receive";
import "i_send";
import "HWBus";

behavior ReadImage(i_receive start, 
		   in uchar image_buffer[IMAGE_SIZE],
		   i_send sync,
		   ISlaveHardwareBusProtocol protocol)
{
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
