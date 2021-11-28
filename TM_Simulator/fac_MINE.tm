* C-Minus Compilation to TM Code
* File: fileNameTM
* Standard prelude:
  0:    LD 6,0(0) 		load gp with maxaddr
  1:   LDA 5,0(6) 		Copy gp to fp
  2:    ST 0,0(0) 		Clear content at loc
* Jump around i/o routines here
* code for input routine
  4:    ST 0,-1(5) 		store return
  5:    IN 0,0,0 		input
  6:    LD 7,-1(5) 		return to caller
* code for output routine
  7:    ST 0,-1(5) 		store return
  8:    LD 0,-2(5) 		load output value
  9:   OUT 0,0,0 		output
 10:    LD 7,-1(5) 		return to caller
  3:   LDA 7,7(7) 		jump around i/o code

* End of standard prelude
* processing function: gcd
 13:    ST 0,-1(5) 		Store return
* jump around function body here
* -> compound statement
*  ******************************* Current frameoffset -2
* processing local simple variable: x1
* -> assign
* -> constant
* the constant value is 2
 14:   LDC 2,2(0) 		load const
* <- constant
 15:    ST 2,-3(6) 		assign: store value
*  ******************************* Current frameoffset -4
* -> SimpleVar
* looking up simplevar id: x1
 16:    LD 2,-3(5) 		Get the result of operation
 17:    ST 2,-2(6) 		assign: calculated value to x1
*  ******************************* Current frameoffset -2
* <- SimpleVar
* -> constant
* the constant value is 1
 18:   LDC 2,1(0) 		load const
* <- constant
 19:    ST 2,-2(6) 		assign: store value
*  ******************************* Current frameoffset -3
 20:    LD 7,-1(5) 		return to caller
 12:   LDA 7,8(7) 		Jump around function body
* <- fundecl
* processing function: main
 22:    ST 0,-1(5) 		Store return
* jump around function body here
* -> compound statement
*  ******************************* Current frameoffset -2
* processing local simple variable: x
* -> assign
* -> constant
* the constant value is 2
 23:   LDC 2,2(0) 		load const
* <- constant
 24:    ST 2,-3(6) 		assign: store value
*  ******************************* Current frameoffset -4
* -> SimpleVar
* looking up simplevar id: x
 25:    LD 2,-3(5) 		Get the result of operation
 26:    ST 2,-2(6) 		assign: calculated value to x
*  ******************************* Current frameoffset -2
* <- SimpleVar
* <- call
 27:    LD 7,-1(5) 		return to caller
 21:   LDA 7,6(7) 		Jump around function body
* <- fundecl
 28:    ST 5,0(5) 		push ofp
 29:   LDA 5,0(5) 		push frame
 30:   LDA 0,1(7) 		load ac with ret ptr
 31:   LDA 7,-11(7) 		jump to main loc
 32:    LD 5,0(5) 		pop frame
 33:  HALT 0,0,0 		
