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

package org.jcsp.lang;

/**
 * <p>
 * Defines an interface for a factory than can create connections.
 * </p>
 *
 * @author Quickstone Technologies Limited
 */
public interface ConnectionFactory
{
    /**
     * Constructs and returns an implementation of
     * <code>One2OneConnection</code>.
     *
     * @return	the constructed <code>One2OneConnection</code> object.
     */
    public One2OneConnection createOne2One();

    /**
     * Constructs and returns an implementation of
     * <code>Any2OneConnection</code>.
     *
     * @return	the constructed <code>Any2OneConnection</code> object.
     */
    public Any2OneConnection createAny2One();

    /**
     * Constructs and returns an implementation of
     * <code>One2AnyConnection</code>.
     *
     * @return	the constructed <code>One2AnyConnection</code> object.
     */
    public One2AnyConnection createOne2Any();

    /**
     * Constructs and returns an implementation of
     * <code>Any2AnyConnection</code>.
     *
     * @return	the constructed <code>Any2AnyConnection</code> object.
     */
    public Any2AnyConnection createAny2Any();
}
