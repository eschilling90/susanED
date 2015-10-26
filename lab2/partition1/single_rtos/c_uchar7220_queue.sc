//#include <c_typed_queue.sh>	/* make the template available */
#include "os_typed_queue.sh"	/* make the template available */

typedef unsigned char  uchar7220[7220];

DEFINE_OS_TYPED_SENDER(uchar7220, uchar7220)
DEFINE_OS_TYPED_RECEIVER(uchar7220, uchar7220)
DEFINE_C_TYPED_QUEUE(uchar7220, uchar7220)

typedef unsigned char  uchar7220_left[7220];

DEFINE_I_TYPED_SENDER(uchar7220_left, uchar7220_left)
DEFINE_OS_TYPED_RECEIVER(uchar7220_left, uchar7220_left)
DEFINE_C_TYPED_LEFT_QUEUE(uchar7220_left, uchar7220_left)

typedef unsigned char  uchar7220_right[7220];

DEFINE_OS_TYPED_SENDER(uchar7220_right, uchar7220_right)
DEFINE_I_TYPED_RECEIVER(uchar7220_right, uchar7220_right)
DEFINE_C_TYPED_RIGHT_QUEUE(uchar7220_right, uchar7220_right)
