/* ========================================================================
 *
 *  This file is part of CODEC, which is a Java package for encoding
 *  and decoding ASN.1 data structures.
 *
 *  Author: Fraunhofer Institute for Computer Graphics Research IGD
 *          Department A8: Security Technology
 *          Fraunhoferstr. 5, 64283 Darmstadt, Germany
 *
 *  Rights: Copyright (c) 2004 by Fraunhofer-Gesellschaft 
 *          zur Foerderung der angewandten Forschung e.V.
 *          Hansastr. 27c, 80686 Munich, Germany.
 *
 * ------------------------------------------------------------------------
 *
 *  The software package is free software; you can redistribute it and/or 
 *  modify it under the terms of the GNU Lesser General Public License as 
 *  published by the Free Software Foundation; either version 2.1 of the 
 *  License, or (at your option) any later version.
 *
 *  This library is distributed in the hope that it will be useful, but 
 *  WITHOUT ANY WARRANTY; without even the implied warranty of 
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU 
 *  Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public 
 *  License along with this software package; if not, write to the Free 
 *  Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, 
 *  MA 02110-1301, USA or obtain a copy of the license at 
 *  http://www.fsf.org/licensing/licenses/lgpl.txt.
 *
 * ------------------------------------------------------------------------
 *
 *  The CODEC library can solely be used and distributed according to 
 *  the terms and conditions of the GNU Lesser General Public License.
 *
 *  The CODEC library has not been tested for the use or application 
 *  for a determined purpose. It is a developing version that can 
 *  possibly contain errors. Therefore, Fraunhofer-Gesellschaft zur 
 *  Foerderung der angewandten Forschung e.V. does not warrant that the 
 *  operation of the CODEC library will be uninterrupted or error-free. 
 *  Neither does Fraunhofer-Gesellschaft zur Foerderung der angewandten 
 *  Forschung e.V. warrant that the CODEC library will operate and 
 *  interact in an uninterrupted or error-free way together with the 
 *  computer program libraries of third parties which the CODEC library 
 *  accesses and which are distributed together with the CODEC library.
 *
 *  Fraunhofer-Gesellschaft zur Foerderung der angewandten Forschung e.V. 
 *  does not warrant that the operation of the third parties's computer 
 *  program libraries themselves which the CODEC library accesses will 
 *  be uninterrupted or error-free.
 *
 *  Fraunhofer-Gesellschaft zur Foerderung der angewandten Forschung e.V. 
 *  shall not be liable for any errors or direct, indirect, special, 
 *  incidental or consequential damages, including lost profits resulting 
 *  from the combination of the CODEC library with software of any user 
 *  or of any third party or resulting from the implementation of the 
 *  CODEC library in any products, systems or services of any user or 
 *  of any third party.
 *
 *  Fraunhofer-Gesellschaft zur Foerderung der angewandten Forschung e.V. 
 *  does not provide any warranty nor any liability that utilization of 
 *  the CODEC library will not interfere with third party intellectual 
 *  property rights or with any other protected third party rights or will 
 *  cause damage to third parties. Fraunhofer Gesellschaft zur Foerderung 
 *  der angewandten Forschung e.V. is currently not aware of any such 
 *  rights.
 *
 *  The CODEC library is supplied without any accompanying services.
 *
 * ========================================================================
 */
package codec.asn1;

import java.io.IOException;

/**
 * Defines the methods that must be implemented by decoders of ASN.1 types.
 * 
 * 
 * @author Volker Roth
 * @version "$Id: Decoder.java,v 1.2 2000/12/06 17:47:28 vroth Exp $"
 */
public interface Decoder {

    public ASN1Type readType() throws ASN1Exception, IOException;

    public void readType(ASN1Type t) throws ASN1Exception, IOException;

    public void readBoolean(ASN1Boolean t) throws ASN1Exception, IOException;

    public void readInteger(ASN1Integer t) throws ASN1Exception, IOException;

    public void readBitString(ASN1BitString t) throws ASN1Exception,
	    IOException;

    public void readOctetString(ASN1OctetString t) throws ASN1Exception,
	    IOException;

    public void readNull(ASN1Null t) throws ASN1Exception, IOException;

    public void readObjectIdentifier(ASN1ObjectIdentifier t)
	    throws ASN1Exception, IOException;

    public void readReal(ASN1Type t) throws ASN1Exception, IOException;

    public void readString(ASN1String t) throws ASN1Exception, IOException;

    public void readCollection(ASN1Collection t) throws ASN1Exception,
	    IOException;

    public void readCollectionOf(ASN1CollectionOf t) throws ASN1Exception,
	    IOException;

    public void readTime(ASN1Time t) throws ASN1Exception, IOException;

    public void readTaggedType(ASN1TaggedType t) throws ASN1Exception,
	    IOException;

    public void readChoice(ASN1Choice t) throws ASN1Exception, IOException;

}
