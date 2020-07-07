package com.parinherm.view

trait ViewMessage {
	abstract void messagePosted(String messageId, List args)
	abstract void createContents()
}
