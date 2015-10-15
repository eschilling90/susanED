#include "susan.sh"

import "c_uchar7220_queue";
import "c_int7220_queue";

import "detect_edges";
import "susan_thin";
import "edge_draw";

     
behavior Susan(i_uchar7220_receiver in_image, i_uchar7220_sender out_image) 
{

    c_int7220_queue r(1ul);
    c_uchar7220_queue mid(1ul);
    c_uchar7220_queue image_pe1_pe2(1ul);

    Susan_pe1 pe1(in_image, r, mid, image_pe1_pe2);
    Susan_pe2 pe2(image_pe1_pe2,r, mid, out_image);

        
    void main(void)
    {
        par {
            pe1;
            pe2;
        }      
    }
   
};   

behavior Susan_pe1(i_uchar7220_receiver in_image,i_int7220_sender r,i_uchar7220_sender mid,i_uchar7220_sender image_pe1_pe2)
{
	
	Edges  edges(in_image,r,mid,image_susan_pe2);

	void main(void)
	{
	   par{
	      edges;
	   }
	}
};


behavior Susan_pe2(i_uchar7220_receiver image_pe1_pe2,i_int7220_receiver r,i_uchar7220_receiver mid, i_char7220_sender out_image)
{

	c_uchar7220_queue mid_edge_draw(1ul);	

	Thin  thin(r,mid,mid_edge_draw);
	Draw  draw(image_pe1_pe2,mid_edge_draw,out_image);

	void main(void)
	{
	   par{
	      thin;
	      draw;
	   {
	}
}
