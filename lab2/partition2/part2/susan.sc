#include "susan.sh"

import "c_uchar7220_queue";
import "c_int7220_queue";

import "detect_edges";
import "susan_thin";
import "edge_draw";



behavior Susan_pe1(i_uchar7220_receiver in_image, i_int7220_sender r_pe1_pe2, i_uchar7220_sender mid_pe1_pe2, i_uchar7220_sender image_pe1_pe2) 
{

    Edges edges(in_image, r_pe1_pe2, mid_pe1_pe2, image_pe1_pe2);
        
    void main(void)
    {
        par {
            edges;
        }      
    }
   
};  


behavior Susan_pe2(i_int7220_receiver r_pe1_pe2, i_uchar7220_receiver mid_pe1_pe2, i_uchar7220_receiver image_pe1_pe2, i_uchar7220_sender out_image) 
{

    c_uchar7220_queue mid_thin_edge(1ul);

    Thin  thin(r_pe1_pe2,mid_pe1_pe2,mid_thin_edge);
    Draw  draw(image_pe1_pe2,mid_thin_edge,out_image);

        
    void main(void)
    {
        par {
	     thin;
	     draw;
        }      
    }
   
}; 
     
behavior Susan(i_uchar7220_receiver in_image, i_uchar7220_sender out_image) 
{

    c_int7220_queue   r_pe1_pe2(1ul);
    c_uchar7220_queue mid_pe1_pe2(1ul);
    c_uchar7220_queue image_pe1_pe2(1ul);

    Susan_pe1 pe1(in_image,r_pe1_pe2,mid_pe1_pe2,image_pe1_pe2);
    Susan_pe2 pe2(r_pe1_pe2,mid_pe1_pe2,image_pe1_pe2,out_image);
        
    void main(void)
    {
        par {
            pe1;
	    pe2;
        }      
    }
   
};   





