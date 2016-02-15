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
import com.leela.entity.CompanyOrderSubType;
import com.leela.entity.OrderSubType;
import com.leela.entity.OrderType;
import com.leela.entity.User;
import com.leela.model.ObjectModel;
import com.leela.model.OrderSubTypeModel;
import com.leela.model.UpdateStatusModel;
import com.leela.util.CommonsUtil;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;

import io.dropwizard.hibernate.UnitOfWork;

@Path("/orderSubType")
@Api
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class OrderSubTypeResource {

    private final GenericDAO dao;

    public OrderSubTypeResource(final SessionFactory sessionFactory) {
        this.dao = new HibernateDAO(sessionFactory);
    }

    @GET
    @Path("/getOrderSubType/{companyId}/{orderTypeId}")
    @ApiOperation(value = "Get order sub type list")
    @UnitOfWork
    public Response getOrderSubType(
            @PathParam("companyId") final Long companyId,
            @PathParam("orderTypeId") final Long orderTypeId) {
        final Map<String, Object> options = new HashMap<String, Object>();
        options.put("companyId", companyId);
        options.put("orderTypeId", orderTypeId);

        @SuppressWarnings("unchecked")
        final List<Object[]> resultList = dao.findByNamedQuery(
                "get.all.order.sub.type.by.order.type",
                new QueryOptions(options));
        final List<ObjectModel> objectModels = CommonsUtil
                .convertToObjectModelWithStatus(resultList);
        return Response.status(200).entity(objectModels).build();
    }

    @SuppressWarnings("unchecked")
    @GET
    @Path("/validateOrderSubType/{name}/{orderSubTypeId}")
    @ApiOperation(value = "Validate order sub type name")
    @UnitOfWork
    public Response validateOrderSubType(@PathParam("name") final String name,
            @PathParam("orderSubTypeId") final Long orderSubTypeId) {
        List<Long> resultList = null;
        final Map<String, Object> options = new HashMap<String, Object>();
        options.put("name", name);
        if (orderSubTypeId != null && orderSubTypeId.longValue() != -1) {
            options.put("orderSubTypeId", orderSubTypeId);
            resultList = dao.findByNamedQuery(
                    "validate.name.order.sub.type.edit",
                    new QueryOptions(options));
        } else {
            resultList = dao.findByNamedQuery(
                    "validate.name.order.sub.type.add",
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
    @Path("/validateOrderSubTypeByCompany/{companyId}/{name}/{orderSubTypeId}")
    @ApiOperation(value = "Validate order sub type name company wise")
    @UnitOfWork
    public Response validateOrderSubTypeByCompany(
            @PathParam("companyId") final Long companyId,
            @PathParam("name") final String name,
            @PathParam("orderSubTypeId") final Long orderSubTypeId) {
        List<Long> resultList = null;
        final Map<String, Object> options = new HashMap<String, Object>();
        options.put("companyId", companyId);
        options.put("name", name);
        if (orderSubTypeId != null && orderSubTypeId.longValue() != -1) {
            options.put("orderSubTypeId", orderSubTypeId);
            resultList = dao.findByNamedQuery(
                    "validate.name.order.sub.type.by.company.edit",
                    new QueryOptions(options));
        } else {
            resultList = dao.findByNamedQuery(
                    "validate.name.order.sub.type.by.company.add",
                    new QueryOptions(options));
        }
        if (resultList != null && !resultList.isEmpty()
                && resultList.get(0) != null) {
            return Response.status(200).entity(resultList.get(0)).build();
        }
        return Response.status(200).entity("-1").build();
    }

    @POST
    @Path("/saveOrderSubType")
    @ApiOperation(value = "Add new order sub type")
    @UnitOfWork
    public Response saveOrderSubType(final OrderSubTypeModel model) {

        final User user = new User();
        user.setId(model.getUserId());

        final Company company = new Company();
        company.setId(model.getCompanyId());

        final OrderType orderType = new OrderType();
        orderType.setId(model.getOrderTypeId());

        final OrderSubType orderSubType = new OrderSubType();
        orderSubType.setCreatedDate(new Date());
        orderSubType.setCreatedBy(user);
        orderSubType.setName(model.getName());
        orderSubType.setOrderType(orderType);
        orderSubType.setStatus(model.getStatus());
        dao.create(orderSubType);

        final CompanyOrderSubType companyOrderSubType = new CompanyOrderSubType();
        companyOrderSubType.setCreatedDate(new Date());
        companyOrderSubType.setCreatedBy(user);
        companyOrderSubType.setCompany(company);
        companyOrderSubType.setOrderSubType(orderSubType);
        companyOrderSubType.setStatus(CommonsUtil.ACTIVE_STATUS);
        dao.create(companyOrderSubType);

        return Response.status(200).entity(orderSubType.getId()).build();
    }

    @PUT
    @Path("/updateOrderSubType/{orderSubTypeId}")
    @ApiOperation(value = "Update order sub type")
    @UnitOfWork
    public Response updateOrderSubType(
            @PathParam("orderSubTypeId") final Long orderSubTypeId,
            final OrderSubTypeModel model) {

        final User user = new User();
        user.setId(model.getUserId());

        final Company company = new Company();
        company.setId(model.getCompanyId());

        final OrderType orderType = new OrderType();
        orderType.setId(model.getOrderTypeId());

        final OrderSubType orderSubType = new OrderSubType();
        orderSubType.setId(orderSubTypeId);
        dao.load(orderSubType);

        orderSubType.setUpdatedDate(new Date());
        orderSubType.setUpdatedBy(user);
        orderSubType.setName(model.getName());
        orderSubType.setOrderType(orderType);
        orderSubType.setStatus(model.getStatus());
        dao.update(orderSubType);

        return Response.status(200).entity(orderSubType.getId()).build();
    }

    @PUT
    @Path("/updateOrderSubTypeStatus/{orderSubTypeId}")
    @ApiOperation(value = "Get order sub type list")
    @UnitOfWork
    public Response updateOrderSubTypeStatus(
            @PathParam("orderSubTypeId") final Long orderSubTypeId,
            final UpdateStatusModel model) {

        final User user = new User();
        user.setId(model.getUserId());

        final OrderSubType orderSubType = new OrderSubType();
        orderSubType.setId(orderSubTypeId);
        dao.load(orderSubType);

        orderSubType.setUpdatedDate(new Date());
        orderSubType.setUpdatedBy(user);
        orderSubType.setStatus(model.getStatus());
        dao.update(orderSubType);

        return Response.status(200).entity(orderSubType.getId()).build();
    }

    @GET
    @Path("/getAciveOrderSubType/{companyId}/{orderTypeId}")
    @ApiOperation(value = "Get active order sub type list by order type")
    @UnitOfWork
    public Response getAciveOrderSubType(
            @PathParam("companyId") final Long companyId,
            @PathParam("orderTypeId") final Long orderTypeId) {
        final Map<String, Object> options = new HashMap<String, Object>();
        options.put("companyId", companyId);
        options.put("orderTypeId", orderTypeId);

        @SuppressWarnings("unchecked")
        final List<Object[]> resultList = dao.findByNamedQuery(
                "get.active.order.sub.type.by.order.type",
                new QueryOptions(options));
        final List<ObjectModel> objectModels = CommonsUtil
                .convertToObjectModel(resultList);
        return Response.status(200).entity(objectModels).build();
    }

    @PUT
    @Path("/saveCompanyOrderSubType")
    @ApiOperation(value = "Save new company order sub type")
    @UnitOfWork
    public Response saveCompanyOrderSubType(final UpdateStatusModel model) {

        final User user = new User();
        user.setId(model.getUserId());

        final Company company = new Company();
        company.setId(model.getCompanyId());

        final OrderSubType orderSubType = new OrderSubType();
        orderSubType.setId(model.getSubObjectId());

        final CompanyOrderSubType companyOrderSubType = new CompanyOrderSubType();
        companyOrderSubType.setCreatedDate(new Date());
        companyOrderSubType.setCreatedBy(user);
        companyOrderSubType.setStatus(model.getStatus());
        companyOrderSubType.setCompany(company);
        companyOrderSubType.setOrderSubType(orderSubType);
        dao.create(companyOrderSubType);

        return Response.status(200).entity(companyOrderSubType.getId()).build();
    }

    @PUT
    @Path("/updateCompanyOrderSubType/{companyOrderSubTypeId}")
    @ApiOperation(value = "Update company order sub type")
    @UnitOfWork
    public Response updateCompanyOrderSubType(
            @PathParam("companyOrderSubTypeId") final Long companyOrderSubTypeId,
            final UpdateStatusModel model) {

        final User user = new User();
        user.setId(model.getUserId());

        final CompanyOrderSubType companyOrderSubType = new CompanyOrderSubType();
        companyOrderSubType.setId(companyOrderSubTypeId);
        dao.load(companyOrderSubType);

        companyOrderSubType.setUpdatedDate(new Date());
        companyOrderSubType.setUpdatedBy(user);
        companyOrderSubType.setStatus(model.getStatus());
        dao.update(companyOrderSubType);

        return Response.status(200).entity(companyOrderSubType.getId()).build();
    }

}
