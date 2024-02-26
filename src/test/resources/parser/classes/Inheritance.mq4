// ------ ERRORS ------
class E : {}

class E : 1 {}

class E : X, {}


// ------ VALID ------

interface A  {};

struct B : A  {};

class C : public A  {};

class D : private A  {};

class E : protected A {};

class F : A, B {};

class G : A, public B {};

class H : private A, B, protected C {};

