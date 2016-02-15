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
import com.leela.entity.CompanyPaymentType;
import com.leela.entity.PaymentType;
import com.leela.entity.User;
import com.leela.model.ObjectModel;
import com.leela.model.PaymentTypeModel;
import com.leela.model.UpdateStatusModel;
import com.leela.util.CommonsUtil;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;

import io.dropwizard.hibernate.UnitOfWork;

@Path("/paymentType")
@Api
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PaymentTypeResource {

    private final GenericDAO dao;

    public PaymentTypeResource(final SessionFactory sessionFactory) {
        this.dao = new HibernateDAO(sessionFactory);
    }

    @GET
    @Path("/getPaymentType/{companyId}")
    @ApiOperation(value = "Get payment type list")
    @UnitOfWork
    public Response getPaymentType(
            @PathParam("companyId") final Long companyId) {
        final Map<String, Object> options = new HashMap<String, Object>();
        options.put("companyId", companyId);

        @SuppressWarnings("unchecked")
        final List<Object[]> resultList = dao.findByNamedQuery(
                "get.all.payment.type.by.company", new QueryOptions(options));
        final List<ObjectModel> objectModels = CommonsUtil
                .convertToObjectModelWithStatus(resultList);
        return Response.status(200).entity(objectModels).build();
    }

    @SuppressWarnings("unchecked")
    @GET
    @Path("/validatePaymentType/{name}/{paymentTypeId}")
    @ApiOperation(value = "Validate payment type name")
    @UnitOfWork
    public Response validatePaymentType(@PathParam("name") final String name,
            @PathParam("paymentTypeId") final Long paymentTypeId) {
        List<Long> resultList = null;
        final Map<String, Object> options = new HashMap<String, Object>();
        options.put("name", name);
        if (paymentTypeId != null && paymentTypeId.longValue() != -1) {
            options.put("paymentTypeId", paymentTypeId);
            resultList = dao.findByNamedQuery("validate.name.payment.type.edit",
                    new QueryOptions(options));
        } else {
            resultList = dao.findByNamedQuery("validate.name.payment.type.add",
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
    @Path("/validatePaymentTypeByCompany/{companyId}/{name}/{paymentTypeId}")
    @ApiOperation(value = "Validate payment type name company wise")
    @UnitOfWork
    public Response validatePaymentTypeByCompany(
            @PathParam("companyId") final Long companyId,
            @PathParam("name") final String name,
            @PathParam("paymentTypeId") final Long paymentTypeId) {
        List<Long> resultList = null;
        final Map<String, Object> options = new HashMap<String, Object>();
        options.put("companyId", companyId);
        options.put("name", name);
        if (paymentTypeId != null && paymentTypeId.longValue() != -1) {
            options.put("paymentTypeId", paymentTypeId);
            resultList = dao.findByNamedQuery(
                    "validate.name.payment.type.by.company.edit",
                    new QueryOptions(options));
        } else {
            resultList = dao.findByNamedQuery(
                    "validate.name.payment.type.by.company.add",
                    new QueryOptions(options));
        }
        if (resultList != null && !resultList.isEmpty()
                && resultList.get(0) != null) {
            return Response.status(200).entity(resultList.get(0)).build();
        }
        return Response.status(200).entity("-1").build();
    }

    @POST
    @Path("/savePaymentType")
    @ApiOperation(value = "Add new payment type")
    @UnitOfWork
    public Response savePaymentType(final PaymentTypeModel model) {

        final User user = new User();
        user.setId(model.getUserId());

        final Company company = new Company();
        company.setId(model.getCompanyId());

        final PaymentType paymentType = new PaymentType();
        paymentType.setCreatedDate(new Date());
        paymentType.setCreatedBy(user);
        paymentType.setName(model.getName());
        paymentType.setStatus(model.getStatus());
        dao.create(paymentType);

        final CompanyPaymentType companyPaymentType = new CompanyPaymentType();
        companyPaymentType.setCreatedDate(new Date());
        companyPaymentType.setCreatedBy(user);
        companyPaymentType.setCompany(company);
        companyPaymentType.setPaymentType(paymentType);
        companyPaymentType.setStatus(CommonsUtil.ACTIVE_STATUS);
        dao.create(companyPaymentType);

        return Response.status(200).entity(paymentType.getId()).build();
    }

    @PUT
    @Path("/updatePaymentType/{paymentTypeId}")
    @ApiOperation(value = "Update payment type")
    @UnitOfWork
    public Response updatePaymentType(
            @PathParam("paymentTypeId") final Long paymentTypeId,
            final PaymentTypeModel model) {

        final User user = new User();
        user.setId(model.getUserId());

        final Company company = new Company();
        company.setId(model.getCompanyId());

        final PaymentType paymentType = new PaymentType();
        paymentType.setId(paymentTypeId);
        dao.load(paymentType);

        paymentType.setUpdatedDate(new Date());
        paymentType.setUpdatedBy(user);
        paymentType.setName(model.getName());
        paymentType.setStatus(model.getStatus());
        dao.update(paymentType);

        return Response.status(200).entity(paymentType.getId()).build();
    }

    @PUT
    @Path("/updatePaymentTypeStatus/{paymentTypeId}")
    @ApiOperation(value = "Get payment type list")
    @UnitOfWork
    public Response updatePaymentTypeStatus(
            @PathParam("paymentTypeId") final Long paymentTypeId,
            final UpdateStatusModel model) {

        final User user = new User();
        user.setId(model.getUserId());

        final PaymentType paymentType = new PaymentType();
        paymentType.setId(paymentTypeId);
        dao.load(paymentType);

        paymentType.setUpdatedDate(new Date());
        paymentType.setUpdatedBy(user);
        paymentType.setStatus(model.getStatus());
        dao.update(paymentType);

        return Response.status(200).entity(paymentType.getId()).build();
    }

    @GET
    @Path("/getAcivePaymentType/{companyId}")
    @ApiOperation(value = "Get active payment type list")
    @UnitOfWork
    public Response getAcivePaymentType(
            @PathParam("companyId") final Long companyId) {
        final Map<String, Object> options = new HashMap<String, Object>();
        options.put("companyId", companyId);

        @SuppressWarnings("unchecked")
        final List<Object[]> resultList = dao.findByNamedQuery(
                "get.active.payment.type.by.company",
                new QueryOptions(options));
        final List<ObjectModel> objectModels = CommonsUtil
                .convertToObjectModel(resultList);
        return Response.status(200).entity(objectModels).build();
    }

    @PUT
    @Path("/saveCompanyPaymentType")
    @ApiOperation(value = "Save new company payment type")
    @UnitOfWork
    public Response saveCompanyPaymentType(final UpdateStatusModel model) {

        final User user = new User();
        user.setId(model.getUserId());

        final Company company = new Company();
        company.setId(model.getCompanyId());

        final PaymentType paymentType = new PaymentType();
        paymentType.setId(model.getSubObjectId());

        final CompanyPaymentType companyPaymentType = new CompanyPaymentType();
        companyPaymentType.setCreatedDate(new Date());
        companyPaymentType.setCreatedBy(user);
        companyPaymentType.setStatus(model.getStatus());
        companyPaymentType.setCompany(company);
        companyPaymentType.setPaymentType(paymentType);
        dao.create(companyPaymentType);

        return Response.status(200).entity(companyPaymentType.getId()).build();
    }

    @PUT
    @Path("/updateCompanyPaymentType/{companyPaymentTypeId}")
    @ApiOperation(value = "Update company payment type")
    @UnitOfWork
    public Response updateCompanyPaymentType(
            @PathParam("companyPaymentTypeId") final Long companyPaymentTypeId,
            final UpdateStatusModel model) {

        final User user = new User();
        user.setId(model.getUserId());

        final CompanyPaymentType companyPaymentType = new CompanyPaymentType();
        companyPaymentType.setId(companyPaymentTypeId);
        dao.load(companyPaymentType);

        companyPaymentType.setUpdatedDate(new Date());
        companyPaymentType.setUpdatedBy(user);
        companyPaymentType.setStatus(model.getStatus());
        dao.update(companyPaymentType);

        return Response.status(200).entity(companyPaymentType.getId()).build();
    }

}
