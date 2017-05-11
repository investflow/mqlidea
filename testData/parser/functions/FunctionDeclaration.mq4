// ------ ERRORS ------

//TODO: void static e1();
void z2(int a b);
void z3(int,,);
void z4("val");
void z5(1);
void z6(int z 1);
void z7(int z, 1);
void z8(const bool const y);
void z9(const bool y&);
void z10(const int []y);
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
void e9(bool& []);
void e10(bool& x[]);
void e10(bool& x[10]);
#define ARRAY_SIZE 10
void e11(bool& x[ARRAY_SIZE]);
void e12(CustomType x);
const int e13();
const CustomType e14();
int const e15();
CustomType const e16();
int e17(CustomType* x);

//templates
void e18(const X<T>& x);
void e19(const X<T1,T2,T3>& x);
void e20(const X<T1<E1,E2>,T2,T3>& x);
void e21(const X<T*>& x);
