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
import com.leela.entity.CompanyProductType;
import com.leela.entity.ProductType;
import com.leela.entity.User;
import com.leela.model.ObjectModel;
import com.leela.model.ProductTypeModel;
import com.leela.model.UpdateStatusModel;
import com.leela.util.CommonsUtil;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;

import io.dropwizard.hibernate.UnitOfWork;

@Path("/productType")
@Api
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ProductTypeResource {

    private final GenericDAO dao;

    public ProductTypeResource(final SessionFactory sessionFactory) {
        this.dao = new HibernateDAO(sessionFactory);
    }

    @GET
    @Path("/getProductType/{companyId}")
    @ApiOperation(value = "Get product type list")
    @UnitOfWork
    public Response getProductType(
            @PathParam("companyId") final Long companyId) {
        final Map<String, Object> options = new HashMap<String, Object>();
        options.put("companyId", companyId);

        @SuppressWarnings("unchecked")
        final List<Object[]> resultList = dao.findByNamedQuery(
                "get.all.product.type.by.company", new QueryOptions(options));
        final List<ObjectModel> objectModels = CommonsUtil
                .convertToObjectModelWithStatus(resultList);
        return Response.status(200).entity(objectModels).build();
    }

    @SuppressWarnings("unchecked")
    @GET
    @Path("/validateProductType/{name}/{productTypeId}")
    @ApiOperation(value = "Validate product type name")
    @UnitOfWork
    public Response validateProductType(@PathParam("name") final String name,
            @PathParam("productTypeId") final Long productTypeId) {
        List<Long> resultList = null;
        final Map<String, Object> options = new HashMap<String, Object>();
        options.put("name", name);
        if (productTypeId != null && productTypeId.longValue() != -1) {
            options.put("productTypeId", productTypeId);
            resultList = dao.findByNamedQuery("validate.name.product.type.edit",
                    new QueryOptions(options));
        } else {
            resultList = dao.findByNamedQuery("validate.name.product.type.add",
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
    @Path("/validateProductTypeByCompany/{companyId}/{name}/{productTypeId}")
    @ApiOperation(value = "Validate product type name company wise")
    @UnitOfWork
    public Response validateProductTypeByCompany(
            @PathParam("companyId") final Long companyId,
            @PathParam("name") final String name,
            @PathParam("productTypeId") final Long productTypeId) {
        List<Long> resultList = null;
        final Map<String, Object> options = new HashMap<String, Object>();
        options.put("companyId", companyId);
        options.put("name", name);
        if (productTypeId != null && productTypeId.longValue() != -1) {
            options.put("productTypeId", productTypeId);
            resultList = dao.findByNamedQuery(
                    "validate.name.product.type.by.company.edit",
                    new QueryOptions(options));
        } else {
            resultList = dao.findByNamedQuery(
                    "validate.name.product.type.by.company.add",
                    new QueryOptions(options));
        }
        if (resultList != null && !resultList.isEmpty()
                && resultList.get(0) != null) {
            return Response.status(200).entity(resultList.get(0)).build();
        }
        return Response.status(200).entity("-1").build();
    }

    @POST
    @Path("/saveProductType")
    @ApiOperation(value = "Add new product type")
    @UnitOfWork
    public Response saveProductType(final ProductTypeModel model) {

        final User user = new User();
        user.setId(model.getUserId());

        final Company company = new Company();
        company.setId(model.getCompanyId());

        final ProductType productType = new ProductType();
        productType.setCreatedDate(new Date());
        productType.setCreatedBy(user);
        productType.setName(model.getName());
        productType.setStatus(model.getStatus());
        dao.create(productType);

        final CompanyProductType companyProductType = new CompanyProductType();
        companyProductType.setCreatedDate(new Date());
        companyProductType.setCreatedBy(user);
        companyProductType.setCompany(company);
        companyProductType.setProductType(productType);
        companyProductType.setStatus(CommonsUtil.ACTIVE_STATUS);
        dao.create(companyProductType);

        return Response.status(200).entity(productType.getId()).build();
    }

    @PUT
    @Path("/updateProductType/{productTypeId}")
    @ApiOperation(value = "Update product type")
    @UnitOfWork
    public Response updateProductType(
            @PathParam("productTypeId") final Long productTypeId,
            final ProductTypeModel model) {

        final User user = new User();
        user.setId(model.getUserId());

        final Company company = new Company();
        company.setId(model.getCompanyId());

        final ProductType productType = new ProductType();
        productType.setId(productTypeId);
        dao.load(productType);

        productType.setUpdatedDate(new Date());
        productType.setUpdatedBy(user);
        productType.setName(model.getName());
        productType.setStatus(model.getStatus());
        dao.update(productType);

        return Response.status(200).entity(productType.getId()).build();
    }

    @PUT
    @Path("/updateProductTypeStatus/{productTypeId}")
    @ApiOperation(value = "Get product type list")
    @UnitOfWork
    public Response updateProductTypeStatus(
            @PathParam("productTypeId") final Long productTypeId,
            final UpdateStatusModel model) {

        final User user = new User();
        user.setId(model.getUserId());

        final ProductType productType = new ProductType();
        productType.setId(productTypeId);
        dao.load(productType);

        productType.setUpdatedDate(new Date());
        productType.setUpdatedBy(user);
        productType.setStatus(model.getStatus());
        dao.update(productType);

        return Response.status(200).entity(productType.getId()).build();
    }

    @GET
    @Path("/getAciveProductType/{companyId}")
    @ApiOperation(value = "Get active product type list")
    @UnitOfWork
    public Response getAciveProductType(
            @PathParam("companyId") final Long companyId) {
        final Map<String, Object> options = new HashMap<String, Object>();
        options.put("companyId", companyId);

        @SuppressWarnings("unchecked")
        final List<Object[]> resultList = dao.findByNamedQuery(
                "get.active.product.type.by.company",
                new QueryOptions(options));
        final List<ObjectModel> objectModels = CommonsUtil
                .convertToObjectModel(resultList);
        return Response.status(200).entity(objectModels).build();
    }

    @PUT
    @Path("/saveCompanyProductType")
    @ApiOperation(value = "Save new company product type")
    @UnitOfWork
    public Response saveCompanyProductType(final UpdateStatusModel model) {

        final User user = new User();
        user.setId(model.getUserId());

        final Company company = new Company();
        company.setId(model.getCompanyId());

        final ProductType productType = new ProductType();
        productType.setId(model.getSubObjectId());

        final CompanyProductType companyProductType = new CompanyProductType();
        companyProductType.setCreatedDate(new Date());
        companyProductType.setCreatedBy(user);
        companyProductType.setStatus(model.getStatus());
        companyProductType.setCompany(company);
        companyProductType.setProductType(productType);
        dao.create(companyProductType);

        return Response.status(200).entity(companyProductType.getId()).build();
    }

    @PUT
    @Path("/updateCompanyProductType/{companyProductTypeId}")
    @ApiOperation(value = "Update company product type")
    @UnitOfWork
    public Response updateCompanyProductType(
            @PathParam("companyProductTypeId") final Long companyProductTypeId,
            final UpdateStatusModel model) {

        final User user = new User();
        user.setId(model.getUserId());

        final CompanyProductType companyProductType = new CompanyProductType();
        companyProductType.setId(companyProductTypeId);
        dao.load(companyProductType);

        companyProductType.setUpdatedDate(new Date());
        companyProductType.setUpdatedBy(user);
        companyProductType.setStatus(model.getStatus());
        dao.update(companyProductType);

        return Response.status(200).entity(companyProductType.getId()).build();
    }

}
