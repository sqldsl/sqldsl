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
package org.jooq.util.example;

import org.jooq.util.DefaultGeneratorStrategy;
import org.jooq.util.Definition;

/**
 * A generator strategy that prefixes / suffixes class names and other artefacts
 * with values taken from JVM parameters.
 * <p>
 * An example variant of the default naming strategy
 * {@link DefaultGeneratorStrategy} adding the prefix and suffixes to generated
 * class names (e.g. to avoid name clashes with existing JPA entities and such).
 * <p>
 * The following JVM parameters are supported:
 * <ul>
 * <li><code>org.jooq.util.example.java-identifier-prefix</code></li>
 * <li><code>org.jooq.util.example.java-identifier-suffix</code></li>
 * <li><code>org.jooq.util.example.java-getter-name-prefix</code></li>
 * <li><code>org.jooq.util.example.java-getter-name-suffix</code></li>
 * <li><code>org.jooq.util.example.java-setter-name-prefix</code></li>
 * <li><code>org.jooq.util.example.java-setter-name-suffix</code></li>
 * <li><code>org.jooq.util.example.java-method-name-prefix</code></li>
 * <li><code>org.jooq.util.example.java-method-name-suffix</code></li>
 * <li><code>org.jooq.util.example.java-class-name-prefix</code></li>
 * <li><code>org.jooq.util.example.java-class-name-suffix</code></li>
 * <li><code>org.jooq.util.example.java-package-name-prefix</code></li>
 * <li><code>org.jooq.util.example.java-package-name-suffix</code></li>
 * <li><code>org.jooq.util.example.java-member-name-prefix</code></li>
 * <li><code>org.jooq.util.example.java-member-name-suffix</code></li>
 * <p>
 * This strategy is to be understood as a working example, not part of the code
 * generation library. It may be modified / adapted in the future. Use at your
 * own risk.
 *
 * @author Lukas Eder
 */
public class JVMArgsGeneratorStrategy extends DefaultGeneratorStrategy {

    @Override
    public String getJavaIdentifier(Definition definition) {
        return System.getProperty("org.jooq.util.example.java-identifier-prefix", "")
            + super.getJavaIdentifier(definition)
            + System.getProperty("org.jooq.util.example.java-identifier-suffix", "");
    }

    @Override
    public String getJavaSetterName(Definition definition, Mode mode) {
        return System.getProperty("org.jooq.util.example.java-setter-name-prefix", "")
            + super.getJavaSetterName(definition, mode)
            + System.getProperty("org.jooq.util.example.java-setter-name-suffix", "");
    }

    @Override
    public String getJavaGetterName(Definition definition, Mode mode) {
        return System.getProperty("org.jooq.util.example.java-getter-name-prefix", "")
            + super.getJavaGetterName(definition, mode)
            + System.getProperty("org.jooq.util.example.java-getter-name-suffix", "");
    }

    @Override
    public String getJavaMethodName(Definition definition, Mode mode) {
        return System.getProperty("org.jooq.util.example.java-method-name-prefix", "")
            + super.getJavaMethodName(definition, mode)
            + System.getProperty("org.jooq.util.example.java-method-name-suffix", "");
    }

    @Override
    public String getJavaClassName(Definition definition, Mode mode) {
        return System.getProperty("org.jooq.util.example.java-class-name-prefix", "")
            + super.getJavaClassName(definition, mode)
            + System.getProperty("org.jooq.util.example.java-class-name-suffix", "");
    }

    @Override
    public String getJavaPackageName(Definition definition, Mode mode) {
        return System.getProperty("org.jooq.util.example.java-package-name-prefix", "")
            + super.getJavaPackageName(definition, mode)
            + System.getProperty("org.jooq.util.example.java-package-name-suffix", "");
    }

    @Override
    public String getJavaMemberName(Definition definition, Mode mode) {
        return System.getProperty("org.jooq.util.example.java-member-name-prefix", "")
            + super.getJavaMemberName(definition, mode)
            + System.getProperty("org.jooq.util.example.java-member-name-suffix", "");
    }
}
