#include "susan.sh"
import "c_uchar7220_queue";
import "setup_brightness_lut";

behavior Median(uchar in0[9810],int i,int j,int x_size, uchar out0)
{
int p[8],k,l,tmp;

 void main (void) {
  p[0]=in0[(i-1)*x_size+j-1];
  p[1]=in0[(i-1)*x_size+j  ];
  p[2]=in0[(i-1)*x_size+j+1];
  p[3]=in0[(i  )*x_size+j-1];
  p[4]=in0[(i  )*x_size+j+1];
  p[5]=in0[(i+1)*x_size+j-1];
  p[6]=in0[(i+1)*x_size+j  ];
  p[7]=in0[(i+1)*x_size+j+1];

  for(k=0; k<7; k++)
    for(l=0; l<(7-k); l++)
      if (p[l]>p[l+1])
      {
        tmp=p[l]; p[l]=p[l+1]; p[l+1]=tmp;
      }

  out0 = ( (p[3]+p[4]) / 2 );
}
};

behavior Enlarge(uchar in0[IMAGE_SIZE],uchar tmp_image[9810])
{
int border;
int x_size,y_size;
int   i, j;

void main(void) {
border = 7;
x_size = X_SIZE;
y_size = Y_SIZE;

  for(i=0; i<IMAGE_SIZE; i++) {  /* copy *in into tmp_image */
    //memcpy(tmp_image+(i+border)*(*x_size+2*border)+border, *in0+i* *x_size, *x_size);
    for (j=0; j<x_size; j++) {
      tmp_image[(i+border)*(x_size+2*border)+border+j] = in0[i*x_size+j];
    }
  }

  for(i=0; i<border; i++) /* copy top and bottom rows; invert as many as necessary */
  {
    //memcpy(tmp_image+(border-1-i)*(*x_size+2*border)+border,*in0+i* *x_size,*x_size);
    //memcpy(tmp_image+(*y_size+border+i)*(*x_size+2*border)+border,*in0+(*y_size-i-1)* *x_size,*x_size);
    for (j=0; j<x_size; j++) {
      tmp_image[(border-1-i)*(x_size+2*border)+border+j] = in0[i*x_size+j];
      tmp_image[(y_size+border+i)*(x_size+2*border)+border+j] = in0[(y_size-i-1)*x_size+j];
    }
  }

  for(i=0; i<border; i++) {/* copy left and right columns */
    for(j=0; j<y_size+2*border; j++)
    {
      tmp_image[j*(x_size+2*border)+border-1-i]=tmp_image[j*(x_size+2*border)+border+i];
      tmp_image[j*(x_size+2*border)+ x_size+border+i]=tmp_image[j*(x_size+2*border)+ x_size+border-1-i];
    }
  }

  //*x_size+=2*border;  /* alter image size */
  //*y_size+=2*border;
  //*in0=tmp_image;      /* repoint in */
}
};

behavior SusanSmoothing(uchar in0[IMAGE_SIZE], uchar bp[512], uchar out0[IMAGE_SIZE])
{

float temp;
int   n_max, increment, mask_size,i,j,k,l,x,y,area,brightness,tmp,centre,x_size,y_size;
uchar dp[225], dpt[225], cp, in1[9810], med;
float total, dt;

Median median(in1,i,j,x_size, med);
Enlarge enlarge(in0, in1);

void main(void) {
dt = 4.0;

  x_size = X_SIZE;
  y_size = Y_SIZE;
  out0=in0;
  //*out0=in0; //might need to be this
  
  mask_size = 7;

  total=0.1; /* test for total's type */

  //enlarge(in0,in1);
  enlarge.main();

  x_size += 14;
  y_size += 14;

  n_max = (mask_size*2) + 1;

  increment = x_size - n_max;

  //dp     = (unsigned char *)malloc(n_max*n_max);
  //dpt    = dp;
  temp   = -16;

  k=0;
  for(i=-mask_size; i<=mask_size; i++)
    for(j=-mask_size; j<=mask_size; j++)
    {
      x = (int) (100.0 * exp( ((float)((i*i)+(j*j))) / temp ));
      dpt[k] = (unsigned char)x;
      k++;
    }

  l=0;
  for (i=mask_size;i<y_size-mask_size;i++)
  {
    for (j=mask_size;j<x_size-mask_size;j++)
    {
      area = 0;
      total = 0;
      dpt = dp;
      //ip = in0 + ((i-mask_size)*x_size) + j - mask_size;
      centre = in1[i*x_size+j];
      cp = bp[centre];
      k=0;
      for(y=-mask_size; y<=mask_size; y++) {
        for(x=-mask_size; x<=mask_size; x++) {
          brightness = in1[((i-mask_size)*x_size) + j - mask_size + k * increment];
          tmp = dpt[k] * (cp-brightness);
          area += tmp;
          total += tmp * brightness;
          k++;
        }
        //ip += increment;
      }
      tmp = area-10000;
      if (tmp==0) {
	median.main();
        out0[l]=med;
      } else {
        out0[l]=((total-(centre*10000))/tmp);
      }
      l++;
    }
  }
}
};

behavior Smoothing(i_uchar7220_receiver in_image, uchar bp[516], i_uchar7220_sender out_image)
{
    uchar image_buffer[IMAGE_SIZE];
    uchar out_image_buffer[IMAGE_SIZE];

    SusanSmooth_ReadInput susan_smooth_read_input(in_image, image_buffer); 
    SusanSmooth_WriteOutput susan_smooth_write_output(out_image, out_image_buffer);
    SusanSmooth susan_smoothing(image_buffer, bp, out_image_buffer);
                 
    void main(void) {
        
        fsm {
                    susan_smooth_read_input: goto susan_smoothing;
                    susan_smoothing: goto susan_smooth_write_output;
                    susan_smooth_write_output: {}
        }                      
    }           
};

behavior SusanSmooth(i_uchar7220_receiver in0,i_uchar7220_sender in0_out)
{

	uchar bp[516];
	
	SetupBrightnessLut setup_brightness_lut(bp);
	Smoothing smoothing(in0,bp,in0_out);
	
	void main(void) {
	   setup_brigntess_lut.main();
	   smoothing.main(); //needs to be converted
	}

};


behavior SusanSmooth_ReadInput(i_uchar7220_receiver in_in0, uchar in0[IMAGE_SIZE])
{

    void main(void) {
        in_in0.receive(&in0);
    }
};

behavior SusanSmooth_WriteOutput(i_uchar7220_sender out_in0, uchar in0[IMAGE_SIZE])
{
    void main(void) {
        out_in0.send(in0);      
    }
};



