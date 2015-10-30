#include "susan.sh"

import "susan";
import "read_image";
import "write_image";
import "c_uchar7220_queue";
import "HWBus";


behavior Design(i_receive start, in uchar image_buffer[IMAGE_SIZE], i_sender out_image_susan)
{

    signal unsigned bit[ADDR_WIDTH-1:0] Addr;
    signal unsigned bit[DATA_WIDTH-1:0] Data;
    signal unsigned bit[1]    ready;
    signal unsigned bit[1]    ack;
    signal unsigned bit[1]    intrpt_read;
    signal unsigned bit[1]    intrpt_write;

    // unfinished!!
    
    ReadImage read_image(start, image_buffer, Addr, Data, ready, ack, intrpt_read);
    PE1 pe1(Addr, Data, ready, ack, intrpt_read, intrpt_write);
    WriteImage write_image(out_image_susan, Addr, Data, ready, ack, intrpt_write);

    void main(void) {
       par {
            read_image.main();
            pe1.main();
            write_image.main();
        }
    }
    
};
