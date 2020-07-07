package com.parinherm.view.graphics


import org.eclipse.draw2d.*
import org.eclipse.draw2d.geometry.Point

import groovy.transform.TypeChecked

@TypeChecked
class FigureMover implements MouseListener, MouseMotionListener{
	
	private final IFigure figure
	private Point location = null
	
	FigureMover(IFigure figure) {
		this.figure = figure
		figure.addMouseListener(this)
		figure.addMouseMotionListener(this)
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		if (!location) return
		def newLoc = e.getLocation()
		if (!newLoc)  return 
		def offset = newLoc.getDifference(location)
		if (offset.width == 0 && offset.height == 0) return
		location = newLoc
		
		def updateMgr = figure.getUpdateManager()
		def layoutMgr = figure.getParent().getLayoutManager()
		def bounds = figure.getBounds()
		updateMgr.addDirtyRegion(figure.getParent(), bounds)
		bounds = bounds.getCopy().translate(offset.width, offset.height)
		layoutMgr.setConstraint(figure, bounds)
		figure.translate(offset.width, offset.height)
		updateMgr.addDirtyRegion(figure.getParent(), bounds)
		e.consume()
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {

		
	}

	@Override
	public void mouseDoubleClicked(MouseEvent e) {
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		location = e.getLocation()
		e.consume()
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		location = null
		e.consume()
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {

		
	}

	@Override
	public void mouseExited(MouseEvent e) {

		
	}

	@Override
	public void mouseHover(MouseEvent e) {

		
	}
}
