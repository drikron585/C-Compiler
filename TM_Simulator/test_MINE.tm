* C-Minus Compilation to TM Code
* File: fileNameTM
* Standard prelude:
  0:    LD 6,0(0) 						Load gp with maxaddr
  1:   LDA 5,0(6) 						Copy gp to fp
  2:    ST 0,0(0) 						Clear content at location
* Jump around i/o routines here
* code for input routine
  4:    ST 0,-1(5) 						store return
  5:    IN 0,0,0 							input
  6:    LD 7,-1(5) 						return to caller
* code for output routine
  7:    ST 0,-1(5) 						store return
  8:    LD 0,-2(5) 						Load output value
  9:   OUT 0,0,0 							output
 10:    LD 7,-1(5) 						return to caller
  3:   LDA 7,7(7) 						Jump around i/o code
* End of standard prelude


* Processing global index variable: x
 11:   LDC 2,10(0) 						Get array length
 12:    ST 2,0(6) 						Assign: Array length to the beginning of array index
* Processing function: minloc
 14:    ST 0,-1(5) 						Store return
* Jump around function body here
* Processing local index variable: x
* Processing local simple variable: low
* Processing local simple variable: high
 15:    LD 1,3(5) 						load arg value
 16:    ST 1,-2(5) 						store arg value
 17:    LD 1,2(5) 						load arg value
 18:    ST 1,-3(5) 						store arg value
 19:    LD 1,1(5) 						load arg value
 20:    ST 1,-4(5) 						store arg value
* -> Compound statement
* Processing local simple variable: i
* Processing local simple variable: y
* Processing local simple variable: k
* -> Assign
* -> SimpleVar
* looking up simplevar id: low
 21:    LD 2,-3(5) 						grab current value of low
 22:    ST 2,-8(5) 						get value of low and add it to the end of the frameoffset
* <- SimpleVar
* -> SimpleVar
* looking up simplevar id: k
 23:    LD 2,-8(5) 						Get the result of operation
 24:    ST 2,-7(5) 						assign: calculated value to k
* <- SimpleVar
* <- Assign
* -> Assign
* -> SimpleVar
* looking up simplevar id: low
 25:    LD 2,-3(5) 						grab current value of low
 26:    ST 2,-8(5) 						get value of low and add it to the end of the frameoffset
* <- SimpleVar
 27:    LD 0,-8(5) 						Get the result of index calculation (false)
 28:   LDC 3,-1(0) 						Get starting point of the array
 29:   SUB 3,3,0 							Adding the start of the index with index number
 30:   ADD 3,3,6 							Current point + offset
 31:   JGE 0,4(7) 						If array isn't out of index
 32:   LDC 2,-1000000(0) 					Load register 0 with out of range below
 33:   OUT 2,0,0 							Output out of range below
 34:    ST 4,-8(5) 						ERROR RECOVERY: Send 0 as calculated value at x
 35:   LDA 7,9(7) 						ERROR RECOVERY: Ignore the rest of the calculation
 36:    LD 2,0(6) 						Load array length
 37:   SUB 0,2,0 							Adding array length to offset to see if out of bounds
 38:   JGT 0,4(7) 						If array isn't out of index
 39:   LDC 2,-2000000(0) 					Load register 0 with out of range aboves
 40:   OUT 2,0,0 							Output out of range below
 41:    ST 4,-8(5) 						ERROR RECOVERY: Send 0 as calculated value at x
 42:   LDA 7,2(7) 						ERROR RECOVERY: Ignore the rest of the calculation
 43:    LD 3,0(3) 						Load the found index
 44:    ST 3,-8(5) 						Assign: Calculated value to x
* -> SimpleVar
* looking up simplevar id: y
 45:    LD 2,-8(5) 						Get the result of operation
 46:    ST 2,-6(5) 						assign: calculated value to y
* <- SimpleVar
* <- Assign
* -> Assign
* -> Op
* -> SimpleVar
* looking up simplevar id: low
 47:    LD 2,-3(5) 						grab current value of low
 48:    ST 2,-8(5) 						get value of low and add it to the end of the frameoffset
* <- SimpleVar
* -> Constant
* The constant value is 1
 49:   LDC 2,1(0) 						Load Const
