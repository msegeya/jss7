/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.mobicents.protocols.ss7.map.service.sms;

import java.io.IOException;

import org.mobicents.protocols.asn.AsnException;
import org.mobicents.protocols.asn.AsnInputStream;
import org.mobicents.protocols.asn.AsnOutputStream;
import org.mobicents.protocols.asn.Tag;
import org.mobicents.protocols.ss7.map.api.MAPException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentException;
import org.mobicents.protocols.ss7.map.api.MAPParsingComponentExceptionReason;
import org.mobicents.protocols.ss7.map.api.primitives.AddressString;
import org.mobicents.protocols.ss7.map.api.primitives.ISDNAddressString;
import org.mobicents.protocols.ss7.map.api.service.sms.AlertServiceCentreRequestIndication;
import org.mobicents.protocols.ss7.map.primitives.ISDNAddressStringImpl;

/**
 * 
 * @author sergey vetyutnev
 * 
 */
public class AlertServiceCentreRequestIndicationImpl extends SmsServiceImpl implements AlertServiceCentreRequestIndication {

	private ISDNAddressString msisdn;
	private AddressString serviceCentreAddress;
	
	public AlertServiceCentreRequestIndicationImpl() {
	}
	
	public AlertServiceCentreRequestIndicationImpl(ISDNAddressString msisdn, AddressString serviceCentreAddress) {
		this.msisdn = msisdn;
		this.serviceCentreAddress = serviceCentreAddress;
	}
	
	@Override
	public ISDNAddressString getMsisdn() {
		return this.msisdn;
	}

	@Override
	public AddressString getServiceCentreAddress() {
		return this.serviceCentreAddress;
	}

	
	@Override
	public int getTag() throws MAPException {
		return Tag.SEQUENCE;
	}

	@Override
	public int getTagClass() {
		return Tag.CLASS_UNIVERSAL;
	}

	@Override
	public boolean getIsPrimitive() {
		return false;
	}

	
	@Override
	public void decodeAll(AsnInputStream ansIS) throws MAPParsingComponentException {

		try {
			int length = ansIS.readLength();
			this._decode(ansIS, length);
		} catch (IOException e) {
			throw new MAPParsingComponentException("IOException when decoding AlertServiceCentreRequest: " + e.getMessage(), e,
					MAPParsingComponentExceptionReason.MistypedParameter);
		} catch (AsnException e) {
			throw new MAPParsingComponentException("AsnException when decoding AlertServiceCentreRequest: " + e.getMessage(), e,
					MAPParsingComponentExceptionReason.MistypedParameter);
		}
	}

	@Override
	public void decodeData(AsnInputStream ansIS, int length) throws MAPParsingComponentException {

		try {
			this._decode(ansIS, length);
		} catch (IOException e) {
			throw new MAPParsingComponentException("IOException when decoding AlertServiceCentreRequest: " + e.getMessage(), e,
					MAPParsingComponentExceptionReason.MistypedParameter);
		} catch (AsnException e) {
			throw new MAPParsingComponentException("AsnException when decoding AlertServiceCentreRequest: " + e.getMessage(), e,
					MAPParsingComponentExceptionReason.MistypedParameter);
		}
	}

	private void _decode(AsnInputStream ansIS, int length) throws MAPParsingComponentException, IOException, AsnException {
		this.msisdn = null;
		this.serviceCentreAddress = null;
		
		AsnInputStream ais = ansIS.readSequenceStreamData(length);
		int num = 0;
		while( true ) {
			if (ais.available() == 0)
				break;
			
			int tag = ais.readTag();
			switch(num) {
			case 0:
				if (ais.getTagClass() != Tag.CLASS_UNIVERSAL || !ais.isTagPrimitive() || tag != Tag.STRING_OCTET)
					throw new MAPParsingComponentException(
							"Error while decoding AlertServiceCentreRequest.msisdn: bad tag or tag class or is not primitive: TagClass=" + ais.getTagClass()
									+ ", tag=" + tag, MAPParsingComponentExceptionReason.MistypedParameter);
				this.msisdn = new ISDNAddressStringImpl();
				this.msisdn.decodeAll(ais);
				break;
			case 1:
				if (ais.getTagClass() != Tag.CLASS_UNIVERSAL || !ais.isTagPrimitive() || tag != Tag.STRING_OCTET)
					throw new MAPParsingComponentException(
							"Error while decoding AlertServiceCentreRequest.serviceCentreAddress: bad tag or tag class or is not primitive: TagClass=" + ais.getTagClass()
									+ ", tag=" + tag, MAPParsingComponentExceptionReason.MistypedParameter);
				this.serviceCentreAddress = new ISDNAddressStringImpl();
				this.serviceCentreAddress.decodeAll(ais);
				break;
			default:
				ais.advanceElement();
				break;
			}
			
			num++;
		}
		
		if (this.msisdn == null || this.serviceCentreAddress == null)
			throw new MAPParsingComponentException("Error while decoding AlertServiceCentreRequest: 2 parameters are mandatory, found " + num,
					MAPParsingComponentExceptionReason.MistypedParameter);
	}
	
	@Override
	public void encodeAll(AsnOutputStream asnOs) throws MAPException {

		this.encodeAll(asnOs, Tag.CLASS_UNIVERSAL, Tag.SEQUENCE);
	}

	@Override
	public void encodeAll(AsnOutputStream asnOs, int tagClass, int tag) throws MAPException {
		
		try {
			asnOs.writeTag(tagClass, false, tag);
			int pos = asnOs.StartContentDefiniteLength();
			this.encodeData(asnOs);
			asnOs.FinalizeContent(pos);
		} catch (AsnException e) {
			throw new MAPException("AsnException when encoding AlertServiceCentreRequest: " + e.getMessage(), e);
		}
	}

	@Override
	public void encodeData(AsnOutputStream asnOs) throws MAPException {
		
		if (this.msisdn == null || this.serviceCentreAddress == null)
			throw new MAPException("Error when encoding AlertServiceCentreRequest: msisdn or serviceCentreAddress must not be empty");
		
		this.msisdn.encodeAll(asnOs);
		this.serviceCentreAddress.encodeAll(asnOs);
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("AlertServiceCentreRequest [");

		if (this.msisdn != null) {
			sb.append("msisdn=");
			sb.append(this.msisdn.toString());
		}
		if (this.serviceCentreAddress != null) {
			sb.append(", serviceCentreAddress=");
			sb.append(this.serviceCentreAddress.toString());
		}

		sb.append("]");

		return sb.toString();
	}
}
