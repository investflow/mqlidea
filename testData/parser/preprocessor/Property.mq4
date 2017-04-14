// ------ ERRORS ------

// Illegal property name
#property not-valid-name

// Illegal property parameters
#property icon ''
#property icon '
#property icon x

// Wrong number of parameters
#property version 1 2
#property version "1" 2 3

// Multiple properties on the same line
#property icon #property icon
#property description #if

// ------ VALID ------

#property strict
#property unknown
#property indicator_colorN clrAliceBlue
#property icon "url"
#property icon ""
#property version '1'
#property version 1
#property version 1;#property version 2;#property version 3
#property version1 1; #property version2