* <- Constant
 50:    ST 2,-9(5) 						Add: Constant value to memory address
 51:    LD 0,-8(5) 						Load arithmetic value 1 to 0
 52:    LD 1,-9(5) 						Load arithmetic value 2 to 1
 53:   ADD 0,0,1 							ac(0) + ac1(1) OP
 54:    ST 0,-8(5) 						assign: store value
* <- Op
* -> SimpleVar
* looking up simplevar id: i
 55:    LD 2,-8(5) 						Get the result of operation
 56:    ST 2,-5(5) 						assign: calculated value to i
* <- SimpleVar
* <- Assign
* -> While
* While: Jump after body comes back here
* -> Op
* -> SimpleVar
* looking up simplevar id: i
 57:    LD 2,-5(5) 						grab current value of i
 58:    ST 2,-8(5) 						get value of i and add it to the end of the frameoffset
* <- SimpleVar
* -> SimpleVar
* looking up simplevar id: high
 59:    LD 2,-4(5) 						grab current value of high
 60:    ST 2,-9(5) 						get value of high and add it to the end of the frameoffset
* <- SimpleVar
 61:    LD 0,-9(5) 						Load ac1
 62:    LD 1,-8(5) 						Load ac
 63:   SUB 0,1,0 							make it zero
 64:   JLT 0,2(7) 						ac < ac1
 65:   LDC 0,0(0) 						FALSE CASE
 66:   LDA 7,1(7) 						UNCONDITIONAL JUMP
 67:   LDC 0,1(0) 						TRUE CASE
* <- Op
* While: Jump to end belongs here
* -> Compound statement
* -> If
* If: Jump after body comes back here
* -> Op
* -> SimpleVar
* looking up simplevar id: i
 69:    LD 2,-5(5) 						grab current value of i
 70:    ST 2,-8(5) 						get value of i and add it to the end of the frameoffset
* <- SimpleVar
 71:    LD 0,-8(5) 						Get the result of index calculation (false)
 72:   LDC 3,-1(0) 						Get starting point of the array
 73:   SUB 3,3,0 							Adding the start of the index with index number
 74:   ADD 3,3,6 							Current point + offset
 75:   JGE 0,4(7) 						If array isn't out of index
 76:   LDC 2,-1000000(0) 					Load register 0 with out of range below
 77:   OUT 2,0,0 							Output out of range below
 78:    ST 4,-8(5) 						ERROR RECOVERY: Send 0 as calculated value at x
 79:   LDA 7,9(7) 						ERROR RECOVERY: Ignore the rest of the calculation
 80:    LD 2,0(6) 						Load array length
 81:   SUB 0,2,0 							Adding array length to offset to see if out of bounds
 82:   JGT 0,4(7) 						If array isn't out of index
 83:   LDC 2,-2000000(0) 					Load register 0 with out of range aboves
 84:   OUT 2,0,0 							Output out of range below
 85:    ST 4,-8(5) 						ERROR RECOVERY: Send 0 as calculated value at x
 86:   LDA 7,2(7) 						ERROR RECOVERY: Ignore the rest of the calculation
 87:    LD 3,0(3) 						Load the found index
 88:    ST 3,-8(5) 						Assign: Calculated value to x
* -> SimpleVar
* looking up simplevar id: y
 89:    LD 2,-6(5) 						grab current value of y
 90:    ST 2,-9(5) 						get value of y and add it to the end of the frameoffset
* <- SimpleVar
 91:    LD 0,-9(5) 						Load ac1
 92:    LD 1,-8(5) 						Load ac
 93:   SUB 0,1,0 							make it zero
 94:   JLT 0,2(7) 						ac < ac1
 95:   LDC 0,0(0) 						FALSE CASE
 96:   LDA 7,1(7) 						UNCONDITIONAL JUMP
 97:   LDC 0,1(0) 						TRUE CASE
* <- Op
* If: Jump to end belongs here
* -> Compound statement
* -> Assign
* -> SimpleVar
* looking up simplevar id: i
 99:    LD 2,-5(5) 						grab current value of i
