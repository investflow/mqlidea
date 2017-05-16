// ------ VALID ------
template<typename T> T ArrayMax(T &arr[])  {
   T max=arr[0];
   return max;
}

template<typename T1,typename T2> string Assign(T1 &var1,T2 var2) {
   var1=(T1)var2;
   return(__FUNCSIG__);
}

template <typename T, template <typename TT> V> void foo(V<T> &con) {
}