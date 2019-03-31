"use strict";




let gotNumOfIdentValues = false,
	gotNumOfInfixes = false,
	gotAllInputs = false,
	resultsShown = false;

let idents = [],
	infixes = [],
	postfixes = [],
	operators = [ "+", "-", "*", "/" ],
	priorities = [ "*", 1, "/", 1, "+", 0, "-", 0 ];

let numOfIdents = 0,
	identIter = 0,
	numOfInfixes = 0,
	infixIter = 0;




clearAll();




document.querySelector("textarea.input").addEventListener("keypress", function(e) {

	let inputField = document.querySelector("textarea.input"),
		isInputIntOnly = false;
	
	if (e.keyCode==13 && inputField.value.trim() && !resultsShown) {

		if (document.querySelector("div.history").style.display!="block") {		// style/appearance-related code
			document.querySelector("div.history").style.display = "block";
		}

		let input = getCodeInput();
		showInputInHistory(input);												// style/appearance-related code
		input = replaceMultiplicationSign(input);
		
		// if input is only an integer, find out which part this integer input
		// is for
		if (isInt(input)) {

			isInputIntOnly = true;

			if (!gotNumOfIdentValues) {
				gotNumOfIdentValues = true;
				numOfIdents = parseInt(input);
			}

			else if (!gotNumOfInfixes && identIter==numOfIdents) {
				gotNumOfInfixes = true;
				numOfInfixes = parseInt(input);
			}

		}

		else {
			isInputIntOnly = false;
		}

		// if input is not integer-only, store identifier values or infixes
		if (!isInputIntOnly && !gotAllInputs) {

			// if got number of identifier values but not number of infixes, store
			// identifier values (until next integer-only input)
			if (
				(gotNumOfIdentValues && !gotNumOfInfixes)
				&& (identIter < numOfIdents)
			) {

				if (storeIdentValue(input)) {
					++identIter;
				}
			}

			else if (gotNumOfIdentValues && gotNumOfInfixes) {

				if (infixIter < numOfInfixes) {

					storeInfix(input);
					++infixIter;
					if (infixIter==numOfInfixes) {
						gotAllInputs = true;
					}
				}

			}

		}

		// if got all inputs and all iterations finished, show results
		if (gotAllInputs) {

			printPostfixes();
			printEvaluatedResults();
			
			resultsShown = true;
			document.querySelector("textarea.input").placeholder = "History";	// style/appearance-related code
			document.querySelector("textarea.input").style.color = "#444";		// style/appearance-related code
			document.querySelector("textarea.input").blur();					// style/appearance-related code
			document.querySelector("textarea.input").disabled = true;			// style/appearance-related code
			document.querySelector("textarea.input").style.textAlign = "center";// style/appearance-related code

		}


		clearCodeInput();														// style/appearance-related code
	
	}

});




function printEvaluatedResults() {

	let outputArea = document.querySelector("fieldset.output");
	
	if (postfixes.length>0) {
		for (let c=0; c<postfixes.length; ++c) {
			outputArea.innerText += "\n" + evaluatePostfix(postfixes[c]);
		}
	}

}




function evaluatePostfix(string) {

	let stack = [];

	for (let c=0; c<string.length; ++c) {

		if (isOperand(string[c])) {
			stack.push(getValue(string[c]));
		}

		else if (isOperator(string[c])) {
			
			switch (string[c]) {
				case "*":
					stack.push(stack.pop() * stack.pop());
					break;
				case "/":
					stack.push(stack.pop() / stack.pop());
					break;
				case "+":
					stack.push(stack.pop() + stack.pop());
					break;
				case "-":
					let oprnd2 = stack.pop();
					let oprnd1 = stack.pop();
					stack.push(oprnd1 - oprnd2);
					break;
			}

		}

		else {
			return "Compilation error";
		}

	}

	return stack.pop();

}




function getValue(char) {
	let index = idents.indexOf(char);
	return index>-1 ? idents[index+1] : "";
}




