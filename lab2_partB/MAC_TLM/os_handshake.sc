// c_handshake.sc: one-way handshake channel between a sender and a receiver
//
// author: Rainer Doemer
//
// modifications: (most recent first)
//
// 10/24/02 RD	replaced "signal" with "handshake" to avoid confusion
// 10/04/02 RD	added rule about safety of exceptions
// 02/18/02 RD	applied naming convention, integrated with distribution
// 02/06/02 RD	generalized capabilities, fixed usage rules
// 01/31/02 RD	separated interface into send an receive interfaces
// 01/23/02 RD	brush up, moved into separate file "handshake.sc"
// 12/27/01 RD	initial version
//
//
// interface rules:
//
// - see files i_send.sc, i_receive.sc
//
// channel rules:
//
// - this handshake channel provides safe one-way synchronization between
//   a sender and a receiver
// - a thread connected to the sender interface acts as a sender
// - a thread connected to the receiver interface acts as a receiver
// - only one sender and one receiver may use the channel at any time;
//   otherwise, the behavior is undefined
// - a call to send() sends a handshake to the receiver; if the receiver
//   is waiting at the time of the send(), it will wake up and resume
//   its execution; otherwise, the handshake is stored until the receiver
//   calls receive()
// - the behavior is undefined if send() is called successively without
//   any calls to receive()
// - a call to receive() lets the receiver wait for a handshake from the sender
// - if a handshake is present at the time of receive(), the call to receive()
//   will immediately return
// - if no handshake is present at the time of receive(), the calling thread
//   is suspended until the sender sends the handshake; then, the receiver
//   will resume its execution
// - calling send() will not suspend the calling thread
// - calling receive() may suspend the calling thread indefinitely
// - this channel is only safe with respect to exceptions, if any exceptions
//   are guaranteed to occur only for all communicating threads simultaneously;
//   the behavior is undefined, if any exceptions occur only for a subset
//   of the communicating threads
// - no restrictions exist for use of 'waitfor'
// - no restrictions exist for use of 'wait', 'notify', 'notifyone'


import "i_send";
import "os_receive";
import "full_rtos";


channel os_handshake implements i_send, os_receive
{
    note _SCE_STANDARD_LIB = { "os_handshake" };
  
    event e;
    bool  f = false,
          w = false;

    void receive(int threadID, OS_API_TOP os_port)
    {
	if (! f)
	{
printf("wait on receive\n");
	    w = true;
	    os_port.pre_wait(threadID);
	    wait e;
	    os_port.post_wait(threadID);
	    w = false;
	}
	f = false;
    }

    void send(void)
    {
	if (w)
	{
	    notify e;
	}
	f = true;
    }
};


// EOF os_handshake.sc
