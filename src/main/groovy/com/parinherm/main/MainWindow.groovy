package com.parinherm.main

import org.eclipse.core.databinding.observable.Realm
import org.eclipse.jface.action.*
import org.eclipse.jface.databinding.swt.DisplayRealm
import org.eclipse.jface.window.ApplicationWindow
import org.eclipse.swt.SWT
import org.eclipse.swt.graphics.Image
import org.eclipse.swt.graphics.Point
import org.eclipse.swt.graphics.Rectangle
import org.eclipse.swt.layout.FillLayout
import org.eclipse.swt.widgets.*

import com.parinherm.ui.controls.CompositeHelper
import com.parinherm.view.DataBindingView
import com.parinherm.view.JSyntaxPaneView
import com.parinherm.view.QuizView
import com.parinherm.view.ScriptView
import com.parinherm.view.ViewMessage
import com.parinherm.view.graphics.ChristmasTreeView


class MainWindow extends ApplicationWindow {
	
	Composite container
	public static final AppCache cache = new AppCache()
	ViewMessage currentView = null
	
	MainWindow(Shell parentShell) {
		super(parentShell)
		try {
			cache.setupImages()
			this.addMenuBar()
			this.addToolBar(SWT.FLAT | SWT.WRAP)
			this.addStatusLine()

			//testing groovy 3 stuff
			//(1..10).forEach(e -> { println e })
			//def add = (int x, int y) -> { def z = y; return x + z }


		}catch(Exception e) {
			println e.message
			throw e
		}
	}
	
	
	
	/* overrides */
	
	protected Control createContents(Composite parent) {
//		parent.setLayout(new FillLayout())
		container = new Composite(parent, SWT.NONE)
		container.setLayout(new FillLayout())
		//def dbTest = new DataBindingTest(container)
		setStatus("what in the hell?")
		getShell().text = "Kernai"
		Image activitySmall = cache.getImage(cache.IMAGE_ACTVITY_SMALL)
		Image activityLarge = cache.getImage(cache.IMAGE_ACTIVITY_LARGE)
		def images = [activitySmall, activityLarge] as Image[]
		getShell().setImages(images)
		
		DataBindingView view = new DataBindingView(container)
		container
	}
	
	MenuManager createMenuManager() {
		
		try {
			
			def win = this
			
			IAction actionOpenFile = new Action("Open") {
				@Override
				public void run() {
					FileDialog dialog = new FileDialog(getShell(), SWT.OPEN);
					final String file = dialog.open();
					if(file != null) {
						try {
							
							setStatus("File loaded successfully: ");
						} catch (IOException e) {
							e.printStackTrace();
							setStatus("Failed to load file: ");
						}
					}
				}
			}
			
			IAction actionViewConsole  = new Action("Console") {
				@Override
				public void run() {
					CompositeHelper.clear(container)
					JSyntaxPaneView view = new JSyntaxPaneView(container)
					win.container.layout
				}
			}
			
			IAction snippets  = new Action("Snippets") {
				@Override
				public void run() {
					CompositeHelper.clear(container)
					ScriptView view = new ScriptView(container)
					win.container.layout
				}
			}
			
			IAction xmasTree = new Action("&XMas Tree") {
				public void run () {
					CompositeHelper.clear(container)
					currentView = new ChristmasTreeView(container)
					container.layout()
					currentView.createContents()
					
				}
			}
			xmasTree.setAccelerator(SWT.MOD1 | (('X' as char) as int))
			
			IAction zoomToFit= new Action("Scale to Fit") {
				void run() {
					currentView.messagePosted("zoom", [0])
				}
			}
			
			IAction zoom50 = new Action("50%") {
				void run() {
					currentView.messagePosted("zoom", [0.5d])
				}
			}
			
			IAction zoom100 = new Action("100%") {
				void run() {
					currentView.messagePosted("zoom", [1d])
				}
			}
			
			IAction zoom200 = new Action("200%") {
				void run() {
					currentView.messagePosted("zoom", [2d])
				}
			}
			
			
			IAction actionQuit = new Action("&Quit") {
				@Override
				public void run() {
					win.close()
				}
			}
			
			//this is how to do accelerator in groovy 
			char q = 'Q'
			actionQuit.setAccelerator(SWT.MOD1 | q as int)
			

			//can't figure out how to do properties in map format
			IAction actionQuiz  = [run: {
				CompositeHelper.clear(container)
				QuizView view = new QuizView(container)
				container.layout()
				}] as Action
			actionQuiz.text = 'Quiz'
			actionQuiz.setAccelerator(SWT.MOD1 | (char)'z' as int)
		
				
			actionOpenFile.description = "blah"
			actionOpenFile.actionDefinitionId = "crap"
			MenuManager mm = new MenuManager("menu")
			
			MenuManager mnuZoom = new MenuManager("&Zoom")
			mnuZoom.add(zoom50)
			mnuZoom.add(zoom100)
			mnuZoom.add(zoom200)
			mnuZoom.add(zoomToFit)

			
			MenuManager fileMenu = new MenuManager("&File")
			fileMenu.add(new Separator())
			fileMenu.add(actionOpenFile)
			fileMenu.add(actionQuit)
			mm.add(fileMenu)

			MenuManager viewMenu = new MenuManager("&View")
			viewMenu.add(actionViewConsole)
			viewMenu.add(snippets)
			viewMenu.add(xmasTree)
			viewMenu.add(mnuZoom)
			
			viewMenu.add(actionQuiz)
			
			mm.add(viewMenu)

						
			MenuManager helpMenu = new MenuManager("&Help")
			//helpMenu.add(ApplicationData.instance().getAction(ApplicationData.ABOUT_ACTION_KEY));
			//mm.add(helpMenu);
			
			mm
		}
		catch (Exception e) {
			println e
			throw e
		}
	}
	
	protected ToolBarManager createToolBarManager(int style) {
		def tbm = new ToolBarManager(SWT.NONE)
		
//		ActionContributionItem item = new ActionContributionItem(exitAction)
//		item.setMode(ActionContributionItem.MODE_FORCE_TEXT)
//		tbm.add(item);
//		
		tbm.update(true)
		tbm
	}
	
	protected StatusLineManager createStatusLineManager() {
		def sl = new StatusLineManager()
		sl
	}
	
	
	/* there is some kind of bug here
	 * if configure shell is called then the menus do not appear
	 * need to do this stuff in the createContents instead
	void configureShell(Shell newShell) {
		newShell.setText("Kernai")
		Image activitySmall = imageRegistry.get(IMAGE_ACTVITY_SMALL)
		Image activityLarge = imageRegistry.get(IMAGE_ACTIVITY_LARGE)
		def images = [activitySmall, activityLarge] as Image[]
		newShell.setImages(images)
	}
	*/
	
	protected Point getInitialSize() {
		def display = Display.getDefault()
		Rectangle rect = display.clientArea
		new Point((rect.width / 2) as int, (rect.height / 2) as int)
	}
	
	
	
	
	
	
	@Override
	public boolean close() {
		cache.db.close()
		return super.close();
	}
	
	
	static void main(String... args) {
		
		try
		{
			println "is this your goddam main?"

			def display = Display.getDefault()
			Runnable run = {
				try {
					def mainwin = new MainWindow(null)
					mainwin.setBlockOnOpen(true)
					mainwin.open()
					display.dispose()
				} catch (Exception e) {
					println e.message
					println e.stackTrace
				}
				
			}
			Realm.runWithDefault(DisplayRealm.getRealm(display), run)
		} catch (Exception e) {
			println e.message
		}
	}	
}
