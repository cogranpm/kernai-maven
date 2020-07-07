package com.parinherm.view.graphics




import org.eclipse.draw2d.Button
import org.eclipse.draw2d.ColorConstants
import org.eclipse.draw2d.Figure
import org.eclipse.draw2d.GridData
import org.eclipse.draw2d.GridLayout
import org.eclipse.draw2d.Label
import org.eclipse.draw2d.LineBorder
import org.eclipse.draw2d.MouseEvent
import org.eclipse.draw2d.MouseListener
import org.eclipse.draw2d.MouseMotionListener
import org.eclipse.draw2d.XYLayout
import org.eclipse.swt.SWT
import org.eclipse.swt.widgets.Display

import com.parinherm.main.AppCache
import com.parinherm.main.MainWindow

class ChristmasTreeFigure extends Figure {
	
	private AppCache cache = MainWindow.cache
	private Label label
	private XYLayout xyLayout = new XYLayout()
	private TrunkFigure trunk = new TrunkFigure()
	
	ChristmasTreeFigure() {
		
		
		//setLayoutManager(xyLayout)
		//put figure as position 20, 20 with it's preferred size
		
		
		
		//setLayoutManager(new ToolbarLayout(true))
		//setLayoutManager(new FreeformLayout())

		def gridLayout = new GridLayout(1, true)
		//gridLayout.numColumns = 1
		setLayoutManager(gridLayout)
		
		
		setBorder(new LineBorder(ColorConstants.black))
		setBackgroundColor(ColorConstants.yellow)
		setOpaque(true)
		def button = new Button("Button", cache.getImage(cache.IMAGE_ACTIVITY_LARGE))
		button.addActionListener({ e -> println "I was clicked"})
		
		label = new Label("Label", cache.getImage(cache.IMAGE_ACTVITY_SMALL))
		label.addMouseListener(new MouseListener() {
			
	
			
			@Override
			public void mouseReleased(MouseEvent arg0) {
				
			}
			
			@Override
			public void mouseDoubleClicked(MouseEvent arg0) {
				println "double clicked label"
			}

			@Override
			public void mousePressed(MouseEvent arg0) {
				
			}
		})
		
		
		label.addMouseMotionListener(new MouseMotionListener() {

			@Override
			public void mouseDragged(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
				label.setIcon(cache.getImage(cache.IMAGE_GOUP))
				
			}

			@Override
			public void mouseExited(MouseEvent arg0) {
				label.setIcon(cache.getImage(cache.IMAGE_ACTVITY_SMALL))
				
			}

			@Override
			public void mouseHover(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseMoved(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
		})
		
		//add(button)
		//add(label)
		
		//trunk.setSize(getSize().width, getSize().height)
		Display display = Display.getCurrent()
		def displayRect = display.getPrimaryMonitor().getClientArea()
		
		//xyLayout.setConstraint(trunk, new Rectangle(0, 0, displayRect.width, displayRect.height))

		GridData gridData = new GridData()
		gridData.grabExcessHorizontalSpace = true
		gridData.grabExcessVerticalSpace = true
		gridData.verticalAlignment = SWT.FILL
		layoutManager.setConstraint(trunk, gridData)
		add(trunk)
	}
}
