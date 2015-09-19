#ifndef MY_SUSANEDGES_H
#define MY_SUSANEDGES_H
#include <stdio.h>
#include <math.h>
#include <string.h>

#define X_SIZE 76
#define Y_SIZE 95

void susan_edges(unsigned char in[X_SIZE*Y_SIZE],int r[X_SIZE*Y_SIZE],unsigned char mid[X_SIZE*Y_SIZE],unsigned char bp[516]);
#endif
