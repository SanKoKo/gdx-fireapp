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

package mk.gdx.firebase.database.queries;

import com.badlogic.gdx.utils.Array;

import mk.gdx.firebase.database.pojos.Filter;
import mk.gdx.firebase.database.pojos.OrderByClause;
import mk.gdx.firebase.database.validators.ArgumentsValidator;
import mk.gdx.firebase.distributions.DatabaseDistribution;

/**
 * Abstraction for firebase database query.
 * <p>
 * Holds basic shared features of all queries: filtering and terminate operation after each query.
 * <p>
 * Keeps order of each query part execution inside {@link #execute()} so it will be the same for each platform.
 *
 * @param <T> Target DatabaseDistribution
 * @param <R> Query execution return type
 */
public abstract class GdxFireappQuery<T extends DatabaseDistribution, R> {

    protected T databaseDistribution;
    protected Array<Filter> filters;
    protected OrderByClause orderByClause;
    protected Array<Object> arguments;
    protected ArgumentsValidator argumentsValidator;

    public GdxFireappQuery(T databaseDistribution) {
        this.databaseDistribution = databaseDistribution;
        filters = new Array<>();
        arguments = new Array<>();
        argumentsValidator = createArgumentsValidator();
    }

    public GdxFireappQuery withArgs(Object... arguments) {
        this.arguments.addAll(arguments);
        return this;
    }

    public GdxFireappQuery with(Array<Filter> filters) {
        this.filters.addAll(filters);
        return this;
    }

    public GdxFireappQuery with(OrderByClause orderByClause) {
        this.orderByClause = orderByClause;
        return this;
    }

    public final R execute() {
        if (argumentsValidator != null)
            argumentsValidator.validate(arguments);
        prepare();
        applyFilters();
        R result = run();
        terminate();
        filters.clear();
        arguments.clear();
        return result;
    }

    protected void prepare() {
        // To overwrite
    }

    /**
     * Creates arguments validator instance.
     *
     * @return ArgumentsValidator, may be null
     */
    protected abstract ArgumentsValidator createArgumentsValidator();

    /**
     * Applies filters/order-by only if they are present.
     */
    protected abstract void applyFilters();

    /**
     * Do some operations on Firebase Database.
     * <p>
     * Only flow here, every validation should be in {@link #prepare()}.
     *
     * @return Results of query execution
     */
    protected abstract R run();

    /**
     * Terminates query.
     * <p>
     * It is means clear all artifacts created by this query execution so next execution will be run with clear state.
     */
    protected abstract void terminate();
}