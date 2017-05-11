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

class D :public A {
    D():A(1, 2, 3) {};
    D(T x):A(t.get(), 2, 3) {};
    virtual void X() = 0;
}

class E {
    int x() const;
}

int E::x() const {

}