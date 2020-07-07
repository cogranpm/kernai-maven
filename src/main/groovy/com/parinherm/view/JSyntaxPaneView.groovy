/* jsyntax pane example */


package com.parinherm.view

import java.awt.BorderLayout
import java.awt.EventQueue
import java.awt.Frame

import javax.swing.JApplet
import javax.swing.JEditorPane
import javax.swing.JScrollPane

import org.eclipse.swt.SWT
import org.eclipse.swt.awt.SWT_AWT
import org.eclipse.swt.layout.FillLayout
import org.eclipse.swt.widgets.Composite

import jsyntaxpane.DefaultSyntaxKit


class JSyntaxPaneView {

	JSyntaxPaneView(Composite parent) {
		
	System.setProperty("sun.awt.noerasebackground", "true")
	Composite composite = new Composite(parent, SWT.EMBEDDED | SWT.NO_BACKGROUND)
	composite.setLayout(new FillLayout())
	SWT_AWT.initializeSwing()
	Frame frame = SWT_AWT.new_Frame(composite)
	frame.setLayout(new BorderLayout())
	
	EventQueue.invokeLater(new Runnable() {
		@Override
		public void run() {

			frame.setLayout(new BorderLayout())
	
			JApplet applet = new JApplet()
			//applet.setLayout(new BorderLayout())
			applet.setFocusCycleRoot(false)
			
			
			JEditorPane ep = new JEditorPane()
			DefaultSyntaxKit.initKit()
			ep.setContentType("text/groovy")
			
			JScrollPane scrollPane = new JScrollPane(ep);
			applet.add(scrollPane, BorderLayout.CENTER)
			applet.getRootPane().getContentPane().add(scrollPane)
	
			applet.init()
			
			frame.add(applet, BorderLayout.CENTER)
			frame.pack()
			frame.setVisible(true)
			//frame.doLayout()
				
		}
	})
	
	}
	


	
//		Console console = new Console()
//		console.setVariable("scriptname", "groovysqlscript")
//		//console?.inputEditor?.getTextEditor()?.setText("blash blah")
//		console.afterExecution = { println "after execute" }
//		console.run()
//		println "the console"
//		console.appendOutput("hello world", null)
}
