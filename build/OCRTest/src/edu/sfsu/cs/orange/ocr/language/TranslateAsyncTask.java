/*
 * Copyright 2011 Robert Theis
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package edu.sfsu.cs.orange.ocr.language;

import com.nature.code.odica.CaptureActivity;
import com.nature.code.odica.R;

import android.graphics.Typeface;
import android.os.AsyncTask;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Class to perform translations in the background.
 */
public final class TranslateAsyncTask extends AsyncTask<String, String, Boolean> {

  private static final String TAG = TranslateAsyncTask.class.getSimpleName();

  private CaptureActivity activity;
//  private TextView textView;
//  private View progressView;
//  private TextView targetLanguageTextView;
  private String sourceLanguageCode;
  private String targetLanguageCode;
  private String sourceText;
  private String translatedText = "";

  public TranslateAsyncTask(CaptureActivity activity, String sourceLanguageCode, String targetLanguageCode, 
      String sourceText) {
    this.activity = activity;
    this.sourceLanguageCode = sourceLanguageCode;
    this.targetLanguageCode = targetLanguageCode;
    this.sourceText = sourceText;
//    textView = (TextView) activity.findViewById(R.id.translation_text_view);
//    progressView = (View) activity.findViewById(R.id.indeterminate_progress_indicator_view);
//    targetLanguageTextView = (TextView) activity.findViewById(R.id.translation_language_text_view);
  }
  
  @Override
  protected Boolean doInBackground(String... arg0) {
	String[] splitEnter = sourceText.split("\n");
	  for(int i = 0; i < splitEnter.length; i++) {
	        String[] splitSpace = splitEnter[i].split("[\\s]+");
	        for(int j = 0; j < splitSpace.length; j++) {
				if(isCancelled()) return false;
//	        	translateAsyncTask = new TranslateAsyncTask(this, sourceLanguageCodeTranslation, targetLanguageCodeTranslation, 
//	        			splitSpace[j]);
//	        	translateAsyncTask.execute();
				
	        	
	        	String translateStr = Translator.translate(activity, sourceLanguageCode, targetLanguageCode, splitSpace[j], this);
	        	if (translateStr.equals(Translator.BAD_TRANSLATION_MSG)) {
	        	      
	        	} else {
	        		if(!translatedText.equals("")) {
		        		translatedText += "\n";
		        	}
	        		translatedText += splitSpace[j] + ":" +translateStr;
	        	}
	            
	        }
	  }
//    translatedText = Translator.translate(activity, sourceLanguageCode, targetLanguageCode, sourceText, this);

    // Check for failed translations.
    if (translatedText.equals(Translator.BAD_TRANSLATION_MSG)) {
      return false;
    }
    
    return true;
  }

  @Override
  protected synchronized void onPostExecute(Boolean result) {
    super.onPostExecute(result);
    
    if (result) {
      //Log.i(TAG, "SUCCESS");
        EditText translateEditView = (EditText)activity.findViewById(R.id.text_tranclate);
        String preText = translateEditView.getText().toString();
        String addText = translatedText;
        if(preText.indexOf(addText) < 0) {
        	if(!preText.equals("")) {
        		preText += "\n";
        	}
        	preText += addText;
        }
        translateEditView.setText(preText);
//      if (targetLanguageTextView != null) {
//        targetLanguageTextView.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL), Typeface.NORMAL);
//      }
//      textView.setText(translatedText);
//      textView.setVisibility(View.VISIBLE);
//      textView.setTextColor(activity.getResources().getColor(R.color.translation_text));

      // Crudely scale betweeen 22 and 32 -- bigger font for shorter text
//      int scaledSize = Math.max(22, 32 - translatedText.length() / 4);
//      textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, scaledSize);

    } else {
      Log.e(TAG, "FAILURE");
      EditText translateEditView = (EditText)activity.findViewById(R.id.text_tranclate);
      translateEditView.setText("");
//      targetLanguageTextView.setTypeface(Typeface.defaultFromStyle(Typeface.ITALIC), Typeface.ITALIC);
//      targetLanguageTextView.setText("Unavailable");

    }
    
    // Turn off the indeterminate progress indicator
//    if (progressView != null) {
//      progressView.setVisibility(View.GONE);
//    }
  }
}
