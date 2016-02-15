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
import com.leela.entity.CompanyProductSubType;
import com.leela.entity.ProductSubType;
import com.leela.entity.ProductType;
import com.leela.entity.User;
import com.leela.model.ObjectModel;
import com.leela.model.ProductSubTypeModel;
import com.leela.model.UpdateStatusModel;
import com.leela.util.CommonsUtil;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;

import io.dropwizard.hibernate.UnitOfWork;

@Path("/productSubType")
@Api
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ProductSubTypeResource {

    private final GenericDAO dao;

    public ProductSubTypeResource(final SessionFactory sessionFactory) {
        this.dao = new HibernateDAO(sessionFactory);
    }

    @GET
    @Path("/getProductSubType/{companyId}/{productTypeId}")
    @ApiOperation(value = "Get product sub type list")
    @UnitOfWork
    public Response getProductSubType(
            @PathParam("companyId") final Long companyId,
            @PathParam("productTypeId") final Long productTypeId) {
        final Map<String, Object> options = new HashMap<String, Object>();
        options.put("companyId", companyId);
        options.put("productTypeId", productTypeId);

        @SuppressWarnings("unchecked")
        final List<Object[]> resultList = dao.findByNamedQuery(
                "get.all.product.sub.type.by.product.type",
                new QueryOptions(options));
        final List<ObjectModel> objectModels = CommonsUtil
                .convertToObjectModelWithStatus(resultList);
        return Response.status(200).entity(objectModels).build();
    }

    @SuppressWarnings("unchecked")
    @GET
    @Path("/validateProductSubType/{name}/{productSubTypeId}")
    @ApiOperation(value = "Validate product sub type name")
    @UnitOfWork
    public Response validateProductSubType(@PathParam("name") final String name,
            @PathParam("productSubTypeId") final Long productSubTypeId) {
        List<Long> resultList = null;
        final Map<String, Object> options = new HashMap<String, Object>();
        options.put("name", name);
        if (productSubTypeId != null && productSubTypeId.longValue() != -1) {
            options.put("productSubTypeId", productSubTypeId);
            resultList = dao.findByNamedQuery(
                    "validate.name.product.sub.type.edit",
                    new QueryOptions(options));
        } else {
            resultList = dao.findByNamedQuery(
                    "validate.name.product.sub.type.add",
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
    @Path("/validateProductSubTypeByCompany/{companyId}/{name}/{productSubTypeId}")
    @ApiOperation(value = "Validate product sub type name company wise")
    @UnitOfWork
    public Response validateProductSubTypeByCompany(
            @PathParam("companyId") final Long companyId,
            @PathParam("name") final String name,
            @PathParam("productSubTypeId") final Long productSubTypeId) {
        List<Long> resultList = null;
        final Map<String, Object> options = new HashMap<String, Object>();
        options.put("companyId", companyId);
        options.put("name", name);
        if (productSubTypeId != null && productSubTypeId.longValue() != -1) {
            options.put("productSubTypeId", productSubTypeId);
            resultList = dao.findByNamedQuery(
                    "validate.name.product.sub.type.by.company.edit",
                    new QueryOptions(options));
        } else {
            resultList = dao.findByNamedQuery(
                    "validate.name.product.sub.type.by.company.add",
                    new QueryOptions(options));
        }
        if (resultList != null && !resultList.isEmpty()
                && resultList.get(0) != null) {
            return Response.status(200).entity(resultList.get(0)).build();
        }
        return Response.status(200).entity("-1").build();
    }

    @POST
    @Path("/saveProductSubType")
    @ApiOperation(value = "Add new product sub type")
    @UnitOfWork
    public Response saveProductSubType(final ProductSubTypeModel model) {

        final User user = new User();
        user.setId(model.getUserId());

        final Company company = new Company();
        company.setId(model.getCompanyId());

        final ProductType productType = new ProductType();
        productType.setId(model.getProductTypeId());

        final ProductSubType productSubType = new ProductSubType();
        productSubType.setCreatedDate(new Date());
        productSubType.setCreatedBy(user);
        productSubType.setName(model.getName());
        productSubType.setProductType(productType);
        productSubType.setStatus(model.getStatus());
        dao.create(productSubType);

        final CompanyProductSubType companyProductSubType = new CompanyProductSubType();
        companyProductSubType.setCreatedDate(new Date());
        companyProductSubType.setCreatedBy(user);
        companyProductSubType.setCompany(company);
        companyProductSubType.setProductSubType(productSubType);
        companyProductSubType.setStatus(CommonsUtil.ACTIVE_STATUS);
        dao.create(companyProductSubType);

        return Response.status(200).entity(productSubType.getId()).build();
    }

    @PUT
    @Path("/updateProductSubType/{productSubTypeId}")
    @ApiOperation(value = "Update product sub type")
    @UnitOfWork
    public Response updateProductSubType(
            @PathParam("productSubTypeId") final Long productSubTypeId,
            final ProductSubTypeModel model) {

        final User user = new User();
        user.setId(model.getUserId());

        final Company company = new Company();
        company.setId(model.getCompanyId());

        final ProductType productType = new ProductType();
        productType.setId(model.getProductTypeId());

        final ProductSubType productSubType = new ProductSubType();
        productSubType.setId(productSubTypeId);
        dao.load(productSubType);

        productSubType.setUpdatedDate(new Date());
        productSubType.setUpdatedBy(user);
        productSubType.setName(model.getName());
        productSubType.setProductType(productType);
        productSubType.setStatus(model.getStatus());
        dao.update(productSubType);

        return Response.status(200).entity(productSubType.getId()).build();
    }

    @PUT
    @Path("/updateProductSubTypeStatus/{productSubTypeId}")
    @ApiOperation(value = "Get product sub type list")
    @UnitOfWork
    public Response updateProductSubTypeStatus(
            @PathParam("productSubTypeId") final Long productSubTypeId,
            final UpdateStatusModel model) {

        final User user = new User();
        user.setId(model.getUserId());

        final ProductSubType productSubType = new ProductSubType();
        productSubType.setId(productSubTypeId);
        dao.load(productSubType);

        productSubType.setUpdatedDate(new Date());
        productSubType.setUpdatedBy(user);
        productSubType.setStatus(model.getStatus());
        dao.update(productSubType);

        return Response.status(200).entity(productSubType.getId()).build();
    }

    @GET
    @Path("/getAciveProductSubType/{companyId}/{productTypeId}")
    @ApiOperation(value = "Get active product sub type list by product type")
    @UnitOfWork
    public Response getAciveProductSubType(
            @PathParam("companyId") final Long companyId,
            @PathParam("productTypeId") final Long productTypeId) {
        final Map<String, Object> options = new HashMap<String, Object>();
        options.put("companyId", companyId);
        options.put("productTypeId", productTypeId);

        @SuppressWarnings("unchecked")
        final List<Object[]> resultList = dao.findByNamedQuery(
                "get.active.product.sub.type.by.product.type",
                new QueryOptions(options));
        final List<ObjectModel> objectModels = CommonsUtil
                .convertToObjectModel(resultList);
        return Response.status(200).entity(objectModels).build();
    }

    @PUT
    @Path("/saveCompanyProductSubType")
    @ApiOperation(value = "Save new company product sub type")
    @UnitOfWork
    public Response saveCompanyProductSubType(final UpdateStatusModel model) {

        final User user = new User();
        user.setId(model.getUserId());

        final Company company = new Company();
        company.setId(model.getCompanyId());

        final ProductSubType productSubType = new ProductSubType();
        productSubType.setId(model.getSubObjectId());

        final CompanyProductSubType companyProductSubType = new CompanyProductSubType();
        companyProductSubType.setCreatedDate(new Date());
        companyProductSubType.setCreatedBy(user);
        companyProductSubType.setStatus(model.getStatus());
        companyProductSubType.setCompany(company);
        companyProductSubType.setProductSubType(productSubType);
        dao.create(companyProductSubType);

        return Response.status(200).entity(companyProductSubType.getId())
                .build();
    }

    @PUT
    @Path("/updateCompanyProductSubType/{companyProductSubTypeId}")
    @ApiOperation(value = "Update company product sub type")
    @UnitOfWork
    public Response updateCompanyProductSubType(
            @PathParam("companyProductSubTypeId") final Long companyProductSubTypeId,
            final UpdateStatusModel model) {

        final User user = new User();
        user.setId(model.getUserId());

        final CompanyProductSubType companyProductSubType = new CompanyProductSubType();
        companyProductSubType.setId(companyProductSubTypeId);
        dao.load(companyProductSubType);

        companyProductSubType.setUpdatedDate(new Date());
        companyProductSubType.setUpdatedBy(user);
        companyProductSubType.setStatus(model.getStatus());
        dao.update(companyProductSubType);

        return Response.status(200).entity(companyProductSubType.getId())
                .build();
    }

}
