package com.tapsense.optighost;

import com.tapsense.optighost.R;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


public class LettersAdapter extends BaseAdapter {
    private Context mContext;

    public LettersAdapter(Context c) {
        mContext = c;
    }

    public int getCount() {
        return mTwentySevenCharacters.length;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
    	LayoutInflater inflater = (LayoutInflater) mContext
    			.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
     
    		View gridView;
        if (convertView == null) {  // if it's not recycled, initialize some attributes
        	gridView = new View(mContext);
        	// get layout from date.xml
			gridView = inflater.inflate(R.layout.letters, null);
 
			// set value into textview
			TextView textView = (TextView) gridView
					.findViewById(R.id.letters_label);
			textView.setText(mTwentySevenCharacters[position]);
			gridView.setBackgroundColor(Color.parseColor("#0090FF"));
			
			if(position == 26)
				gridView.setBackgroundColor(Color.parseColor("#FFFFFF"));
			
        } else {
        	gridView = (View) convertView;
        }

        return gridView;
    }

    // references to our dates
    private String[] mTwentySevenCharacters = {
           "A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"," "
    };
}
