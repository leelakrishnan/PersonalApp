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
import com.leela.entity.BusinessType;
import com.leela.entity.Company;
import com.leela.entity.User;
import com.leela.model.BusinessTypeModel;
import com.leela.model.ObjectModel;
import com.leela.model.UpdateStatusModel;
import com.leela.util.CommonsUtil;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;

import io.dropwizard.hibernate.UnitOfWork;

@Path("/businessType")
@Api
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class BusinessTypeResource {

    private final GenericDAO dao;

    public BusinessTypeResource(final SessionFactory sessionFactory) {
        this.dao = new HibernateDAO(sessionFactory);
    }

    @GET
    @Path("/getBusinessTypes")
    @ApiOperation(value = "Get business type list")
    @UnitOfWork
    public Response getBusinessTypes() {
        final Map<String, Object> options = new HashMap<String, Object>();

        @SuppressWarnings("unchecked")
        final List<Object[]> resultList = dao.findByNamedQuery(
                "get.all.business.types", new QueryOptions(options));
        final List<ObjectModel> objectModels = CommonsUtil
                .convertToObjectModelWithStatus(resultList);
        return Response.status(200).entity(objectModels).build();
    }

    @SuppressWarnings("unchecked")
    @GET
    @Path("/validateBusinessType/{name}/{businessTypeId}")
    @ApiOperation(value = "Validate business type name")
    @UnitOfWork
    public Response validateBusinessType(@PathParam("name") final String name,
            @PathParam("businessTypeId") final Long businessTypeId) {
        List<Long> resultList = null;
        final Map<String, Object> options = new HashMap<String, Object>();
        options.put("name", name);
        if (businessTypeId != null && businessTypeId.longValue() != -1) {
            options.put("businessTypeId", businessTypeId);
            resultList = dao.findByNamedQuery(
                    "validate.name.business.type.edit",
                    new QueryOptions(options));
        } else {
            resultList = dao.findByNamedQuery("validate.name.business.type.add",
                    new QueryOptions(options));
        }
        if (resultList != null && !resultList.isEmpty()
                && resultList.get(0) != null) {
            return Response.status(200).entity(resultList.get(0)).build();
        }
        return Response.status(200).entity("-1").build();
    }

    @POST
    @Path("/saveBusinessType")
    @ApiOperation(value = "Add new business type")
    @UnitOfWork
    public Response saveBusinessType(final BusinessTypeModel model) {

        final User user = new User();
        user.setId(model.getUserId());

        final Company company = new Company();
        company.setId(model.getCompanyId());

        final BusinessType businessType = new BusinessType();
        businessType.setCreatedDate(new Date());
        businessType.setCreatedBy(user);
        businessType.setName(model.getName());
        businessType.setStatus(model.getStatus());
        dao.create(businessType);

        return Response.status(200).entity(businessType.getId()).build();
    }

    @PUT
    @Path("/updateBusinessType/{businessTypeId}")
    @ApiOperation(value = "Update business type")
    @UnitOfWork
    public Response updateBusinessType(
            @PathParam("businessTypeId") final Long businessTypeId,
            final BusinessTypeModel model) {

        final User user = new User();
        user.setId(model.getUserId());

        final Company company = new Company();
        company.setId(model.getCompanyId());

        final BusinessType businessType = new BusinessType();
        businessType.setId(businessTypeId);
        dao.load(businessType);

        businessType.setUpdatedDate(new Date());
        businessType.setUpdatedBy(user);
        businessType.setName(model.getName());
        businessType.setStatus(model.getStatus());
        dao.update(businessType);

        return Response.status(200).entity(businessType.getId()).build();
    }

    @PUT
    @Path("/updateBusinessTypeStatus/{businessTypeId}")
    @ApiOperation(value = "Get business type list")
    @UnitOfWork
    public Response updateBusinessTypeStatus(
            @PathParam("businessTypeId") final Long businessTypeId,
            final UpdateStatusModel model) {

        final User user = new User();
        user.setId(model.getUserId());

        final BusinessType businessType = new BusinessType();
        businessType.setId(businessTypeId);
        dao.load(businessType);

        businessType.setUpdatedDate(new Date());
        businessType.setUpdatedBy(user);
        businessType.setStatus(model.getStatus());
        dao.update(businessType);

        return Response.status(200).entity(businessType.getId()).build();
    }

    @GET
    @Path("/getAciveBusinessTypes")
    @ApiOperation(value = "Get active business type list")
    @UnitOfWork
    public Response getAciveBusinessTypes() {
        final Map<String, Object> options = new HashMap<String, Object>();

        @SuppressWarnings("unchecked")
        final List<Object[]> resultList = dao.findByNamedQuery(
                "get.active.business.types", new QueryOptions(options));
        final List<ObjectModel> objectModels = CommonsUtil
                .convertToObjectModel(resultList);
        return Response.status(200).entity(objectModels).build();
    }

}
