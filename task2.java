import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.IOException;



public class task2 {



    private static boolean isInt(String line) {
        char [] ch = line.toCharArray();
        return ( ch.length==1 && isDigit(ch[0]) ) ? true : false;
    }



    private static boolean isLetter(char ch) {
        return ( 
                    ( 65<=((int) ch) && ((int) ch)<=90 ) 
                    || 97<=((int) ch) && ((int) ch)<=122 
                ) ? true : false;
    }



    private static boolean isDigit(char ch) {
        return ( 48<=((int) ch) && ((int) ch)<=57 ) ? true : false;
    }



    private static boolean isEmail(String line) {

        // Function overview:
        // ------------------
        // Split a line of string using the characters '.' and '@' and get 3
        // parts for an email address:
        //   (1) username, (2) domain part 1, (3) domain part 2.
        // Check each character for each part. If any invalid character is
        // detected, return 'false' to main method. The following RegEx
        // expression defines the valid characters for an email address:
        //   [a-zA-Z][a-zA-Z0-9]*@[a-zA-Z]*.com

        // If length is greater than 1 in both cases, at least one '.' and one
        // '@' exists in the string. So, it may be an email address. Otherwise,
        // it is definitely not an email.
        // Need to escape period (.) since it is a "metacharacter" in RegEx:
        //   https://www.regular-expressions.info/characters.html
        if ( line.split("\\.").length>1 && line.split("@").length>1 ) {

            char [] string = line.toCharArray();

            // If first character of string is not a letter, it is not a valid
            // email address.
            if ( isLetter(string[0]) ) {

                int pos = 1;
                
                // Scout ahead until loop is broken from inside when '@' is
                // detected.
                while (true) {
                    
                    // When '@' is detected, break loop to move on to the check
                    // for the next part. (Characters until now would have been
                    // valid, otherwise loop would have been broken before
                    // reaching here.)
                    // The username part of the email address would have had at
                    // least one letter. For example, the following email
                    // addresses are considered valid:
                    //   a@something.com
                    //   y@something.com
                    //   Z@something.com
                    if (string[pos]=='@') {
                        break;
                    }

                    // As long as a letter or a digit comes after another until
                    // '@' is detected, just pass and increase the value for
                    // position.
                    else if ( isDigit(string[pos]) || isLetter(string[pos]) ) {
                        ++pos;
                    }

                    else {
                        return false;
                    }

                }

                // Check the character immediately next to '@'. Return 'false'
                // if it is not a letter.
                // For example, the following email addresses are considered
                // invalid:
                //   something@.
                //   something@.com
                //   something@1.com
                ++pos;
                if ( !isLetter(string[pos]) ) {
                    return false;
                }

                // Check if all characters are letters until '.' is detected.
                while (pos<string.length) {
                    if (string[pos]=='.') {
                        break;
                    } else if ( isLetter(string[pos]) ) {
                        ++pos;
                    } else {
                        return false;
                    }
                }

                ++pos;

                // Match "com" if only 3 characters remain. Otherwise, return
                // 'false'.
                return ((string.length-pos)==3) ? ( 
                                                        string[pos]=='c' 
                                                        && string[pos+1]=='o' 
                                                        && string[pos+2]=='m' 
                                                    ) ? true : false : false;
                
                // // Exapnded form of the same code, using if-else. 
                // // Because ternary operators are cool!
                // if ((string.length-pos)==3) {
                //     if ( string[pos]=='c' 
                //         && string[pos+1]=='o' 
                //         && string[pos+2]=='m' 
                //     ) {
                //         return true;
                //     } else {
                //      return false;
                //     }
                // } else {
                //  return false;
                // }

            }

            // First character is not a letter:
            else {
                return false;
            }

        }

        // Does not contain both an '@' and a '.':
        else {
            return false;
        }

    }



    private static boolean isWebsite(String line) {

        String [] parts = line.split("\\.");
        char [] string = line.toCharArray();

        // Only process if input string can be split into 3 parts using '.'
        // where the length of each part are, respectively, 3, greater than 1,
        // and 3. Which means that string will follow the format: 
        //   www.[a-zA-Z][a-zA-Z0-9]*.com
        // Need to escape period (.) since it is a "metacharacter" in RegEx:
        //   https://www.regular-expressions.info/characters.html
        if ( parts.length==3 
                && parts[0].length()==3
                && parts[1].length()>=1
                && parts[2].length()==3 ) {

            int pos = 0;

            // Check if first part is "www" and last part is "com".
            if ( string[pos]=='w' 
                    && string[pos+1]=='w' 
                    && string[pos+2]=='w' 
                    && string[string.length-3]=='c' 
                    && string[string.length-2]=='o' 
                    && string[string.length-1]=='m' ) {

                // Check if the character after first '.' is a letter. If not,
                // return 'false'.
                pos += (3+1);
                if ( !isLetter(string[pos]) ) {
                    return false;
                }

                // Run loop until second '.' is detected. If any invalid
                // character is detected, return 'false'. Otherwise, if '.' is
                // detected, return 'true'.
                while (true) {
                    
                    if (string[pos]=='.') {
                        return true;
                    } else if (isLetter(string[pos])) {
                        ++pos;
                    } else {
                        return false;
                    }

                }

            }

            // First and last parts are not "www" and "com", respectively:
            else {
                return false;
            }

        }

        // Input string can not be split into 3 parts, or their lengths are not
        // 3, greater than 1, and 3, respectively:
        else {
            return false;
        }

    }


    
    public static void main(String [] args)  {

        try {

            BufferedReader br = new BufferedReader( new FileReader("input.txt") );
            String line = "";
            boolean isInt = false;
            int iteration;

            while ( (line = br.readLine())!=null ) {

                isInt = isInt(line);

                // If line is an integer, consider it the number of upcoming
                // input lines to read and parse.
                if (isInt) {

                    iteration = Character.getNumericValue(line.charAt(0));

                    // Read and parse input lines only the number specified.
                    // Ignore all further lines.
                    for (int c=0; c<iteration; ++c) {

                        if ( (line = br.readLine())!=null ) {
                            System.out.println( 
                                ( isEmail(line) ? ("Email, " + (c+1)) 
                                    : isWebsite(line) ? ("Web, " + (c+1)) 
                                        : ("None! ") + (c+1) )
                            );
                        }

                    }

                }

                // Extra input lines (more than the number given as input):
                else {
                    System.out.println("Excuse me, there are more inputs than specified!");
                }

            }

        }

        catch (FileNotFoundException e) {
            System.out.println("Input file missing! Somebody call the police!");
        }
        catch (IOException e) {
            System.out.println("Something happened with file reading." 
                + " An investigation committee is being prepared.\n" 
                + " Meanwhile, read this: https://stackoverflow.com/a/2629740"
            );
        }
        catch (Exception e) {
            System.out.println("Something happened. No idea what. Let's get on with life.");
        }

    }



}