package de.dhbw.caput.tinf21b1b6;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

final class Parser {
	static List<RegularExpression> parsedGroups = new ArrayList<>();
	static List<Character> groupOperationsInOrder = new ArrayList<>();
	static boolean firstRun = true;
	static boolean allGroupsFound = false;

	/*
	 *	Die Idee:
	 * 	Erst alle Gruppen Parsen und in eine Liste / Stack schreiben
	 * 	zusätzlich dazu auch die Operationen zwischen den Gruppen, in der richtigen Reihenfolge speichern
	 * 	Mögliches Problem: Gruppen innerhalb von Gruppen => wird erst später wichtig.
	 */
	public static RegularExpression parse( String string ){
		RegularExpression result = new RegularExpression.EmptySet();
		int openingBrackets = 0;
		boolean group = false;

		if (firstRun) {
			searchForGroups(string);
		}

		if (!allGroupsFound) {
			for (int i = 0; i < string.length(); i++) {
				char currentChar = string.charAt(i);

				if (!group) {
					switch (currentChar) {
						case '(':
							break;
						case 'ε':
							break;
						case '·':
							i++;
							result = parseConcatenation(string.substring(i), result);
							break;
						case '|':
							i++;
							result = parseUnion(string.substring(i), result);
							break;
						default:
							// A Literal normally does not stand alone, except in the beginning of the RegEx.
							if (result instanceof RegularExpression.EmptySet) {
								result = new RegularExpression.Literal(currentChar);
							}
							break;
					}
				}

				if (string.charAt(i) == '(') {
					group = true;
					openingBrackets++;
				} else if (string.charAt(i) == ')') {
					openingBrackets--;
				}
				if (openingBrackets == 0) {
					group = false;
				}
			}
		} else {
			for (int i = groupOperationsInOrder.size() - 1; i >= 0; i--) {
				switch (groupOperationsInOrder.get(i)) {
					case '·':
						parsedGroups.set(i, new RegularExpression.Concatenation(parsedGroups.get(i), parsedGroups.get(i + 1)));
						break;
					case '|':
						parsedGroups.set(i, new RegularExpression.Union(parsedGroups.get(i), parsedGroups.get(i + 1)));
						break;
					case '*':
						parsedGroups.set(i, new RegularExpression.KleeneStar(parsedGroups.get(i)));
						break;
				}
			}
			result = parsedGroups.get(0);
			resetParser();
		}

		System.out.println(result);
		return result;
	}

	private static void resetParser() {
		parsedGroups = new ArrayList<>();
		groupOperationsInOrder = new ArrayList<>();
		firstRun = true;
		allGroupsFound = false;
	}

	private static void searchForGroups(String string) {
		int openingBrackets = 0;
		boolean newGroupFound = false;
		List<Integer> bracketPositions = new ArrayList<>();
		firstRun = false;

		if (string.charAt(0) != '(') {
			parsedGroups.add(new RegularExpression.Literal(string.charAt(0)));
			groupOperationsInOrder.add(string.charAt(1));
		}

		for (int i = 0; i < string.length(); i++) {
			if (string.charAt(i) == '(') {
				openingBrackets++;
				bracketPositions.add(i);
				newGroupFound = true;
			} else if (string.charAt(i) == ')') {
				openingBrackets--;
			}
			if (openingBrackets == 0 && newGroupFound) {
				parsedGroups.add(parse(string.substring(bracketPositions.get(0) + 1, i)));
				groupOperationsInOrder.add(string.charAt(i + 1));
				bracketPositions.remove(0);
				newGroupFound = false;
			}
		}
		allGroupsFound = true;
	}

	private static RegularExpression parseConcatenation(String string, RegularExpression currentRegEx) {
		RegularExpression second = new RegularExpression.Literal(string.charAt(0));
		if (string.length() > 1) {
			if (string.charAt(1) == '*') {
				second = new RegularExpression.KleeneStar(second);
			}
		}

		return new RegularExpression.Concatenation(currentRegEx, second);
	}

	private static RegularExpression parseUnion(String string, RegularExpression currentRegEx) {
		RegularExpression second = new RegularExpression.Literal(string.charAt(0));
		if (string.length() > 1) {
			if (string.charAt(1) == '*') {
				second = new RegularExpression.KleeneStar(second);
			}
		}

		return new RegularExpression.Union(currentRegEx, second);
	}
}
