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
import com.leela.entity.CompanyShippingOption;
import com.leela.entity.ShippingOption;
import com.leela.entity.User;
import com.leela.model.ObjectModel;
import com.leela.model.ShippingOptionModel;
import com.leela.model.UpdateStatusModel;
import com.leela.util.CommonsUtil;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;

import io.dropwizard.hibernate.UnitOfWork;

@Path("/shippingOption")
@Api
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ShippingOptionResource {

    private final GenericDAO dao;

    public ShippingOptionResource(final SessionFactory sessionFactory) {
        this.dao = new HibernateDAO(sessionFactory);
    }

    @GET
    @Path("/getShippingOption/{companyId}")
    @ApiOperation(value = "Get shipping option list")
    @UnitOfWork
    public Response getShippingOption(
            @PathParam("companyId") final Long companyId) {
        final Map<String, Object> options = new HashMap<String, Object>();
        options.put("companyId", companyId);

        @SuppressWarnings("unchecked")
        final List<Object[]> resultList = dao.findByNamedQuery(
                "get.all.shipping.option.by.company",
                new QueryOptions(options));
        final List<ObjectModel> objectModels = CommonsUtil
                .convertToObjectModelWithStatus(resultList);
        return Response.status(200).entity(objectModels).build();
    }

    @SuppressWarnings("unchecked")
    @GET
    @Path("/validateShippingOption/{name}/{shippingOptionId}")
    @ApiOperation(value = "Validate shipping option name")
    @UnitOfWork
    public Response validateShippingOption(@PathParam("name") final String name,
            @PathParam("shippingOptionId") final Long shippingOptionId) {
        List<Long> resultList = null;
        final Map<String, Object> options = new HashMap<String, Object>();
        options.put("name", name);
        if (shippingOptionId != null && shippingOptionId.longValue() != -1) {
            options.put("shippingOptionId", shippingOptionId);
            resultList = dao.findByNamedQuery(
                    "validate.name.shipping.option.edit",
                    new QueryOptions(options));
        } else {
            resultList = dao.findByNamedQuery(
                    "validate.name.shipping.option.add",
                    new QueryOptions(options));
        }
        if (resultList != null && !resultList.isEmpty()
                && resultList.get(0) != null) {
            return Response.status(200).entity(resultList.get(0)).build();
        }
        return Response.status(200).entity("-1").build();
    }

    @SuppressWarnings("unchecked")
    @GET
    @Path("/validateShippingOptionByCompany/{companyId}/{name}/{shippingOptionId}")
    @ApiOperation(value = "Validate shipping option name company wise")
    @UnitOfWork
    public Response validateShippingOptionByCompany(
            @PathParam("companyId") final Long companyId,
            @PathParam("name") final String name,
            @PathParam("shippingOptionId") final Long shippingOptionId) {
        List<Long> resultList = null;
        final Map<String, Object> options = new HashMap<String, Object>();
        options.put("companyId", companyId);
        options.put("name", name);
        if (shippingOptionId != null && shippingOptionId.longValue() != -1) {
            options.put("shippingOptionId", shippingOptionId);
            resultList = dao.findByNamedQuery(
                    "validate.name.shipping.option.by.company.edit",
                    new QueryOptions(options));
        } else {
            resultList = dao.findByNamedQuery(
                    "validate.name.shipping.option.by.company.add",
                    new QueryOptions(options));
        }
        if (resultList != null && !resultList.isEmpty()
                && resultList.get(0) != null) {
            return Response.status(200).entity(resultList.get(0)).build();
        }
        return Response.status(200).entity("-1").build();
    }

    @POST
    @Path("/saveShippingOption")
    @ApiOperation(value = "Add new shipping option")
    @UnitOfWork
    public Response saveShippingOption(final ShippingOptionModel model) {

        final User user = new User();
        user.setId(model.getUserId());

        final Company company = new Company();
        company.setId(model.getCompanyId());

        final ShippingOption shippingOption = new ShippingOption();
        shippingOption.setCreatedDate(new Date());
        shippingOption.setCreatedBy(user);
        shippingOption.setName(model.getName());
        shippingOption.setStatus(model.getStatus());
        dao.create(shippingOption);

        final CompanyShippingOption companyShippingOption = new CompanyShippingOption();
        companyShippingOption.setCreatedDate(new Date());
        companyShippingOption.setCreatedBy(user);
        companyShippingOption.setCompany(company);
        companyShippingOption.setShippingOption(shippingOption);
        companyShippingOption.setStatus(CommonsUtil.ACTIVE_STATUS);
        dao.create(companyShippingOption);

        return Response.status(200).entity(shippingOption.getId()).build();
    }

    @PUT
    @Path("/updateShippingOption/{shippingOptionId}")
    @ApiOperation(value = "Update shipping option")
    @UnitOfWork
    public Response updateShippingOption(
            @PathParam("shippingOptionId") final Long shippingOptionId,
            final ShippingOptionModel model) {

        final User user = new User();
        user.setId(model.getUserId());

        final Company company = new Company();
        company.setId(model.getCompanyId());

        final ShippingOption shippingOption = new ShippingOption();
        shippingOption.setId(shippingOptionId);
        dao.load(shippingOption);

        shippingOption.setUpdatedDate(new Date());
        shippingOption.setUpdatedBy(user);
        shippingOption.setName(model.getName());
        shippingOption.setStatus(model.getStatus());
        dao.update(shippingOption);

        return Response.status(200).entity(shippingOption.getId()).build();
    }

    @PUT
    @Path("/updateShippingOptionStatus/{shippingOptionId}")
    @ApiOperation(value = "Get shipping option list")
    @UnitOfWork
    public Response updateShippingOptionStatus(
            @PathParam("shippingOptionId") final Long shippingOptionId,
            final UpdateStatusModel model) {

        final User user = new User();
        user.setId(model.getUserId());

        final ShippingOption shippingOption = new ShippingOption();
        shippingOption.setId(shippingOptionId);
        dao.load(shippingOption);

        shippingOption.setUpdatedDate(new Date());
        shippingOption.setUpdatedBy(user);
        shippingOption.setStatus(model.getStatus());
        dao.update(shippingOption);

        return Response.status(200).entity(shippingOption.getId()).build();
    }

    @GET
    @Path("/getAciveShippingOption/{companyId}")
    @ApiOperation(value = "Get active shipping option list")
    @UnitOfWork
    public Response getAciveShippingOption(
            @PathParam("companyId") final Long companyId) {
        final Map<String, Object> options = new HashMap<String, Object>();
        options.put("companyId", companyId);

        @SuppressWarnings("unchecked")
        final List<Object[]> resultList = dao.findByNamedQuery(
                "get.active.shipping.option.by.company",
                new QueryOptions(options));
        final List<ObjectModel> objectModels = CommonsUtil
                .convertToObjectModel(resultList);
        return Response.status(200).entity(objectModels).build();
    }

    @PUT
    @Path("/saveCompanyShippingOption")
    @ApiOperation(value = "Save new company shipping option")
    @UnitOfWork
    public Response saveCompanyShippingOption(final UpdateStatusModel model) {

        final User user = new User();
        user.setId(model.getUserId());

        final Company company = new Company();
        company.setId(model.getCompanyId());

        final ShippingOption shippingOption = new ShippingOption();
        shippingOption.setId(model.getSubObjectId());

        final CompanyShippingOption companyShippingOption = new CompanyShippingOption();
        companyShippingOption.setCreatedDate(new Date());
        companyShippingOption.setCreatedBy(user);
        companyShippingOption.setStatus(model.getStatus());
        companyShippingOption.setCompany(company);
        companyShippingOption.setShippingOption(shippingOption);
        dao.create(companyShippingOption);

        return Response.status(200).entity(companyShippingOption.getId())
                .build();
    }

    @PUT
    @Path("/updateCompanyShippingOption/{companyShippingOptionId}")
    @ApiOperation(value = "Update company shipping option")
    @UnitOfWork
    public Response updateCompanyShippingOption(
            @PathParam("companyShippingOptionId") final Long companyShippingOptionId,
            final UpdateStatusModel model) {

        final User user = new User();
        user.setId(model.getUserId());

        final CompanyShippingOption companyShippingOption = new CompanyShippingOption();
        companyShippingOption.setId(companyShippingOptionId);
        dao.load(companyShippingOption);

        companyShippingOption.setUpdatedDate(new Date());
        companyShippingOption.setUpdatedBy(user);
        companyShippingOption.setStatus(model.getStatus());
        dao.update(companyShippingOption);

        return Response.status(200).entity(companyShippingOption.getId())
                .build();
    }

}
