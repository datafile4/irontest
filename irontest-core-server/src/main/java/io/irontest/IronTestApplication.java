package io.irontest;

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.Option;
import com.jayway.jsonpath.spi.json.JacksonJsonProvider;
import com.jayway.jsonpath.spi.json.JsonProvider;
import com.jayway.jsonpath.spi.mapper.JacksonMappingProvider;
import com.jayway.jsonpath.spi.mapper.MappingProvider;
import com.roskart.dropwizard.jaxws.EndpointBuilder;
import com.roskart.dropwizard.jaxws.JAXWSBundle;
import io.dropwizard.Application;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.forms.MultiPartBundle;
import io.dropwizard.jdbi.DBIFactory;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.dropwizard.views.ViewBundle;
import io.irontest.core.assertion.AssertionVerifierFactory;
import io.irontest.db.*;
import io.irontest.resources.*;
import io.irontest.ws.ArticleSOAP;
import org.glassfish.jersey.filter.LoggingFilter;
import org.skife.jdbi.v2.DBI;

import java.util.EnumSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

/**
 * Created by Zheng on 20/06/2015.
 */
public class IronTestApplication extends Application<IronTestConfiguration> {
    public static void main(String[] args) throws Exception {
        new IronTestApplication().run(args);
    }

    private JAXWSBundle jaxWsBundle = new JAXWSBundle();

    @Override
    public String getName() {
        return "Iron Test";
    }

    @Override
    public void initialize(Bootstrap<IronTestConfiguration> bootstrap) {
        bootstrap.addBundle(new AssetsBundle("/assets/app", "/ui", "index.htm", "ui"));
        bootstrap.addBundle(new AssetsBundle("/META-INF/resources/webjars", "/ui/lib", null, "uilib"));
        bootstrap.addBundle(jaxWsBundle);
        bootstrap.addBundle(new MultiPartBundle());
        bootstrap.addBundle(new ViewBundle<IronTestConfiguration>(){
            @Override
            public Map<String, Map<String, String>> getViewConfiguration(IronTestConfiguration config) {
                return config.getViewRendererConfiguration();
            }
        });
        Configuration.setDefaults(new Configuration.Defaults() {
            private final JsonProvider jsonProvider = new JacksonJsonProvider();
            private final MappingProvider mappingProvider = new JacksonMappingProvider();

            @Override
            public JsonProvider jsonProvider() {
                return jsonProvider;
            }

            @Override
            public MappingProvider mappingProvider() {
                return mappingProvider;
            }

            @Override
            public Set<Option> options() {
                return EnumSet.noneOf(Option.class);
            }
        });
    }

    @Override
    public void run(IronTestConfiguration configuration, Environment environment) throws Exception {
        createSystemResources(configuration, environment);
        createSampleResources(configuration, environment);
    }

    private void createSystemResources(IronTestConfiguration configuration, Environment environment) {
        final DBIFactory factory = new DBIFactory();
        final DBI jdbi = factory.build(environment, configuration.getSystemDatabase(), "systemDatabase");

        //  create DAO objects
        final FolderDAO folderDAO = jdbi.onDemand(FolderDAO.class);
        final EnvironmentDAO environmentDAO = jdbi.onDemand(EnvironmentDAO.class);
        final EndpointDAO endpointDAO = jdbi.onDemand(EndpointDAO.class);
        final TestcaseDAO testcaseDAO = jdbi.onDemand(TestcaseDAO.class);
        final TestcaseRunDAO testcaseRunDAO = jdbi.onDemand(TestcaseRunDAO.class);
        testcaseRunDAO.setEnvironmentObjectMapper(environment.getObjectMapper());
        final TeststepDAO teststepDAO = jdbi.onDemand(TeststepDAO.class);
        final AssertionDAO assertionDAO = jdbi.onDemand(AssertionDAO.class);
        final UtilsDAO utilsDAO = jdbi.onDemand(UtilsDAO.class);
        final FolderTreeNodeDAO folderTreeNodeDAO = jdbi.onDemand(FolderTreeNodeDAO.class);

        //  create database tables
        //  order is important!!! (there are foreign keys linking them)
        folderDAO.createSequenceIfNotExists();
        folderDAO.createTableIfNotExists();
        folderDAO.insertARootNodeIfNotExists();
        environmentDAO.createSequenceIfNotExists();
        environmentDAO.createTableIfNotExists();
        endpointDAO.createSequenceIfNotExists();
        endpointDAO.createTableIfNotExists();
        testcaseDAO.createSequenceIfNotExists();
        testcaseDAO.createTableIfNotExists();
        testcaseRunDAO.createSequenceIfNotExists();
        testcaseRunDAO.createTableIfNotExists();
        teststepDAO.createSequenceIfNotExists();
        teststepDAO.createTableIfNotExists();
        assertionDAO.createSequenceIfNotExists();
        assertionDAO.createTableIfNotExists();

        //  register REST resources
        environment.jersey().register(new EndpointResource(endpointDAO));
        environment.jersey().register(new TestcaseResource(testcaseDAO, teststepDAO));
        environment.jersey().register(new FolderTreeNodeResource(folderTreeNodeDAO));
        environment.jersey().register(new TeststepResource(teststepDAO, utilsDAO));
        environment.jersey().register(new WSDLResource());
        environment.jersey().register(new EnvironmentResource(environmentDAO));
        environment.jersey().register(new TestcaseRunResource(testcaseDAO, teststepDAO, utilsDAO, testcaseRunDAO));

        //  register JSON services
        environment.jersey().register(new JSONService(new AssertionVerifierFactory(), endpointDAO));

        //  register jersey LoggingFilter
        environment.jersey().register(new LoggingFilter(Logger.getLogger(LoggingFilter.class.getName()), true));

        //  register exception mappers
        environment.jersey().register(new IronTestLoggingExceptionMapper());
    }

    private void createSampleResources(IronTestConfiguration configuration, Environment environment) {
        final DBIFactory factory = new DBIFactory();
        final DBI jdbi = factory.build(environment, configuration.getSampleDatabase(), "sampleDatabase");

        //  create DAO objects
        final ArticleDAO articleDAO = jdbi.onDemand(ArticleDAO.class);

        //  create database tables
        articleDAO.createTableIfNotExists();

        //  register REST resources
        environment.jersey().register(new ArticleResource(articleDAO));

        //  register SOAP web services
        jaxWsBundle.publishEndpoint(new EndpointBuilder("/article", new ArticleSOAP(articleDAO)));
    }
}
