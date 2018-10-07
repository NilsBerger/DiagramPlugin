/*
 * Copyright 1998-2018 Konstantin Bulenkov
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package Utils;

public final class HashUtils
{
    public static final int PRIME = 31;

    private HashUtils()
    {
    }

    public static final int calcHashCode(final int hash, final boolean input)
    {
        return PRIME * hash + (input ? 1 : 0);
    }

    public static final int calcHashCode(final int hash, final int input)
    {
        return PRIME * hash + input;
    }

    public static final int calcHashCode(final int hash, final long input)
    {
        return PRIME * hash + (int) (input ^ (input >>> 32));
    }

    public static final int calcHashCode(final int hash, final float input)
    {
        return calcHashCode(hash, Float.floatToIntBits(input));
    }

    public static final int calcHashCode(final int hash, final double input)
    {
        return calcHashCode(hash, Double.doubleToLongBits(input));
    }

    public static final int calcHashCode(final int hash, final Object input)
    {
        return (input == null) ? 0 : PRIME * hash + input.hashCode();
    }
}