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
import com.leela.entity.CompanyOrderStatus;
import com.leela.entity.OrderStatus;
import com.leela.entity.User;
import com.leela.model.ObjectModel;
import com.leela.model.OrderStatusModel;
import com.leela.model.UpdateStatusModel;
import com.leela.util.CommonsUtil;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;

import io.dropwizard.hibernate.UnitOfWork;

@Path("/orderStatus")
@Api
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class OrderStatusResource {

    private final GenericDAO dao;

    public OrderStatusResource(final SessionFactory sessionFactory) {
        this.dao = new HibernateDAO(sessionFactory);
    }

    @GET
    @Path("/getOrderStatus/{companyId}")
    @ApiOperation(value = "Get order status list")
    @UnitOfWork
    public Response getOrderStatus(
            @PathParam("companyId") final Long companyId) {
        final Map<String, Object> options = new HashMap<String, Object>();
        options.put("companyId", companyId);

        @SuppressWarnings("unchecked")
        final List<Object[]> resultList = dao.findByNamedQuery(
                "get.all.order.status.by.company", new QueryOptions(options));
        final List<ObjectModel> objectModels = CommonsUtil
                .convertToObjectModelWithStatus(resultList);
        return Response.status(200).entity(objectModels).build();
    }

    @SuppressWarnings("unchecked")
    @GET
    @Path("/validateOrderStatus/{name}/{orderStatusId}")
    @ApiOperation(value = "Validate order status name")
    @UnitOfWork
    public Response validateOrderStatus(@PathParam("name") final String name,
            @PathParam("orderStatusId") final Long orderStatusId) {
        List<Long> resultList = null;
        final Map<String, Object> options = new HashMap<String, Object>();
        options.put("name", name);
        if (orderStatusId != null && orderStatusId.longValue() != -1) {
            options.put("orderStatusId", orderStatusId);
            resultList = dao.findByNamedQuery("validate.name.order.status.edit",
                    new QueryOptions(options));
        } else {
            resultList = dao.findByNamedQuery("validate.name.order.status.add",
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
    @Path("/validateOrderStatusByCompany/{companyId}/{name}/{orderStatusId}")
    @ApiOperation(value = "Validate order status name company wise")
    @UnitOfWork
    public Response validateOrderStatusByCompany(
            @PathParam("companyId") final Long companyId,
            @PathParam("name") final String name,
            @PathParam("orderStatusId") final Long orderStatusId) {
        List<Long> resultList = null;
        final Map<String, Object> options = new HashMap<String, Object>();
        options.put("companyId", companyId);
        options.put("name", name);
        if (orderStatusId != null && orderStatusId.longValue() != -1) {
            options.put("orderStatusId", orderStatusId);
            resultList = dao.findByNamedQuery(
                    "validate.name.order.status.by.company.edit",
                    new QueryOptions(options));
        } else {
            resultList = dao.findByNamedQuery(
                    "validate.name.order.status.by.company.add",
                    new QueryOptions(options));
        }
        if (resultList != null && !resultList.isEmpty()
                && resultList.get(0) != null) {
            return Response.status(200).entity(resultList.get(0)).build();
        }
        return Response.status(200).entity("-1").build();
    }

    @POST
    @Path("/saveOrderStatus")
    @ApiOperation(value = "Add new order status")
    @UnitOfWork
    public Response saveOrderStatus(final OrderStatusModel model) {

        final User user = new User();
        user.setId(model.getUserId());

        final Company company = new Company();
        company.setId(model.getCompanyId());

        final OrderStatus orderStatus = new OrderStatus();
        orderStatus.setCreatedDate(new Date());
        orderStatus.setCreatedBy(user);
        orderStatus.setName(model.getName());
        orderStatus.setStatus(model.getStatus());
        dao.create(orderStatus);

        final CompanyOrderStatus companyOrderStatus = new CompanyOrderStatus();
        companyOrderStatus.setCreatedDate(new Date());
        companyOrderStatus.setCreatedBy(user);
        companyOrderStatus.setCompany(company);
        companyOrderStatus.setOrderStatus(orderStatus);
        companyOrderStatus.setStatus(CommonsUtil.ACTIVE_STATUS);
        dao.create(companyOrderStatus);

        return Response.status(200).entity(orderStatus.getId()).build();
    }

    @PUT
    @Path("/updateOrderStatus/{orderStatusId}")
    @ApiOperation(value = "Update order status")
    @UnitOfWork
    public Response updateOrderStatus(
            @PathParam("orderStatusId") final Long orderStatusId,
            final OrderStatusModel model) {

        final User user = new User();
        user.setId(model.getUserId());

        final Company company = new Company();
        company.setId(model.getCompanyId());

        final OrderStatus orderStatus = new OrderStatus();
        orderStatus.setId(orderStatusId);
        dao.load(orderStatus);

        orderStatus.setUpdatedDate(new Date());
        orderStatus.setUpdatedBy(user);
        orderStatus.setName(model.getName());
        orderStatus.setStatus(model.getStatus());
        dao.update(orderStatus);

        return Response.status(200).entity(orderStatus.getId()).build();
    }

    @PUT
    @Path("/updateOrderStatusStatus/{orderStatusId}")
    @ApiOperation(value = "Get order status list")
    @UnitOfWork
    public Response updateOrderStatusStatus(
            @PathParam("orderStatusId") final Long orderStatusId,
            final UpdateStatusModel model) {

        final User user = new User();
        user.setId(model.getUserId());

        final OrderStatus orderStatus = new OrderStatus();
        orderStatus.setId(orderStatusId);
        dao.load(orderStatus);

        orderStatus.setUpdatedDate(new Date());
        orderStatus.setUpdatedBy(user);
        orderStatus.setStatus(model.getStatus());
        dao.update(orderStatus);

        return Response.status(200).entity(orderStatus.getId()).build();
    }

    @GET
    @Path("/getAciveOrderStatus/{companyId}")
    @ApiOperation(value = "Get active order status list")
    @UnitOfWork
    public Response getAciveOrderStatus(
            @PathParam("companyId") final Long companyId) {
        final Map<String, Object> options = new HashMap<String, Object>();
        options.put("companyId", companyId);

        @SuppressWarnings("unchecked")
        final List<Object[]> resultList = dao.findByNamedQuery(
                "get.active.order.status.by.company",
                new QueryOptions(options));
        final List<ObjectModel> objectModels = CommonsUtil
                .convertToObjectModel(resultList);
        return Response.status(200).entity(objectModels).build();
    }

    @PUT
    @Path("/saveCompanyOrderStatus")
    @ApiOperation(value = "Save new company order status")
    @UnitOfWork
    public Response saveCompanyOrderStatus(final UpdateStatusModel model) {

        final User user = new User();
        user.setId(model.getUserId());

        final Company company = new Company();
        company.setId(model.getCompanyId());

        final OrderStatus orderStatus = new OrderStatus();
        orderStatus.setId(model.getSubObjectId());

        final CompanyOrderStatus companyOrderStatus = new CompanyOrderStatus();
        companyOrderStatus.setCreatedDate(new Date());
        companyOrderStatus.setCreatedBy(user);
        companyOrderStatus.setStatus(model.getStatus());
        companyOrderStatus.setCompany(company);
        companyOrderStatus.setOrderStatus(orderStatus);
        dao.create(companyOrderStatus);

        return Response.status(200).entity(companyOrderStatus.getId()).build();
    }

    @PUT
    @Path("/updateCompanyOrderStatus/{companyOrderStatusId}")
    @ApiOperation(value = "Update company order status")
    @UnitOfWork
    public Response updateCompanyOrderStatus(
            @PathParam("companyOrderStatusId") final Long companyOrderStatusId,
            final UpdateStatusModel model) {

        final User user = new User();
        user.setId(model.getUserId());

        final CompanyOrderStatus companyOrderStatus = new CompanyOrderStatus();
        companyOrderStatus.setId(companyOrderStatusId);
        dao.load(companyOrderStatus);

        companyOrderStatus.setUpdatedDate(new Date());
        companyOrderStatus.setUpdatedBy(user);
        companyOrderStatus.setStatus(model.getStatus());
        dao.update(companyOrderStatus);

        return Response.status(200).entity(companyOrderStatus.getId()).build();
    }

}
