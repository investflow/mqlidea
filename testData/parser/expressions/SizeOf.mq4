// ------ ERRORS ------
int e01 = sizeof();
int e02 = sizeof(-);
int e03 = sizeof(--1);
int e04 = sizeof((int));

// ------ VALID ------
int v01 = sizeof(int);
int v02 = sizeof(string);
int v03 = sizeof(1);
int v04 = sizeof(-1);
int v05 = sizeof(1-1);
int v06 = sizeof((2+2));