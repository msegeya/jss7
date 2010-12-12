package org.mobicents.protocols.ss7.mtp.cli;

import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;

/**
 * 
 * @author amit bhayani
 * 
 */
public class SS7Command extends AbstractCommand {

	public SS7Command() {
	}

	protected SS7Command(String[] ss7Commands) {
		super(ss7Commands);
	}

	private void showShowCmdHelp() {
		System.out
				.println("                                                             ss7 addlinkset linkset-name        Add a new linkset");
		System.out
				.println("ss7 linkset linkset-name network-indicator {international | national | reserved | spare}        Configure the network indicator for a linkset");
		System.out
				.println("                                            ss7 linkset linkset-name local-pc point-code        configure the local point code for a linkset");
		System.out
				.println("                                         ss7 linkset linkset-name adjacent-pc point-code        configure the adjacent point code for a linkset");
		System.out
				.println("                        ss7 linkset linkset-name local-ip local-ip local-port local-port        configure the localAddress for M3UA");
		System.out
				.println("                                                              ss7 deletelinkset link-set        delete linkset");
		System.out
				.println("                                              ss7 linkset linkset-name addlink link-name        add the link to linkset");
		System.out
				.println("                                                            ss7 link link-name span span        configure the span for link");
		System.out
				.println("                                                      ss7 link link-name channel channel        configure the channel for link");
		System.out
				.println("                                                            ss7 link link-name code code        configure the code for link");
		System.out
				.println("                                                                ss7 deletelink link-name        delete link");
		System.out
				.println("                                                      ss7 inhibit linkset-name link-name        inhibit a link");
		System.out
				.println("                                                    ss7 uninhibit linkset-name link-name        uninhibit a link");

	}

