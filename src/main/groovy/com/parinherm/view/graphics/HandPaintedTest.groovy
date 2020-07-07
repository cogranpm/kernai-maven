package com.parinherm.view.graphics

import org.eclipse.draw2d.Figure
import org.eclipse.draw2d.Graphics
import org.eclipse.draw2d.geometry.Point
import org.eclipse.swt.widgets.Composite
import org.eclipse.swt.widgets.Display

class HandPaintedTest extends Figure {
	
	private Composite parent
	
	HandPaintedTest(Composite parent) {
		super()
		this.parent = parent
	
		
//		final def layout = new ToolbarLayout()
//		layout.setSpacing(1)
//		setLayoutManager(layout)
		//setPreferredSize(600, 800)
//		setBackgroundColor(ColorConstants.yellow)
		//setPreferredSize(-1, -1)
	}
	
	@Override
	protected void paintFigure(Graphics graphics) {
		def r = parent.getClientArea()
		//def r = Display.getCurrent().getBounds()
		//println parent.getClientArea()
		//graphics.setForegroundColor(ColorConstants.black)
		//graphics.setBackgroundColor(ColorConstants.red)
		//draw a line from top to bottom
		graphics.drawLine(new Point(r.width / 2, 0), new Point(r.width / 2, r.height))
		//graphics.drawLine(20, 0, 20, 50)
		graphics.drawText("howdy thar", new Point(r.width / 2, r.height / 2))
		
		(1..20).each { 
			Integer increment = r.height / 50
			Integer height = (r.height / 2) + (it * increment)
			def startAt = new Point(0, height)
			def endAt = new Point(r.width, height)
			graphics.drawLine(startAt, endAt) 
			height = (r.height / 2) - (it * increment)
			startAt.y = height
			endAt.y = height
			graphics.drawLine(startAt, endAt)
		}
	}
	
}
