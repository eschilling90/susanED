// c_bp_queue.sc:	instantiation of template c_typed_queue.sh
//			with type 'unsigned char[516]'
//

#include <c_typed_queue.sh>	/* make the template available */

typedef unsigned char uchar516[516];

// define the tranceiver interface for data type 'unsigned char[516]'
DEFINE_I_TYPED_TRANCEIVER(bp, uchar516 )

// define the sender interface for data type 'unsigned char[516]'
DEFINE_I_TYPED_SENDER(bp, uchar516)

// define the receiver interface for data type 'unsigned char[516]'
DEFINE_I_TYPED_RECEIVER(bp, uchar516)


// define the queue channel for data type 'unsigned char [516]'
DEFINE_C_TYPED_QUEUE(bp, uchar516)


// EOF c_bp_queue.sc
