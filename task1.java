import java.io.BufferedReader;
import java.io.FileReader;

import java.util.Arrays;
import java.util.ArrayList;

import java.util.regex.Pattern;
import java.util.regex.Matcher;





public class task1 {



    public static String [] keywords     = { 
        "abstract", "continue", "for", "new", "switch", 
        "assert", "default", "goto", "package", "synchronized", 
        "boolean", "do", "if", "private", "this", 
        "break", "double", "implements", "protected", "throw", 
        "byte", "else", "import", "public", "throws", 
        "case", "enum", "instanceof", "return", "transient", 
        "catch", "extends", "int", "short", "try", 
        "char", "final", "interface", "static", "void", 
        "class", "finally", "long", "strictfp", "volatile", 
        "const", "float", "native", "super", "while" 
    };
    public static String [] indentifiers = {};
    public static String [] mathOps      = { "=", "+", "-", "*", "/", "++", "--" };
    public static String [] logicalOps   = { ">", "<", "==", "!", "!=", ">=", "<=" };
    public static String [] numericals   = {};
    public static String [] others       = { "(", ")", "{", "}", "[", "]", ",", ";", "&&", "||" };
    
    public static ArrayList<String> recordedKeys    = new ArrayList<String>();
    public static ArrayList<String> recordedIds     = new ArrayList<String>();
    public static ArrayList<String> recordedMathOps = new ArrayList<String>();
    public static ArrayList<String> recordedLogOps  = new ArrayList<String>();
    public static ArrayList<String> recordedNums    = new ArrayList<String>();
    public static ArrayList<String> recordedOthers  = new ArrayList<String>();

    public static ArrayList<String> unclassifieds   = new ArrayList<String>();
    
    
    
    public static String trimInternalSpace(String line) {
        // Need to write
        return line;
    }



    public static String padWithWhitespaces(String line) {

        String paddedLine = "";
        char [] ch = line.toCharArray();
        
        for (int c=0; c<ch.length; ++c) {

            if ( ch[c]==',' || ch[c]==';' ) {
                paddedLine += " " + ch[c];
            }

            else if ( 
                        ch[c]=='(' 
                        || ch[c]=='{' 
                        || ch[c]=='[' 
                        || ch[c]==')' 
                        || ch[c]=='}' 
                        || ch[c]==']' 
                        || ( ch[c]=='=' && ( ch[c-1]!='!' || ch[c+1]!='=' ) ) 
                        || ( ch[c]=='=' && ch[c+1]!='=' ) 
                        || ( ch[c]=='+' && ch[c+1]!='+' ) 
                        || ( ch[c]=='-' && ch[c+1]!='-' ) 
                        || ( ch[c]=='*' && ch[c+1]!='*' ) 
                        || ch[c]=='/' 
                        || ch[c]=='%' 
                        || ( ch[c]=='>' && ch[c+1]!='=' ) 
                        || ( ch[c]=='<' && ch[c+1]!='=' ) 
                      ) {
                paddedLine += " " + ch[c] + " ";
            }

            else {
                paddedLine += ch[c];
            }

        }

        return paddedLine;

    }
    


    public static boolean isKeyword(String token) {
        return ( Arrays.asList(keywords).contains(token) ) ? true : false;
    }
    


    // The following pieces of code are apparently valid and it is weird:
    //
    //     String _ = "someString";
    //     String __ = "someString";
    //
    // Source for identifier validation:
    //   https://docs.oracle.com/javase/specs/jls/se7/html/jls-3.html#jls-3.8

    public static boolean isIdentifier(String token) {

        Pattern idPattern = Pattern.compile("([a-zA-Z_]+)+\\w*");
        Matcher match = idPattern.matcher(token);

        // Return "true" only when the entire token - not a substring - matches
        // with the pattern.
        return ( match.find() && match.group().length()==token.length() ) ? true : false;

    }
    


    public static boolean isMathOp(String token) {
        return ( Arrays.asList(mathOps).contains(token) ) ? true : false;
    }
    


    public static boolean isLogicalOp(String token) {
        return ( Arrays.asList(logicalOps).contains(token) ) ? true : false;
    }
    


