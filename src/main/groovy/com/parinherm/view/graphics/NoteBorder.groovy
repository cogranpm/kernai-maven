package com.parinherm.view.graphics

import org.eclipse.draw2d.AbstractBorder
import org.eclipse.draw2d.ColorConstants
import org.eclipse.draw2d.Graphics
import org.eclipse.draw2d.IFigure
import org.eclipse.draw2d.geometry.Insets
import org.eclipse.draw2d.geometry.Rectangle
import org.eclipse.swt.SWT

class NoteBorder extends AbstractBorder {
	
	public static final int FOLD = 10

	@Override
	public Insets getInsets(IFigure arg0) {
		return new Insets(1, 2 + FOLD, 2, 2)
	}

	@Override
	public void paint(IFigure figure, Graphics graphics, Insets insets) {
		Rectangle r = figure.getBounds().getCopy();
		r.crop(insets);
		graphics.setLineWidth(1);
		// solid long edges around border
		graphics.drawLine(r.x + FOLD, r.y, r.x + r.width - 1, r.y);
		graphics.drawLine(r.x, r.y + FOLD, r.x, r.y + r.height - 1);
		graphics.drawLine(r.x + r.width - 1, r.y, r.x + r.width - 1,
		r.y + r.height - 1);
		graphics.drawLine(r.x, r.y + r.height - 1, r.x + r.width - 1,
		r.y + r.height - 1);
		 // solid short edges
		graphics.drawLine(r.x + FOLD, r.y, r.x + FOLD, r.y + FOLD);
		graphics.drawLine(r.x, r.y + FOLD, r.x + FOLD, r.y + FOLD);
		// gray small triangle
		graphics.setBackgroundColor(ColorConstants.lightGray);
		graphics.fillPolygon([ r.x, r.y + FOLD, r.x + FOLD, r.y, r.x + FOLD, r.y + FOLD ] as int[]);
		// dotted short diagonal line
		graphics.setLineStyle(SWT.LINE_DOT);
		graphics.drawLine(r.x, r.y + FOLD, r.x + FOLD, r.y);
		
		
	}
}
