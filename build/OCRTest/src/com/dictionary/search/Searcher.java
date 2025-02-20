package com.dictionary.search;

import java.sql.SQLException;
import java.util.ArrayList;

import android.os.AsyncTask;

import edu.sfsu.cs.orange.ocr.language.Translator;

public class Searcher {
	public ArrayList<Word> searchWords(String src, String fromLanguage, String toLanguage, AsyncTask<String, String, Boolean> asyncTask) throws SQLException {
		ArrayList<Word> searchedlist = new ArrayList<Word>();
		String wordBankname = "wordBank_" + fromLanguage + "_" + toLanguage; 
		// String wordBankname = 'wordBank';
		Dicworker dicwkr = new Dicworker(wordBankname, asyncTask);

		if(asyncTask.isCancelled()) return searchedlist;
		searchedlist = dicwkr.getWords(src);
		// 類似性の高いwordだけを抽出
		if(asyncTask.isCancelled()) return searchedlist;
		searchedlist = extractRelatives(src, searchedlist);
		return searchedlist;
	}

	private ArrayList<Word> extractRelatives(String src,
			ArrayList<Word> targetlist) {
		JaroWinkler jwklr = new JaroWinkler();
		ArrayList<Word> extractedWords = new ArrayList<Word>();
		Word top = new Word("", "", 0.2);
		Word second = new Word("", "", 0.1);
		Word third = new Word("", "", 0.0);
		for (Word w : targetlist) {
			w.distance = jwklr.getDistance(src, w.from_word);
			if (w.distance > 0.8) {
				if (w.distance >= top.distance) {
					top = w;
				}
				if (w.distance >= second.distance && w.distance < top.distance) {
					second = w;
				}
				if (w.distance >= third.distance && w.distance < top.distance
						&& w.distance < second.distance) {
					third = w;
				}
			}
		}
		if (top.to_word != "") {
			extractedWords.add(top);
		}
		if (second.to_word != "") {
			extractedWords.add(second);
		}
		if (third.to_word != "") {
			extractedWords.add(third);
		}
		return extractedWords;
	}
}
