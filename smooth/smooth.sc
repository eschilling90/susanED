#include "susan.sh"

import "c_uchar7220_queue";
import "c_int7220_queue";

import "susan_smooth1";

     
behavior Smooth(i_uchar7220_receiver in_image, i_uchar7220_sender out_image) 
{

    // c_uchar7220_queue image_smooth(1ul);	// commented by TT

    // SusanSmooth susan(in_image, image_smooth);	// modified by TT
    SusanSmooth susan(in_image, out_image);
        
    void main(void)
    {
        fsm {
            susan: goto susan;
        }      
    }
   
};   


