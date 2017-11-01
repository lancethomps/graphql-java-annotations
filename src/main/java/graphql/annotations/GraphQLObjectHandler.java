/**
 * Copyright 2016 Yurii Rashkovskii
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
 */
package graphql.annotations;

import graphql.annotations.searchAlgorithms.CannotCastMemberException;
import graphql.annotations.util.GraphQLOutputObjectRetriever;
import graphql.schema.GraphQLObjectType;
import graphql.schema.GraphQLOutputType;

public class GraphQLObjectHandler {

    private GraphQLOutputObjectRetriever outputObjectRetriever;

    public GraphQLObjectHandler(GraphQLOutputObjectRetriever outputObjectRetriever) {
        this.outputObjectRetriever = outputObjectRetriever;
    }

    public GraphQLObjectHandler() {
        this(new GraphQLOutputObjectRetriever());
    }

    public GraphQLObjectType getObject(Class<?> object, ProcessingElementsContainer container) throws GraphQLAnnotationsException, CannotCastMemberException {
        GraphQLOutputType type = outputObjectRetriever.getOutputType(object, container);
        if (type instanceof GraphQLObjectType) {
            return (GraphQLObjectType) type;
        } else {
            throw new IllegalArgumentException("Object resolve to a " + type.getClass().getSimpleName());
        }
    }
}