	@Override
	public boolean encode(ByteBuffer byteBuffer) {
		if (ss7Commands.length < 3 || ss7Commands.length > 7) {
			System.out.println("Invalid command");
			this.showShowCmdHelp();
			return false;
		}

		// Header
		byteBuffer.put((byte) CliCmdEnum.SS7.getCmdInt());

		if (ss7Commands[1].compareTo("addlinkset") == 0) {
			if (ss7Commands.length != 3) {
				System.out.println("Invalid command");
				System.out
						.println("ss7 addlinkset linkset-name        Add a new linkset");
				return false;
			}

			// Body
			byteBuffer.put((byte) CliCmdEnum.ADDLINKSET.getCmdInt());
			byteBuffer.put((byte) ss7Commands[2].length());
			byteBuffer.put(ss7Commands[2].getBytes());

			return true;

		} else if (ss7Commands[1].compareTo("linkset") == 0) {

			// Body
			byteBuffer.put((byte) CliCmdEnum.LINKSET.getCmdInt()); // command
			byteBuffer.put((byte) ss7Commands[2].length()); // length
			byteBuffer.put(ss7Commands[2].getBytes()); // value

			if (ss7Commands[3].compareTo("network-indicator") == 0) {
				if (ss7Commands.length != 5) {
					System.out.println("Invalid command");
					System.out
							.println("ss7 linkset linkset-name network-indicator {international | national | reserved | spare}        Configure the network indicator for a linkset");
					return false;
				} else if (!(ss7Commands[4].compareTo("international") == 0
						|| ss7Commands[4].compareTo("national") == 0
						|| ss7Commands[4].compareTo("reserved") == 0 || ss7Commands[4]
						.compareTo("spare") == 0)) {
					System.out.println("Invalid command");
					System.out
							.println("ss7 linkset linkset-name network-indicator {international | national | reserved | spare}        Configure the network indicator for a linkset");

				}

				byteBuffer.put((byte) CliCmdEnum.NETWORK_INDICATOR.getCmdInt()); // command
				byteBuffer.put(ZERO_LENGTH); // length

				byteBuffer.put((byte) CliCmdEnum.getCommand(ss7Commands[4])
						.getCmdInt()); // command
				return true;

			} else if (ss7Commands[3].compareTo("local-pc") == 0) {

				if (ss7Commands.length != 5) {
					System.out.println("Invalid command");
					System.out
							.println("ss7 linkset linkset-name local-pc point-code        configure the local point code for a linkset");
					return false;
				}
				// TODO : Add check if point-code is Integer?

				byteBuffer.put((byte) CliCmdEnum.LOCAL_PC.getCmdInt()); // command
				byteBuffer.put((byte) ss7Commands[4].length()); // length
				byteBuffer.put(ss7Commands[4].getBytes()); // value

				return true;

			} else if (ss7Commands[3].compareTo("adjacent-pc") == 0) {
				if (ss7Commands.length != 5) {
					System.out.println("Invalid command");
					System.out
							.println("ss7 linkset linkset-name adjacent-pc point-code        configure the adjacent point code for a linkset");
					return false;
				}
				// TODO : Add check if point-code is Integer?

				byteBuffer.put((byte) CliCmdEnum.ADJACENT_PC.getCmdInt()); // command
				byteBuffer.put((byte) ss7Commands[4].length()); // length
				byteBuffer.put(ss7Commands[4].getBytes()); // value

				return true;

			} else if (ss7Commands[3].compareTo("local-ip") == 0) {

				if (ss7Commands.length != 7) {
					System.out.println("Invalid command");
					System.out
							.println("ss7 linkset linkset-name local-ip local-ip local-port local-port        configure the localAddress for M3UA");
					return false;
				} else if (ss7Commands[5].compareTo("local-port") != 0) {
					System.out.println("Invalid command");
					System.out
							.println("ss7 linkset linkset-name local-ip local-ip local-port local-port        configure the localAddress for M3UA");

				} else if (!Utils.validateIp(ss7Commands[4])) {
					System.out.println("Invalid IP Address");
					System.out
							.println("ss7 linkset linkset-name local-ip local-ip local-port local-port        configure the localAddress for M3UA");
				}

				int port = Utils.validatePort(ss7Commands[6]);
				if (port == -1) {
					System.out.println("Invalid Port");
					System.out
							.println("ss7 linkset linkset-name local-ip local-ip local-port local-port        configure the localAddress for M3UA");
					return false;
				}

				byteBuffer.put((byte) CliCmdEnum.LOCAL_IP.getCmdInt()); // command
				byteBuffer.put((byte) ss7Commands[4].length()); // length
				byteBuffer.put(ss7Commands[4].getBytes()); // value

				byteBuffer.put((byte) CliCmdEnum.LOCAL_PORT.getCmdInt()); // command
				byteBuffer.put((byte) 0x02); // length
				byteBuffer.put((byte) ((port & 0x0000ff00) >> 8)); // value
				byteBuffer.put((byte) (port & 0x000000ff)); // value

				return true;

			} else if (ss7Commands[3].compareTo("addlink") == 0) {
				if (ss7Commands.length != 5) {
					System.out.println("Invalid command");
					System.out
							.println("ss7 linkset linkset-name addlink link-name        add the link to linkset");
					return false;
				}

				byteBuffer.put((byte) CliCmdEnum.ADDLINK.getCmdInt()); // command
				byteBuffer.put((byte) ss7Commands[4].length()); // length
				byteBuffer.put(ss7Commands[4].getBytes()); // value

				return true;

			} else {
				System.out.println("Invalid command");
				System.out
						.println("ss7 linkset linkset-name network-indicator {international | national | reserved | spare}        Configure the network indicator for a linkset");
				System.out
						.println("                                            ss7 linkset linkset-name local-pc point-code        configure the local point code for a linkset");
				System.out
						.println("                                         ss7 linkset linkset-name adjacent-pc point-code        configure the adjacent point code for a linkset");
				System.out
						.println("                        ss7 linkset linkset-name local-ip local-ip local-port local-port        configure the localAddress for M3UA");
				System.out
						.println("                                              ss7 linkset linkset-name addlink link-name        add the link to linkset");
			}
			return false;

		} else if (ss7Commands[1].compareTo("deletelinkset") == 0) {
			if (ss7Commands.length != 3) {
				System.out.println("Invalid command");
				System.out
						.println("ss7 deletelinkset link-set        delete linkset");
				return false;
			}

			// Body
			byteBuffer.put((byte) CliCmdEnum.DELETELINKSET.getCmdInt());
			byteBuffer.put((byte) ss7Commands[2].length());
			byteBuffer.put(ss7Commands[2].getBytes());
			return true;

		} else if (ss7Commands[1].compareTo("link") == 0) {
			if (ss7Commands.length != 5) {
				System.out.println("Invalid command");
				System.out
						.println("      ss7 link link-name span span        configure the span for link");
				System.out
						.println("ss7 link link-name channel channel        configure the channel for link");
				System.out
						.println("      ss7 link link-name code code        configure the code for link");

				return false;
			}

			// Body
			byteBuffer.put((byte) CliCmdEnum.LINK.getCmdInt()); // command
			byteBuffer.put((byte) ss7Commands[2].length()); // length
			byteBuffer.put(ss7Commands[2].getBytes()); // value

			if (ss7Commands[3].compareTo("span") == 0) {
				int span = Utils.validateSpan(ss7Commands[4]);
				if (span == -1) {
					System.out.println("Invalid Span");
					System.out
							.println("ss7 link link-name span span        configure the span for link");
					return false;
				}

				// Body
				byteBuffer.put((byte) CliCmdEnum.SPAN.getCmdInt()); // command
				byteBuffer.put((byte) 0x01); // length
				byteBuffer.put((byte) span); // value
				return true;

			} else if (ss7Commands[3].compareTo("channel") == 0) {
				int channel = Utils.validateChannel(ss7Commands[4]);
				if (channel == -1) {
					System.out.println("Invalid channel");
					System.out
							.println("ss7 link link-name channel channel        configure the channel for link");
					return false;
				}

				// Body
				byteBuffer.put((byte) CliCmdEnum.CHANNEL.getCmdInt()); // command
				byteBuffer.put((byte) 0x01); // length
				byteBuffer.put((byte) channel); // value

				return true;
			} else if (ss7Commands[3].compareTo("code") == 0) {
				int code = Utils.validateCode(ss7Commands[4]);
				if (code == -1) {
					System.out.println("Invalid code");
					System.out
							.println("ss7 link link-name code code        configure the code for link");
					return false;
				}

				// Body
				byteBuffer.put((byte) CliCmdEnum.CODE.getCmdInt()); // command
				byteBuffer.put((byte) 0x02); // length
				byteBuffer.put((byte) ((code & 0x0000ff00) >> 8)); // value
				byteBuffer.put((byte) (code & 0x000000ff)); // value

				return true;
			} else {
				System.out.println("Invalid command");
				System.out
						.println("      ss7 link link-name span span        configure the span for link");
				System.out
						.println("ss7 link link-name channel channel        configure the channel for link");
				System.out
						.println("      ss7 link link-name code code        configure the code for link");

				return false;
			}

		} else if (ss7Commands[1].compareTo("deletelink") == 0) {
			if (ss7Commands.length != 3) {
				System.out.println("Invalid command");
				System.out
						.println("ss7 deletelink link-name        delete link");
				return false;
			}

			// Body
			byteBuffer.put((byte) CliCmdEnum.DELETELINK.getCmdInt());
			byteBuffer.put((byte) ss7Commands[2].length());
			byteBuffer.put(ss7Commands[2].getBytes());
			return true;

		} else if (ss7Commands[1].compareTo("inhibit") == 0) {
			if (ss7Commands.length != 4) {
				System.out.println("Invalid command");
				System.out
						.println("ss7 inhibit linkset-name link-name        inhibit a link");
				return false;
			}

			// Body
			byteBuffer.put((byte) CliCmdEnum.INHIBIT.getCmdInt()); // command
			byteBuffer.put(ZERO_LENGTH); // length

			byteBuffer.put((byte) CliCmdEnum.LINKSET.getCmdInt()); // command
			byteBuffer.put((byte) ss7Commands[2].length()); // length
			byteBuffer.put(ss7Commands[2].getBytes()); // value

			byteBuffer.put((byte) CliCmdEnum.LINK.getCmdInt()); // command
			byteBuffer.put((byte) ss7Commands[3].length()); // length
			byteBuffer.put(ss7Commands[3].getBytes()); // value

			return true;

		} else if (ss7Commands[1].compareTo("uninhibit") == 0) {
			if (ss7Commands.length != 4) {
				System.out.println("Invalid command");
				System.out
						.println("ss7 uninhibit linkset-name link-name        uninhibit a link");
				return false;
			}

			// Body
			byteBuffer.put((byte) CliCmdEnum.UNINHIBIT.getCmdInt()); // command
			byteBuffer.put(ZERO_LENGTH); // length

			byteBuffer.put((byte) CliCmdEnum.LINKSET.getCmdInt()); // command
			byteBuffer.put((byte) ss7Commands[2].length()); // length
			byteBuffer.put(ss7Commands[2].getBytes()); // value

			byteBuffer.put((byte) CliCmdEnum.LINK.getCmdInt()); // command
			byteBuffer.put((byte) ss7Commands[3].length()); // length
			byteBuffer.put(ss7Commands[3].getBytes()); // value

			return true;
		} else {
			System.out.println("Invalid command");
			this.showShowCmdHelp();
			return false;
		}
		// return false;
	}

