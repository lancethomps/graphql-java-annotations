package graphql.annotations;

import graphql.ExecutionResult;
import graphql.ExecutionResultImpl;
import graphql.GraphQL;
import graphql.schema.GraphQLInterfaceType;
import graphql.schema.GraphQLObjectType;
import graphql.schema.GraphQLSchema;
import graphql.schema.TypeResolver;
import org.testng.annotations.Test;

import java.util.*;

import static org.testng.AssertJUnit.assertEquals;


public class GraphQLFragmentTest {

    static Map<String, GraphQLObjectType> registry;

    @Test
    public void test() throws Exception {

        registry = new HashMap<>();

        GraphQLInterfaceType iface = (GraphQLInterfaceType) GraphQLAnnotations.iface(MyInterface.class);

        GraphQLObjectType rootType = GraphQLAnnotations.object(RootObject.class);

        GraphQLObjectType objectType2 = GraphQLAnnotations.object(MyObject2.class);

        registry.put("MyObject2", objectType2);

        GraphQLObjectType objectType = GraphQLAnnotations.object(MyObject.class);

        registry.put("MyObject", objectType);

        GraphQLSchema schema = GraphQLSchema.newSchema()
                .query(rootType)
                .build(new HashSet(Arrays.asList(iface, rootType, objectType, objectType2)));

        GraphQL graphQL2 = new GraphQL(schema);

        ExecutionResult graphQLResult = graphQL2.execute("{items { ... on MyObject {a, my {b}} ... on MyObject2 {a, b}  }}", new RootObject());
        Set resultMap = ((Map) graphQLResult.getData()).entrySet();

        assertEquals(graphQLResult.getErrors().size(), 0);
        assertEquals(resultMap.size(), 1);
    }

    public static class RootObject {
        @GraphQLField
        public List<MyInterface> getItems() {
            return Arrays.asList(new MyObject(), new MyObject2());
        }
    }

    public static class MyObject implements MyInterface {
        public String getA() {
            return "a1";
        }

        public String getB() {
            return "b1";
        }

        @GraphQLField
        public MyObject2 getMy() {
            return new MyObject2();
        }
    }

    public static class MyObject2 implements MyInterface {
        public String getA() {
            return "a2";
        }

        public String getB() {
            return "b2";
        }
    }

    @GraphQLTypeResolver(value = MyTypeResolver.class)
    public static interface MyInterface {
        @GraphQLField
        public String getA();

        @GraphQLField
        public String getB();
    }

    public static class MyTypeResolver implements TypeResolver {

        @Override
        public GraphQLObjectType getType(Object object) {
            return registry.get(object.getClass().getSimpleName());
        }
    }


}