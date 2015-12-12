#include "susan.sh"

import "i_receive";
import "c_uchar7220_queue";

behavior ReadImage(i_receive start, in uchar image_buffer[IMAGE_SIZE], i_uchar7220_sender out_image)
{

    void main(void) {
        int i;
        uchar image_buffer_out[IMAGE_SIZE];

        while (true) {
// printf("read image: ready to get start\n");
            start.receive();
// printf("read image: got start signal\n");
            for (i=0; i<IMAGE_SIZE; i++)
                image_buffer_out[i] = image_buffer[i];
// printf("read image: ready to send image to susan\n");
            out_image.send(image_buffer_out);       
// printf("read image: image sent\n");
        }
    }
         
}; 
