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
 * Used internally within the JCSP network infrastructure to represent a maximum speed.
 *
 * @author Quickstone Technologies Limited
 */
public class MaxSpeed extends Spec implements XMLConfigConstants
{
   MaxSpeed(int maxSpeed)
   {
      super(SPEC_NAME_MAXSPEED, true);
      this.maxSpeed = maxSpeed;
   }
   
   public String getStringValue()
   {
      return "" + maxSpeed;
   }
   
   public boolean equals(Object o)
   {
      if(o instanceof MaxSpeed)
      {
         MaxSpeed other = (MaxSpeed) o;
         return maxSpeed == other.maxSpeed;
      }
      return false;
   }
   
   public int hashCode()
   {
      return maxSpeed;
   }
   
   public int getValue()
   {
      return maxSpeed;
   }
   private int maxSpeed;
}