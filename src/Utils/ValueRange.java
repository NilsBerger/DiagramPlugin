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

/**
 * Hilfsklasse für Wertebereiche mit Minimum und Maximum
 * <br>
 * Typen müssen Subtyp von Number sein und Comparable erfüllen
 *
 * @author Michael Inden
 *
 * Copyright 2011 by Michael Inden
 */
public final class ValueRange<T extends Number & Comparable<T>>
{
    private final T minValue;

    private final T maxValue;

    public ValueRange(final T minValue, final T maxValue)
    {
        if (!(minValue.compareTo(maxValue) <= 0))
            throw new IllegalArgumentException("minValue " + minValue + " must be <= maxValue " + maxValue);

        this.minValue = CopyObjectUtils.copyObject(minValue);
        this.maxValue = CopyObjectUtils.copyObject(maxValue);
    }

    public final T getMinValue()
    {
        return CopyObjectUtils.copyObject(minValue);
    }

    public final T getMaxValue()
    {
        return CopyObjectUtils.copyObject(maxValue);
    }

    public boolean contains(final T value)
    {
        return (value.compareTo(minValue) >= 0 && value.compareTo(maxValue) <= 0);
    }

    @Override
    public String toString()
    {
        return "ValueRange [" + minValue + " -- " + maxValue + "]";
    }

    public String createErrorMessage(final T value)
    {
        if (contains(value))
            return "";

        return "value " + value + " not in range [" + minValue + " -- " + maxValue + "]";
    }
}