function printPostfixes() {

	let output = "";

	if (infixes.length>0) {
		for (let c=0; c<infixes.length; ++c) {
			postfixes.push(convertToPostfix(infixes[c]));
			output += postfixes.length-1==0 
				? postfixes[postfixes.length-1]
				: "\n" + postfixes[postfixes.length-1];
		}
	}

	document.querySelector("fieldset.output").innerText = output;

}




function convertToPostfix(string) {
	
	let stack = [],
		output = "";

	for (let c=0; c<string.length; ++c) {

		if (isLetter(string[c])) {
			output += string[c];
		}

		else if (string[c]=="(") {
			stack.push(string[c]);
		}

		// separate function since it may run recursively
		else if (isOperator(string[c])) {
			[stack, output] = ifCharIsOperator(stack, output, string[c]);
		}

		else if (string[c]==")") {
			stack.pop();
			while (stack[stack.length-1]!="(") {
				output += stack[stack.length-1];
				stack.pop();
			}
			stack.pop();
		}

	}

	if (stack.length>0) {
		for (let c=0; c<=stack.length; ++c) {
			output += stack[stack.length-1];
			stack.pop();
		}
	}

	return output;

}




function ifCharIsOperator(stack, output, char) {

	if (
		stack.length==0 
		|| stack[stack.length-1]=="("
		|| (
			isOperator(stack[stack.length-1])
			&& priority(char) > priority(stack[stack.length-1])
		)
	) {
		stack.push(char);
	}

	else {

		output += stack[stack.length-1];
		stack.pop();
		
		if (isOperator(char)) {
			[stack, output] = ifCharIsOperator(stack, output, char);
		}

	}

	return [stack, output];

}




function priority(char) {
	let index = priorities.indexOf(char);
	return (index>-1) ? priorities[index+1] : -1;
}




function storeIdentValue(input) {

	let parts = input.split("=");

	if (parts.length==2 && isInt(parts[1]) && parts[0].trim() ) {

		// check for duplicate
		if (idents.indexOf(parts[0])>-1) {
			idents[idents.indexOf(parts[0])+1] = parts[1];
		} else {
			idents.push(
				parts[0].trim(),
				parseInt(parts[1]),
			);
		}

		return true;

	}

	else {
		return false;
	}

}




function storeInfix(input) {

	infixes.push(input.trim());

}




function isInt(string) {
	for (let c=0; c<string.length; ++c) {
		if (!isDigit(string[c])) {
			return false;
		}
	}
	return true;
}




function isDigit(char) {
	return (48<=char.charCodeAt() && char.charCodeAt()<=57);
}




function isLetter(char) {
	return (
		(65<=char.charCodeAt() && char.charCodeAt()<=90) 
		|| (97<=char.charCodeAt() && char.charCodeAt()<=122) 
	);
}




function isOperator(char) {
	return operators.indexOf(char)>-1;
}




function isOperand(char) {
	return idents.indexOf(char)>-1;
}




function showInputInHistory(string) {											// style/appearance-related code
	let historyArea = document.querySelector("div.history");
	historyArea.innerText += historyArea.innerText ? ("\n" + string) : string;
}




function getCodeInput() {
	return document.querySelector("textarea.input").value.trim();
}




function replaceMultiplicationSign(string) {
	
	let replaced = "";

	for (let c=0; c<string.length; ++c) {
		replaced += string[c]=="x" ? "*" : string[c];
	}

	return replaced;

}




function hideRunButton() {														// style/appearance-related code
	document.querySelector("button.run-button").style.display = "none";
	document.querySelector("textarea.input").disabled = false;
	document.querySelector("textarea.input").focus();
}




function clearCodeInput() {														// style/appearance-related code
	if (document.querySelector("textarea.input").value) {
		document.querySelector("textarea.input").value = "";
	}
}




function clearAll() {															// style/appearance-related code
	if (document.querySelector("fieldset.output").innerText) {
		document.querySelector("fieldset.output").innerText = "";
	}
	clearCodeInput();
	document.querySelector("button.run-button").style.display = "";
}
