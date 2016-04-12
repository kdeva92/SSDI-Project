package org.ChatApplication.server.handlers.instructionHandler;

public class SenderFactory {

	private static SenderFactory instructionHandlerFactory;

	private SenderFactory() {

	}

	public static SenderFactory getFactory() {
		if (instructionHandlerFactory == null) {
			instructionHandlerFactory = new SenderFactory();
		}
		return instructionHandlerFactory;
	}

	// if property set to Test return other handler
	public IInstructionHandler getInstructionHandler() {
		return InstructionHandler.getInstructionHandler();
	}

}