100:    ST 2,-8(5) 						get value of i and add it to the end of the frameoffset
* <- SimpleVar
101:    LD 0,-8(5) 						Get the result of index calculation (false)
102:   LDC 3,-1(0) 						Get starting point of the array
103:   SUB 3,3,0 							Adding the start of the index with index number
104:   ADD 3,3,6 							Current point + offset
105:   JGE 0,4(7) 						If array isn't out of index
106:   LDC 2,-1000000(0) 					Load register 0 with out of range below
107:   OUT 2,0,0 							Output out of range below
108:    ST 4,-8(5) 						ERROR RECOVERY: Send 0 as calculated value at x
109:   LDA 7,9(7) 						ERROR RECOVERY: Ignore the rest of the calculation
110:    LD 2,0(6) 						Load array length
111:   SUB 0,2,0 							Adding array length to offset to see if out of bounds
112:   JGT 0,4(7) 						If array isn't out of index
113:   LDC 2,-2000000(0) 					Load register 0 with out of range aboves
114:   OUT 2,0,0 							Output out of range below
115:    ST 4,-8(5) 						ERROR RECOVERY: Send 0 as calculated value at x
116:   LDA 7,2(7) 						ERROR RECOVERY: Ignore the rest of the calculation
117:    LD 3,0(3) 						Load the found index
118:    ST 3,-8(5) 						Assign: Calculated value to x
* -> SimpleVar
* looking up simplevar id: y
119:    LD 2,-8(5) 						Get the result of operation
120:    ST 2,-6(5) 						assign: calculated value to y
* <- SimpleVar
* <- Assign
* -> Assign
* -> SimpleVar
* looking up simplevar id: i
121:    LD 2,-5(5) 						grab current value of i
122:    ST 2,-8(5) 						get value of i and add it to the end of the frameoffset
* <- SimpleVar
* -> SimpleVar
* looking up simplevar id: k
123:    LD 2,-8(5) 						Get the result of operation
124:    ST 2,-7(5) 						assign: calculated value to k
* <- SimpleVar
* <- Assign
* <- Compound statement
 98:   JEQ 0,26(7) 						End of If
* <- If
* -> Assign
* -> Op
* -> SimpleVar
* looking up simplevar id: i
125:    LD 2,-5(5) 						grab current value of i
126:    ST 2,-8(5) 						get value of i and add it to the end of the frameoffset
* <- SimpleVar
* -> Constant
* The constant value is 1
127:   LDC 2,1(0) 						Load Const
* <- Constant
128:    ST 2,-9(5) 						Add: Constant value to memory address
129:    LD 0,-8(5) 						Load arithmetic value 1 to 0
130:    LD 1,-9(5) 						Load arithmetic value 2 to 1
131:   ADD 0,0,1 							ac(0) + ac1(1) OP
132:    ST 0,-8(5) 						assign: store value
* <- Op
* -> SimpleVar
* looking up simplevar id: i
133:    LD 2,-8(5) 						Get the result of operation
134:    ST 2,-5(5) 						assign: calculated value to i
* <- SimpleVar
* <- Assign
* <- Compound statement
135:   LDA 7,-79(7) 					While: Absolute jump to test
 68:   JEQ 0,67(7) 						While: Jump to end
* <- While
* -> SimpleVar
* looking up simplevar id: k
136:    LD 2,-7(5) 						grab current value of k
137:    ST 2,-8(5) 						get value of k and add it to the end of the frameoffset
* <- SimpleVar
138:    LD 0,-8(5) 						Load return into register
* <- Compound statement
139:    LD 7,-1(5) 						Return to caller
 13:   LDA 7,126(7) 					Jump around function body
