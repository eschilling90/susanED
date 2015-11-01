#include "susan.sh"

import "susan";
import "read_image";
import "write_image";
import "c_uchar7220_queue";
import "HWBus";
import "os_double_handshake_signal";


behavior Design(i_receive start, in uchar image_buffer[IMAGE_SIZE], i_sender out_image_susan)
{
    HardwareBusProtocolMACTLM BusMACTLM;
    os_double_handshake_signal intr_read;
    os_double_handshake_signal intr_write;
    
    ReadImage read_image(start, image_buffer, intr_read, BusMACTLM);
    PE1 pe1(BusMACTLM, intr_read, intr_write);
    WriteImage write_image(out_image_susan, intr_write, BusMACTLM);

    void main(void) {
       par {
            read_image.main();
            pe1.main();
            write_image.main();
        }
    }
    
};
