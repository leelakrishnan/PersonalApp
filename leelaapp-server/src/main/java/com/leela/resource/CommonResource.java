package com.leela.resource;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.hibernate.SessionFactory;

import com.leela.dao.GenericDAO;
import com.leela.dao.HibernateDAO;
import com.leela.dao.QueryOptions;
import com.leela.entity.Company;
import com.leela.entity.CompanyCountry;
import com.leela.entity.CompanyCurrency;
import com.leela.entity.Country;
import com.leela.entity.FailedLoginLog;
import com.leela.entity.RequestUrlInfo;
import com.leela.entity.User;
import com.leela.model.ObjectModel;
import com.leela.model.UpdateStatusModel;
import com.leela.util.CommonsUtil;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;

import io.dropwizard.hibernate.UnitOfWork;

@Path("/common")
@Api
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CommonResource {

    private final GenericDAO dao;

    public CommonResource(final SessionFactory sessionFactory) {
        this.dao = new HibernateDAO(sessionFactory);
    }

    @GET
    @Path("/getContryList")
    @ApiOperation(value = "Get country list")
    @UnitOfWork
    public Response listCountry() {
        final Map<String, Object> options = new HashMap<String, Object>();

        @SuppressWarnings("unchecked")
        final List<Object[]> resultList = dao.findByNamedQuery(
                "get.all.countries", new QueryOptions(options));
        final List<ObjectModel> objectModels = CommonsUtil
                .convertToObjectModel(resultList);
        return Response.status(200).entity(objectModels).build();
    }

    @GET
    @Path("/getStateListByCountryId/{countryId}")
    @ApiOperation(value = "Get state list by country")
    @UnitOfWork
    public Response listStateByCountry(
            @PathParam("countryId") final Long countryId) {
        final Map<String, Object> options = new HashMap<String, Object>();
        options.put("countryId", countryId);

        @SuppressWarnings("unchecked")
        final List<Object[]> resultList = dao.findByNamedQuery(
                "get.states.by.country.id", new QueryOptions(options));
        final List<ObjectModel> objectModels = CommonsUtil
                .convertToObjectModel(resultList);
        return Response.status(200).entity(objectModels).build();
    }

    @GET
    @Path("/getCityListByStateId/{stateId}")
    @ApiOperation(value = "Get city list by state")
    @UnitOfWork
    public Response listCityByState(@PathParam("stateId") final Long stateId) {
        final Map<String, Object> options = new HashMap<String, Object>();
        options.put("stateId", stateId);

        @SuppressWarnings("unchecked")
        final List<Object[]> resultList = dao.findByNamedQuery(
                "get.cities.by.state.id", new QueryOptions(options));
        final List<ObjectModel> objectModels = CommonsUtil
                .convertToObjectModel(resultList);
        return Response.status(200).entity(objectModels).build();
    }

    @GET
    @Path("/getAciveCountryByCompany/{companyId}")
    @ApiOperation(value = "Get all country list by company")
    @UnitOfWork
    public Response getAciveCountryByCompany(
            @PathParam("companyId") final Long companyId) {
        final String query = "SELECT c.country_id, c.country_name, "
                + "CASE WHEN cc.status THEN '1' ELSE '0' "
                + "END AS `status`, cc.company_country_id FROM "
                + "countries c LEFT JOIN company_countries cc "
                + "ON cc.country_id = c.country_id "
                + "AND cc.company_id = :companyId WHERE c.status = 1 ";

        final Map<String, Object> options = new HashMap<String, Object>();
        options.put("companyId", companyId);

        @SuppressWarnings("unchecked")
        final List<Object[]> resultList = dao.findByNativeQuery(query,
                new QueryOptions(options));
        final List<ObjectModel> objectModels = CommonsUtil
                .convertToObjectModelWithStatusAndSubObjectId(resultList);
        return Response.status(200).entity(objectModels).build();
    }

    @POST
    @Path("/saveCompanyCountry")
    @ApiOperation(value = "Save new company country status")
    @UnitOfWork
    public Response saveCompanyCountry(final UpdateStatusModel model) {

        final User user = new User();
        user.setId(model.getUserId());

        final Company company = new Company();
        company.setId(model.getCompanyId());

        final Country country = new Country();
        country.setId(model.getSubObjectId());

        final CompanyCountry companyCountry = new CompanyCountry();
        companyCountry.setCreatedDate(new Date());
        companyCountry.setCreatedBy(user);
        companyCountry.setStatus(model.getStatus());
        companyCountry.setCompany(company);
        companyCountry.setCountry(country);
        dao.create(companyCountry);

        return Response.status(200).entity(companyCountry.getId()).build();
    }

    @PUT
    @Path("/updateCompanyCountry/{companyCountryId}")
    @ApiOperation(value = "Update company country status")
    @UnitOfWork
    public Response updateCompanyCountry(
            @PathParam("companyCountryId") final Long companyCountryId,
            final UpdateStatusModel model) {

        final User user = new User();
        user.setId(model.getUserId());

        final CompanyCountry companyCountry = new CompanyCountry();
        companyCountry.setId(companyCountryId);
        dao.load(companyCountry);

        companyCountry.setUpdatedDate(new Date());
        companyCountry.setUpdatedBy(user);
        companyCountry.setStatus(model.getStatus());
        dao.update(companyCountry);

        return Response.status(200).entity(companyCountry.getId()).build();
    }

    @GET
    @Path("/getContryListByCompany/{companyId}")
    @ApiOperation(value = "Get country list")
    @UnitOfWork
    public Response getContryListByCompany(
            @PathParam("companyId") final Long companyId) {
        final Map<String, Object> options = new HashMap<String, Object>();
        options.put("companyId", companyId);

        @SuppressWarnings("unchecked")
        final List<Object[]> resultList = dao.findByNamedQuery(
                "get.active.countries.by.company", new QueryOptions(options));
        final List<ObjectModel> objectModels = CommonsUtil
                .convertToObjectModel(resultList);
        return Response.status(200).entity(objectModels).build();
    }

    @POST
    @Path("/saveRequestUrlInfo")
    @ApiOperation(value = "Save request url log")
    @UnitOfWork
    public Response saveRequestUrlInfo(final RequestUrlInfo model) {
        dao.create(model);
        return Response.status(200).entity(model.getId()).build();
    }

    @POST
    @Path("/saveFailedLoginLog")
    @ApiOperation(value = "Save failed login log")
    @UnitOfWork
    public Response saveFailedLoginLog(final FailedLoginLog model) {
        dao.create(model);
        return Response.status(200).entity(model.getId()).build();
    }

    @GET
    @Path("/getAciveCurrencyByCompany/{companyId}")
    @ApiOperation(value = "Get all currency list by company")
    @UnitOfWork
    public Response getAciveCurrencyByCompany(
            @PathParam("companyId") final Long companyId) {
        final String query = "SELECT c.country_id, c.Currency_desc, "
                + "CASE WHEN cc.status THEN '1' ELSE '0' "
                + "END AS `status`, cc.company_currency_id FROM "
                + "countries c LEFT JOIN company_currencies cc "
                + "ON cc.country_id = c.country_id "
                + "AND cc.company_id = :companyId WHERE c.status = 1 ";

        final Map<String, Object> options = new HashMap<String, Object>();
        options.put("companyId", companyId);

        @SuppressWarnings("unchecked")
        final List<Object[]> resultList = dao.findByNativeQuery(query,
                new QueryOptions(options));
        final List<ObjectModel> objectModels = CommonsUtil
                .convertToObjectModelWithStatusAndSubObjectId(resultList);
        return Response.status(200).entity(objectModels).build();
    }

    @POST
    @Path("/saveCompanyCurrency")
    @ApiOperation(value = "Save new company currency status")
    @UnitOfWork
    public Response saveCompanyCurrency(final UpdateStatusModel model) {

        final User user = new User();
        user.setId(model.getUserId());

        final Company company = new Company();
        company.setId(model.getCompanyId());

        final Country country = new Country();
        country.setId(model.getSubObjectId());

        final CompanyCurrency companyCurrency = new CompanyCurrency();
        companyCurrency.setCreatedDate(new Date());
        companyCurrency.setCreatedBy(user);
        companyCurrency.setStatus(model.getStatus());
        companyCurrency.setCompany(company);
        companyCurrency.setCountry(country);
        dao.create(companyCurrency);

        return Response.status(200).entity(companyCurrency.getId()).build();
    }

    @PUT
    @Path("/updateCompanyCurrency/{companyCurrencyId}")
    @ApiOperation(value = "Update company currency status")
    @UnitOfWork
    public Response updateCompanyCurrency(
            @PathParam("companyCurrencyId") final Long companyCurrencyId,
            final UpdateStatusModel model) {

        final User user = new User();
        user.setId(model.getUserId());

        final CompanyCurrency companyCurrency = new CompanyCurrency();
        companyCurrency.setId(companyCurrencyId);
        dao.load(companyCurrency);

        companyCurrency.setUpdatedDate(new Date());
        companyCurrency.setUpdatedBy(user);
        companyCurrency.setStatus(model.getStatus());
        dao.update(companyCurrency);

        return Response.status(200).entity(companyCurrency.getId()).build();
    }

    @GET
    @Path("/getCurrencyListByCompany/{companyId}")
    @ApiOperation(value = "Get country list")
    @UnitOfWork
    public Response getCurrencyListByCompany(
            @PathParam("companyId") final Long companyId) {
        final Map<String, Object> options = new HashMap<String, Object>();
        options.put("companyId", companyId);

        @SuppressWarnings("unchecked")
        final List<Object[]> resultList = dao.findByNamedQuery(
                "get.active.currencies.by.company", new QueryOptions(options));
        final List<ObjectModel> objectModels = CommonsUtil
                .convertToObjectModel(resultList);
        return Response.status(200).entity(objectModels).build();
    }

}
