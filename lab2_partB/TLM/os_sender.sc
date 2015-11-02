// i_sender.sc: type-less sender interface
//
// author: Rainer Doemer
//
// modifications: (most recent first)
//
// 04/22/03 RD	added 'const' qualifier to sender interface
// 02/13/02 RD	applied naming convention, integrated with distribution
// 02/05/02 RD	parameterized name of the queue
// 02/04/02 RD	refined usage rules
// 02/01/02 RD	defined specific features and inserted actual code
// 01/31/02 RD	initial version
//
//
// interface rules:
//
// - a connected thread acts as a sender
// - a call to send() sends out a packet of data to a connected channel
// - data packets are typeless (i.e. an array of bytes in the memory)
//   and may vary in size for separate calls to send()
// - calling send() may suspend the calling thread indefinitely


interface os_sender
{
    note _SCE_STANDARD_LIB = { "os_sender" };
  
    void send(const void *d, unsigned long l);
};


// EOF i_sender.sc
