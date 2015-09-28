// c_image_thread_queue.sc:	instantiation of template c_typed_queue.sh
//			with type 'unsigned char[7220]'
//

#include <c_typed_queue.sh>	/* make the template available */

typedef unsigned char uchar1805[1805];

// define the tranceiver interface for data type 'unsigned char[516]'
DEFINE_I_TYPED_TRANCEIVER(imageth, uchar1805 )

// define the sender interface for data type 'unsigned char[516]'
DEFINE_I_TYPED_SENDER(imageth, uchar1805)

// define the receiver interface for data type 'unsigned char[516]'
DEFINE_I_TYPED_RECEIVER(imageth, uchar1805)


// define the queue channel for data type 'unsigned char [516]'
DEFINE_C_TYPED_QUEUE(imageth, uchar1805)


// EOF c_bp_queue.sc
