package com.tapsense.optighost;

public class TrieNode {
	
	char letter;
	TrieNode[] links;
	boolean fullWord;
	
	// constructor
	public TrieNode(char letter, boolean fullWord){
		this.letter = letter;
		links = new TrieNode[26];
		this.fullWord = fullWord;
	}

}
