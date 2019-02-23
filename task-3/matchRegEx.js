var CHARS = [];
const QUANT = [ "?", "+", "*" ];



function matchRegEx(pattern, input) {

	parseRegEx(pattern);

	// return true;

}



function parseRegEx(pattern) {

	for (let i=0; i<pattern.length; ++i) {

		let char, 
			// The values '0', '2', and '3' will refer, respectively, to the
			// symbols '?', '+', and '*'. The value '1' will mean only a single
			// occurrence.
			occurrence = -1;

			console.log(pattern+"  ---  "+pattern[i]);
		if (pattern[i]=='[') {
			let range = "";
			++i;
			while (true) {
				if (pattern[i]!=']') {
					// keep concatenating
					range += pattern[i];
					++i;
				} else {
					// character after ']'
					++i;
					switch (pattern[i]) {
						case '?':
							occurrence = 0;
							break;
						case '+':
							occurrence = 2;
							break;
						case '*':
							occurrence = 3;
							break;
					}
					// no '?' or '+' or '*' - meaning single occurrence
					occurrence = (
						isLetter(pattern[i]) 
						|| isDigit(pattern[i])
						|| !isQuant(pattern[i])
					) ? 1 : -1;
					recordRange(range, occurrence);
					break;
				}
			}
		}

	}

}



/*
 * @param string range
 * @param integer occurrence
*/
function recordRange(range, occurrence) {
	console.log(range);
	console.log(occurrence);
	if (occurrence>0) {
		// create object in global array
		CHARS.push({
			"range": {
				"status": true,
				"ranges": [],
				"occurrence": ""
			}
		});
		// length must be multiple of 3
		for (let i=0; i<range.length; i+=3) {
			let ranges = CHARS[CHARS.length-1].range.ranges;
			ranges.push({
				"start": range[i],
				"end": range[i+2]
			});
			ranges[ranges.length-1].type = isLetter(range[i]) 
				? "letter" : isDigit(range[i]) 
					? "digit" : "other";
		}
		CHARS[CHARS.length-1].range.occurrence = occurrence;
	}
}



/*
 * @param string character
 * @param string typeFlag: '0' for letter, '1' for digit
 * @param integer occurrence: '0', '1', '2', and '3' for, respectively,
 *   '?', single instance, '+', and '*'
*/
function recordSingle(character, typeFlag, occurrence) {
	let object = {};
	object.type = typeFlag==0 ? "letter" : "digit";
}



function isLetter(character) {
	return (
		(65<=character.charCodeAt() && character.charCodeAt()<=90) 
		|| (97<=character.charCodeAt() && character.charCodeAt()<=122) 
	) ? true : false;
}



function isDigit(character) {
	return (48<=character.charCodeAt() && character.charCodeAt()<=57) ? true : false;
}



function isQuant(character) {
	return (QUANT.indexOf(character)>-1) ? true : false;
}