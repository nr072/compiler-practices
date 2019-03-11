"use strict";




const returnTypes = [ "void", "boolean", "int", "String", "float", "double" ];

let input = "";
let readFromFile = true;
let methods = [];




function readInput(fromFile, e) {

    readFromFile = fromFile;

    // the first time, read input from file
    if (readFromFile) {

        // clear previous data and hide edit button
        hideEditButton();
        methods = [];
        input = document.querySelector("td.code").innerText.split("\n");
        document.querySelector("td.code").style.border = "";

        if (!e.target.files[0]) {
            return;
        }

        let reader = new FileReader();

        reader.onload = function(e) {
            document.querySelector("td.code").innerText = e.target.result;
            input = e.target.result.split("\n");
        }

        reader.readAsText(e.target.files[0]);

    }

    // clear previously saved method data and read new input from webpage
    else {
        methods = [];
        input = document.querySelector("td.code").innerText.split("\n");
    }

}




function getMethods(){

    // clear result method data and read input from webpage (not file)
    if (!readFromFile) {
        document.querySelector("td.output").innerText = "";
        readInput(false);
    }


    for (let c=0; c<input.length; ++c) {

        let line = input[c];

        // insert whitespaces before and/or after characters such as ',', '+',
        // '=', braces, etc., to split at whitespaces easily later
        line = padWithWhitespaces(line);

        let tokens = line.split(" ");
        let prunedLine = "";
        let returnType = null;
        let methodName = null;

        for (let c2=0; c2<tokens.length; ++c2) {

            // if either already got return type or current token is return
            // type (methods must have a return type), extract method name;
            // otherwise continue loop
            if (returnType || isReturnType(tokens[c2])) {
                
                // if return type not found already and was just found, store 
                // it
                // will not enter this scope any more
                if (!returnType) {
                    returnType = isReturnType(tokens[c2]) ? tokens[c2] : "";
                }

                // id return type already found, store all tokens from after
                // return type till before "{"
                // add all tokens except "(" separated by a whitespace
                else {
                    if (tokens[c2]=="{") {
                        break;
                    } else if (tokens[c2]=="(") {
                        prunedLine += tokens[c2];
                    } else {
                        prunedLine += " " + tokens[c2];
                    }
                }

                // trim leading whitespace, so method name can be checked by
                // splitting at whitespaces
                prunedLine = prunedLine.trim();

            }

        }

        // take the part before "(" and check if it is one word only
        if (prunedLine) {
            let parts = ( prunedLine.split("(")[0] ).split(" ");
            methodName = parts.length==1 ? parts[0] : null;
        }

        // if valid method name found, store name, return type, and parameter(s)
        if (methodName) {

            let method = {};

            method.name = methodName;
            method.returnType = returnType;
            method.param = collapseMethodParam(prunedLine);

            methods.push(method);
        }

    }

    // show result on webpage
    printResult(methods);

    // if run once, show edit button for code
    if (methods.length>0) {
        showEditButton();
    }

}




function padWithWhitespaces(line) {

    let paddedLine = "";

    for (let c=0; c<line.length; ++c) {

        paddedLine += (
            line[c]=="(" || line[c]==")"
            || line[c]=="{" || line[c]=="}"
            || ( line[c]=="[" 
                && ( c+1<line.length && line[c+1]!="]" ) )
            || ( line[c]=="]" 
                && ( c-1>=0 && line[c-1]!="[" ) )
            || ( line[c]=="+" 
                && ( c-1>=0 && line[c-1]!="+" )
                && ( c+1<line.length && line[c+1]!="+" ) )
            || ( line[c]=="-" 
                && ( c-1>=0 && line[c-1]!="-" )
                && ( c+1<line.length && line[c+1]!="-" ) )
            || ( line[c]=="=" 
                && ( c-1>=0 && line[c-1]!="=" )
                && ( c+1<line.length && line[c+1]!="=" ) )
            || line[c]==","
            || line[c]==";"
        ) ? (" " + line[c] + " ") : line[c] ;

    }

    return paddedLine;

}




function isReturnType(token) {
    return returnTypes.indexOf(token)>-1 ? true : false;
}




function isLetter(char) {
    return (
        (65<=char.charCodeAt() && char.charCodeAt()<=90) 
        || (97<=char.charCodeAt() && char.charCodeAt()<=122) 
    );
}




function isDigit(char) {
    return (48<=char.charCodeAt() && char.charCodeAt()<=57);
}




function isValidCharForMethodName(char) {
    return (char=="_");
}




function isValidMethodName(token) {

    // return "false" if first character is neither a letter nor a valid 
    // character
    if (!isLetter(token[0]) && !isValidCharForMethodName(token[0])) {
        return false;
    }

    else {

        let validSofar = true;

        // check every character, whether they are valid or not
        // if invalid found, stop loop and return "false"
        for (let c=0; c<token.length && validSofar; ++c) {

            validSofar = (
                isLetter(token[c]) 
                || isDigit(token[c]) 
                || isValidCharForMethodName(token[c])
            );

        }

        return validSofar;

    }

}




function collapseMethodParam(param) {
    
    let collapsed = "";
    
    // extract only the part between "(" and ")"
    param = param.split("(").length>0 ? ( param.split("(")[1] ).split(")")[0] : "";

    for (let c=0; c<param.length; ++c) {

        // skip the whitespace after "("
        if (param[c]=="(") {
            ++c;
        }
        // overwrite the whitespace before ")" and ","
        else if (param[c]==")" || param[c]==",") {
            // cut out from first character till before last whitespace
            collapsed = collapsed.slice(0, collapsed.length-3);
        }
        // in all cases, keep concating
        collapsed += param[c];

    }

    return ("(" + collapsed.trim() + ")");

}




function printResult(methods) {

    if (methods.length>0) {

        let result = "Methods:\n";
        
        for (let c=0; c<methods.length; ++c) {
            result += methods[c].name + methods[c].param + "," 
                + " return type: " + methods[c].returnType + "\n";
        }

        document.querySelector("td.output").innerText = result;

    }

}




function showEditButton() {
    document.querySelector("button.edit-button").style.display = "inline-block";
}




function hideEditButton() {
    document.querySelector("button.edit-button").style.display = "";
}




function makeCodeEditable() {
    document.querySelector("td.code").contentEditable = true;
    document.querySelector("td.code").style.border = "1px solid #aca";
    readFromFile = false;
}