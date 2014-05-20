package com.tapsense.optighost;

import java.util.ArrayList;
import android.util.Log;

public class TrieDataStructure {

	ArrayList<String> listOfAllWordsFromSubtree;
	
	public TrieDataStructure() {
		listOfAllWordsFromSubtree = new ArrayList<String>();
	}
	
	public void clearWordsList(){
		listOfAllWordsFromSubtree.clear();
	}
	
	public ArrayList<String> getAllWordsList(){
		return listOfAllWordsFromSubtree;
	}

	public TrieNode createTree() {
		return (new TrieNode('\0', false));
	}

	public void insertWord(TrieNode root, String word) {
		int offset = 97;
		int l = word.length();
		char[] letters = word.toCharArray();
		TrieNode curNode = root;

		for (int i = 0; i < l; i++) {
			if (curNode.links[letters[i] - offset] == null)
				curNode.links[letters[i] - offset] = new TrieNode(letters[i],
						i == l - 1 ? true : false);
			curNode = curNode.links[letters[i] - offset];
		}
	}

	public void printTree(TrieNode root, int level, char[] branch, String enteredWord) {
		if (root == null)
			return;

		for (int i = 0; i < root.links.length; i++) {
			branch[level] = root.letter;
			printTree(root.links[i], level + 1, branch, enteredWord);
		}

		if (root.fullWord && level > 4) {
			StringBuilder builder = new StringBuilder();
			for (int j = 0; j <= level; j++) {
				builder.append(branch[j]);
			}
			if(!builder.toString().equals(enteredWord))
			listOfAllWordsFromSubtree.add(builder.toString());
		}
	}

}