    public static boolean isNumerical(String token) {

        Pattern intPattern = Pattern.compile("\\d+");
        Matcher intMatch = intPattern.matcher(token);
        
        Pattern decPattern = Pattern.compile("\\d+(.\\d+)?");
        Matcher decMatch = decPattern.matcher(token);
        
        if (decMatch.find()) {
            return true;
        } else if (intMatch.find()) {
            return true;
        } else {
            return false;
        }

    }


    
    public static boolean isOther(String token) {
        return ( Arrays.asList(others).contains(token) ) ? true : false;
    }

    

    public static boolean recordToken(String token, int index) {
        switch (index){
            case 0:
                if ( !(recordedKeys.contains(token)) ) {
                    recordedKeys.add(token);
                }
                break;
            case 1:
                if ( !(recordedIds.contains(token)) ) {
                    recordedIds.add(token);
                }
                break;
            case 2:
                if ( !(recordedMathOps.contains(token)) ) {
                    recordedMathOps.add(token);
                }
                break;
            case 3:
                if ( !(recordedLogOps.contains(token)) ) {
                    recordedLogOps.add(token);
                }
                break;
            case 4:
                if ( !(recordedNums.contains(token)) ) {
                    recordedNums.add(token);
                }
                break;
            case 5:
                if ( !(recordedOthers.contains(token)) ) {
                    recordedOthers.add(token);
                }
                break;
        }
        return true;
    }



    public static boolean recordUnclassified(String token) {
        if ( !(unclassifieds.contains(token)) ) {
            unclassifieds.add(token);
        }
        return false;
    }



    public static void printTokensFromArrayList(ArrayList list, char ch) {
        if (list.size()>0) {
            System.out.print( list.get(0) );
            for (int c=1; c<list.size(); ++c) {
                System.out.print( ((ch!='\\') ? ch : "") + " " + list.get(c) );
            }
        }
        System.out.println();
    }



    public static void printRecordedTokens() {

        String [] keys = { "Keywords", "Identifiers", "Math operators", "Logical operators", "Numerical values", "Others" };

        System.out.print( keys[0] + ": " );
        printTokensFromArrayList( recordedKeys, ',');

        System.out.print( keys[1] + ": " );
        printTokensFromArrayList( recordedIds, ',');

        System.out.print( keys[2] + ": " );
        printTokensFromArrayList( recordedMathOps, ',');

        System.out.print( keys[3] + ": " );
        printTokensFromArrayList( recordedLogOps, ',');

        System.out.print( keys[4] + ": " );
        printTokensFromArrayList( recordedNums, ',');

        System.out.print( keys[5] + ": " );
        printTokensFromArrayList( recordedOthers, '\\');

    }



    public static void printUnclassifiedElements() {
        System.out.print("Unclassified: ");
        if (unclassifieds.size()>0) {
            System.out.print(unclassifieds.get(0));
            for (int c=0; c<unclassifieds.size(); ++c) {
                System.out.print(unclassifieds.get(c));
            }
        }
        System.out.println();
    }
    


    
    
    public static void main(String [] nothing) {
        
        try {
            
            BufferedReader br = new BufferedReader( new FileReader("input.txt") );
            String line = "";

            while ( ( line = br.readLine() )!=null ) {
                
                line = line.trim();

                // ### Need to write this ###
                line = trimInternalSpace(line);
                // System.out.println(line);

                line = padWithWhitespaces(line);
                
                boolean added = false;
                String [] tokens = line.split(" ");
                
                for (int c=0; c<tokens.length; ++c) {
                    
                    // System.out.println(tokens[c]);
                    // System.out.println( isKeyword(tokens[c]) );
                    
                    added = ( isKeyword(tokens[c]) )             ? recordToken( tokens[c], 0 )
                        : ( isIdentifier(tokens[c]) )            ? recordToken( tokens[c], 1 )
                            : ( isMathOp(tokens[c]) )            ? recordToken( tokens[c], 2 )
                                : ( isLogicalOp(tokens[c]) )     ? recordToken( tokens[c], 3 )
                                    : ( isNumerical(tokens[c]) ) ? recordToken( tokens[c], 4 )
                                        : ( isOther(tokens[c]) ) ? recordToken( tokens[c], 5 )
                                            : recordUnclassified(tokens[c]);
                    
                }

            }

            printRecordedTokens();
            printUnclassifiedElements();

        }
        
        catch (Exception e) {
            System.out.println("Error: File not found!");
        }
        
    }


    
}