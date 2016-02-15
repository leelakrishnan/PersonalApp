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
import com.leela.entity.CompanyProductSpecification;
import com.leela.entity.ProductSpecification;
import com.leela.entity.ProductSpecificationGroup;
import com.leela.entity.User;
import com.leela.model.ObjectModel;
import com.leela.model.ProductSpecificationModel;
import com.leela.model.UpdateStatusModel;
import com.leela.util.CommonsUtil;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;

import io.dropwizard.hibernate.UnitOfWork;

@Path("/productSpecification")
@Api
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ProductSpecificationResource {

    private final GenericDAO dao;

    public ProductSpecificationResource(final SessionFactory sessionFactory) {
        this.dao = new HibernateDAO(sessionFactory);
    }

    @GET
    @Path("/getProductSpecification/{companyId}/{productSpecificationGroupId}")
    @ApiOperation(value = "Get product specification list")
    @UnitOfWork
    public Response getProductSpecification(
            @PathParam("companyId") final Long companyId,
            @PathParam("productSpecificationGroupId") final Long productSpecificationGroupId) {
        final Map<String, Object> options = new HashMap<String, Object>();
        options.put("companyId", companyId);
        options.put("productSpecificationGroupId", productSpecificationGroupId);

        @SuppressWarnings("unchecked")
        final List<Object[]> resultList = dao.findByNamedQuery(
                "get.all.product.specification.by.product.specification.group",
                new QueryOptions(options));
        final List<ObjectModel> objectModels = CommonsUtil
                .convertToObjectModelWithStatus(resultList);
        return Response.status(200).entity(objectModels).build();
    }

    @SuppressWarnings("unchecked")
    @GET
    @Path("/validateProductSpecification/{name}/{productSpecificationId}")
    @ApiOperation(value = "Validate product specification name")
    @UnitOfWork
    public Response validateProductSpecification(
            @PathParam("name") final String name,
            @PathParam("productSpecificationId") final Long productSpecificationId) {
        List<Long> resultList = null;
        final Map<String, Object> options = new HashMap<String, Object>();
        options.put("name", name);
        if (productSpecificationId != null
                && productSpecificationId.longValue() != -1) {
            options.put("productSpecificationId", productSpecificationId);
            resultList = dao.findByNamedQuery(
                    "validate.name.product.specification.edit",
                    new QueryOptions(options));
        } else {
            resultList = dao.findByNamedQuery(
                    "validate.name.product.specification.add",
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
    @Path("/validateProductSpecificationByCompany/{companyId}/{name}/{productSpecificationId}")
    @ApiOperation(value = "Validate product specification name company wise")
    @UnitOfWork
    public Response validateProductSpecificationByCompany(
            @PathParam("companyId") final Long companyId,
            @PathParam("name") final String name,
            @PathParam("productSpecificationId") final Long productSpecificationId) {
        List<Long> resultList = null;
        final Map<String, Object> options = new HashMap<String, Object>();
        options.put("companyId", companyId);
        options.put("name", name);
        if (productSpecificationId != null
                && productSpecificationId.longValue() != -1) {
            options.put("productSpecificationId", productSpecificationId);
            resultList = dao.findByNamedQuery(
                    "validate.name.product.specification.by.company.edit",
                    new QueryOptions(options));
        } else {
            resultList = dao.findByNamedQuery(
                    "validate.name.product.specification.by.company.add",
                    new QueryOptions(options));
        }
        if (resultList != null && !resultList.isEmpty()
                && resultList.get(0) != null) {
            return Response.status(200).entity(resultList.get(0)).build();
        }
        return Response.status(200).entity("-1").build();
    }

    @POST
    @Path("/saveProductSpecification")
    @ApiOperation(value = "Add new product specification")
    @UnitOfWork
    public Response saveProductSpecification(
            final ProductSpecificationModel model) {

        final User user = new User();
        user.setId(model.getUserId());

        final Company company = new Company();
        company.setId(model.getCompanyId());

        final ProductSpecificationGroup productSpecificationGroup = new ProductSpecificationGroup();
        productSpecificationGroup.setId(model.getProductSpecificationGroupId());

        final ProductSpecification productSpecification = new ProductSpecification();
        productSpecification.setCreatedDate(new Date());
        productSpecification.setCreatedBy(user);
        productSpecification.setName(model.getName());
        productSpecification
                .setProductSpecificationGroup(productSpecificationGroup);
        productSpecification.setStatus(model.getStatus());
        dao.create(productSpecification);

        final CompanyProductSpecification companyProductSpecification = new CompanyProductSpecification();
        companyProductSpecification.setCreatedDate(new Date());
        companyProductSpecification.setCreatedBy(user);
        companyProductSpecification.setCompany(company);
        companyProductSpecification
                .setProductSpecification(productSpecification);
        companyProductSpecification.setStatus(CommonsUtil.ACTIVE_STATUS);
        dao.create(companyProductSpecification);

        return Response.status(200).entity(productSpecification.getId())
                .build();
    }

    @PUT
    @Path("/updateProductSpecification/{productSpecificationId}")
    @ApiOperation(value = "Update product specification")
    @UnitOfWork
    public Response updateProductSpecification(
            @PathParam("productSpecificationId") final Long productSpecificationId,
            final ProductSpecificationModel model) {

        final User user = new User();
        user.setId(model.getUserId());

        final Company company = new Company();
        company.setId(model.getCompanyId());

        final ProductSpecificationGroup productSpecificationGroup = new ProductSpecificationGroup();
        productSpecificationGroup.setId(model.getProductSpecificationGroupId());

        final ProductSpecification productSpecification = new ProductSpecification();
        productSpecification.setId(productSpecificationId);
        dao.load(productSpecification);

        productSpecification.setUpdatedDate(new Date());
        productSpecification.setUpdatedBy(user);
        productSpecification.setName(model.getName());
        productSpecification
                .setProductSpecificationGroup(productSpecificationGroup);
        productSpecification.setStatus(model.getStatus());
        dao.update(productSpecification);

        return Response.status(200).entity(productSpecification.getId())
                .build();
    }

    @PUT
    @Path("/updateProductSpecificationStatus/{productSpecificationId}")
    @ApiOperation(value = "Get product specification list")
    @UnitOfWork
    public Response updateProductSpecificationStatus(
            @PathParam("productSpecificationId") final Long productSpecificationId,
            final UpdateStatusModel model) {

        final User user = new User();
        user.setId(model.getUserId());

        final ProductSpecification productSpecification = new ProductSpecification();
        productSpecification.setId(productSpecificationId);
        dao.load(productSpecification);

        productSpecification.setUpdatedDate(new Date());
        productSpecification.setUpdatedBy(user);
        productSpecification.setStatus(model.getStatus());
        dao.update(productSpecification);

        return Response.status(200).entity(productSpecification.getId())
                .build();
    }

    @GET
    @Path("/getAciveProductSpecification/{companyId}/{productSpecificationGroupId}")
    @ApiOperation(value = "Get active product specification list by product specification group")
    @UnitOfWork
    public Response getAciveProductSpecification(
            @PathParam("companyId") final Long companyId,
            @PathParam("productSpecificationGroupId") final Long productSpecificationGroupId) {
        final Map<String, Object> options = new HashMap<String, Object>();
        options.put("companyId", companyId);
        options.put("productSpecificationGroupId", productSpecificationGroupId);

        @SuppressWarnings("unchecked")
        final List<Object[]> resultList = dao.findByNamedQuery(
                "get.active.product.specification.by.product.specification.group",
                new QueryOptions(options));
        final List<ObjectModel> objectModels = CommonsUtil
                .convertToObjectModel(resultList);
        return Response.status(200).entity(objectModels).build();
    }

    @PUT
    @Path("/saveCompanyProductSpecification")
    @ApiOperation(value = "Save new company product specification")
    @UnitOfWork
    public Response saveCompanyProductSpecification(
            final UpdateStatusModel model) {

        final User user = new User();
        user.setId(model.getUserId());

        final Company company = new Company();
        company.setId(model.getCompanyId());

        final ProductSpecification productSpecification = new ProductSpecification();
        productSpecification.setId(model.getSubObjectId());

        final CompanyProductSpecification companyProductSpecification = new CompanyProductSpecification();
        companyProductSpecification.setCreatedDate(new Date());
        companyProductSpecification.setCreatedBy(user);
        companyProductSpecification.setStatus(model.getStatus());
        companyProductSpecification.setCompany(company);
        companyProductSpecification
                .setProductSpecification(productSpecification);
        dao.create(companyProductSpecification);

        return Response.status(200).entity(companyProductSpecification.getId())
                .build();
    }

    @PUT
    @Path("/updateCompanyProductSpecification/{companyProductSpecificationId}")
    @ApiOperation(value = "Update company product specification")
    @UnitOfWork
    public Response updateCompanyProductSpecification(
            @PathParam("companyProductSpecificationId") final Long companyProductSpecificationId,
            final UpdateStatusModel model) {

        final User user = new User();
        user.setId(model.getUserId());

        final CompanyProductSpecification companyProductSpecification = new CompanyProductSpecification();
        companyProductSpecification.setId(companyProductSpecificationId);
        dao.load(companyProductSpecification);

        companyProductSpecification.setUpdatedDate(new Date());
        companyProductSpecification.setUpdatedBy(user);
        companyProductSpecification.setStatus(model.getStatus());
        dao.update(companyProductSpecification);

        return Response.status(200).entity(companyProductSpecification.getId())
                .build();
    }

}
