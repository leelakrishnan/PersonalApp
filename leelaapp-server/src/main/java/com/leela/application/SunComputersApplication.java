package com.leela.application;

import java.util.Set;

import javax.persistence.Entity;

import org.hibernate.SessionFactory;
import org.reflections.Reflections;

import com.google.common.collect.ImmutableList;
import com.leela.configuration.APIConfiguration;
import com.leela.mail.SendEmailService;
import com.leela.resource.BrandResource;
import com.leela.resource.BusinessTypeResource;
import com.leela.resource.CommonResource;
import com.leela.resource.CompanyResource;
import com.leela.resource.ForgotPasswordResource;
import com.leela.resource.FrontEndUserRegistrationResource;
import com.leela.resource.FrontEndUserVerificationResource;
import com.leela.resource.OrderResource;
import com.leela.resource.OrderStatusResource;
import com.leela.resource.OrderSubTypeResource;
import com.leela.resource.OrderTypeResource;
import com.leela.resource.PaymentTypeResource;
import com.leela.resource.ProductCategoryResource;
import com.leela.resource.ProductCategorySpecificationResource;
import com.leela.resource.ProductResource;
import com.leela.resource.ProductSpecificationGroupResource;
import com.leela.resource.ProductSpecificationResource;
import com.leela.resource.ProductSubCategoryResource;
import com.leela.resource.ProductSubTypeResource;
import com.leela.resource.ProductTypeResource;
import com.leela.resource.PubnubResource;
import com.leela.resource.ShippingOptionResource;
import com.leela.resource.SupplierResource;
import com.leela.resource.UserResource;
import com.leela.resource.UserStatusResource;
import com.leela.resource.UserTypeResource;

import io.dropwizard.Application;
import io.dropwizard.db.PooledDataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.hibernate.SessionFactoryFactory;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.federecio.dropwizard.swagger.SwaggerBundle;
import io.federecio.dropwizard.swagger.SwaggerBundleConfiguration;

public class SunComputersApplication extends Application<APIConfiguration> {

    private static final String ENTITY_PACKAGE = "com.leela.entity";

    protected final HibernateBundle<APIConfiguration> hibernate = new HibernateBundle<APIConfiguration>(
            getEntities(), new SessionFactoryFactory()) {
        @Override
        public PooledDataSourceFactory getDataSourceFactory(
                final APIConfiguration configuration) {

            return configuration.getDataSourceFactory();
        }
    };

    private final SwaggerBundle<APIConfiguration> swagger = new SwaggerBundle<APIConfiguration>() {

        @Override
        protected SwaggerBundleConfiguration getSwaggerBundleConfiguration(
                final APIConfiguration configuration) {

            return configuration.swaggerBundleConfiguration;
        }
    };

    @Override
    public void initialize(final Bootstrap<APIConfiguration> bootstrap) {
        bootstrap.addBundle(hibernate);
        bootstrap.addBundle(swagger);

    }

    public static void main(final String[] args) throws Exception {
        new SunComputersApplication().run(args);
    }

    public ImmutableList<Class<?>> getEntities() {

        final Reflections reflections = new Reflections(ENTITY_PACKAGE);
        final Set<Class<? extends Object>> allClasses = reflections
                .getTypesAnnotatedWith(Entity.class);
        return ImmutableList.copyOf(allClasses);

    }

    @Override
    public void run(final APIConfiguration configuration,
            final Environment environment) throws Exception {
        final SendEmailService emailer = new SendEmailService(configuration);

        final SessionFactory sessionFactory = this.hibernate
                .getSessionFactory();

        environment.jersey().register(new CommonResource(sessionFactory));
        environment.jersey().register(
                new FrontEndUserRegistrationResource(sessionFactory, emailer));
        environment.jersey()
                .register(new FrontEndUserVerificationResource(sessionFactory));
        environment.jersey()
                .register(new ForgotPasswordResource(sessionFactory, emailer));
        environment.jersey().register(new UserStatusResource(sessionFactory));
        environment.jersey().register(new UserTypeResource(sessionFactory));
        environment.jersey().register(new BusinessTypeResource(sessionFactory));
        environment.jersey().register(new OrderStatusResource(sessionFactory));
        environment.jersey().register(new OrderTypeResource(sessionFactory));
        environment.jersey().register(new OrderSubTypeResource(sessionFactory));
        environment.jersey().register(new BrandResource(sessionFactory));
        environment.jersey()
                .register(new ProductSpecificationResource(sessionFactory));
        environment.jersey().register(
                new ProductSpecificationGroupResource(sessionFactory));
        environment.jersey()
                .register(new ProductCategoryResource(sessionFactory));
        environment.jersey()
                .register(new ProductSubCategoryResource(sessionFactory));
        environment.jersey().register(new ProductTypeResource(sessionFactory));
        environment.jersey()
                .register(new ProductSubTypeResource(sessionFactory));
        environment.jersey().register(new ProductResource(sessionFactory));
        environment.jersey().register(new SupplierResource(sessionFactory));
        environment.jersey().register(
                new ProductCategorySpecificationResource(sessionFactory));
        environment.jersey()
                .register(new ShippingOptionResource(sessionFactory));
        environment.jersey().register(new PaymentTypeResource(sessionFactory));
        environment.jersey().register(new OrderResource(sessionFactory));
        environment.jersey().register(new CompanyResource(sessionFactory));
        environment.jersey().register(new UserResource(sessionFactory));
        environment.jersey().register(new PubnubResource());

    }

}
