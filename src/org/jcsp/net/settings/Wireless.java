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

package org.jcsp.net.settings;

/**
 * Used internally within the JCSP network infrastructure to represent a wireless protocol.
 *
 * @author Quickstone Technologies Limited
 */
public class Wireless extends Spec implements Req
{
   Wireless(int wireless, boolean isReq)
   {
      super(SPEC_NAME_WIRELESS, true, isReq);
      if (wireless > 0)
         this.wireless = 1;
      else if (wireless < 0)
         this.wireless = -1;
      else
         this.wireless = 0;
   }
   
   public String getStringValue()
   {
      return "" + wireless;
   }
   
   public int getValue()
   {
      return wireless;
   }
   
   public Class getType()
   {
      return Boolean.TYPE;
   }
   
   public String getComparator()
   {
      return REQ_COMPARATOR_EQUALS;
   }
   
   public int getIntValue()
   {
      return getValue();
   }
   
   public double getDoubleValue()
   {
      throw new UnsupportedOperationException("Type is int");
   }
   
   public boolean getBooleanValue()
   {
      throw new UnsupportedOperationException("Type is int");
   }
   
   public boolean equals(Object o)
   {
      if(o instanceof Wireless)
      {
         Wireless other = (Wireless) o;
         return wireless == other.wireless;
      }
      return false;
   }
   
   public int hashCode()
   {
      return wireless;
   }
   
   private int wireless;
}