	@Override
	public void decode(ByteBuffer byteBuffer) {
		super.decode(byteBuffer);
		try {

			int cmd = byteBuffer.get();
			int cmdLength;
			if (cmd == CliCmdEnum.ADDLINKSET.getCmdInt()) {
				cmdLength = byteBuffer.get();
				while (linksetName.length() < cmdLength) {
					linksetName.append((char)byteBuffer.get());
				}

				// String name = getString(byteBuffer, 0, cmdLength);
				this.cLICmdListener.addLinkSet(linksetName, byteBuffer);

			} else if (cmd == CliCmdEnum.LINKSET.getCmdInt()) {
				cmdLength = byteBuffer.get();
				while (linksetName.length() < cmdLength) {
					linksetName.append((char)byteBuffer.get());
				}
				// String linksetName = getString(byteBuffer, 0, cmdLength);

				// Net Cmd
				cmd = byteBuffer.get();
				if (cmd == CliCmdEnum.NETWORK_INDICATOR.getCmdInt()) {
					cmdLength = byteBuffer.get();// zero

					cmd = byteBuffer.get();// international | national |
					// reserved | spare
					CliCmdEnum networkInd = CliCmdEnum.getCommand(cmd);
					if (networkInd == null) {
						sendErrorMsg(byteBuffer);
						return;
					}

					this.cLICmdListener.networkIndicator(linksetName,
							networkInd, byteBuffer);
				} else if (cmd == CliCmdEnum.LOCAL_PC.getCmdInt()) {
					cmdLength = byteBuffer.get();

					while (pointCode.length() < cmdLength) {
						pointCode.append((char)byteBuffer.get());
					}

					// String localPc = getString(byteBuffer, 0, cmdLength);
					this.cLICmdListener.localPointCode(linksetName, pointCode,
							byteBuffer);

				} else if (cmd == CliCmdEnum.ADJACENT_PC.getCmdInt()) {
					cmdLength = byteBuffer.get();
					// String adjacentPc = getString(byteBuffer, 0, cmdLength);

					while (pointCode.length() < cmdLength) {
						pointCode.append((char)byteBuffer.get());
					}

					this.cLICmdListener.adjacentPointCode(linksetName,
							pointCode, byteBuffer);

				} else if (cmd == CliCmdEnum.LOCAL_IP.getCmdInt()) {
					cmdLength = byteBuffer.get();
					while (localIp.length() < cmdLength) {
						localIp.append((char)byteBuffer.get());
					}

					// String localIp = getString(byteBuffer, 0, cmdLength);

					cmd = byteBuffer.get(); // LOCAL_PORT
					cmdLength = byteBuffer.get();
					int localPort = ((byteBuffer.get() << 8) | byteBuffer.get());

					this.cLICmdListener.localIpPort(linksetName, localIp,
							localPort, byteBuffer);

				} else if (cmd == CliCmdEnum.ADDLINK.getCmdInt()) {
					cmdLength = byteBuffer.get();
					while (linkName.length() < cmdLength) {
						linkName.append((char)byteBuffer.get());
					}
					// String linkName = getString(byteBuffer, 0, cmdLength);
					this.cLICmdListener.addLink(linksetName, linkName,
							byteBuffer);
				} else {
					sendErrorMsg(byteBuffer);
					return;
				}

			} else if (cmd == CliCmdEnum.LINK.getCmdInt()) {
				cmdLength = byteBuffer.get();
				while (linkName.length() < cmdLength) {
					linkName.append((char)byteBuffer.get());
				}
				// String linkName = getString(byteBuffer, 0, cmdLength);

				// Net Cmd
				cmd = byteBuffer.get();
				if (cmd == CliCmdEnum.SPAN.getCmdInt()) {
					cmdLength = byteBuffer.get();
					int span = byteBuffer.get();

					this.cLICmdListener.span(linkName, span, byteBuffer);
				} else if (cmd == CliCmdEnum.CHANNEL.getCmdInt()) {
					cmdLength = byteBuffer.get();
					int channel = byteBuffer.get();

					this.cLICmdListener.channel(linkName, channel, byteBuffer);
				} else if (cmd == CliCmdEnum.CODE.getCmdInt()) {
					cmdLength = byteBuffer.get();
					int code = ((byteBuffer.get() << 8) | byteBuffer.get());

					this.cLICmdListener.code(linkName, code, byteBuffer);
				} else {
					sendErrorMsg(byteBuffer);
					return;
				}

			} else if (cmd == CliCmdEnum.DELETELINKSET.getCmdInt()) {
				cmdLength = byteBuffer.get();
				// String name = getString(byteBuffer, 0, cmdLength);
				while (linksetName.length() < cmdLength) {
					linksetName.append((char)byteBuffer.get());
				}
				this.cLICmdListener.deleteLinkSet(linksetName, byteBuffer);
			} else if (cmd == CliCmdEnum.DELETELINK.getCmdInt()) {
				cmdLength = byteBuffer.get();
				while (linkName.length() < cmdLength) {
					linkName.append((char)byteBuffer.get());
				}
				// String name = getString(byteBuffer, 0, cmdLength);
				this.cLICmdListener.deleteLink(linkName, byteBuffer);
			} else if (cmd == CliCmdEnum.INHIBIT.getCmdInt()) {
				cmdLength = byteBuffer.get();// zero

				cmd = byteBuffer.get();// LINKSET
				cmdLength = byteBuffer.get();
				while (linksetName.length() < cmdLength) {
					linksetName.append((char)byteBuffer.get());
				}
				// String linksetName = getString(byteBuffer, 0, cmdLength);

				cmd = byteBuffer.get();// LINK
				cmdLength = byteBuffer.get();
				while (linkName.length() < cmdLength) {
					linkName.append((char)byteBuffer.get());
				}
				// String linkName = getString(byteBuffer, 0, cmdLength);

				this.cLICmdListener.inhibit(linksetName, linkName, byteBuffer);

			} else if (cmd == CliCmdEnum.UNINHIBIT.getCmdInt()) {
				cmdLength = byteBuffer.get();// zero

				cmd = byteBuffer.get();// LINKSET
				cmdLength = byteBuffer.get();
				while (linksetName.length() < cmdLength) {
					linksetName.append((char)byteBuffer.get());
				}
				// String linksetName = getString(byteBuffer, 0, cmdLength);

				cmd = byteBuffer.get();// LINK
				cmdLength = byteBuffer.get();
				// String linkName = getString(byteBuffer, 0, cmdLength);
				while (linkName.length() < cmdLength) {
					linkName.append((char)byteBuffer.get());
				}

				this.cLICmdListener
						.uninhibit(linksetName, linkName, byteBuffer);
			} else {
				sendErrorMsg(byteBuffer);
				return;
			}
		} catch (BufferUnderflowException bufe) {
			// ideally should never happen
			sendErrorMsg(byteBuffer);
		}
	}

}
