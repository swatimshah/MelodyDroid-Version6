package com.droid.melodydroid.display;

import com.droid.melodydroid.R;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CriteriaItemAdapterView extends LinearLayout {

	public CriteriaItemAdapterView(Context context, CriteriaItem criteriaItem, int position) {
		super(context);

		this.setOrientation(VERTICAL);
		this.setTag(criteriaItem);

		View v = inflate(context, R.layout.criteria_row, null);

		TextView criteriaName = (TextView) v.findViewById(R.id.criteriaName);
		criteriaName.setText(criteriaItem.getCriteriaName());
		//if("Artist".equals(criteriaItem.getCriteriaName()) || "Title".equals(criteriaItem.getCriteriaName()))
		if(position % 2 == 1)
			criteriaName.setTextColor(0xff444444);
		
		TextView criteriaValue = (TextView) v.findViewById(R.id.criteriaValue);
		criteriaValue.setText(criteriaItem.getCriteriaValue());
		//if("Artist".equals(criteriaItem.getCriteriaName()) || "Title".equals(criteriaItem.getCriteriaName()))
		if(position % 2 == 1)
			criteriaValue.setTextColor(0xff444444);
		
		addView(v);
	}
}
