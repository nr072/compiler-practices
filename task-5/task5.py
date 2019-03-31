# manually inserted production rules
rules = [
    "E -> TQ",
    "T -> FZ",
    "F -> i",
    "Z -> #",
    "Q -> +TQ",
    "Z -> *FZ",
    "Q -> #",
    "F -> (E)",
    "E -> #"
]
heads = []
bodies = []



# get separated heads and bodies from production rules
# can be later improved and user inputs stored instead of manual insertion
def getHeadAndBody(rules):
    heads = []
    bodies = []
    for rule in rules:
        parts = rule.split("->")
        parts[0] = parts[0].strip()
        parts[1] = parts[1].strip()
        if len(parts)==2 and parts[0] and parts[1] and len(parts[0])==1:
            heads.append(parts[0])
            bodies.append(parts[1])
    return [heads, bodies]



# join to make string, convert to list, reverse, and join again
def safeReverse(lst):
    rev = "".join(lst)
    rev = list(rev)
    rev.reverse()
    return "".join(rev)



# try to make output look like a table
def output(matched, stack, inp, action):
    matched = "      " if matched=="" else (matched + "     ")
    stack = safeReverse(stack)
    inp = safeReverse(inp)
    action  = "      " if  action=="" else action
    print(
        matched, "      ",
        stack, "      ",
        inp, "      ",
        action
    )



# take input and stack, and match as far as possible and delete matched parts
def match(matched ,stack, inp):
    r = len(inp) if len(inp)<len(stack) else len(stack)
    hasMatched = False
    action = "match"
    for i in range(r):
        # match and delete characters until last "$" is reached
        if (not len(stack)==1 and inp[len(inp)-1]==stack[len(stack)-1]):
            hasMatched = True
            matched += inp.pop()
            action += " " + stack.pop()
        else:
            break
    return [matched, stack, inp, action, hasMatched]



# get index of matching rule body whose first character matches input's first
# character
# otherwise just get index of matching head
def getIndex(top, inpFirstChar):
    indices = []
    index = -1
    for i in range(len(heads)):
        if top==heads[i] and bodies[i][0]==inpFirstChar:
            index = i
            break
        elif top==heads[i]:
            indices.append(i)
    # if input is empty, resolve all matching epsilons
    if len(inp)==1:
        for i in indices:
            if bodies[i][0]=="#":
                index = i
                break
    else:
        index = indices[0] if index==-1 else index
    return index



# expand first/top character of stack
def expand(stack, inp):
    action = ""
    hasExpanded = False
    top = stack[len(stack)-1]
    if (top in heads):
        hasExpanded = True
        # get index of matching rule body (prioritises first-character match)
        index = getIndex(top, inp[len(inp)-1])
        expanded = list(bodies[index])
        # reverse expanded part to push in stack
        expanded.reverse()
        stack.pop()
        # push nothing in stack if expanded to find epsilon
        if not (len(expanded)==1 and expanded[0]=="#"):
            stack.extend(expanded)
        action += "output " + heads[index] + " -> " + bodies[index]
    return [stack, action, hasExpanded]



def panic(stack, inp):
    popped = inp.pop()
    output("", stack, inp, ("Panic mode: delete " + popped))
    return inp



# get all production rules to show to user at the beginning
def getAllRules():
    allRules = ""
    for rule in rules:
        allRules += "\n    " + rule
    return allRules



print(
    "Manually inserted production rules have been used"
    + " (kindly alter in code if necessary):"
    + getAllRules()
    + "\n"
    + "(Notes:"
    + "\n"
    + "  1. \"#\", \"Q\", \"Z\" have been used to indicate"
    + " epsilon, E' and T' respectively"
    + "\n"
    + "  2. This code only works for single-character symbols"
    + "\n"
    + "  3. This code only works for whitespace-less string input"
    + ")"
    + "\n"
)



inp = list(input("Enter string to match (without any whitespace): ") + "$")
# reverese list to easily pop elements
inp.reverse()

print(
    "\nMatched      Stack        Input        Action",
    "\n-------      -------      -------      -------"
)

[heads, bodies] = getHeadAndBody(rules)

stack = []
stack.append("$")
stack.append(heads[0])

matched = ""
action = ""

output(matched, stack, inp, action)

# for every character, either match with input or expand using production rule
while not len(stack)==1:
    hasMatched = False
    hasExpanded = False
    [matched, stack, inp, action, hasMatched] = match(matched, stack, inp)
    # if no match, expand
    if not hasMatched:
        [stack, action, hasExpanded] = expand(stack, inp)
    # if neither matches nor can be expanded:
    if (not hasMatched) and (not hasExpanded):
        inp = panic(stack, inp)
        # break
    else:
        output(matched, stack, inp, action)
