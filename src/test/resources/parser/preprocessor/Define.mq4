// ------ ERRORS ------

#define
#define 1
#define "string"


// ------ VALID ------
#define X1 1
#define X2 1+1
#define X3 1+1 // comment is allowed
#define X4 X1*X2 // comment is allowed
#define X5 1 + \
1

#define X6 X1 \ // some comment
* X1

#define clrAliceBlue // color constants can be redefined

#define x  \ // only next line is in the #define block

#define NEXT_BLOCK 1


#define x + \
1 \ // only next line is in the #define block

#define NEXT_BLOCK 1