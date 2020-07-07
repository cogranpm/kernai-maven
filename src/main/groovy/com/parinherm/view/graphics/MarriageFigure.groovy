package com.parinherm.view.graphics

import org.eclipse.draw2d.*
import org.eclipse.draw2d.geometry.PointList
import org.eclipse.draw2d.geometry.Rectangle

class MarriageFigure extends PolygonShape{
	
	private static final PointList ARROWHEAD = new PointList ([0, 0, -2, 2, -2, 0, -2, -2, 0, 0] as int[])
	public static final RADIUS = 26
	
	MarriageFigure(int year){
		Rectangle r = new Rectangle(0, 0, 50, 50)
		setStart(r.getTop())
		addPoint(r.getTop())
		addPoint(r.getLeft())
		addPoint(r.getBottom())
		addPoint(r.getRight())
		addPoint(r.getTop())
		setEnd(r.getTop())
		setFill(true)
		setBackgroundColor(ColorConstants.lightGray)
		setPreferredSize(r.getSize().expand(1, 1))
		
		setLayoutManager(new StackLayout())
		add(new Label("$year") {
			@Override
			public boolean containsPoint(int x, int y) {
				false
			}
		})
		
		new FigureMover(this)
	}
	
	
	
	PolylineConnection addParent(IFigure figure) {
		def connection = new PolylineConnection()
		connection.setSourceAnchor(new ChopboxAnchor(figure))
		connection.setTargetAnchor(new MarriageAnchor(this))
		
		def decoration = new PolygonDecoration()
		decoration.setTemplate(ARROWHEAD)
		decoration.setBackgroundColor(ColorConstants.darkGray)
		connection.setTargetDecoration(decoration)
		
		connection
	}
	
	PolylineConnection addChild(IFigure figure) {
		def connection = new PolylineConnection()
		connection.setSourceAnchor(new MarriageAnchor(this))
		connection.setTargetAnchor(new ChopboxAnchor(figure))
		
		def decoration = new PolygonDecoration()
		decoration.setTemplate(ARROWHEAD)
		decoration.setBackgroundColor(ColorConstants.white)
		connection.setTargetDecoration(decoration)
		
		connection
	}
	
	
}

