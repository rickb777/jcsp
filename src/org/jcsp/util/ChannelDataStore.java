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

package org.jcsp.util;

/**
 * This is the interface for object channel plug-ins that define their buffering
 * characteristics.
 * <H2>Description</H2>
 * <TT>ChannelDataStore</TT> defines the interface to the logic used by
 * channels defined in the <TT>org.jcsp.lang</TT> package to manage the data
 * being communicated.
 * <P>
 * This enables that logic to be varied by creating channels specifying
 * a particular implementation of this interface.  This reduces the number of
 * classes that would otherwise need to be defined.  The default channel
 * constructor (with no parameters) uses the <TT>ZeroBuffer</TT> implementation,
 * which gives the standard CSP semantics -- no buffering and full synchronisation
 * between reading and writing processes.
 * See the static
 * {@link org.jcsp.lang.Channel#createOne2One(org.jcsp.util.ChannelDataStore) <TT>create</TT>}
 * methods of {@link org.jcsp.lang.Channel} etc.
 * <P>
 * <I>Note: instances of </I><TT>ChannelDataStore</TT><I> implementations are
 * used by the various channel classes within </I><TT>com.quickstone.com.quickstone.jcsp.lang</TT><I>
 * in a thread-safe way.  They are not intended for any other purpose.
 * Developers of new </I><TT>ChannelDataStore</TT><I> implementations,
 * therefore, do not need to worry about thread safety (e.g. by making its
 * methods </I><TT>synchronized</TT><I>).  Also, developers can assume that
 * the documented pre-conditions for invoking the </I><TT>get</TT><I>
 * and </I><TT>put</TT><I> methods will be met.</I>
 *
 * @see org.jcsp.util.ZeroBuffer
 * @see org.jcsp.util.Buffer
 * @see org.jcsp.util.OverWriteOldestBuffer
 * @see org.jcsp.util.OverWritingBuffer
 * @see org.jcsp.util.OverFlowingBuffer
 * @see org.jcsp.util.InfiniteBuffer
 * @see org.jcsp.lang.Channel
 *
 * @author P.D.Austin
 */

//}}}

public interface ChannelDataStore extends Cloneable
{
    /** Indicates that the <TT>ChannelDataStore</TT> is empty
     * -- it can accept only a <TT>put</TT>.
     */
    public final static int EMPTY = 0;

    /**
     * Indicates that the <TT>ChannelDataStore</TT> is neither empty nor full
     * -- it can accept either a <TT>put</TT> or a <TT>get</TT> call.
     */
    public final static int NONEMPTYFULL = 1;

    /** Indicates that the <TT>ChannelDataStore</TT> is full
     * -- it can accept only a <TT>get</TT>.
     */
    public final static int FULL = 2;

    /**
     * Returns the current state of the <TT>ChannelDataStore</TT>.
     *
     * @return the current state of the <TT>ChannelDataStore</TT> (<TT>EMPTY</TT>,
     * <TT>NONEMPTYFULL</TT> or <TT>FULL</TT>)
     */
    public abstract int getState();

    /**
     * Puts a new <TT>Object</TT> into the <TT>ChannelDataStore</TT>.
     * <P>
     * <I>Pre-condition</I>: <TT>getState</TT> must not currently return <TT>FULL</TT>.
     *
     * @param value the <TT>Object</TT> to put into the <TT>ChannelDataStore</TT>
     */
    public abstract void put(Object value);

    /**
     * Returns an <TT>Object</TT> from the <TT>ChannelDataStore</TT>.
     * <P>
     * <I>Pre-condition</I>: <TT>getState</TT> must not currently return <TT>EMPTY</TT>.
     *
     * @return an <TT>Object</TT> from the <TT>ChannelDataStore</TT>
     */
    public abstract Object get();

    /**
     * Returns a new (and <TT>EMPTY</TT>) <TT>ChannelDataStore</TT> with the same
     * creation parameters as this one.
     * <P>
     * <I>Note: Only the size and structure of the </I><TT>ChannelDataStore</TT><I> should
     * be cloned, not any stored data.</I>
     *
     * @return the cloned instance of this <TT>ChannelDataStore</TT>.
     */
    public abstract Object clone();
}
