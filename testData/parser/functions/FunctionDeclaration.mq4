// ------ ERRORS ------

//TODO: void static e1();
void z2(int a b);
void z3(int,,);
void z4("val");
void z5(1);
void z5(a);
void z6(int z 1);
void z7(int z, 1);
void z8(const bool const y);
void z100(x


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

// const & other modifiers
void e1(const int x);
void e2(bool const y);
void e3(double &);
void e4(const float &);
void e5(double& x);
void e6(double & y);
void e7(double &z);
void e8(bool const &a);


