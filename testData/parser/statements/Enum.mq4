#define CONSTANT 100

// ------ ERRORS ------
enum;
enum {,};
enum X{Y=};
enum X{Y=,};
enum X{,Y};
enum {A=int};
enum {A=1+(};
enum {A=1++1};
enum {A=*1};
enum {A=()};

// ------ VALID ------
enum {};
enum X {};
enum {A};
enum X{A};
enum X{A=1};
enum X{A=2, B};
enum X{A=3, B,};
enum X{A='0'};
enum X{A='1', B=1};
enum X{A='1', B=1, C=CONSTANT};
enum {A='1', B=1, C=CONSTANT, D};

enum EVALS {
   A=1+1,
   B=1+'A',
   C=2*(1+'A'),
   D=sizeof(int),
   E=(1+1),
   F=(1+1),
   G=1/1,
   H=1*2,
   I=10-20,
   K=1<<5,
   L=32>>1,
   M=33&34,
   N=33|34,
   O=6^7,
   P=CONSTANT,
   Q=(1+1)*2-(2+2)/3<<1,
   R=(1),
   S=+1,
   T=-1,
   U=-'A',
   V=+'A',
   W=clrBlue,
   X=C'128,128,128'
};

