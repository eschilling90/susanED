#include <stdio.h>

#define X_SIZE 76
#define Y_SIZE 95

import "c_queue";

behavior susan_thin(i_receiver port_r, i_receiver port_mid1, i_sender port_mid2)
{
int r[X_SIZE*Y_SIZE];
unsigned char mid[X_SIZE*Y_SIZE];
int   l[9], centre, nlinks, npieces,
      b01, b12, b21, b10,
      p1, p2, p3, p4,
      b00, b02, b20, b22,
      m, n, a, b, x, y, i, j;
unsigned char *mp;

  void main(void)
  {
    printf("start susan thin\n");
    port_r.receive(r, X_SIZE*Y_SIZE*sizeof(int));
    printf("r is received\n");
    port_mid1.receive(mid, X_SIZE*Y_SIZE*sizeof(unsigned char));
    printf("mid1 is received\n");
  for (i=4;i<Y_SIZE-4;i++)
    for (j=4;j<X_SIZE-4;j++)
      if (mid[i*X_SIZE+j]<8)
      {
        centre = r[i*X_SIZE+j];

        mp=mid + (i-1)*X_SIZE + j-1;

        n = (*mp<8) +
            (*(mp+1)<8) +
            (*(mp+2)<8) +
            (*(mp+X_SIZE)<8) +
            (*(mp+X_SIZE+2)<8) +
            (*(mp+X_SIZE+X_SIZE)<8) +
            (*(mp+X_SIZE+X_SIZE+1)<8) +
            (*(mp+X_SIZE+X_SIZE+2)<8);

        if (n==0)
          mid[i*X_SIZE+j]=100;

        if ( (n==1) && (mid[i*X_SIZE+j]<6) )
        {

          l[0]=r[(i-1)*X_SIZE+j-1]; l[1]=r[(i-1)*X_SIZE+j]; l[2]=r[(i-1)*X_SIZE+j+1];
          l[3]=r[(i  )*X_SIZE+j-1]; l[4]=0;                 l[5]=r[(i  )*X_SIZE+j+1];
          l[6]=r[(i+1)*X_SIZE+j-1]; l[7]=r[(i+1)*X_SIZE+j]; l[8]=r[(i+1)*X_SIZE+j+1];

          if (mid[(i-1)*X_SIZE+j-1]<8)        { l[0]=0; l[1]=0; l[3]=0; l[2]*=2; 
                                                l[6]*=2; l[5]*=3; l[7]*=3; l[8]*=4; }
          else { if (mid[(i-1)*X_SIZE+j]<8)   { l[1]=0; l[0]=0; l[2]=0; l[3]*=2; 
                                                l[5]*=2; l[6]*=3; l[8]*=3; l[7]*=4; }
          else { if (mid[(i-1)*X_SIZE+j+1]<8) { l[2]=0; l[1]=0; l[5]=0; l[0]*=2; 
                                                l[8]*=2; l[3]*=3; l[7]*=3; l[6]*=4; }
          else { if (mid[(i)*X_SIZE+j-1]<8)   { l[3]=0; l[0]=0; l[6]=0; l[1]*=2; 
                                                l[7]*=2; l[2]*=3; l[8]*=3; l[5]*=4; }
          else { if (mid[(i)*X_SIZE+j+1]<8)   { l[5]=0; l[2]=0; l[8]=0; l[1]*=2; 
                                                l[7]*=2; l[0]*=3; l[6]*=3; l[3]*=4; }
          else { if (mid[(i+1)*X_SIZE+j-1]<8) { l[6]=0; l[3]=0; l[7]=0; l[0]*=2; 
                                                l[8]*=2; l[1]*=3; l[5]*=3; l[2]*=4; }
          else { if (mid[(i+1)*X_SIZE+j]<8)   { l[7]=0; l[6]=0; l[8]=0; l[3]*=2; 
                                                l[5]*=2; l[0]*=3; l[2]*=3; l[1]*=4; }
          else { if (mid[(i+1)*X_SIZE+j+1]<8) { l[8]=0; l[5]=0; l[7]=0; l[6]*=2; 
                                                l[2]*=2; l[1]*=3; l[3]*=3; l[0]*=4; } }}}}}}}


          for(y=0; y<3; y++)
            for(x=0; x<3; x++)
              if (l[y+y+y+x]>m) { m=l[y+y+y+x]; a=y; b=x; }

          if (m>0)
          {
            if (mid[i*X_SIZE+j]<4)
              mid[(i+a-1)*X_SIZE+j+b-1] = 4;
            else
              mid[(i+a-1)*X_SIZE+j+b-1] = mid[i*X_SIZE+j]+1;
            if ( (a+a+b) < 3 )
	    {
              i+=a-1;
              j+=b-2;
              if (i<4) i=4;
              if (j<4) j=4;
	    }
	  }
        }

        if (n==2)
	{
          b00 = mid[(i-1)*X_SIZE+j-1]<8;
          b02 = mid[(i-1)*X_SIZE+j+1]<8;
	  b20 = mid[(i+1)*X_SIZE+j-1]<8;
          b22 = mid[(i+1)*X_SIZE+j+1]<8;
          if ( ((b00+b02+b20+b22)==2) && ((b00|b22)&(b02|b20)))
	  {
            if (b00) 
	    {
              if (b02) { x=0; y=-1; }
              else     { x=-1; y=0; }
	    }
            else
	    {
              if (b02) { x=1; y=0; }
              else     { x=0; y=1; }
	    }
            if (((float)r[(i+y)*X_SIZE+j+x]/(float)centre) > 0.7)
	    {
              if ( ( (x==0) && (mid[(i+(2*y))*X_SIZE+j]>7) && (mid[(i+(2*y))*X_SIZE+j-1]>7) && (mid[(i+(2*y))*X_SIZE+j+1]>7) ) ||
                   ( (y==0) && (mid[(i)*X_SIZE+j+(2*x)]>7) && (mid[(i+1)*X_SIZE+j+(2*x)]>7) && (mid[(i-1)*X_SIZE+j+(2*x)]>7) ) )
	      {
                mid[(i)*X_SIZE+j]=100;
                mid[(i+y)*X_SIZE+j+x]=3;
	      }
	    }
	  }
          else
          {
            b01 = mid[(i-1)*X_SIZE+j  ]<8;
            b12 = mid[(i  )*X_SIZE+j+1]<8;
            b21 = mid[(i+1)*X_SIZE+j  ]<8;
            b10 = mid[(i  )*X_SIZE+j-1]<8;

#ifdef IGNORETHIS
            if ( (b00&b01)|(b00&b10)|(b02&b01)|(b02&b12)|(b20&b10)|(b20&b21)|(b22&b21)|(b22&b12) )
	    {  
              if ( ((b01)&(mid[(i-2)*X_SIZE+j-1]>7)&(mid[(i-2)*X_SIZE+j]>7)&(mid[(i-2)*X_SIZE+j+1]>7)&
                                    ((b00&((2*r[(i-1)*X_SIZE+j+1])>centre))|(b02&((2*r[(i-1)*X_SIZE+j-1])>centre)))) |
                   ((b10)&(mid[(i-1)*X_SIZE+j-2]>7)&(mid[(i)*X_SIZE+j-2]>7)&(mid[(i+1)*X_SIZE+j-2]>7)&
                                    ((b00&((2*r[(i+1)*X_SIZE+j-1])>centre))|(b20&((2*r[(i-1)*X_SIZE+j-1])>centre)))) |
                   ((b12)&(mid[(i-1)*X_SIZE+j+2]>7)&(mid[(i)*X_SIZE+j+2]>7)&(mid[(i+1)*X_SIZE+j+2]>7)&
                                    ((b02&((2*r[(i+1)*X_SIZE+j+1])>centre))|(b22&((2*r[(i-1)*X_SIZE+j+1])>centre)))) |
                   ((b21)&(mid[(i+2)*X_SIZE+j-1]>7)&(mid[(i+2)*X_SIZE+j]>7)&(mid[(i+2)*X_SIZE+j+1]>7)&
                                    ((b20&((2*r[(i+1)*X_SIZE+j+1])>centre))|(b22&((2*r[(i+1)*X_SIZE+j-1])>centre)))) )
	      {
                mid[(i)*X_SIZE+j]=100;
                if (b10&b20) j-=2;
                if (b00|b01|b02) { i--; j-=2; }
  	      }
	    }
#endif

            if ( ((b01+b12+b21+b10)==2) && ((b10|b12)&(b01|b21)) &&
                 ((b01&((mid[(i-2)*X_SIZE+j-1]<8)|(mid[(i-2)*X_SIZE+j+1]<8)))|(b10&((mid[(i-1)*X_SIZE+j-2]<8)|(mid[(i+1)*X_SIZE+j-2]<8)))|
                (b12&((mid[(i-1)*X_SIZE+j+2]<8)|(mid[(i+1)*X_SIZE+j+2]<8)))|(b21&((mid[(i+2)*X_SIZE+j-1]<8)|(mid[(i+2)*X_SIZE+j+1]<8)))) )
	    {
              mid[(i)*X_SIZE+j]=100;
              i--;
              j-=2;
              if (i<4) i=4;
              if (j<4) j=4;
	    }
	  }
	}

        if (n>2)
        {
          b01 = mid[(i-1)*X_SIZE+j  ]<8;
          b12 = mid[(i  )*X_SIZE+j+1]<8;
          b21 = mid[(i+1)*X_SIZE+j  ]<8;
          b10 = mid[(i  )*X_SIZE+j-1]<8;
          if((b01+b12+b21+b10)>1)
          {
            b00 = mid[(i-1)*X_SIZE+j-1]<8;
            b02 = mid[(i-1)*X_SIZE+j+1]<8;
	    b20 = mid[(i+1)*X_SIZE+j-1]<8;
	    b22 = mid[(i+1)*X_SIZE+j+1]<8;
            p1 = b00 | b01;
            p2 = b02 | b12;
            p3 = b22 | b21;
            p4 = b20 | b10;

            if( ((p1 + p2 + p3 + p4) - ((b01 & p2)+(b12 & p3)+(b21 & p4)+(b10 & p1))) < 2)
            {
              mid[(i)*X_SIZE+j]=100;
              i--;
              j-=2;
              if (i<4) i=4;
              if (j<4) j=4;
            }
          }
        }
      }
    printf("start to send mid2\n");
    port_mid2.send(mid, X_SIZE*Y_SIZE*sizeof(unsigned char));
    printf("mid2 is sent\n");
  }
};

behavior susan_thin_fsm(i_receiver port_r, i_receiver port_mid1, i_sender port_mid2)
{
  susan_thin inst(port_r, port_mid1, port_mid2);
  void main(void)
  {
    fsm{inst: goto inst;}
  }
};
