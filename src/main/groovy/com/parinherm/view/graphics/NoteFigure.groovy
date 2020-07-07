package com.parinherm.view.graphics

import org.eclipse.draw2d.ColorConstants
import org.eclipse.draw2d.Graphics
import org.eclipse.draw2d.Label
import org.eclipse.draw2d.geometry.Rectangle

class NoteFigure extends Label {
	
	NoteFigure(String note){
		super(note)
		//setOpaque(true)
		//setBackgroundColor(ColorConstants.white)
		setBorder(new NoteBorder())
	}
	
	@Override
	protected void paintFigure(Graphics graphics) {
		graphics.setBackgroundColor(ColorConstants.white)
		Rectangle b = getBounds()
		final int fold = NoteBorder.FOLD
		graphics.fillRectangle(b.x + fold, b.y, b.width - fold, fold)
		graphics.fillRectangle(b.x, b.y + fold, b.width, b.height - fold)
		
		super.paintFigure(graphics)
	}
}
