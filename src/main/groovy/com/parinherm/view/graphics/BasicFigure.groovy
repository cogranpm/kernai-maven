package com.parinherm.view.graphics

import org.eclipse.draw2d.BorderLayout
import org.eclipse.draw2d.Figure
import org.eclipse.draw2d.FlowLayout
import org.eclipse.draw2d.Graphics
import org.eclipse.draw2d.Label
import org.eclipse.draw2d.LineBorder

class BasicFigure  extends Figure{
	
	BasicFigure() {
		
		setPreferredSize(2000, 2000)
		//setLayoutManager(new BorderLayout())
		setLayoutManager(new FlowLayout())
		setBorder(new LineBorder())
		
		1000.times { 
			add(createLabel("howdy $it"))
		}
		
		

	}
	
	private Label createLabel(String caption) {
		Label label = new Label()
		label.setText(caption)
		label
	}
	
	@Override
	protected void paintFigure(Graphics graphics) {
		
		/*
		Rectangle rect = getClientArea()
		graphics.drawText("hello my name is fred and I am here now", new Point(rect.width().intdiv(2), rect.height().intdiv(2)))

		//draw a tree trunk
		Integer trunkWidth = 35
		Integer middlex = rect.width().intdiv(2)
		Integer middley = rect.height().intdiv(2)
		graphics.drawRectangle(middlex - trunkWidth, 0, trunkWidth, rect.height())

		//draw a branch
		Integer currentY = 0
		(0..20).each {
			graphics.drawRectangle(0, currentY, rect.width(), trunkWidth)
			currentY = currentY + (trunkWidth * 4)
		}
		*/
		super.paintFigure(graphics);
	}

    @Override
    protected void paintClientArea(Graphics graphics) {

		super.paintClientArea(graphics)
    }
}