* <- Fundecl
* Processing function: sort
141:    ST 0,-1(5) 						Store return
* Jump around function body here
* Processing local index variable: x
* Processing local simple variable: low
* Processing local simple variable: high
142:    LD 1,3(5) 						load arg value
143:    ST 1,-2(5) 						store arg value
144:    LD 1,2(5) 						load arg value
145:    ST 1,-3(5) 						store arg value
146:    LD 1,1(5) 						load arg value
147:    ST 1,-4(5) 						store arg value
* -> Compound statement
* Processing local simple variable: i
* Processing local simple variable: k
* -> Assign
* -> SimpleVar
* looking up simplevar id: low
148:    LD 2,-3(5) 						grab current value of low
149:    ST 2,-7(5) 						get value of low and add it to the end of the frameoffset
* <- SimpleVar
* -> SimpleVar
* looking up simplevar id: i
150:    LD 2,-7(5) 						Get the result of operation
151:    ST 2,-5(5) 						assign: calculated value to i
* <- SimpleVar
* <- Assign
* -> While
* While: Jump after body comes back here
* -> Op
* -> SimpleVar
* looking up simplevar id: i
152:    LD 2,-5(5) 						grab current value of i
153:    ST 2,-7(5) 						get value of i and add it to the end of the frameoffset
* <- SimpleVar
* -> Op
* -> SimpleVar
* looking up simplevar id: high
154:    LD 2,-4(5) 						grab current value of high
155:    ST 2,-8(5) 						get value of high and add it to the end of the frameoffset
* <- SimpleVar
* -> Constant
* The constant value is 1
156:   LDC 2,1(0) 						Load Const
* <- Constant
157:    ST 2,-9(5) 						Add: Constant value to memory address
158:    LD 0,-8(5) 						Load arithmetic value 1 to 0
159:    LD 1,-9(5) 						Load arithmetic value 2 to 1
160:   SUB 0,0,1 							ac(0) - ac1(1) OP
161:    ST 0,-8(5) 						assign: store value
* <- Op
162:    LD 0,-8(5) 						Load ac1
163:    LD 1,-7(5) 						Load ac
164:   SUB 0,1,0 							make it zero
165:   JLT 0,2(7) 						ac < ac1
166:   LDC 0,0(0) 						FALSE CASE
167:   LDA 7,1(7) 						UNCONDITIONAL JUMP
168:   LDC 0,1(0) 						TRUE CASE
* <- Op
* While: Jump to end belongs here
* -> Compound statement
* Processing local simple variable: t
* -> Assign
* -> Call of function: minloc
********** simple ************* IndexVar x
* -> SimpleVar
* looking up simplevar id: x
170:   LDA 2,0(6) 						grab current address of x
171:    ST 2,-8(5) 						get value of x and add it to the end of the frameoffset
* <- SimpleVar
********** simple ************* SimpleVar i
* -> SimpleVar
* looking up simplevar id: i
172:    LD 2,-5(5) 						grab current value of i
173:    ST 2,-9(5) 						get value of i and add it to the end of the frameoffset
* <- SimpleVar
********** simple ************* SimpleVar high
* -> SimpleVar
* looking up simplevar id: high
174:    LD 2,-4(5) 						grab current value of high
175:    ST 2,-10(5) 					get value of high and add it to the end of the frameoffset
* <- SimpleVar
176:    ST 5,-11(5) 					Push ofp
177:   LDA 5,-11(5) 					Push frame
178:   LDA 0,1(7) 						Load ac with return line
179:   LDA 7,-166(7) 					Jump to minloc func location
180:    LD 5,0(5) 						Pop frame
181:    ST 0,-8(5) 						Assign: Return value at current frameoffset
* <- Call
* -> SimpleVar
* looking up simplevar id: k
182:    LD 2,-8(5) 						Get the result of operation
183:    ST 2,-6(5) 						assign: calculated value to k
* <- SimpleVar
* <- Assign
* -> Assign
* -> SimpleVar
* looking up simplevar id: k
184:    LD 2,-6(5) 						grab current value of k
185:    ST 2,-8(5) 						get value of k and add it to the end of the frameoffset
* <- SimpleVar
186:    LD 0,-8(5) 						Get the result of index calculation (false)
187:   LDC 3,-1(0) 						Get starting point of the array
188:   SUB 3,3,0 							Adding the start of the index with index number
189:   ADD 3,3,6 							Current point + offset
190:   JGE 0,4(7) 						If array isn't out of index
191:   LDC 2,-1000000(0) 					Load register 0 with out of range below
192:   OUT 2,0,0 							Output out of range below
193:    ST 4,-8(5) 						ERROR RECOVERY: Send 0 as calculated value at x
194:   LDA 7,9(7) 						ERROR RECOVERY: Ignore the rest of the calculation
195:    LD 2,0(6) 						Load array length
196:   SUB 0,2,0 							Adding array length to offset to see if out of bounds
197:   JGT 0,4(7) 						If array isn't out of index
198:   LDC 2,-2000000(0) 					Load register 0 with out of range aboves
199:   OUT 2,0,0 							Output out of range below
200:    ST 4,-8(5) 						ERROR RECOVERY: Send 0 as calculated value at x
201:   LDA 7,2(7) 						ERROR RECOVERY: Ignore the rest of the calculation
202:    LD 3,0(3) 						Load the found index
203:    ST 3,-8(5) 						Assign: Calculated value to x
* -> SimpleVar
* looking up simplevar id: t
204:    LD 2,-8(5) 						Get the result of operation
205:    ST 2,-7(5) 						assign: calculated value to t
* <- SimpleVar
* <- Assign
* -> Assign
* -> SimpleVar
* looking up simplevar id: i
206:    LD 2,-5(5) 						grab current value of i
207:    ST 2,-8(5) 						get value of i and add it to the end of the frameoffset
* <- SimpleVar
208:    LD 0,-8(5) 						Get the result of index calculation (false)
209:   LDC 3,-1(0) 						Get starting point of the array
210:   SUB 3,3,0 							Adding the start of the index with index number
211:   ADD 3,3,6 							Current point + offset
212:   JGE 0,4(7) 						If array isn't out of index
213:   LDC 2,-1000000(0) 					Load register 0 with out of range below
214:   OUT 2,0,0 							Output out of range below
215:    ST 4,-8(5) 						ERROR RECOVERY: Send 0 as calculated value at x
216:   LDA 7,9(7) 						ERROR RECOVERY: Ignore the rest of the calculation
217:    LD 2,0(6) 						Load array length
218:   SUB 0,2,0 							Adding array length to offset to see if out of bounds
219:   JGT 0,4(7) 						If array isn't out of index
220:   LDC 2,-2000000(0) 					Load register 0 with out of range aboves
221:   OUT 2,0,0 							Output out of range below
222:    ST 4,-8(5) 						ERROR RECOVERY: Send 0 as calculated value at x
223:   LDA 7,2(7) 						ERROR RECOVERY: Ignore the rest of the calculation
224:    LD 3,0(3) 						Load the found index
225:    ST 3,-8(5) 						Assign: Calculated value to x
* -> SimpleVar
* looking up simplevar id: k
226:    LD 2,-6(5) 						grab current value of k
227:    ST 2,-9(5) 						get value of k and add it to the end of the frameoffset
* <- SimpleVar
228:    LD 0,-9(5) 						Get the result of index calculation (true)
229:   LDC 3,-1(0) 						Get starting point of the array
230:   SUB 2,3,0 							Adding the start of the index with index number
231:   ADD 3,2,6 							Current point + offset
232:   JGE 0,3(7) 						If array isn't out of index
233:   LDC 2,-1000000(0) 					Load register 0 with out of range below
234:   OUT 2,0,0 							Output out of range below
235:   LDA 7,8(7) 						ERROR RECOVERY: Ignore the rest of the calculation
236:    LD 2,0(6) 						Load array length
237:   SUB 0,2,0 							Adding array length to offset to see if out of bounds
238:   JGT 0,3(7) 						If array isn't out of index
239:   LDC 2,-2000000(0) 					Load register 0 with out of range aboves
240:   OUT 2,0,0 							Output out of range below
241:   LDA 7,2(7) 						ERROR RECOVERY: Ignore the rest of the calculation
242:    LD 2,-8(5) 						Get the index value
243:    ST 2,0(3) 						Assign: Calculated value to x
* <- Assign
* -> Assign
* -> SimpleVar
* looking up simplevar id: t
244:    LD 2,-7(5) 						grab current value of t
245:    ST 2,-8(5) 						get value of t and add it to the end of the frameoffset
* <- SimpleVar
* -> SimpleVar
* looking up simplevar id: i
246:    LD 2,-5(5) 						grab current value of i
247:    ST 2,-9(5) 						get value of i and add it to the end of the frameoffset
* <- SimpleVar
248:    LD 0,-9(5) 						Get the result of index calculation (true)
249:   LDC 3,-1(0) 						Get starting point of the array
250:   SUB 2,3,0 							Adding the start of the index with index number
251:   ADD 3,2,6 							Current point + offset
252:   JGE 0,3(7) 						If array isn't out of index
253:   LDC 2,-1000000(0) 					Load register 0 with out of range below
254:   OUT 2,0,0 							Output out of range below
255:   LDA 7,8(7) 						ERROR RECOVERY: Ignore the rest of the calculation
256:    LD 2,0(6) 						Load array length
257:   SUB 0,2,0 							Adding array length to offset to see if out of bounds
258:   JGT 0,3(7) 						If array isn't out of index
259:   LDC 2,-2000000(0) 					Load register 0 with out of range aboves
260:   OUT 2,0,0 							Output out of range below
261:   LDA 7,2(7) 						ERROR RECOVERY: Ignore the rest of the calculation
262:    LD 2,-8(5) 						Get the index value
263:    ST 2,0(3) 						Assign: Calculated value to x
* <- Assign
* -> Assign
* -> Op
* -> SimpleVar
* looking up simplevar id: i
264:    LD 2,-5(5) 						grab current value of i
265:    ST 2,-8(5) 						get value of i and add it to the end of the frameoffset
* <- SimpleVar
* -> Constant
* The constant value is 1
266:   LDC 2,1(0) 						Load Const
* <- Constant
267:    ST 2,-9(5) 						Add: Constant value to memory address
268:    LD 0,-8(5) 						Load arithmetic value 1 to 0
269:    LD 1,-9(5) 						Load arithmetic value 2 to 1
270:   ADD 0,0,1 							ac(0) + ac1(1) OP
271:    ST 0,-8(5) 						assign: store value
* <- Op
* -> SimpleVar
* looking up simplevar id: i
272:    LD 2,-8(5) 						Get the result of operation
273:    ST 2,-5(5) 						assign: calculated value to i
* <- SimpleVar
* <- Assign
* <- Compound statement
274:   LDA 7,-123(7) 					While: Absolute jump to test
169:   JEQ 0,105(7) 					While: Jump to end
* <- While
* <- Compound statement
275:    LD 7,-1(5) 						Return to caller
140:   LDA 7,135(7) 					Jump around function body
* <- Fundecl
* Processing function: main
277:    ST 0,-1(5) 						Store return
* Jump around function body here
* -> Compound statement
* Processing local simple variable: i
* -> Assign
* -> Constant
* The constant value is 0
278:   LDC 2,0(0) 						Load Const
* <- Constant
279:    ST 2,-3(5) 						Add: Constant value to memory address
* -> SimpleVar
* looking up simplevar id: i
280:    LD 2,-3(5) 						Get the result of operation
281:    ST 2,-2(5) 						assign: calculated value to i
* <- SimpleVar
* <- Assign
* -> While
* While: Jump after body comes back here
* -> Op
* -> SimpleVar
* looking up simplevar id: i
282:    LD 2,-2(5) 						grab current value of i
283:    ST 2,-3(5) 						get value of i and add it to the end of the frameoffset
* <- SimpleVar
* -> Constant
* The constant value is 10
284:   LDC 2,10(0) 						Load Const
* <- Constant
285:    ST 2,-4(5) 						Add: Constant value to memory address
286:    LD 0,-4(5) 						Load ac1
287:    LD 1,-3(5) 						Load ac
288:   SUB 0,1,0 							make it zero
289:   JLT 0,2(7) 						ac < ac1
290:   LDC 0,0(0) 						FALSE CASE
291:   LDA 7,1(7) 						UNCONDITIONAL JUMP
292:   LDC 0,1(0) 						TRUE CASE
* <- Op
* While: Jump to end belongs here
* -> Compound statement
* -> Assign
* -> Call of function: input
294:    ST 5,-3(5) 						Push ofp
295:   LDA 5,-3(5) 						Push frame
296:   LDA 0,1(7) 						Load ac with return line
297:   LDA 7,-294(7) 					Jump to input func location
298:    LD 5,0(5) 						Pop frame
299:    ST 0,-3(5) 						Assign: Return value at current frameoffset
* <- Call
* -> SimpleVar
* looking up simplevar id: i
300:    LD 2,-2(5) 						grab current value of i
301:    ST 2,-4(5) 						get value of i and add it to the end of the frameoffset
* <- SimpleVar
302:    LD 0,-4(5) 						Get the result of index calculation (true)
303:   LDC 3,-1(0) 						Get starting point of the array
304:   SUB 2,3,0 							Adding the start of the index with index number
305:   ADD 3,2,6 							Current point + offset
306:   JGE 0,3(7) 						If array isn't out of index
307:   LDC 2,-1000000(0) 					Load register 0 with out of range below
308:   OUT 2,0,0 							Output out of range below
309:   LDA 7,8(7) 						ERROR RECOVERY: Ignore the rest of the calculation
310:    LD 2,0(6) 						Load array length
311:   SUB 0,2,0 							Adding array length to offset to see if out of bounds
312:   JGT 0,3(7) 						If array isn't out of index
313:   LDC 2,-2000000(0) 					Load register 0 with out of range aboves
314:   OUT 2,0,0 							Output out of range below
315:   LDA 7,2(7) 						ERROR RECOVERY: Ignore the rest of the calculation
316:    LD 2,-3(5) 						Get the index value
317:    ST 2,0(3) 						Assign: Calculated value to x
* <- Assign
* -> Assign
* -> Op
* -> SimpleVar
* looking up simplevar id: i
318:    LD 2,-2(5) 						grab current value of i
319:    ST 2,-3(5) 						get value of i and add it to the end of the frameoffset
* <- SimpleVar
* -> Constant
* The constant value is 1
320:   LDC 2,1(0) 						Load Const
* <- Constant
321:    ST 2,-4(5) 						Add: Constant value to memory address
322:    LD 0,-3(5) 						Load arithmetic value 1 to 0
323:    LD 1,-4(5) 						Load arithmetic value 2 to 1
324:   ADD 0,0,1 							ac(0) + ac1(1) OP
325:    ST 0,-3(5) 						assign: store value
* <- Op
* -> SimpleVar
* looking up simplevar id: i
326:    LD 2,-3(5) 						Get the result of operation
327:    ST 2,-2(5) 						assign: calculated value to i
* <- SimpleVar
* <- Assign
* <- Compound statement
328:   LDA 7,-47(7) 					While: Absolute jump to test
293:   JEQ 0,35(7) 						While: Jump to end
* <- While
* -> Call of function: sort
********** simple ************* IndexVar x
* -> SimpleVar
* looking up simplevar id: x
329:   LDA 2,0(6) 						grab current address of x
330:    ST 2,-3(5) 						get value of x and add it to the end of the frameoffset
* <- SimpleVar
*********************** IntExp 0
* -> Constant
* The constant value is 0
331:   LDC 2,0(0) 						Load Const
* <- Constant
332:    ST 2,-4(5) 						Add: Constant value to memory address
*********************** IntExp 10
* -> Constant
* The constant value is 10
333:   LDC 2,10(0) 						Load Const
* <- Constant
334:    ST 2,-5(5) 						Add: Constant value to memory address
335:    ST 5,-6(5) 						Push ofp
336:   LDA 5,-6(5) 						Push frame
337:   LDA 0,1(7) 						Load ac with return line
338:   LDA 7,-198(7) 					Jump to sort func location
339:    LD 5,0(5) 						Pop frame
* <- Call
* -> Assign
* -> Constant
* The constant value is 0
340:   LDC 2,0(0) 						Load Const
* <- Constant
341:    ST 2,-6(5) 						Add: Constant value to memory address
* -> SimpleVar
* looking up simplevar id: i
342:    LD 2,-6(5) 						Get the result of operation
343:    ST 2,-2(5) 						assign: calculated value to i
* <- SimpleVar
* <- Assign
* -> While
* While: Jump after body comes back here
* -> Op
* -> SimpleVar
* looking up simplevar id: i
344:    LD 2,-2(5) 						grab current value of i
345:    ST 2,-6(5) 						get value of i and add it to the end of the frameoffset
* <- SimpleVar
* -> Constant
* The constant value is 10
346:   LDC 2,10(0) 						Load Const
* <- Constant
347:    ST 2,-7(5) 						Add: Constant value to memory address
348:    LD 0,-7(5) 						Load ac1
349:    LD 1,-6(5) 						Load ac
350:   SUB 0,1,0 							make it zero
351:   JLT 0,2(7) 						ac < ac1
352:   LDC 0,0(0) 						FALSE CASE
353:   LDA 7,1(7) 						UNCONDITIONAL JUMP
354:   LDC 0,1(0) 						TRUE CASE
* <- Op
* While: Jump to end belongs here
* -> Compound statement
* -> Call of function: output
* -> SimpleVar
* looking up simplevar id: i
356:    LD 2,-2(5) 						grab current value of i
357:    ST 2,-6(5) 						get value of i and add it to the end of the frameoffset
* <- SimpleVar
358:    LD 0,-6(5) 						Get the result of index calculation (false)
359:   LDC 3,-1(0) 						Get starting point of the array
360:   SUB 3,3,0 							Adding the start of the index with index number
361:   ADD 3,3,6 							Current point + offset
362:   JGE 0,4(7) 						If array isn't out of index
363:   LDC 2,-1000000(0) 					Load register 0 with out of range below
364:   OUT 2,0,0 							Output out of range below
365:    ST 4,-6(5) 						ERROR RECOVERY: Send 0 as calculated value at x
366:   LDA 7,9(7) 						ERROR RECOVERY: Ignore the rest of the calculation
367:    LD 2,0(6) 						Load array length
368:   SUB 0,2,0 							Adding array length to offset to see if out of bounds
369:   JGT 0,4(7) 						If array isn't out of index
370:   LDC 2,-2000000(0) 					Load register 0 with out of range aboves
371:   OUT 2,0,0 							Output out of range below
372:    ST 4,-6(5) 						ERROR RECOVERY: Send 0 as calculated value at x
373:   LDA 7,2(7) 						ERROR RECOVERY: Ignore the rest of the calculation
374:    LD 3,0(3) 						Load the found index
375:    ST 3,-6(5) 						Assign: Calculated value to x
376:    LD 2,-6(5) 						Get value of index variable x
377:    ST 2,-8(5) 						Store arg val
378:    ST 5,-6(5) 						Push ofp
379:   LDA 5,-6(5) 						Push frame
380:   LDA 0,1(7) 						Load ac with return line
381:   LDA 7,-375(7) 					Jump to output func location
382:    LD 5,0(5) 						Pop frame
* <- Call
* -> Assign
* -> Op
* -> SimpleVar
* looking up simplevar id: i
383:    LD 2,-2(5) 						grab current value of i
384:    ST 2,-6(5) 						get value of i and add it to the end of the frameoffset
* <- SimpleVar
* -> Constant
* The constant value is 1
385:   LDC 2,1(0) 						Load Const
* <- Constant
386:    ST 2,-7(5) 						Add: Constant value to memory address
387:    LD 0,-6(5) 						Load arithmetic value 1 to 0
388:    LD 1,-7(5) 						Load arithmetic value 2 to 1
389:   ADD 0,0,1 							ac(0) + ac1(1) OP
390:    ST 0,-6(5) 						assign: store value
* <- Op
* -> SimpleVar
* looking up simplevar id: i
391:    LD 2,-6(5) 						Get the result of operation
392:    ST 2,-2(5) 						assign: calculated value to i
* <- SimpleVar
* <- Assign
* <- Compound statement
393:   LDA 7,-50(7) 					While: Absolute jump to test
355:   JEQ 0,38(7) 						While: Jump to end
* <- While
* <- Compound statement
394:    LD 7,-1(5) 						Return to caller
276:   LDA 7,118(7) 					Jump around function body
* <- Fundecl
395:    ST 5,-11(5) 					Push ofp
396:   LDA 5,-11(5) 					Push frame
397:   LDA 0,1(7) 						Load ac with return line
398:   LDA 7,-122(7) 					Jump to main location
399:    LD 5,0(5) 						Pop frame
400:  HALT 0,0,0 							End of program
