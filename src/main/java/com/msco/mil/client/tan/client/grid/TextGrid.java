package com.msco.mil.client.tan.client.grid;

import com.google.gwt.user.client.ui.HTML;
import com.msco.mil.client.tan.sencha.ExampleStyles;
import com.sencha.gxt.widget.core.client.ContentPanel;

public class TextGrid extends ContentPanel {

	public TextGrid(String text) {
		HTML html = new HTML(text);
		html.addStyleName(ExampleStyles.get().paddedText());
		this.add(html);
		this.setHeaderVisible(false);
	}
}
