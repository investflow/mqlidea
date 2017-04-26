#define CONSTANT 100

// ------ ERRORS ------
enum;
enum {,};
enum X{Y=};
enum X{Y=,};
enum X{,Y};


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