/*
 * Copyright (C) 2012 Mobs and Geeks
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file 
 * except in compliance with the License. You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the 
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, 
 * either express or implied. See the License for the specific language governing permissions and 
 * limitations under the License.
 */

package com.app.gmv3.utilities.validation.annotation;


import com.app.gmv3.utilities.validation.Rules;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Specifies constraints such as min length, max length and trim on textual data contained
 * in {@code View}s.
 *
 * @author Ragunath Jawahar <rj@mobsandgeeks.com>
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface TextRule {
    int order();
    int minLength()      default 0;
    int maxLength()      default Integer.MAX_VALUE;
    boolean trim()       default true;
    String message()     default Rules.EMPTY_STRING;
    int messageResId()   default 0;
}