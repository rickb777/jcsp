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

package org.jcsp.demos.util;

/**
 * @author Quickstone Technologies Limited
 */
public class ComplexDouble implements Cloneable {

    public ComplexDouble(double d, double d1) {
        real = d;
        imag = d1;
    }

    public ComplexDouble add(ComplexDouble complexdouble) {
        real += complexdouble.real;
        imag += complexdouble.imag;
        return this;
    }

    public ComplexDouble addImag(double d) {
        imag += d;
        return this;
    }

    public ComplexDouble addReal(double d) {
        real += d;
        return this;
    }

    public Object clone() {
        Object obj = null;
        try {
            obj = super.clone();
        } catch (CloneNotSupportedException clonenotsupportedexception) {
            System.out.println(
                String.valueOf(clonenotsupportedexception)
                    + " -- can't be happening !!!");
        }
        return obj;
    }

    public ComplexDouble div(ComplexDouble complexdouble) {
        double d =
            complexdouble.real * complexdouble.real
                + complexdouble.imag * complexdouble.imag;
        double d1 = (real * complexdouble.real + imag * complexdouble.imag) / d;
        imag = (imag * complexdouble.real - real * complexdouble.imag) / d;
        real = d1;
        return this;
    }

    public double getImag() {
        return imag;
    }

    public double getReal() {
        return real;
    }

    public double modulus() {
        return Math.sqrt(real * real + imag * imag);
    }

    public double modulusSquared() {
        return real * real + imag * imag;
    }

    public ComplexDouble mult(ComplexDouble complexdouble) {
        double d = real * complexdouble.real - imag * complexdouble.imag;
        imag = imag * complexdouble.real + real * complexdouble.imag;
        real = d;
        return this;
    }

    public ComplexDouble scale(double d) {
        real *= d;
        imag *= d;
        return this;
    }

    public ComplexDouble set(double d, double d1) {
        real = d;
        imag = d1;
        return this;
    }

    public ComplexDouble set(ComplexDouble complexdouble) {
        real = complexdouble.real;
        imag = complexdouble.imag;
        return this;
    }

    public ComplexDouble setImag(double d) {
        imag = d;
        return this;
    }

    public ComplexDouble setReal(double d) {
        real = d;
        return this;
    }

    public ComplexDouble sub(ComplexDouble complexdouble) {
        real -= complexdouble.real;
        imag -= complexdouble.imag;
        return this;
    }

    private double real;
    private double imag;
}
