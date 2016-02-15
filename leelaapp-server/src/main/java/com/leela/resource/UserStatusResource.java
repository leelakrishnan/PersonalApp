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
import com.leela.entity.CompanyUserStatus;
import com.leela.entity.User;
import com.leela.entity.UserStatus;
import com.leela.model.ObjectModel;
import com.leela.model.UpdateStatusModel;
import com.leela.model.UserStatusModel;
import com.leela.util.CommonsUtil;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;

import io.dropwizard.hibernate.UnitOfWork;

@Path("/userStatus")
@Api
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserStatusResource {

    private final GenericDAO dao;

    public UserStatusResource(final SessionFactory sessionFactory) {
        this.dao = new HibernateDAO(sessionFactory);
    }

    @GET
    @Path("/getUserStatus/{companyId}")
    @ApiOperation(value = "Get user status list")
    @UnitOfWork
    public Response getUserStatus(
            @PathParam("companyId") final Long companyId) {
        final Map<String, Object> options = new HashMap<String, Object>();
        options.put("companyId", companyId);

        @SuppressWarnings("unchecked")
        final List<Object[]> resultList = dao.findByNamedQuery(
                "get.all.user.status.by.company", new QueryOptions(options));
        final List<ObjectModel> objectModels = CommonsUtil
                .convertToObjectModelWithStatus(resultList);
        return Response.status(200).entity(objectModels).build();
    }

    @SuppressWarnings("unchecked")
    @GET
    @Path("/validateUserStatus/{name}/{userStatusId}")
    @ApiOperation(value = "Validate user status name")
    @UnitOfWork
    public Response validateUserStatus(@PathParam("name") final String name,
            @PathParam("userStatusId") final Long userStatusId) {
        List<Long> resultList = null;
        final Map<String, Object> options = new HashMap<String, Object>();
        options.put("name", name);
        if (userStatusId != null && userStatusId.longValue() != -1) {
            options.put("userStatusId", userStatusId);
            resultList = dao.findByNamedQuery("validate.name.user.status.edit",
                    new QueryOptions(options));
        } else {
            resultList = dao.findByNamedQuery("validate.name.user.status.add",
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
    @Path("/validateUserStatusByCompany/{companyId}/{name}/{userStatusId}")
    @ApiOperation(value = "Validate user status name company wise")
    @UnitOfWork
    public Response validateUserStatusByCompany(
            @PathParam("companyId") final Long companyId,
            @PathParam("name") final String name,
            @PathParam("userStatusId") final Long userStatusId) {
        List<Long> resultList = null;
        final Map<String, Object> options = new HashMap<String, Object>();
        options.put("companyId", companyId);
        options.put("name", name);
        if (userStatusId != null && userStatusId.longValue() != -1) {
            options.put("userStatusId", userStatusId);
            resultList = dao.findByNamedQuery(
                    "validate.name.user.status.by.company.edit",
                    new QueryOptions(options));
        } else {
            resultList = dao.findByNamedQuery(
                    "validate.name.user.status.by.company.add",
                    new QueryOptions(options));
        }
        if (resultList != null && !resultList.isEmpty()
                && resultList.get(0) != null) {
            return Response.status(200).entity(resultList.get(0)).build();
        }
        return Response.status(200).entity("-1").build();
    }

    @POST
    @Path("/saveUserStatus")
    @ApiOperation(value = "Add new user status")
    @UnitOfWork
    public Response saveUserStatus(final UserStatusModel model) {

        final User user = new User();
        user.setId(model.getUserId());

        final Company company = new Company();
        company.setId(model.getCompanyId());

        final UserStatus userStatus = new UserStatus();
        userStatus.setCreatedDate(new Date());
        userStatus.setCreatedBy(user);
        userStatus.setName(model.getName());
        userStatus.setStatus(model.getStatus());
        dao.create(userStatus);

        final CompanyUserStatus companyUserStatus = new CompanyUserStatus();
        companyUserStatus.setCreatedDate(new Date());
        companyUserStatus.setCreatedBy(user);
        companyUserStatus.setCompany(company);
        companyUserStatus.setUserStatus(userStatus);
        companyUserStatus.setStatus(CommonsUtil.ACTIVE_STATUS);
        dao.create(companyUserStatus);

        return Response.status(200).entity(userStatus.getId()).build();
    }

    @PUT
    @Path("/updateUserStatus/{userStatusId}")
    @ApiOperation(value = "Update user status")
    @UnitOfWork
    public Response updateUserStatus(
            @PathParam("userStatusId") final Long userStatusId,
            final UserStatusModel model) {

        final User user = new User();
        user.setId(model.getUserId());

        final Company company = new Company();
        company.setId(model.getCompanyId());

        final UserStatus userStatus = new UserStatus();
        userStatus.setId(userStatusId);
        dao.load(userStatus);

        userStatus.setUpdatedDate(new Date());
        userStatus.setUpdatedBy(user);
        userStatus.setName(model.getName());
        userStatus.setStatus(model.getStatus());
        dao.update(userStatus);

        return Response.status(200).entity(userStatus.getId()).build();
    }

    @PUT
    @Path("/updateUserStatusStatus/{userStatusId}")
    @ApiOperation(value = "Get user status list")
    @UnitOfWork
    public Response updateUserStatusStatus(
            @PathParam("userStatusId") final Long userStatusId,
            final UpdateStatusModel model) {

        final User user = new User();
        user.setId(model.getUserId());

        final UserStatus userStatus = new UserStatus();
        userStatus.setId(userStatusId);
        dao.load(userStatus);

        userStatus.setUpdatedDate(new Date());
        userStatus.setUpdatedBy(user);
        userStatus.setStatus(model.getStatus());
        dao.update(userStatus);

        return Response.status(200).entity(userStatus.getId()).build();
    }

    @GET
    @Path("/getAciveUserStatus/{companyId}")
    @ApiOperation(value = "Get active user status list")
    @UnitOfWork
    public Response getAciveUserStatus(
            @PathParam("companyId") final Long companyId) {
        final Map<String, Object> options = new HashMap<String, Object>();
        options.put("companyId", companyId);

        @SuppressWarnings("unchecked")
        final List<Object[]> resultList = dao.findByNamedQuery(
                "get.active.user.status.by.company", new QueryOptions(options));
        final List<ObjectModel> objectModels = CommonsUtil
                .convertToObjectModel(resultList);
        return Response.status(200).entity(objectModels).build();
    }

    @PUT
    @Path("/saveCompanyUserStatus")
    @ApiOperation(value = "Save new company user status")
    @UnitOfWork
    public Response saveCompanyUserStatus(final UpdateStatusModel model) {

        final User user = new User();
        user.setId(model.getUserId());

        final Company company = new Company();
        company.setId(model.getCompanyId());

        final UserStatus userStatus = new UserStatus();
        userStatus.setId(model.getSubObjectId());

        final CompanyUserStatus companyUserStatus = new CompanyUserStatus();
        companyUserStatus.setCreatedDate(new Date());
        companyUserStatus.setCreatedBy(user);
        companyUserStatus.setStatus(model.getStatus());
        companyUserStatus.setCompany(company);
        companyUserStatus.setUserStatus(userStatus);
        dao.create(companyUserStatus);

        return Response.status(200).entity(companyUserStatus.getId()).build();
    }

    @PUT
    @Path("/updateCompanyUserStatus/{companyUserStatusId}")
    @ApiOperation(value = "Update company user status")
    @UnitOfWork
    public Response updateCompanyUserStatus(
            @PathParam("companyUserStatusId") final Long companyUserStatusId,
            final UpdateStatusModel model) {

        final User user = new User();
        user.setId(model.getUserId());

        final CompanyUserStatus companyUserStatus = new CompanyUserStatus();
        companyUserStatus.setId(companyUserStatusId);
        dao.load(companyUserStatus);

        companyUserStatus.setUpdatedDate(new Date());
        companyUserStatus.setUpdatedBy(user);
        companyUserStatus.setStatus(model.getStatus());
        dao.update(companyUserStatus);

        return Response.status(200).entity(companyUserStatus.getId()).build();
    }

}
