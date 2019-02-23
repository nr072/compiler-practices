"use strict";



var CHARS = [];
const QUANT = [ "?", "+", "*" ];



function matchRegExPattern(pattern, input) {

	parseRegEx(pattern);

	// return true;

}



function parseRegEx(pattern) {

	console.log(pattern);

	for (let i=0; i<pattern.length; ++i) {

		let char = pattern[i];
		console.log(pattern+" - - "+char+ "  "+i);

		switch (char) {
			case "[":
				// records in global array and returns next index
				i = recordCharRange(pattern, i);
				break;
			// case "(":
		}

		if (isLetter(char) || isDigit(char)) {
			i = recordSingleChar(pattern, i);
		}

		else {
			// i = recordOtherChar(pattern, i);
		}

	}

	console.log(CHARS);

}



/*
 * 
 * 
 * @param string
 * @param integer: index of "["
 * @return integer: index of character just after current range
 */
function recordCharRange(pattern, i) {

	let range = "",
		char = pattern[++i],
		occurrence = {},
		obj = {};

	while (true) {

		if (char=="]") {
			occurrence = getOccurrence(pattern, i);
			// send 3rd argument for recording range
			recordAndPushToGlobal(range, occurrence, true);
			break;
		}

		// concatenate until "]"
		else {
			range += char;
			char = pattern[++i];
		}

	}

	return i;

}



/*
 * @param string
 * @param integer 
 * @return integer: next character's index
 */
function recordSingleChar(pattern, i) {

	let char = pattern[i],
		occurrence = {};

	if (i+1<pattern.length) {

		if (isQuant(pattern[i+1])) {
			occurrence = getOccurrence(pattern, i);
			recordAndPushToGlobal(pattern[i], occurrence);
		}

	}

	return i;

}



function recordOtherChar() {}



/*
 * looks ahead for quantifier of current character/range
 *
 * @param pattern
 * @param i
 * @return object: contains min and max occurrences and next index
 */
// function getQuant(pattern, i) {
// 	return (pattern[i-1]=="]") 
// 		? getQuantForRange(pattern, i) 
// 		: getQuantForSingle(pattern, i);
// }



function getOccurrence(pattern, i) {
	let obj = {};
	// look ahead for range's quantifier
	obj = getQuant(pattern, i+1);
	return {
		"min": obj.min,
		"max": obj.max,
		"i": obj.i
	}

}



/*
 * looks ahead for quantifier of current range
 *
 * @param pattern
 * @param i
 * @return object: contains min and max occurrences and next index
 */
function getQuant(pattern, i) {

	let char = pattern[i],
		occurrence = {};

	// returns object containing min and max occurrences and next index
	return isQuant(char) 
		? getSingleQuant(char, i) 
		: char=="{" 
			? getQuantRange(pattern, i) 
			// if no quantifier of any kind, occurrence must be only once,
			// and current index will be next index
			: { "min": 1, "max": 1, "i": i };

}



function isQuant(char) {
	return QUANT.indexOf(char)>-1 ? true : false;
}



/*
 * finds out min and max occurrences for current character/range
 *
 * @param pattern
 * @param i
 * @return object: contains min and max occurrences
 */
function getQuantRange(pattern, i) {

	// ### ONLY WORKS FOR single-digit min/max ###

	let char = pattern[++i],
		min = "",
		max = "";

	// get min first
	// concat until "," and then convert to integer
	// if "{" found, return object
	while (true) {
		if (char==",") {
			parseInt(min);
			break;
		} else if (char=="}") {
			return { "min": min, "max": min, "i": i }
		} else {
			min += char;
			char = pattern[++i];
		}
	}

	// go to character after ","
	char = pattern[++i];

	// get max
	// concat until "}" and then convert to integer; if empty, assume "*"
	while (true) {
		if (char=="}") {
			max = (max) ? parseInt(max) : "*";
			break;
		} else {
			max += char;
			char = pattern[++i];
		}
	}

	return {
		"min": min,
		"max": max,
		"i": i
	}

}



/*
 * finds out min and max occurrences for current character
 *
 * @param pattern
 * @param i
 * @return object: contains min and max occurrences
 */
function getSingleQuant(char, i) {
	switch (char) {
		case "?":
			return { "min": 0, "max": 1, "i": i }
		case "+":
			return { "min": 1, "max": "*", "i": i }
		case "*":
			return { "min": 0, "max": "*", "i": i }
	}
}



/*
 * finds out starting and ending character sets and types for current range,
 * inserts into object for current range and pushes object to global array
 *
 * @param string range
 * @param object occurrence
 */
function recordAndPushToGlobal(input, occurrence, isRange) {
console.log(input);
console.log("input != range");

	// "range": "BOOLEAN",
	// "character": "CHAR",
	// "type": "letter \\ digit \\ other",
	// "occur_min": "INT",
	// "occur_max": "INT \\ *",
	// "negation": "BOOLEAN",
	// "ranges": [
	// 	{
	// 		"start": "CHAR",
	// 		"end": "CHAR"
	// 		"type": "letter \\ digit \\ other"
	// 	}
	// ]

	let char = input[0],
		obj = {},
		index,
		range;

	if (isRange) {
		range = input;
		obj.range = true;
		obj.ranges = [];
	} else {
		obj.range = false;
	}

	// set flag for negation
	obj.negation = char=="^" ? true : false;

	// if negation symbol exists, move index one character ahead
	index = obj.negation ? 1 : 0;

	if (isRange) {
		// get object array containing starting and ending characters and type
		obj.ranges.push.apply(obj.ranges, getCharRangeSets(range, index));
	} else {
		obj.character = char;
		obj.type = isLetter(char) ? "letter" : isDigit(char) ? "digit" : "other";
	}

	obj.occur_min = occurrence.min;
	obj.occur_max = occurrence.max;

	CHARS.push(obj);

}



/*
 * extracts characters/ranges from current total range
 *
 * @param pattern
 * @param i
 * @return array: contains objects full of min-max occurrence sets and types
 */
function getCharRangeSets(range, index) {

	let array = [];

	for (let i=index; i<range.length; ++i) {

		let obj = {};

		// if next character exists, check if range and record starting and
		// ending characters and type of starting character
		if (i+1<range.length) { 
			if (range[i+1]=="-") {
				obj.start = range[i];
				obj.end = range[i+2];
				obj.type = getCharType(range[i]);
				i +=2;
			}
		}

		// if no more character, consider current as both start and end
		else {
			obj.start = range[i];
			obj.end = range[i];
			obj.type = getCharType(range[i]);
		}

		// store every character/range in array
		array.push(obj);

	}

	return array;

}



function getCharType(char) {
	return isLetter(char) ? "letter" : isDigit(char) ? "digit" : "other";
}



function isLetter(char) {
	let ascii = char.charCodeAt();
	return ((65<=ascii && ascii<=90) || (97<=ascii && ascii<=122)) ? true : false;
}



function isDigit(char) {
	let ascii = char.charCodeAt();
	return (48<=ascii & ascii<=57) ? true : false;
}