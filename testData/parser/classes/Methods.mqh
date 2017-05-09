// ------ VALID ------

class A {
    A(){};
    ~A(){};
    void m(){}
}

class B {
    B(void);
    ~B(void);
    void m(void);
}
B::~B(void) {}
B::~B(void) {}
void B::m(){}


class C {
~
/*comment*/
C();
};

C::
~
/*comment*/
C(void) {}