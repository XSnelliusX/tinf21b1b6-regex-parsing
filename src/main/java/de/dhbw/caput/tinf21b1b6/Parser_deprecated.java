package de.dhbw.caput.tinf21b1b6;

final class Parser_deprecated {


	public static RegularExpression parse( String string, boolean kleeneStar ) {
		RegularExpression result = new RegularExpression.EmptyWord();
		RegularExpression lastGroup = new RegularExpression.EmptySet();
		boolean group = false;
		int groupStartIndex = 0;
		int groupEndIndex = 0;


		for (int i = 0; i < string.length(); i++) {
			boolean kleeneStarPresent = false;
			if (string.charAt(i) == '(') {
				group = true;
				groupStartIndex = i;
				groupEndIndex = groupStartIndex + getGroupEndDistance(string.substring(groupStartIndex));
				String groupToParse =  string.substring(groupStartIndex + 1, groupEndIndex);
				if (string.length() < groupEndIndex + 1) {
					if (string.charAt(groupEndIndex + 1) == '*') {
						kleeneStarPresent = true;
					}
				}
				lastGroup = parse(groupToParse, kleeneStarPresent);
				if (result instanceof RegularExpression.EmptyWord) {
					result = lastGroup;
				} else {
					result = new RegularExpression.Concatenation(result, lastGroup);
				}
				i = groupEndIndex;
			}

			if (!group) {
				switch (string.charAt(i)) {
					case '*':
						break;
					case '·':
						i++;
						// Check for Group
						if (string.charAt(i) == '(') {
							group = true;
							groupStartIndex = i;
							groupEndIndex = groupStartIndex + getGroupEndDistance(string.substring(groupStartIndex));
							if (string.length() > groupEndIndex + 1) {
								if (string.charAt(groupEndIndex + 1) == '*') {
									kleeneStarPresent = true;
								}
							}
							String groupToParse = string.substring(groupStartIndex + 1, groupEndIndex);
							lastGroup = parse(groupToParse, kleeneStarPresent);
							i = groupEndIndex;
							if (kleeneStarPresent) {
								result = new RegularExpression.Concatenation(result, new RegularExpression.KleeneStar(lastGroup));
							} else {
								result = new RegularExpression.Concatenation(result, lastGroup);
							}

						} else {
							RegularExpression currentLiteral = new RegularExpression.Literal(string.charAt(i));
							if (string.length() > i + 1) {
								if (string.charAt(i + 1) == '*') {
									result = new RegularExpression.Concatenation(result, new RegularExpression.KleeneStar(currentLiteral));
								} else {
									result = new RegularExpression.Concatenation(result, currentLiteral);
								}
							} else {
								result = new RegularExpression.Concatenation(result, currentLiteral);
							}
						}
						break;
					case '|':
						i++;
						// Check for Group
						if (string.charAt(i) == '(') {
							group = true;
							groupStartIndex = i;
							groupEndIndex = groupStartIndex + getGroupEndDistance(string.substring(groupStartIndex));
							String groupToParse =  string.substring(groupStartIndex + 1, groupEndIndex);
							if (string.length() > groupEndIndex + 1) {
								if (string.charAt(groupEndIndex + 1) == '*') {
									kleeneStarPresent = true;
								}
							}
							lastGroup = parse(groupToParse, kleeneStarPresent);
							i = groupEndIndex;
							if (kleeneStarPresent) {
								result = new RegularExpression.Union(result, new RegularExpression.KleeneStar(lastGroup));
							} else {
									result = new RegularExpression.Union(result, lastGroup);
							}
						} else {
							RegularExpression currentLiteral = new RegularExpression.Literal(string.charAt(i));
							if (string.length() > i + 1) {
								if (string.charAt(i + 1) == '*') {
									result = new RegularExpression.Union(result, new RegularExpression.KleeneStar(currentLiteral));
								} else {
									result = new RegularExpression.Union(result, currentLiteral);
								}
							} else {
								result = new RegularExpression.Union(result, currentLiteral);
							}
						}
						break;
					default:
						RegularExpression currentLiteral = new RegularExpression.Literal(string.charAt(i));
						if (result instanceof RegularExpression.EmptyWord) {
							result = currentLiteral;
						} else {
							if (string.charAt(i) == '*') {
								result = new RegularExpression.Concatenation(result, new RegularExpression.KleeneStar(currentLiteral));
							} else {
								result = new RegularExpression.Concatenation(result, currentLiteral);
							}
						}
						break;
				}
			}

			if (string.charAt(i) == ')') {
				group = false;
			}
		}

		if (kleeneStar) {
			result = new RegularExpression.KleeneStar(result);
		}

		System.out.println(result);
		return result;
	}

	private static int getGroupEndDistance(String string) {
		int result = 0;
		int openingBrackets = 0;

		for (int i = 0; i < string.length(); i++) {
			if (string.charAt(i) == '(') {
				openingBrackets++;
			} else if (string.charAt(i) == ')') {
				openingBrackets--;
			}
			if (openingBrackets == 0) {
				return i;

			}
		}
		return -9999999;
	}
}
