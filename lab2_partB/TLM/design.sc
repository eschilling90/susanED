#include "susan.sh"

import "susan";
import "read_image";
import "write_image";
import "c_uchar7220_queue";
import "HWBus";
import "os_handshake";


behavior Design(i_receive start, in uchar image_buffer[IMAGE_SIZE], i_sender out_image_susan)
{
    HardwareBusProtocolTLM BusTLM;
    c_handshake intr_read;
    c_handshake intr_write;
    
    ReadImage read_image(start, image_buffer, intr_read, BusTLM);
    PE1 pe1(BusTLM, intr_read, intr_write);
    WriteImage write_image(out_image_susan, intr_write, BusTLM);

    void main(void) {
       par {
            read_image.main();
            pe1.main();
            write_image.main();
        }
    }
    
};
