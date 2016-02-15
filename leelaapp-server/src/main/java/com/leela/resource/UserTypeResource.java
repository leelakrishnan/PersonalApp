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
import com.leela.entity.CompanyUserType;
import com.leela.entity.User;
import com.leela.entity.UserType;
import com.leela.model.ObjectModel;
import com.leela.model.UpdateStatusModel;
import com.leela.model.UserTypeModel;
import com.leela.util.CommonsUtil;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;

import io.dropwizard.hibernate.UnitOfWork;

@Path("/userType")
@Api
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserTypeResource {

    private final GenericDAO dao;

    public UserTypeResource(final SessionFactory sessionFactory) {
        this.dao = new HibernateDAO(sessionFactory);
    }

    @GET
    @Path("/getUserType/{companyId}")
    @ApiOperation(value = "Get user type list")
    @UnitOfWork
    public Response getUserType(@PathParam("companyId") final Long companyId) {
        final Map<String, Object> options = new HashMap<String, Object>();
        options.put("companyId", companyId);

        @SuppressWarnings("unchecked")
        final List<Object[]> resultList = dao.findByNamedQuery(
                "get.all.user.type.by.company", new QueryOptions(options));
        final List<ObjectModel> objectModels = CommonsUtil
                .convertToObjectModelWithStatus(resultList);
        return Response.status(200).entity(objectModels).build();
    }

    @SuppressWarnings("unchecked")
    @GET
    @Path("/validateUserType/{name}/{userTypeId}")
    @ApiOperation(value = "Validate user type name")
    @UnitOfWork
    public Response validateUserType(@PathParam("name") final String name,
            @PathParam("userTypeId") final Long userTypeId) {
        List<Long> resultList = null;
        final Map<String, Object> options = new HashMap<String, Object>();
        options.put("name", name);
        if (userTypeId != null && userTypeId.longValue() != -1) {
            options.put("userTypeId", userTypeId);
            resultList = dao.findByNamedQuery("validate.name.user.type.edit",
                    new QueryOptions(options));
        } else {
            resultList = dao.findByNamedQuery("validate.name.user.type.add",
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
    @Path("/validateUserTypeByCompany/{companyId}/{name}/{userTypeId}")
    @ApiOperation(value = "Validate user type name company wise")
    @UnitOfWork
    public Response validateUserTypeByCompany(
            @PathParam("companyId") final Long companyId,
            @PathParam("name") final String name,
            @PathParam("userTypeId") final Long userTypeId) {
        List<Long> resultList = null;
        final Map<String, Object> options = new HashMap<String, Object>();
        options.put("companyId", companyId);
        options.put("name", name);
        if (userTypeId != null && userTypeId.longValue() != -1) {
            options.put("userTypeId", userTypeId);
            resultList = dao.findByNamedQuery(
                    "validate.name.user.type.by.company.edit",
                    new QueryOptions(options));
        } else {
            resultList = dao.findByNamedQuery(
                    "validate.name.user.type.by.company.add",
                    new QueryOptions(options));
        }
        if (resultList != null && !resultList.isEmpty()
                && resultList.get(0) != null) {
            return Response.status(200).entity(resultList.get(0)).build();
        }
        return Response.status(200).entity("-1").build();
    }

    @POST
    @Path("/saveUserType")
    @ApiOperation(value = "Add new user type")
    @UnitOfWork
    public Response saveUserType(final UserTypeModel model) {

        final User user = new User();
        user.setId(model.getUserId());

        final Company company = new Company();
        company.setId(model.getCompanyId());

        final UserType userType = new UserType();
        userType.setCreatedDate(new Date());
        userType.setCreatedBy(user);
        userType.setName(model.getName());
        userType.setStatus(model.getStatus());
        dao.create(userType);

        final CompanyUserType companyUserType = new CompanyUserType();
        companyUserType.setCreatedDate(new Date());
        companyUserType.setCreatedBy(user);
        companyUserType.setCompany(company);
        companyUserType.setUserType(userType);
        companyUserType.setStatus(CommonsUtil.ACTIVE_STATUS);
        dao.create(companyUserType);

        return Response.status(200).entity(userType.getId()).build();
    }

    @PUT
    @Path("/updateUserType/{userTypeId}")
    @ApiOperation(value = "Update user type")
    @UnitOfWork
    public Response updateUserType(
            @PathParam("userTypeId") final Long userTypeId,
            final UserTypeModel model) {

        final User user = new User();
        user.setId(model.getUserId());

        final Company company = new Company();
        company.setId(model.getCompanyId());

        final UserType userType = new UserType();
        userType.setId(userTypeId);
        dao.load(userType);

        userType.setUpdatedDate(new Date());
        userType.setUpdatedBy(user);
        userType.setName(model.getName());
        userType.setStatus(model.getStatus());
        dao.update(userType);

        return Response.status(200).entity(userType.getId()).build();
    }

    @PUT
    @Path("/updateUserTypeStatus/{userTypeId}")
    @ApiOperation(value = "Get user type list")
    @UnitOfWork
    public Response updateUserTypeStatus(
            @PathParam("userTypeId") final Long userTypeId,
            final UpdateStatusModel model) {

        final User user = new User();
        user.setId(model.getUserId());

        final UserType userType = new UserType();
        userType.setId(userTypeId);
        dao.load(userType);

        userType.setUpdatedDate(new Date());
        userType.setUpdatedBy(user);
        userType.setStatus(model.getStatus());
        dao.update(userType);

        return Response.status(200).entity(userType.getId()).build();
    }

    @GET
    @Path("/getAciveUserType/{companyId}")
    @ApiOperation(value = "Get active user type list")
    @UnitOfWork
    public Response getAciveUserType(
            @PathParam("companyId") final Long companyId) {
        final Map<String, Object> options = new HashMap<String, Object>();
        options.put("companyId", companyId);

        @SuppressWarnings("unchecked")
        final List<Object[]> resultList = dao.findByNamedQuery(
                "get.active.user.type.by.company", new QueryOptions(options));
        final List<ObjectModel> objectModels = CommonsUtil
                .convertToObjectModel(resultList);
        return Response.status(200).entity(objectModels).build();
    }

    @PUT
    @Path("/saveCompanyUserType")
    @ApiOperation(value = "Save new company user type")
    @UnitOfWork
    public Response saveCompanyUserType(final UpdateStatusModel model) {

        final User user = new User();
        user.setId(model.getUserId());

        final Company company = new Company();
        company.setId(model.getCompanyId());

        final UserType userType = new UserType();
        userType.setId(model.getSubObjectId());

        final CompanyUserType companyUserType = new CompanyUserType();
        companyUserType.setCreatedDate(new Date());
        companyUserType.setCreatedBy(user);
        companyUserType.setStatus(model.getStatus());
        companyUserType.setCompany(company);
        companyUserType.setUserType(userType);
        dao.create(companyUserType);

        return Response.status(200).entity(companyUserType.getId()).build();
    }

    @PUT
    @Path("/updateCompanyUserType/{companyUserTypeId}")
    @ApiOperation(value = "Update company user type")
    @UnitOfWork
    public Response updateCompanyUserType(
            @PathParam("companyUserTypeId") final Long companyUserTypeId,
            final UpdateStatusModel model) {

        final User user = new User();
        user.setId(model.getUserId());

        final CompanyUserType companyUserType = new CompanyUserType();
        companyUserType.setId(companyUserTypeId);
        dao.load(companyUserType);

        companyUserType.setUpdatedDate(new Date());
        companyUserType.setUpdatedBy(user);
        companyUserType.setStatus(model.getStatus());
        dao.update(companyUserType);

        return Response.status(200).entity(companyUserType.getId()).build();
    }

}
