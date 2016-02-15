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
import com.leela.entity.CompanyProductCategory;
import com.leela.entity.ProductCategory;
import com.leela.entity.User;
import com.leela.model.ObjectModel;
import com.leela.model.ProductCategoryModel;
import com.leela.model.UpdateStatusModel;
import com.leela.util.CommonsUtil;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;

import io.dropwizard.hibernate.UnitOfWork;

@Path("/productCategory")
@Api
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ProductCategoryResource {

    private final GenericDAO dao;

    public ProductCategoryResource(final SessionFactory sessionFactory) {
        this.dao = new HibernateDAO(sessionFactory);
    }

    @GET
    @Path("/getProductCategory/{companyId}")
    @ApiOperation(value = "Get product category list")
    @UnitOfWork
    public Response getProductCategory(
            @PathParam("companyId") final Long companyId) {
        final Map<String, Object> options = new HashMap<String, Object>();
        options.put("companyId", companyId);

        @SuppressWarnings("unchecked")
        final List<Object[]> resultList = dao.findByNamedQuery(
                "get.all.product.category.by.company",
                new QueryOptions(options));
        final List<ObjectModel> objectModels = CommonsUtil
                .convertToObjectModelWithStatus(resultList);
        return Response.status(200).entity(objectModels).build();
    }

    @SuppressWarnings("unchecked")
    @GET
    @Path("/validateProductCategory/{name}/{productCategoryId}")
    @ApiOperation(value = "Validate product category name")
    @UnitOfWork
    public Response validateProductCategory(
            @PathParam("name") final String name,
            @PathParam("productCategoryId") final Long productCategoryId) {
        List<Long> resultList = null;
        final Map<String, Object> options = new HashMap<String, Object>();
        options.put("name", name);
        if (productCategoryId != null && productCategoryId.longValue() != -1) {
            options.put("productCategoryId", productCategoryId);
            resultList = dao.findByNamedQuery(
                    "validate.name.product.category.edit",
                    new QueryOptions(options));
        } else {
            resultList = dao.findByNamedQuery(
                    "validate.name.product.category.add",
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
    @Path("/validateProductCategoryByCompany/{companyId}/{name}/{productCategoryId}")
    @ApiOperation(value = "Validate product category name company wise")
    @UnitOfWork
    public Response validateProductCategoryByCompany(
            @PathParam("companyId") final Long companyId,
            @PathParam("name") final String name,
            @PathParam("productCategoryId") final Long productCategoryId) {
        List<Long> resultList = null;
        final Map<String, Object> options = new HashMap<String, Object>();
        options.put("companyId", companyId);
        options.put("name", name);
        if (productCategoryId != null && productCategoryId.longValue() != -1) {
            options.put("productCategoryId", productCategoryId);
            resultList = dao.findByNamedQuery(
                    "validate.name.product.category.by.company.edit",
                    new QueryOptions(options));
        } else {
            resultList = dao.findByNamedQuery(
                    "validate.name.product.category.by.company.add",
                    new QueryOptions(options));
        }
        if (resultList != null && !resultList.isEmpty()
                && resultList.get(0) != null) {
            return Response.status(200).entity(resultList.get(0)).build();
        }
        return Response.status(200).entity("-1").build();
    }

    @POST
    @Path("/saveProductCategory")
    @ApiOperation(value = "Add new product category")
    @UnitOfWork
    public Response saveProductCategory(final ProductCategoryModel model) {

        final User user = new User();
        user.setId(model.getUserId());

        final Company company = new Company();
        company.setId(model.getCompanyId());

        final ProductCategory productCategory = new ProductCategory();
        productCategory.setCreatedDate(new Date());
        productCategory.setCreatedBy(user);
        productCategory.setName(model.getName());
        productCategory.setStatus(model.getStatus());
        dao.create(productCategory);

        final CompanyProductCategory companyProductCategory = new CompanyProductCategory();
        companyProductCategory.setCreatedDate(new Date());
        companyProductCategory.setCreatedBy(user);
        companyProductCategory.setCompany(company);
        companyProductCategory.setProductCategory(productCategory);
        companyProductCategory.setStatus(CommonsUtil.ACTIVE_STATUS);
        dao.create(companyProductCategory);

        return Response.status(200).entity(productCategory.getId()).build();
    }

    @PUT
    @Path("/updateProductCategory/{productCategoryId}")
    @ApiOperation(value = "Update product category")
    @UnitOfWork
    public Response updateProductCategory(
            @PathParam("productCategoryId") final Long productCategoryId,
            final ProductCategoryModel model) {

        final User user = new User();
        user.setId(model.getUserId());

        final Company company = new Company();
        company.setId(model.getCompanyId());

        final ProductCategory productCategory = new ProductCategory();
        productCategory.setId(productCategoryId);
        dao.load(productCategory);

        productCategory.setUpdatedDate(new Date());
        productCategory.setUpdatedBy(user);
        productCategory.setName(model.getName());
        productCategory.setStatus(model.getStatus());
        dao.update(productCategory);

        return Response.status(200).entity(productCategory.getId()).build();
    }

    @PUT
    @Path("/updateProductCategoryStatus/{productCategoryId}")
    @ApiOperation(value = "Get product category list")
    @UnitOfWork
    public Response updateProductCategoryStatus(
            @PathParam("productCategoryId") final Long productCategoryId,
            final UpdateStatusModel model) {

        final User user = new User();
        user.setId(model.getUserId());

        final ProductCategory productCategory = new ProductCategory();
        productCategory.setId(productCategoryId);
        dao.load(productCategory);

        productCategory.setUpdatedDate(new Date());
        productCategory.setUpdatedBy(user);
        productCategory.setStatus(model.getStatus());
        dao.update(productCategory);

        return Response.status(200).entity(productCategory.getId()).build();
    }

    @GET
    @Path("/getAciveProductCategory/{companyId}")
    @ApiOperation(value = "Get active product category list")
    @UnitOfWork
    public Response getAciveProductCategory(
            @PathParam("companyId") final Long companyId) {
        final Map<String, Object> options = new HashMap<String, Object>();
        options.put("companyId", companyId);

        @SuppressWarnings("unchecked")
        final List<Object[]> resultList = dao.findByNamedQuery(
                "get.active.product.category.by.company",
                new QueryOptions(options));
        final List<ObjectModel> objectModels = CommonsUtil
                .convertToObjectModel(resultList);
        return Response.status(200).entity(objectModels).build();
    }

    @PUT
    @Path("/saveCompanyProductCategory")
    @ApiOperation(value = "Save new company product category")
    @UnitOfWork
    public Response saveCompanyProductCategory(final UpdateStatusModel model) {

        final User user = new User();
        user.setId(model.getUserId());

        final Company company = new Company();
        company.setId(model.getCompanyId());

        final ProductCategory productCategory = new ProductCategory();
        productCategory.setId(model.getSubObjectId());

        final CompanyProductCategory companyProductCategory = new CompanyProductCategory();
        companyProductCategory.setCreatedDate(new Date());
        companyProductCategory.setCreatedBy(user);
        companyProductCategory.setStatus(model.getStatus());
        companyProductCategory.setCompany(company);
        companyProductCategory.setProductCategory(productCategory);
        dao.create(companyProductCategory);

        return Response.status(200).entity(companyProductCategory.getId())
                .build();
    }

    @PUT
    @Path("/updateCompanyProductCategory/{companyProductCategoryId}")
    @ApiOperation(value = "Update company product category")
    @UnitOfWork
    public Response updateCompanyProductCategory(
            @PathParam("companyProductCategoryId") final Long companyProductCategoryId,
            final UpdateStatusModel model) {

        final User user = new User();
        user.setId(model.getUserId());

        final CompanyProductCategory companyProductCategory = new CompanyProductCategory();
        companyProductCategory.setId(companyProductCategoryId);
        dao.load(companyProductCategory);

        companyProductCategory.setUpdatedDate(new Date());
        companyProductCategory.setUpdatedBy(user);
        companyProductCategory.setStatus(model.getStatus());
        dao.update(companyProductCategory);

        return Response.status(200).entity(companyProductCategory.getId())
                .build();
    }

}
