package com.parinherm.view.graphics

import org.eclipse.draw2d.ColorConstants
import org.eclipse.draw2d.RectangleFigure
import org.eclipse.draw2d.XYLayout
import org.eclipse.draw2d.geometry.Rectangle

class BranchFigure extends RectangleFigure {
	
	XYLayout layout = new XYLayout()
	NoteFigure mainLabel = new NoteFigure("BRANCH")
	
	
	BranchFigure(){
		super()
		setBackgroundColor(ColorConstants.lightGray)
		setLayoutManager(layout)
		add(mainLabel)
		revalidate()
	}
	
	@Override
	public void validate() {
		layout.setConstraint(mainLabel, new Rectangle(size.width / 2 as Integer, 0, mainLabel.preferredSize.width, mainLabel.preferredSize.height))
		super.validate();

	}
}
