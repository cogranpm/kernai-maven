package com.parinherm.view.graphics


import org.eclipse.draw2d.ColorConstants
import org.eclipse.draw2d.CompoundBorder
import org.eclipse.draw2d.Figure
import org.eclipse.draw2d.Graphics
import org.eclipse.draw2d.GridData
import org.eclipse.draw2d.GridLayout
import org.eclipse.draw2d.IFigure
import org.eclipse.draw2d.ImageFigure
import org.eclipse.draw2d.Label
import org.eclipse.draw2d.LineBorder
import org.eclipse.draw2d.MarginBorder
import org.eclipse.draw2d.ToolbarLayout
import org.eclipse.swt.graphics.Image
import org.eclipse.swt.graphics.Pattern
import org.eclipse.swt.widgets.Display

import groovy.transform.TypeChecked

@TypeChecked
class PersonFigure extends Figure {
	
	
	PersonFigure(String name, Image image, int birthYear, int deathYear){
		
		final def layout = new ToolbarLayout()
		layout.setSpacing(1)
		setLayoutManager(layout)
		setPreferredSize(100, 100)
		setBorder(new CompoundBorder(new LineBorder(1), new MarginBorder(2,2,2,2)))
		add(new Label(name))
		
		
		//display image to the left of the name/date
		IFigure imageNameDates = new Figure()
		final def gridLayout = new GridLayout(2, false)
		gridLayout.marginHeight = 0
		gridLayout.marginWidth = 0
		gridLayout.horizontalSpacing = 1
		imageNameDates.setLayoutManager(gridLayout)
		add(imageNameDates)
		imageNameDates.add(new ImageFigure(image))
		
		//display name and date to the right of the image
		def nameDates = new Figure()
		nameDates.setLayoutManager(new ToolbarLayout())
		imageNameDates.add(nameDates, new GridData(GridData.FILL_HORIZONTAL))
		nameDates.add(new Label(name))
		
		String datesText = "$birthYear - ${deathYear != -1 ? deathYear : ''}"
		add(new Label(datesText))
		new FigureMover(this)
	}
	
	@Override
	public void paintFigure(Graphics graphics) {
		def r = getBounds()
		graphics.setBackgroundPattern(new Pattern(Display.getCurrent(), r.x, r.y, r.x + r.width, r.y + r.height, ColorConstants.white, ColorConstants.lightGray))
		graphics.fillRectangle(r)
	}
}
