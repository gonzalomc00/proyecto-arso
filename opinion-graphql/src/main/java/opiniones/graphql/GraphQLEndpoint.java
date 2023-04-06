package opiniones.graphql;

import javax.servlet.annotation.WebServlet;

import com.coxautodev.graphql.tools.SchemaParser;

import graphql.schema.GraphQLSchema;
import graphql.servlet.SimpleGraphQLServlet;

@SuppressWarnings("serial")
@WebServlet(urlPatterns = "/graphql")
public class GraphQLEndpoint extends SimpleGraphQLServlet {

	public GraphQLEndpoint() {
        super(buildSchema());
    }

    private static GraphQLSchema buildSchema() {
      
        return SchemaParser.newParser()
                .file("schema.graphqls") //indica cual es el esquema
                .resolvers(new Query(), new Mutation()) // resolver -> clases que saben implementar el esquema
                .build()
                .makeExecutableSchema();
    }
}