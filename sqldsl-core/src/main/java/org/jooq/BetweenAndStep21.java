/**
 * Copyright (c) 2009-2013, Lukas Eder, lukas.eder@gmail.com
 * All rights reserved.
 *
 * This software is licensed to you under the Apache License, Version 2.0
 * (the "License"); You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * . Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 *
 * . Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * . Neither the name "jOOQ" nor the names of its contributors may be
 *   used to endorse or promote products derived from this software without
 *   specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package org.jooq;

import javax.annotation.Generated;

/**
 * An intermediate DSL type for the construction of a <code>BETWEEN</code>
 * predicate.
 *
 * @author Lukas Eder
 */
@Generated("This class was generated using jOOQ-tools")
public interface BetweenAndStep21<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21> {

    /**
     * Create a condition to check this field against some bounds
     */
    @Support
    Condition and(Field<T1> maxValue1, Field<T2> maxValue2, Field<T3> maxValue3, Field<T4> maxValue4, Field<T5> maxValue5, Field<T6> maxValue6, Field<T7> maxValue7, Field<T8> maxValue8, Field<T9> maxValue9, Field<T10> maxValue10, Field<T11> maxValue11, Field<T12> maxValue12, Field<T13> maxValue13, Field<T14> maxValue14, Field<T15> maxValue15, Field<T16> maxValue16, Field<T17> maxValue17, Field<T18> maxValue18, Field<T19> maxValue19, Field<T20> maxValue20, Field<T21> maxValue21);

    /**
     * Create a condition to check this field against some bounds
     */
    @Support
    Condition and(T1 maxValue1, T2 maxValue2, T3 maxValue3, T4 maxValue4, T5 maxValue5, T6 maxValue6, T7 maxValue7, T8 maxValue8, T9 maxValue9, T10 maxValue10, T11 maxValue11, T12 maxValue12, T13 maxValue13, T14 maxValue14, T15 maxValue15, T16 maxValue16, T17 maxValue17, T18 maxValue18, T19 maxValue19, T20 maxValue20, T21 maxValue21);

    /**
     * Create a condition to check this field against some bounds
     */
    @Support
    Condition and(Row21<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21> maxValue);

    /**
     * Create a condition to check this field against some bounds
     */
    @Support
    Condition and(Record21<T1, T2, T3, T4, T5, T6, T7, T8, T9, T10, T11, T12, T13, T14, T15, T16, T17, T18, T19, T20, T21> maxValue);

}
