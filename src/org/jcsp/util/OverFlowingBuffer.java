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

package org.jcsp.util;

import java.io.Serializable;

/**
 * This is used to create a buffered object channel that always accepts input,
 * discarding its last entered data if full.
 * <H2>Description</H2>
 * It is an implementation of <TT>ChannelDataStore</TT> that yields
 * a <I>FIFO</I> buffered semantics for a channel.  When empty, the channel blocks readers.
 * When full, a writer will be accepted but the written value <I>overflows</I> the buffer
 * and is lost to the channel.
 * See the static
 * {@link org.jcsp.lang.Channel#createOne2One(org.jcsp.util.ChannelDataStore) <TT>create</TT>}
 * methods of {@link org.jcsp.lang.Channel} etc.
 * <P>
 * The <TT>getState</TT> method returns <TT>EMPTY</TT> or <TT>NONEMPTYFULL</TT>, but
 * never <TT>FULL</TT>.
 *
 * @see org.jcsp.util.ZeroBuffer
 * @see org.jcsp.util.Buffer
 * @see org.jcsp.util.OverWriteOldestBuffer
 * @see org.jcsp.util.OverWritingBuffer
 * @see org.jcsp.util.InfiniteBuffer
 * @see org.jcsp.lang.Channel
 *
 * @author P.D.Austin
 */

public class OverFlowingBuffer implements ChannelDataStore, Serializable {
    /** The storage for the buffered Objects */
    private final Object[] buffer;

    /** The number of Objects stored in the Buffer */
    private int counter = 0;

    /** The index of the oldest element (when  counter > 0) */
    private int firstIndex = 0;

    /** The index of the next free element (when  counter < buffer.length) */
    private int lastIndex = 0;

    /**
     * Construct a new <TT>OverFlowingBuffer</TT> with the specified size.
     *
     * @param size the number of Objects the OverFlowingBuffer can store.
     * @throws BufferSizeError if <TT>size</TT> is zero or negative.  Note: no action
     * should be taken to <TT>try</TT>/<TT>catch</TT> this exception
     * - application code generating it is in error and needs correcting.
     */
    public OverFlowingBuffer(int size) {
        if (size <= 0) {
            throw new BufferSizeError(
                    "\n*** Attempt to create an overflowing buffered channel with negative or zero capacity"
                    );
        }
        buffer = new Object[size];
    }

    /**
     * Returns the oldest <TT>Object</TT> from the <TT>OverFlowingBuffer</TT> and removes it.
     * <P>
     * <I>Pre-condition</I>: <TT>getState</TT> must not currently return <TT>EMPTY</TT>.
     *
     * @return the oldest <TT>Object</TT> from the <TT>OverFlowingBuffer</TT>
     */
    public Object get() {
        Object value = buffer[firstIndex];
        buffer[firstIndex] = null;
        firstIndex = (firstIndex + 1) % buffer.length;
        counter--;
        return value;
    }
    
    /**
     * Returns the oldest object from the buffer but does not remove it.
     * 
     * <I>Pre-condition</I>: <TT>getState</TT> must not currently return <TT>EMPTY</TT>.
     *
     * @return the oldest <TT>Object</TT> from the <TT>Buffer</TT>
     */
    public Object startGet()
    {
      return buffer[firstIndex];
    }
    
    /**
     * Removes the oldest object from the buffer.     
     */
    public void endGet()
    {
      buffer[firstIndex] = null;
      firstIndex = (firstIndex + 1) % buffer.length;
      counter--;
    }

    /**
     * Puts a new <TT>Object</TT> into the <TT>OverFlowingBuffer</TT>.
     * <P>
     * If <TT>OverFlowingBuffer</TT> is full, the item is discarded.
     *
     * @param value the Object to put into the OverFlowingBuffer
     */
    public void put(Object value) {
        if (counter < buffer.length) {
            buffer[lastIndex] = value;
            lastIndex = (lastIndex + 1) % buffer.length;
            counter++;
        }
    }

    /**
     * Returns the current state of the <TT>OverFlowingBuffer</TT>.
     *
     * @return the current state of the <TT>OverFlowingBuffer</TT> (<TT>EMPTY</TT> or
     * <TT>NONEMPTYFULL</TT>)
     */
    public int getState()
    {
        if (counter == 0)
            return EMPTY;
        else
            return NONEMPTYFULL;
    }

    /**
     * Returns a new (and <TT>EMPTY</TT>) <TT>OverFlowingBuffer</TT> with the same
     * creation parameters as this one.
     * <P>
     * <I>Note: Only the size and structure of the </I><TT>OverFlowingBuffer</TT><I> is
     * cloned, not any stored data.</I>
     *
     * @return the cloned instance of this <TT>OverFlowingBuffer</TT>.
     */
    public Object clone()
    {
        return new OverFlowingBuffer(buffer.length);
    }
    
    public void removeAll()
    {
        counter = 0;
        firstIndex = 0;
        lastIndex = 0;
        
        for (int i = 0;i < buffer.length;i++) {
        	//Null the objects so they can be garbage collected:
        	buffer[i] = null;
        }
    }
}
