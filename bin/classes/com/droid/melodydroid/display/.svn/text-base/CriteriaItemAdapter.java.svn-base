package com.droid.melodydroid.display;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

public class CriteriaItemAdapter extends ArrayAdapter<CriteriaItem> {

	private Context context;
	private List<CriteriaItem> listOfCriteria;

	private int[] colors = new int[] { 0xff444444, 0xff888888 };	
												   	
	public CriteriaItemAdapter(Context context, int textviewResourceId,
			List<CriteriaItem> listOfCriteria) {
		super(context, textviewResourceId, listOfCriteria);
		this.context = context;
		this.listOfCriteria = listOfCriteria;
	}

	public View getView(int position, View view, ViewGroup viewGroup) {
		CriteriaItem criteriaItem = listOfCriteria.get(position);
		View myView = new CriteriaItemAdapterView(context, criteriaItem, position);
		int colorPos = position % colors.length;
		myView.setBackgroundColor(colors[colorPos]);
		return myView;
	}
}
