#include "susan.sh"

import "i_sender";
import "c_uchar7220_queue";

behavior WriteImage(i_uchar7220_receiver in_image, i_sender out_image)
{

    void main(void) {
        
        uchar image_buffer[IMAGE_SIZE];
        
        while (true) {
// printf("write image: ready to receive image buffer\n");
            in_image.receive(&image_buffer);
// printf("write image: buffer received, about to send it to monitor\n");
            out_image.send(image_buffer, sizeof(image_buffer));       
// printf("write image: buffer sent to monitor\n");
        }
    }
         
}; 
