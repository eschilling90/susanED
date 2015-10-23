#include "susan.sh"

import "susan";
import "read_image";
import "write_image";
import "c_uchar7220left_queue";
import "c_uchar7220right_queue";
import "full_rtos";


behavior Design(i_receive start, in uchar image_buffer[IMAGE_SIZE], i_sender out_image_susan)
{
    RTOS_TOP top_rtos;

    c_uchar7220left_queue in_image(1ul, top_rtos);
    c_uchar7220right_queue out_image(1ul, top_rtos);

    
    ReadImage read_image(start, image_buffer, in_image);
    SusanFSM susan(in_image, out_image, top_rtos);
    WriteImage write_image(out_image, out_image_susan);

    void main(void) {
       par {
            read_image.main();
            susan.main();
            write_image.main();
        }
    }
    
};
