package com.parinherm.view.graphics


import org.eclipse.draw2d.Button
import org.eclipse.draw2d.ColorConstants
import org.eclipse.draw2d.RectangleFigure
import org.eclipse.draw2d.XYLayout
import org.eclipse.draw2d.geometry.Dimension
import org.eclipse.draw2d.geometry.Point
import org.eclipse.draw2d.geometry.Rectangle


class TrunkFigure  extends RectangleFigure{
	
	XYLayout layout = new XYLayout()
	Button button = new Button("fred")
	
	TrunkFigure() {
		super()
		setBackgroundColor(ColorConstants.blue)
		this.setLayoutManager(layout)
		
		//layout.setConstraint(button, new Rectangle(0, size.height / 2 as Integer, button.preferredSize.width, button.preferredSize.height))
		add(button)
		
		button.addActionListener({
			setBackgroundColor(ColorConstants.cyan)
		})

	}
	
	@Override
	public void validate() {
		//making sure the button is always in the middle of the figure
		layout.setConstraint(button, new Rectangle(0, size.height / 2 as Integer, button.preferredSize.width, button.preferredSize.height))
		super.validate();
	}
	

	def relocate() {
		//layout.setConstraint(button, new Rectangle(0, size.height / 2 as Integer, button.preferredSize.width, button.preferredSize.height))
	}

	
		
}
