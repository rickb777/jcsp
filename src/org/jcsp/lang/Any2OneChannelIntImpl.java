    //////////////////////////////////////////////////////////////////////
    //                                                                  //
    //  JCSP ("CSP for Java") Libraries                                 //
    //  Copyright (C) 1996-2006 Peter Welch and Paul Austin.            //
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
    //                                                                  //
    //                                                                  //
    //////////////////////////////////////////////////////////////////////

package org.jcsp.lang;

/**
 * This implements an any-to-one integer channel,
 * safe for use by many writers and one reader.Refer to the {@link Any2OneChannelInt} interface for
 * a fuller description.
 *
 * @see org.jcsp.lang.One2OneChannelIntImpl
 * @see org.jcsp.lang.One2AnyChannelIntImpl
 * @see org.jcsp.lang.Any2AnyChannelIntImpl
 * @see org.jcsp.util.ints.ChannelDataStoreInt
 *
 * @author P.D.Austin and P.H.Welch
 */

class Any2OneChannelIntImpl extends Any2OneIntImpl
{
	Any2OneChannelIntImpl() {
		super(new One2OneChannelIntImpl());
	}
}
