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

package org.jcsp.util.ints;

import java.io.Serializable;

/**
 * This is used to create a buffered integer channel that always accepts input,
 * discarding its last entered data if full.
 * <H2>Description</H2>
 * <TT>OverFlowingBufferInt</TT> is an implementation of <TT>ChannelDataStoreInt</TT> that yields
 * a <I>FIFO</I> buffered semantics for a channel.  When empty, the channel blocks readers.
 * When full, a writer will be accepted but the written value <I>overflows</I> the buffer
 * and is lost to the channel.
 * See the static
 * {@link org.jcsp.lang.ChannelInt#createOne2One(org.jcsp.util.ints.ChannelDataStoreInt)
 * <TT>create</TT>} methods of {@link org.jcsp.lang.ChannelInt} etc.
 * <P>
 * The <TT>getState</TT> method returns <TT>EMPTY</TT> or <TT>NONEMPTYFULL</TT>, but
 * never <TT>FULL</TT>.
 *
 * @see org.jcsp.util.ints.ZeroBufferInt
 * @see org.jcsp.util.ints.BufferInt
 * @see org.jcsp.util.ints.OverWriteOldestBufferInt
 * @see org.jcsp.util.ints.OverWritingBufferInt
 * @see org.jcsp.util.ints.InfiniteBufferInt
 * @see org.jcsp.lang.ChannelInt
 *
 * @author P.D.Austin
 */

public class OverFlowingBufferInt implements ChannelDataStoreInt, Serializable
{
    /** The storage for the buffered ints */
    private final int[] buffer;

    /** The number of ints stored in the Buffer */
    private int counter = 0;

    /** The index of the oldest element (when counter > 0) */
    private int firstIndex = 0;

    /** The index of the next free element (when counter < buffer.length) */
    private int lastIndex = 0;

    /**
     * Construct a new <TT>OverFlowingBufferInt</TT> with the specified size.
     *
     * @param size the number of <TT>int</TT>s the <TT>OverFlowingBufferInt</TT> can store.
     * @throws BufferIntSizeError if <TT>size</TT> is zero or negative.  Note: no action
     * should be taken to <TT>try</TT>/<TT>catch</TT> this exception
     * - application code generating it is in error and needs correcting.
     */
    public OverFlowingBufferInt(int size)
    {
        if (size <= 0)
            throw new BufferIntSizeError
                    ("\n*** Attempt to create an overflowing buffered channel with negative or zero capacity");
        buffer = new int[size];
    }

    /**
     * Returns the oldest <TT>int</TT> from the <TT>OverFlowingBufferInt</TT> and removes it.
     * <P>
     * <I>Pre-condition</I>: <TT>getState</TT> must not currently return <TT>EMPTY</TT>.
     *
     * @return the oldest <TT>int</TT> from the <TT>OverFlowingBufferInt</TT>
     */
    public int get()
    {
        int value = buffer[firstIndex];
        firstIndex = (firstIndex + 1) % buffer.length;
        counter--;
        return value;
    }
    
    /**
     * Returns the oldest integer from the buffer but does not remove it.
     * 
     * <I>Pre-condition</I>: <TT>getState</TT> must not currently return <TT>EMPTY</TT>.
     *
     * @return the oldest <TT>int</TT> from the <TT>Buffer</TT>
     */
    public int startGet()
    {
      return buffer[firstIndex];
    }
    
    /**
     * Removes the oldest integer from the buffer.     
     */
    public void endGet()
    {      
      firstIndex = (firstIndex + 1) % buffer.length;
      counter--;
    }

    /**
     * Puts a new <TT>int</TT> into the <TT>OverFlowingBufferInt</TT>.
     * <P>
     * If <TT>OverFlowingBufferInt</TT> is full, the item is discarded.
     *
     * @param value the <TT>int</TT> to put into the <TT>OverFlowingBufferInt</TT>
     */
    public void put(int value)
    {
        if (counter < buffer.length)
        {
            buffer[lastIndex] = value;
            lastIndex = (lastIndex + 1) % buffer.length;
            counter++;
        }
    }

    /**
     * Returns the current state of the <TT>OverFlowingBufferInt</TT>.
     *
     * @return the current state of the <TT>OverFlowingBufferInt</TT> (<TT>EMPTY</TT> or
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
     * Returns a new (and <TT>EMPTY</TT>) <TT>OverFlowingBufferInt</TT> with the same
     * creation parameters as this one.
     * <P>
     * <I>Note: Only the size and structure of the </I><TT>OverFlowingBufferInt</TT><I> is
     * cloned, not any stored data.</I>
     *
     * @return the cloned instance of this <TT>OverFlowingBufferInt</TT>.
     */
    public Object clone()
    {
        return new OverFlowingBufferInt(buffer.length);
    }
    
    public void removeAll() {
  	  counter = 0;
  	  firstIndex = 0;
  	  lastIndex = 0;
    }
}
