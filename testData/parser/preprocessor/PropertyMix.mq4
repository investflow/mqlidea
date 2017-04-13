// 2 properties share the same line
#property strict #property icon "url"

// property line can contain a comment
#property stacksize 10 // some comment

// 1-line block comments are allowed
/*some comment*/ #property stacksize 10
#property /*some comment*/ stacksize 10
#property /*some comment 1 */ stacksize /* some comment 2*/ 10  /* some comment 3*/


// multi-line mode is error
#property
stacksize 20

#property stacksize
20

#property /*some comment 1 */ stacksize /* some comment 2*/
10