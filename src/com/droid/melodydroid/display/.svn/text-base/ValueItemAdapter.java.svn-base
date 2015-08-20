package com.droid.melodydroid.display;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

public class ValueItemAdapter extends ArrayAdapter<ValueItem> {

	private Context context;
	private List<ValueItem> listOfValues;
	
	public ValueItemAdapter(Context context, int textviewResourceId, List<ValueItem> listOfValues) {
		super(context, textviewResourceId, listOfValues);
		this.context = context;
		this.listOfValues = listOfValues;
	}
	
	public View getView(int position, View view, ViewGroup viewGroup) {
		ValueItem valueItem = listOfValues.get(position);
		return new ValueItemAdapterView(context, valueItem);
	}
}
