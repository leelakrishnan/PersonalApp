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
import com.leela.entity.CompanyProductSubCategory;
import com.leela.entity.ProductCategory;
import com.leela.entity.ProductSubCategory;
import com.leela.entity.User;
import com.leela.model.ObjectModel;
import com.leela.model.ProductSubCategoryModel;
import com.leela.model.UpdateStatusModel;
import com.leela.util.CommonsUtil;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;

import io.dropwizard.hibernate.UnitOfWork;

@Path("/productSubCategory")
@Api
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ProductSubCategoryResource {

    private final GenericDAO dao;

    public ProductSubCategoryResource(final SessionFactory sessionFactory) {
        this.dao = new HibernateDAO(sessionFactory);
    }

    @GET
    @Path("/getProductSubCategory/{companyId}/{productCategoryId}")
    @ApiOperation(value = "Get product sub category list")
    @UnitOfWork
    public Response getProductSubCategory(
            @PathParam("companyId") final Long companyId,
            @PathParam("productCategoryId") final Long productCategoryId) {
        final Map<String, Object> options = new HashMap<String, Object>();
        options.put("companyId", companyId);
        options.put("productCategoryId", productCategoryId);

        @SuppressWarnings("unchecked")
        final List<Object[]> resultList = dao.findByNamedQuery(
                "get.all.product.sub.category.by.product.category",
                new QueryOptions(options));
        final List<ObjectModel> objectModels = CommonsUtil
                .convertToObjectModelWithStatus(resultList);
        return Response.status(200).entity(objectModels).build();
    }

    @SuppressWarnings("unchecked")
    @GET
    @Path("/validateProductSubCategory/{name}/{productSubCategoryId}")
    @ApiOperation(value = "Validate product sub category name")
    @UnitOfWork
    public Response validateProductSubCategory(
            @PathParam("name") final String name,
            @PathParam("productSubCategoryId") final Long productSubCategoryId) {
        List<Long> resultList = null;
        final Map<String, Object> options = new HashMap<String, Object>();
        options.put("name", name);
        if (productSubCategoryId != null
                && productSubCategoryId.longValue() != -1) {
            options.put("productSubCategoryId", productSubCategoryId);
            resultList = dao.findByNamedQuery(
                    "validate.name.product.sub.category.edit",
                    new QueryOptions(options));
        } else {
            resultList = dao.findByNamedQuery(
                    "validate.name.product.sub.category.add",
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
    @Path("/validateProductSubCategoryByCompany/{companyId}/{name}/{productSubCategoryId}")
    @ApiOperation(value = "Validate product sub category name company wise")
    @UnitOfWork
    public Response validateProductSubCategoryByCompany(
            @PathParam("companyId") final Long companyId,
            @PathParam("name") final String name,
            @PathParam("productSubCategoryId") final Long productSubCategoryId) {
        List<Long> resultList = null;
        final Map<String, Object> options = new HashMap<String, Object>();
        options.put("companyId", companyId);
        options.put("name", name);
        if (productSubCategoryId != null
                && productSubCategoryId.longValue() != -1) {
            options.put("productSubCategoryId", productSubCategoryId);
            resultList = dao.findByNamedQuery(
                    "validate.name.product.sub.category.by.company.edit",
                    new QueryOptions(options));
        } else {
            resultList = dao.findByNamedQuery(
                    "validate.name.product.sub.category.by.company.add",
                    new QueryOptions(options));
        }
        if (resultList != null && !resultList.isEmpty()
                && resultList.get(0) != null) {
            return Response.status(200).entity(resultList.get(0)).build();
        }
        return Response.status(200).entity("-1").build();
    }

    @POST
    @Path("/saveProductSubCategory")
    @ApiOperation(value = "Add new product sub category")
    @UnitOfWork
    public Response saveProductSubCategory(
            final ProductSubCategoryModel model) {

        final User user = new User();
        user.setId(model.getUserId());

        final Company company = new Company();
        company.setId(model.getCompanyId());

        final ProductCategory productCategory = new ProductCategory();
        productCategory.setId(model.getProductCategoryId());

        final ProductSubCategory productSubCategory = new ProductSubCategory();
        productSubCategory.setCreatedDate(new Date());
        productSubCategory.setCreatedBy(user);
        productSubCategory.setName(model.getName());
        productSubCategory.setProductCategory(productCategory);
        productSubCategory.setStatus(model.getStatus());
        dao.create(productSubCategory);

        final CompanyProductSubCategory companyProductSubCategory = new CompanyProductSubCategory();
        companyProductSubCategory.setCreatedDate(new Date());
        companyProductSubCategory.setCreatedBy(user);
        companyProductSubCategory.setCompany(company);
        companyProductSubCategory.setProductSubCategory(productSubCategory);
        companyProductSubCategory.setStatus(CommonsUtil.ACTIVE_STATUS);
        dao.create(companyProductSubCategory);

        return Response.status(200).entity(productSubCategory.getId()).build();
    }

    @PUT
    @Path("/updateProductSubCategory/{productSubCategoryId}")
    @ApiOperation(value = "Update product sub category")
    @UnitOfWork
    public Response updateProductSubCategory(
            @PathParam("productSubCategoryId") final Long productSubCategoryId,
            final ProductSubCategoryModel model) {

        final User user = new User();
        user.setId(model.getUserId());

        final Company company = new Company();
        company.setId(model.getCompanyId());

        final ProductCategory productCategory = new ProductCategory();
        productCategory.setId(model.getProductCategoryId());

        final ProductSubCategory productSubCategory = new ProductSubCategory();
        productSubCategory.setId(productSubCategoryId);
        dao.load(productSubCategory);

        productSubCategory.setUpdatedDate(new Date());
        productSubCategory.setUpdatedBy(user);
        productSubCategory.setName(model.getName());
        productSubCategory.setProductCategory(productCategory);
        productSubCategory.setStatus(model.getStatus());
        dao.update(productSubCategory);

        return Response.status(200).entity(productSubCategory.getId()).build();
    }

    @PUT
    @Path("/updateProductSubCategoryStatus/{productSubCategoryId}")
    @ApiOperation(value = "Get product sub category list")
    @UnitOfWork
    public Response updateProductSubCategoryStatus(
            @PathParam("productSubCategoryId") final Long productSubCategoryId,
            final UpdateStatusModel model) {

        final User user = new User();
        user.setId(model.getUserId());

        final ProductSubCategory productSubCategory = new ProductSubCategory();
        productSubCategory.setId(productSubCategoryId);
        dao.load(productSubCategory);

        productSubCategory.setUpdatedDate(new Date());
        productSubCategory.setUpdatedBy(user);
        productSubCategory.setStatus(model.getStatus());
        dao.update(productSubCategory);

        return Response.status(200).entity(productSubCategory.getId()).build();
    }

    @GET
    @Path("/getAciveProductSubCategory/{companyId}/{productCategoryId}")
    @ApiOperation(value = "Get active product sub category list by product category")
    @UnitOfWork
    public Response getAciveProductSubCategory(
            @PathParam("companyId") final Long companyId,
            @PathParam("productCategoryId") final Long productCategoryId) {
        final Map<String, Object> options = new HashMap<String, Object>();
        options.put("companyId", companyId);
        options.put("productCategoryId", productCategoryId);

        @SuppressWarnings("unchecked")
        final List<Object[]> resultList = dao.findByNamedQuery(
                "get.active.product.sub.category.by.product.category",
                new QueryOptions(options));
        final List<ObjectModel> objectModels = CommonsUtil
                .convertToObjectModel(resultList);
        return Response.status(200).entity(objectModels).build();
    }

    @PUT
    @Path("/saveCompanyProductSubCategory")
    @ApiOperation(value = "Save new company product sub category")
    @UnitOfWork
    public Response saveCompanyProductSubCategory(
            final UpdateStatusModel model) {

        final User user = new User();
        user.setId(model.getUserId());

        final Company company = new Company();
        company.setId(model.getCompanyId());

        final ProductSubCategory productSubCategory = new ProductSubCategory();
        productSubCategory.setId(model.getSubObjectId());

        final CompanyProductSubCategory companyProductSubCategory = new CompanyProductSubCategory();
        companyProductSubCategory.setCreatedDate(new Date());
        companyProductSubCategory.setCreatedBy(user);
        companyProductSubCategory.setStatus(model.getStatus());
        companyProductSubCategory.setCompany(company);
        companyProductSubCategory.setProductSubCategory(productSubCategory);
        dao.create(companyProductSubCategory);

        return Response.status(200).entity(companyProductSubCategory.getId())
                .build();
    }

    @PUT
    @Path("/updateCompanyProductSubCategory/{companyProductSubCategoryId}")
    @ApiOperation(value = "Update company product sub category")
    @UnitOfWork
    public Response updateCompanyProductSubCategory(
            @PathParam("companyProductSubCategoryId") final Long companyProductSubCategoryId,
            final UpdateStatusModel model) {

        final User user = new User();
        user.setId(model.getUserId());

        final CompanyProductSubCategory companyProductSubCategory = new CompanyProductSubCategory();
        companyProductSubCategory.setId(companyProductSubCategoryId);
        dao.load(companyProductSubCategory);

        companyProductSubCategory.setUpdatedDate(new Date());
        companyProductSubCategory.setUpdatedBy(user);
        companyProductSubCategory.setStatus(model.getStatus());
        dao.update(companyProductSubCategory);

        return Response.status(200).entity(companyProductSubCategory.getId())
                .build();
    }

}
