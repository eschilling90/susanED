// c_intarray_queue.sc:	instantiation of template c_typed_queue.sh
//			with type 'int[7220]'
//

#include <c_typed_queue.sh>	/* make the template available */

typedef int int7220[7220];

// define the tranceiver interface for data type 'int[7220]'
DEFINE_I_TYPED_TRANCEIVER(intarray, int7220 )

// define the sender interface for data type 'int[7220]'
DEFINE_I_TYPED_SENDER(intarray, int7220)

// define the receiver interface for data type 'int[7220]'
DEFINE_I_TYPED_RECEIVER(intarray, int7220)


// define the queue channel for data type 'int [7220]'
DEFINE_C_TYPED_QUEUE(intarray, int7220)


// EOF c_bp_queue.sc
