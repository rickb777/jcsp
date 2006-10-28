//////////////////////////////////////////////////////////////////////
//                                                                  //
//  JCSP ("CSP for Java") Libraries                                 //
//  Copyright (C) 1996-2001 Peter Welch and Paul Austin.            //
//                2001-2004 Quickstone Technologies Limited.        //
//                                                                  //
//  This library is free software; you can redistribute it and/or   //
//  modify it under the terms of the GNU Lesser General Public      //
//  License as published by the Free Software Foundation; either    //
//  version 2.1 of the License, or (at your option) any later       //
//  version.                                                        //
//                                                                  //
//  This library is distributed in the hope that it will be         //
//  useful, but WITHOUT ANY WARRANTY; without even the implied      //
//  warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR         //
//  PURPOSE. See the GNU Lesser General Public License for more     //
//  details.                                                        //
//                                                                  //
//  You should have received a copy of the GNU Lesser General       //
//  Public License along with this library; if not, write to the    //
//  Free Software Foundation, Inc., 59 Temple Place, Suite 330,     //
//  Boston, MA 02111-1307, USA.                                     //
//                                                                  //
//  Author contact: P.H.Welch@ukc.ac.uk                             //
//                  mailbox@quickstone.com                          //
//                                                                  //
//////////////////////////////////////////////////////////////////////

package org.jcsp.net;

/**
 * <p>
 * This interface defines methods for constructing Networked
 * channel ends.
 * </p>
 *
 * @author Quickstone Technologies Limited
 */
public interface NetChannelEndFactory
{
   /**
    * <p>
    * Constructs a <code>NetAltingChannelInput</code> object.
    * </p>
    *
    * @return the constructed <code>NetAltingChannelInput</code> object.
    */
   public NetAltingChannelInput createNet2One();
   
   /**
    * <p>
    * Constructs a <code>NetSharedChannelInput</code> object.
    * </p>
    *
    * @return the constructed <code>NetSharedChannelInput</code> object.
    */
   public NetSharedChannelInput createNet2Any();
   
   /**
    * <p>
    * Constructs a <code>NetChannelOutput</code> object.
    * </p>
    *
    * @return the constructed <code>NetChannelOutput</code> object.
    */
   public NetChannelOutput createOne2Net(NetChannelLocation loc);
   
   /**
    * <p>
    * Constructs a <code>NetSharedChannelOutput</code> object.
    * </p>
    *
    * @return the constructed <code>NetSharedChannelOutput</code> object.
    */
   public NetSharedChannelOutput createAny2Net(NetChannelLocation loc);
}