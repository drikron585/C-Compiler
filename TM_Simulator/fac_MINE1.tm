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
* processing function: main
* jump around function body here
 12:    ST 0,-1(5) 		Store return
* -> compound statement
* processing local simple variable: x
* -> assign
* -> SimpleVar
* looking up simplevar id: x
* <- SimpleVar
* -> call of function: input
 13:    ST 5,-3(5) 		push ofp
 14:   LDA 5,-3(5) 		push frame
 15:   LDA 0,1(7) 		load ac with ret ptr
 16:   LDA 7,-13(7) 		jump to input func loc
 17:    LD 5,0(5) 		pop frame
* <- call
* <- assign
 18:    ST 0,-3(6) 		store value of input to variable x
* -> call of function: output
 19:    LD 2,-3(6) 		Get value of x
 20:    ST 2,-5(5) 		Store arg val
 21:    ST 5,-3(5) 		push ofp
 22:   LDA 5,-3(5) 		push frame
 23:   LDA 0,1(7) 		load ac with ret ptr
 24:   LDA 7,-18(7) 		jump to output func loc
 25:    LD 5,0(5) 		pop frame
* -> SimpleVar
* looking up simplevar id: x
* <- SimpleVar
* <- call
 26:    LD 7,-1(5) 		return to caller
 11:   LDA 7,16(7) 		jump around function body
* <- fundecl
 28:    ST 5,0(5) 		push ofp
 29:   LDA 5,0(5) 		push frame
 30:   LDA 0,1(7) 		load ac with ret ptr
 31:   LDA 7,-20(7) 		jump to main loc
 32:    LD 5,0(5) 		pop frame
 33:  HALT 0,0,0 		
