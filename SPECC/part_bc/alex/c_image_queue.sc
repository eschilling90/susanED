// c_image_queue.sc:	instantiation of template c_typed_queue.sh
//			with type 'unsigned char[7220]'
//

#include <c_typed_queue.sh>	/* make the template available */

typedef unsigned char uchar7220[7220];

// define the tranceiver interface for data type 'unsigned char[516]'
DEFINE_I_TYPED_TRANCEIVER(image, uchar7220 )

// define the sender interface for data type 'unsigned char[516]'
DEFINE_I_TYPED_SENDER(image, uchar7220)

// define the receiver interface for data type 'unsigned char[516]'
DEFINE_I_TYPED_RECEIVER(image, uchar7220)


// define the queue channel for data type 'unsigned char [516]'
DEFINE_C_TYPED_QUEUE(image, uchar7220)


// EOF c_bp_queue.sc
