// i_receive.sc: receiver interface for one-way handshake
//
// author: Rainer Doemer
//
// modifications: (most recent first)
//
// 10/24/02 RD	replaced "signal" with "handshake" to avoid confusion
// 02/18/02 RD	applied naming convention, integrated with distribution
// 02/06/02 RD	generalized capabilities, fixed usage rules
// 01/31/02 RD	separated interface into send an receive interfaces
// 01/23/02 RD	brush up, moved into separate file "handshake.sc"
// 12/27/01 RD	initial version
//
//
// interface rules:
//
// - a connected thread acts as a receiver
// - a call to receive() lets the receiver wait for a handshake from the sender
// - if a handshake is present at the time of receive(), the call to receive()
//   will immediately return
// - if no handshake is present at the time of receive(), the calling thread
//   is suspended until the sender sends the handshake; then, the receiver
//   will resume its execution
// - calling receive() may suspend the calling thread indefinitely

import "full_rtos";

interface os_receive
{
    note _SCE_STANDARD_LIB = { "os_receive" };
  
    void receive(int threadID, OS_API_TOP os_port);
};


// EOF os_receive.sc
