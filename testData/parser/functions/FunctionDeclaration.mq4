// ------ ERRORS ------

//TODO: void static e1();
void e2(int a b);
void e3(int,,);
void e4("val");
void e5(1);
void e5(a);
void e6(int z 1);
void e7(int z, 1);
void e8(x


// ------ VALID ------

void a1();
void a2()
;
void a3();void a4();

// parameters
bool b1(string x);
bool b2(double x, float y);

// default values
bool c1(short x = 1);
int c2(char x, bool y = true);
string c3(string s = "default value");
//TODO: double c4(short x = 1 + 1);
//TODO: long c5(string x = "a"+"b");
#define X1  1
char c6(int x = X1);

// unnamed parameters
void d1(int);
void d2(int, string);
void d3(bool, char c);
void d4(bool b, char);
void d5(long b,);