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
 
 
 
 * * *



## Task 2

User will be asked first to input an integer value n followed by n lines of Strings. You have to find out whether it is an email or a web address or neither, along with its line number.

  - You are not allowed to use any kind of built-in Regular Expression.
  - The first part of a valid email address must start with a letter. But subsequent characters can be either letters or digits.
  - The first part of a valid web address must be "www".
  - The middle part of a valid email address (domain name; between the '@' and the '.') must not contain anything other than a letter.
  - The middle part of a valid web address (domain name) must start with a letter. But subsequent characters can be either letters or digits.
  - The 3rd (last) part of both a valid email and web address must be "com".

### Input:

4  
theEarthMovesRoundTheSun@gmail.com  
www.duh.com  
www.thisIsAValidEmailAddress.com  
you@liar.com  
3  
1IsAnInteger@smart.com  
www.validateYourEmails.com  
http://www.nobodyLovesMe.com  

### Output:

Email, 1  
Web, 2  
Web, 3  
Email, 4  
None! 1  
Web, 2  
None! 3  



 * * *



## Task 3

Read a number from file, and then read that many lines of regular expressinos. The read another number and that many lines of string inputs. Match the RexEx patterns with the inputs and show if they matched or not by printing the sequence number (as read) of the pattern.

If one input matches multiple patterns, print all sequence numbers separated by commas.

Write your own RegEx parser. You must not use any built-in method or package for this.

For this task, you need to detect the following RexEx patterns. If you succeed, you may write a parser for all RegEx patterns.

Description |  RE  | Valid | Invalid
:---:|:---:|:---:|:---:
Zero or more | a(bc)*de | ade | abde
 | | abcbcde | abcbde
One or more | a(bc)+de | abcde | ade
 | | abcbcde | abc
Once or not at all | a(bc)?de | ade | abc
 | | abcde | abcbcde
Character classes | [a-m]* | blackmail | above
 | | imbecile | below
Negation of character classes | [^aeiou] | b | a
 | | c | e
Exactly N times | [^aeiou]{6} | rhythm | rhythms
 | | syzygy | syzygy

### Input:

2  
ab*c*d  
a*b(cd)+e?f  
3  
acccd  
abbbbbcccc  
bcdcdef  

### Output:

YES, 1  
NO, 0  
YES, 2  
 
### Input:

3  
[a-c]{3}cab+(da)*f  
db*a[^def]{2}gh  
def[k-p]*p+  
5  
defkmnpmpp  
acbcabbf  
pqrstdd  
dbaabggh  
dbbbbamkgh  

### Output:

YES, 3  
YES, 1  
NO, 0  
NO, 0  
YES, 2  


