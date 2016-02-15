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
import com.leela.entity.CompanyOrderType;
import com.leela.entity.OrderType;
import com.leela.entity.User;
import com.leela.model.ObjectModel;
import com.leela.model.OrderTypeModel;
import com.leela.model.UpdateStatusModel;
import com.leela.util.CommonsUtil;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;

import io.dropwizard.hibernate.UnitOfWork;

@Path("/orderType")
@Api
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class OrderTypeResource {

    private final GenericDAO dao;

    public OrderTypeResource(final SessionFactory sessionFactory) {
        this.dao = new HibernateDAO(sessionFactory);
    }

    @GET
    @Path("/getOrderType/{companyId}")
    @ApiOperation(value = "Get order type list")
    @UnitOfWork
    public Response getOrderType(@PathParam("companyId") final Long companyId) {
        final Map<String, Object> options = new HashMap<String, Object>();
        options.put("companyId", companyId);

        @SuppressWarnings("unchecked")
        final List<Object[]> resultList = dao.findByNamedQuery(
                "get.all.order.type.by.company", new QueryOptions(options));
        final List<ObjectModel> objectModels = CommonsUtil
                .convertToObjectModelWithStatus(resultList);
        return Response.status(200).entity(objectModels).build();
    }

    @SuppressWarnings("unchecked")
    @GET
    @Path("/validateOrderType/{name}/{orderTypeId}")
    @ApiOperation(value = "Validate order type name")
    @UnitOfWork
    public Response validateOrderType(@PathParam("name") final String name,
            @PathParam("orderTypeId") final Long orderTypeId) {
        List<Long> resultList = null;
        final Map<String, Object> options = new HashMap<String, Object>();
        options.put("name", name);
        if (orderTypeId != null && orderTypeId.longValue() != -1) {
            options.put("orderTypeId", orderTypeId);
            resultList = dao.findByNamedQuery("validate.name.order.type.edit",
                    new QueryOptions(options));
        } else {
            resultList = dao.findByNamedQuery("validate.name.order.type.add",
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
    @Path("/validateOrderTypeByCompany/{companyId}/{name}/{orderTypeId}")
    @ApiOperation(value = "Validate order type name company wise")
    @UnitOfWork
    public Response validateOrderTypeByCompany(
            @PathParam("companyId") final Long companyId,
            @PathParam("name") final String name,
            @PathParam("orderTypeId") final Long orderTypeId) {
        List<Long> resultList = null;
        final Map<String, Object> options = new HashMap<String, Object>();
        options.put("companyId", companyId);
        options.put("name", name);
        if (orderTypeId != null && orderTypeId.longValue() != -1) {
            options.put("orderTypeId", orderTypeId);
            resultList = dao.findByNamedQuery(
                    "validate.name.order.type.by.company.edit",
                    new QueryOptions(options));
        } else {
            resultList = dao.findByNamedQuery(
                    "validate.name.order.type.by.company.add",
                    new QueryOptions(options));
        }
        if (resultList != null && !resultList.isEmpty()
                && resultList.get(0) != null) {
            return Response.status(200).entity(resultList.get(0)).build();
        }
        return Response.status(200).entity("-1").build();
    }

    @POST
    @Path("/saveOrderType")
    @ApiOperation(value = "Add new order type")
    @UnitOfWork
    public Response saveOrderType(final OrderTypeModel model) {

        final User user = new User();
        user.setId(model.getUserId());

        final Company company = new Company();
        company.setId(model.getCompanyId());

        final OrderType orderType = new OrderType();
        orderType.setCreatedDate(new Date());
        orderType.setCreatedBy(user);
        orderType.setName(model.getName());
        orderType.setStatus(model.getStatus());
        dao.create(orderType);

        final CompanyOrderType companyOrderType = new CompanyOrderType();
        companyOrderType.setCreatedDate(new Date());
        companyOrderType.setCreatedBy(user);
        companyOrderType.setCompany(company);
        companyOrderType.setOrderType(orderType);
        companyOrderType.setStatus(CommonsUtil.ACTIVE_STATUS);
        dao.create(companyOrderType);

        return Response.status(200).entity(orderType.getId()).build();
    }

    @PUT
    @Path("/updateOrderType/{orderTypeId}")
    @ApiOperation(value = "Update order type")
    @UnitOfWork
    public Response updateOrderType(
            @PathParam("orderTypeId") final Long orderTypeId,
            final OrderTypeModel model) {

        final User user = new User();
        user.setId(model.getUserId());

        final Company company = new Company();
        company.setId(model.getCompanyId());

        final OrderType orderType = new OrderType();
        orderType.setId(orderTypeId);
        dao.load(orderType);

        orderType.setUpdatedDate(new Date());
        orderType.setUpdatedBy(user);
        orderType.setName(model.getName());
        orderType.setStatus(model.getStatus());
        dao.update(orderType);

        return Response.status(200).entity(orderType.getId()).build();
    }

    @PUT
    @Path("/updateOrderTypeStatus/{orderTypeId}")
    @ApiOperation(value = "Get order type list")
    @UnitOfWork
    public Response updateOrderTypeStatus(
            @PathParam("orderTypeId") final Long orderTypeId,
            final UpdateStatusModel model) {

        final User user = new User();
        user.setId(model.getUserId());

        final OrderType orderType = new OrderType();
        orderType.setId(orderTypeId);
        dao.load(orderType);

        orderType.setUpdatedDate(new Date());
        orderType.setUpdatedBy(user);
        orderType.setStatus(model.getStatus());
        dao.update(orderType);

        return Response.status(200).entity(orderType.getId()).build();
    }

    @GET
    @Path("/getAciveOrderType/{companyId}")
    @ApiOperation(value = "Get active order type list")
    @UnitOfWork
    public Response getAciveOrderType(
            @PathParam("companyId") final Long companyId) {
        final Map<String, Object> options = new HashMap<String, Object>();
        options.put("companyId", companyId);

        @SuppressWarnings("unchecked")
        final List<Object[]> resultList = dao.findByNamedQuery(
                "get.active.order.type.by.company", new QueryOptions(options));
        final List<ObjectModel> objectModels = CommonsUtil
                .convertToObjectModel(resultList);
        return Response.status(200).entity(objectModels).build();
    }

    @PUT
    @Path("/saveCompanyOrderType")
    @ApiOperation(value = "Save new company order type")
    @UnitOfWork
    public Response saveCompanyOrderType(final UpdateStatusModel model) {

        final User user = new User();
        user.setId(model.getUserId());

        final Company company = new Company();
        company.setId(model.getCompanyId());

        final OrderType orderType = new OrderType();
        orderType.setId(model.getSubObjectId());

        final CompanyOrderType companyOrderType = new CompanyOrderType();
        companyOrderType.setCreatedDate(new Date());
        companyOrderType.setCreatedBy(user);
        companyOrderType.setStatus(model.getStatus());
        companyOrderType.setCompany(company);
        companyOrderType.setOrderType(orderType);
        dao.create(companyOrderType);

        return Response.status(200).entity(companyOrderType.getId()).build();
    }

    @PUT
    @Path("/updateCompanyOrderType/{companyOrderTypeId}")
    @ApiOperation(value = "Update company order type")
    @UnitOfWork
    public Response updateCompanyOrderType(
            @PathParam("companyOrderTypeId") final Long companyOrderTypeId,
            final UpdateStatusModel model) {

        final User user = new User();
        user.setId(model.getUserId());

        final CompanyOrderType companyOrderType = new CompanyOrderType();
        companyOrderType.setId(companyOrderTypeId);
        dao.load(companyOrderType);

        companyOrderType.setUpdatedDate(new Date());
        companyOrderType.setUpdatedBy(user);
        companyOrderType.setStatus(model.getStatus());
        dao.update(companyOrderType);

        return Response.status(200).entity(companyOrderType.getId()).build();
    }

}
