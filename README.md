# Question

* * *

## Task 1

Program a simple lexical analyzer that will build a symbol table from given stream of chars. You will need to read a file named "input.txt" to collect all chars. For simplicity, input file will be a C program without headers and methods. Then you will identify all the numerical values, identifiers, keywords, math operators, logical operators and others [distinct].

See the example for more details. You can assume that, there will be a space after each keywords. But, removal of space will add bonus points.

### Input:

```c
int a, b, c;
float d, e;
a = b = 5;
c = 6;
if ( a > b)
{
            c = a - b;
            e = d - 2.0;
}
else
{
            d = e + 6.0;
            b = a + c;
}
```

### Output:

**Keywords:** int, float, if, else  
 **Identifiers:** a, b, c, d, e  
 **Math Operators:** +, -, =  
 **Logical Operators:** >  
 **Numerical Values:** 5, 6, 2.0, 6.0  
 **Others:** , ; ( ) { }  
