package org.mobicents.protocols.ss7.mtp.cli;

import java.nio.ByteBuffer;

import javolution.text.TextBuilder;

/**
 * 
 * @author amit bhayani
 * 
 */
public abstract class AbstractCommand {

	protected String[] ss7Commands;

	protected byte ZERO_LENGTH = 0x00;

	protected CLICmdListener cLICmdListener = null;

	protected TextBuilder linksetName = new TextBuilder();
	protected TextBuilder linkName = new TextBuilder();
	protected TextBuilder pointCode = new TextBuilder();
	protected TextBuilder localIp = new TextBuilder();

	protected AbstractCommand() {
	}

	protected AbstractCommand(String[] ss7Commands) {
		this.ss7Commands = ss7Commands;
	}

	public abstract boolean encode(ByteBuffer byteBuffer);

	public void decode(ByteBuffer byteBuffer) {
		linksetName.clear();
		linkName.clear();
		pointCode.clear();
	}

	public CLICmdListener getCLICmdListener() {
		return cLICmdListener;
	}

	public void setCLICmdListener(CLICmdListener cmdListener) {
		cLICmdListener = cmdListener;
	}

	protected void sendErrorMsg(ByteBuffer byteBuffer) {
		byteBuffer.clear();
		byteBuffer.put("Unrecognized command".getBytes());
	}

	protected String getString(ByteBuffer byteBuffer, int offSet, int length) {
		byte[] data = new byte[length];
		byteBuffer.get(data, offSet, length);
		return new String(data);
	}

	protected boolean hasMoreBytes(ByteBuffer byteBuffer) {
		return (byteBuffer.position() < byteBuffer.limit());
	}

}
