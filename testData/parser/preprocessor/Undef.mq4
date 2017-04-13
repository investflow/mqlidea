// classic form of undef
#undef A

// error: parameter is required
#undef
#undef
B

// multiple statements on the same line
#undef X #undef Y

// error: parameter must be an identifier
#undef 1
#undef 'A'
#undef "B"
#undef #undef

// check that parsing is restored
#undef B