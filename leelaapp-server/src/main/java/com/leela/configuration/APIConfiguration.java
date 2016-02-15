package com.leela.configuration;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonProperty;

import io.dropwizard.Configuration;
import io.dropwizard.db.DataSourceFactory;
import io.federecio.dropwizard.swagger.SwaggerBundleConfiguration;

public class APIConfiguration extends Configuration {
    @NotEmpty
    @JsonProperty
    private String smtpHost;

    @Min(1)
    @Max(65535)
    @JsonProperty
    private int smtpPort;

    @NotEmpty
    @JsonProperty
    private String healthCheckEmailAddress;

    @NotEmpty
    @JsonProperty
    private String password;

    @JsonProperty
    private String applicationVersionFilePath;

    @Valid
    @NotNull
    @JsonProperty("database")
    private DataSourceFactory dataSourceFactory = new DataSourceFactory();

    @JsonProperty("swagger")
    public SwaggerBundleConfiguration swaggerBundleConfiguration;

    public String getSmtpHost() {
        return smtpHost;
    }

    public int getSmtpPort() {
        return smtpPort;
    }

    public String getHealthCheckEmailAddress() {
        return healthCheckEmailAddress;
    }

    public String getPassword() {
        return password;
    }

    public String getApplicationVersionFilePath() {
        return applicationVersionFilePath;
    }

    public DataSourceFactory getDataSourceFactory() {
        return dataSourceFactory;
    }

    public void setDataSourceFactory(
            final DataSourceFactory dataSourceFactory) {
        this.dataSourceFactory = dataSourceFactory;
    }

}
