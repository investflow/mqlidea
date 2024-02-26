// ------ VALID ------
template<typename T1> class C1{};
template<typename T1, typename T2> class C2{};
template<typename T1> class C3 : public C1<T1>{};
template<typename T1> class C4 : public C1<T1*>{};
template<typename T1> class C5 : public C2<T1*, T1>{};

template<typename T1, typename T2> class C6 : C2<T1, C1<T1>>{};
template<typename T1, typename T2> class C7 : C1<C1<T1>>{};
template<typename T1, typename T2> class C8 : C1<C1<C1<T1>>>{};
template<typename T1, typename T2> class C9 : C1<C1<C1<C1<T1>>>>{};
template<typename T1, typename T2> class C10 : C1<C1<C1<C1<T1*>>*>>{};

