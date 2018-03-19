/*
 * Copyright 2017 mk
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package mk.gdx.firebase.ios.database.providers;

import com.google.firebasedatabase.FIRDatabaseQuery;

import mk.gdx.firebase.database.FilterResolver;
import mk.gdx.firebase.database.FilterType;

/**
 * Provides filtering for {@code DatabaseReference} instance.
 */
public class FIRQueryFilterResolver implements FilterResolver<FIRDatabaseQuery, FIRDatabaseQuery>
{

    public static final String WRONG_ARGUMENT_TYPE = "Wrong argument type. Available type is: Integer.";
    public static final String WRONG_ARGUMENT_TYPE2 = "Wrong argument type. Available types are: String, Boolean, Double.";
    public static final String MISSING_FILTER_ARGUMENTS = "Missing filter arguments.";


    @Override
    public <V> FIRDatabaseQuery resolve(FilterType filterType, FIRDatabaseQuery target, V[] filterArguments)
    {
        switch (filterType) {
            case LIMIT_FIRST:
                if (!(filterArguments[0] instanceof Integer))
                    throw new IllegalArgumentException(WRONG_ARGUMENT_TYPE);
                return target.queryLimitedToFirst((Long) filterArguments[0]);
            case LIMIT_LAST:
                if (!(filterArguments[0] instanceof Integer))
                    throw new IllegalArgumentException(WRONG_ARGUMENT_TYPE);
                return target.queryLimitedToLast((Long) filterArguments[0]);
            case START_AT:
                if (filterArguments[0] instanceof Double) {
                    return target.queryStartingAtValue((Double) filterArguments[0]);
                } else if (filterArguments[0] instanceof Boolean) {
                    return target.queryStartingAtValue((Boolean) filterArguments[0]);
                } else if (filterArguments[0] instanceof String) {
                    return target.queryStartingAtValue((String) filterArguments[0]);
                } else {
                    throw new IllegalArgumentException(WRONG_ARGUMENT_TYPE2);
                }
            case END_AT:
                if (filterArguments[0] instanceof Double) {
                    return target.queryEndingAtValue((Double) filterArguments[0]);
                } else if (filterArguments[0] instanceof Boolean) {
                    return target.queryEndingAtValue((Boolean) filterArguments[0]);
                } else if (filterArguments[0] instanceof String) {
                    return target.queryEndingAtValue((String) filterArguments[0]);
                } else {
                    throw new IllegalArgumentException(WRONG_ARGUMENT_TYPE2);
                }
            case EQUAL_TO:
                if (filterArguments[0] instanceof Double) {
                    return target.queryEqualToValue((Double) filterArguments[0]);
                } else if (filterArguments[0] instanceof Boolean) {
                    return target.queryEqualToValue((Boolean) filterArguments[0]);
                } else if (filterArguments[0] instanceof String) {
                    return target.queryEqualToValue((String) filterArguments[0]);
                } else {
                    throw new IllegalArgumentException(WRONG_ARGUMENT_TYPE2);
                }
            default:
                throw new IllegalStateException();
        }
    }
}