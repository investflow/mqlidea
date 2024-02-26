// ------ ERRORS ------

// Missing parameter
#undef
#undef
B
// Parameter must be an identifier
#undef 1
#undef 'A'
#undef "B"
#undef #undef

// Multiple parameters
#undef X Y

// ------ VALID ------

#undef A
#undef A;
#undef X; #undef Y