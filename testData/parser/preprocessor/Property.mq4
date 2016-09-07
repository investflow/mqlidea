// OptionalAnyLiteralValidator
#property strict
#property strict "some value"

// Not supported property
#property not-allowed-value

// RequiredLiteralValidator
#property icon "url"
#property icon 1

// RequiredLiteralValidator -> Integer
#property stacksize
#property stacksize 11
#property stacksize 1.1

// RequiredAnyLiteralValidator
#property version 1
#property version
#property version 1.1
#property version "1.1"

// RequiredLiteralValidator -> String
#property description 1
#property description ""
#property description "some string"

// RequiredNumericValidator
#property indicator_minimum 1
#property indicator_minimum "1.1"
#property indicator_minimum 1.1

