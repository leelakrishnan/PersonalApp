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
import com.leela.entity.CompanyProductSpecificationGroup;
import com.leela.entity.ProductSpecificationGroup;
import com.leela.entity.User;
import com.leela.model.ObjectModel;
import com.leela.model.ProductSpecificationGroupModel;
import com.leela.model.UpdateStatusModel;
import com.leela.util.CommonsUtil;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;

import io.dropwizard.hibernate.UnitOfWork;

@Path("/productSpecificationGroup")
@Api
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ProductSpecificationGroupResource {

    private final GenericDAO dao;

    public ProductSpecificationGroupResource(
            final SessionFactory sessionFactory) {
        this.dao = new HibernateDAO(sessionFactory);
    }

    @GET
    @Path("/getProductSpecificationGroup/{companyId}")
    @ApiOperation(value = "Get Product specification group list")
    @UnitOfWork
    public Response getProductSpecificationGroup(
            @PathParam("companyId") final Long companyId) {
        final Map<String, Object> options = new HashMap<String, Object>();
        options.put("companyId", companyId);

        @SuppressWarnings("unchecked")
        final List<Object[]> resultList = dao.findByNamedQuery(
                "get.all.product.specification.group.by.company",
                new QueryOptions(options));
        final List<ObjectModel> objectModels = CommonsUtil
                .convertToObjectModelWithStatus(resultList);
        return Response.status(200).entity(objectModels).build();
    }

    @SuppressWarnings("unchecked")
    @GET
    @Path("/validateProductSpecificationGroup/{name}/{productSpecificationGroupId}")
    @ApiOperation(value = "Validate Product specification group name")
    @UnitOfWork
    public Response validateProductSpecificationGroup(
            @PathParam("name") final String name,
            @PathParam("productSpecificationGroupId") final Long productSpecificationGroupId) {
        List<Long> resultList = null;
        final Map<String, Object> options = new HashMap<String, Object>();
        options.put("name", name);
        if (productSpecificationGroupId != null
                && productSpecificationGroupId.longValue() != -1) {
            options.put("productSpecificationGroupId",
                    productSpecificationGroupId);
            resultList = dao.findByNamedQuery(
                    "validate.name.product.specification.group.edit",
                    new QueryOptions(options));
        } else {
            resultList = dao.findByNamedQuery(
                    "validate.name.product.specification.group.add",
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
    @Path("/validateProductSpecificationGroupByCompany/{companyId}/{name}/{productSpecificationGroupId}")
    @ApiOperation(value = "Validate Product specification group name company wise")
    @UnitOfWork
    public Response validateProductSpecificationGroupByCompany(
            @PathParam("companyId") final Long companyId,
            @PathParam("name") final String name,
            @PathParam("productSpecificationGroupId") final Long productSpecificationGroupId) {
        List<Long> resultList = null;
        final Map<String, Object> options = new HashMap<String, Object>();
        options.put("companyId", companyId);
        options.put("name", name);
        if (productSpecificationGroupId != null
                && productSpecificationGroupId.longValue() != -1) {
            options.put("productSpecificationGroupId",
                    productSpecificationGroupId);
            resultList = dao.findByNamedQuery(
                    "validate.name.product.specification.group.by.company.edit",
                    new QueryOptions(options));
        } else {
            resultList = dao.findByNamedQuery(
                    "validate.name.product.specification.group.by.company.add",
                    new QueryOptions(options));
        }
        if (resultList != null && !resultList.isEmpty()
                && resultList.get(0) != null) {
            return Response.status(200).entity(resultList.get(0)).build();
        }
        return Response.status(200).entity("-1").build();
    }

    @POST
    @Path("/saveProductSpecificationGroup")
    @ApiOperation(value = "Add new Product specification group")
    @UnitOfWork
    public Response saveProductSpecificationGroup(
            final ProductSpecificationGroupModel model) {

        final User user = new User();
        user.setId(model.getUserId());

        final Company company = new Company();
        company.setId(model.getCompanyId());

        final ProductSpecificationGroup productSpecificationGroup = new ProductSpecificationGroup();
        productSpecificationGroup.setCreatedDate(new Date());
        productSpecificationGroup.setCreatedBy(user);
        productSpecificationGroup.setName(model.getName());
        productSpecificationGroup.setStatus(model.getStatus());
        dao.create(productSpecificationGroup);

        final CompanyProductSpecificationGroup companyProductSpecificationGroup = new CompanyProductSpecificationGroup();
        companyProductSpecificationGroup.setCreatedDate(new Date());
        companyProductSpecificationGroup.setCreatedBy(user);
        companyProductSpecificationGroup.setCompany(company);
        companyProductSpecificationGroup
                .setProductSpecificationGroup(productSpecificationGroup);
        companyProductSpecificationGroup.setStatus(CommonsUtil.ACTIVE_STATUS);
        dao.create(companyProductSpecificationGroup);

        return Response.status(200).entity(productSpecificationGroup.getId())
                .build();
    }

    @PUT
    @Path("/updateProductSpecificationGroup/{productSpecificationGroupId}")
    @ApiOperation(value = "Update Product specification group")
    @UnitOfWork
    public Response updateProductSpecificationGroup(
            @PathParam("productSpecificationGroupId") final Long productSpecificationGroupId,
            final ProductSpecificationGroupModel model) {

        final User user = new User();
        user.setId(model.getUserId());

        final Company company = new Company();
        company.setId(model.getCompanyId());

        final ProductSpecificationGroup productSpecificationGroup = new ProductSpecificationGroup();
        productSpecificationGroup.setId(productSpecificationGroupId);
        dao.load(productSpecificationGroup);

        productSpecificationGroup.setUpdatedDate(new Date());
        productSpecificationGroup.setUpdatedBy(user);
        productSpecificationGroup.setName(model.getName());
        productSpecificationGroup.setStatus(model.getStatus());
        dao.update(productSpecificationGroup);

        return Response.status(200).entity(productSpecificationGroup.getId())
                .build();
    }

    @PUT
    @Path("/updateProductSpecificationGroupStatus/{productSpecificationGroupId}")
    @ApiOperation(value = "Get Product specification group list")
    @UnitOfWork
    public Response updateProductSpecificationGroupStatus(
            @PathParam("productSpecificationGroupId") final Long productSpecificationGroupId,
            final UpdateStatusModel model) {

        final User user = new User();
        user.setId(model.getUserId());

        final ProductSpecificationGroup productSpecificationGroup = new ProductSpecificationGroup();
        productSpecificationGroup.setId(productSpecificationGroupId);
        dao.load(productSpecificationGroup);

        productSpecificationGroup.setUpdatedDate(new Date());
        productSpecificationGroup.setUpdatedBy(user);
        productSpecificationGroup.setStatus(model.getStatus());
        dao.update(productSpecificationGroup);

        return Response.status(200).entity(productSpecificationGroup.getId())
                .build();
    }

    @GET
    @Path("/getAciveProductSpecificationGroup/{companyId}")
    @ApiOperation(value = "Get active Product specification group list")
    @UnitOfWork
    public Response getAciveProductSpecificationGroup(
            @PathParam("companyId") final Long companyId) {
        final Map<String, Object> options = new HashMap<String, Object>();
        options.put("companyId", companyId);

        @SuppressWarnings("unchecked")
        final List<Object[]> resultList = dao.findByNamedQuery(
                "get.active.product.specification.group.by.company",
                new QueryOptions(options));
        final List<ObjectModel> objectModels = CommonsUtil
                .convertToObjectModel(resultList);
        return Response.status(200).entity(objectModels).build();
    }

    @PUT
    @Path("/saveCompanyProductSpecificationGroup")
    @ApiOperation(value = "Save new company Product specification group")
    @UnitOfWork
    public Response saveCompanyProductSpecificationGroup(
            final UpdateStatusModel model) {

        final User user = new User();
        user.setId(model.getUserId());

        final Company company = new Company();
        company.setId(model.getCompanyId());

        final ProductSpecificationGroup productSpecificationGroup = new ProductSpecificationGroup();
        productSpecificationGroup.setId(model.getSubObjectId());

        final CompanyProductSpecificationGroup companyProductSpecificationGroup = new CompanyProductSpecificationGroup();
        companyProductSpecificationGroup.setCreatedDate(new Date());
        companyProductSpecificationGroup.setCreatedBy(user);
        companyProductSpecificationGroup.setStatus(model.getStatus());
        companyProductSpecificationGroup.setCompany(company);
        companyProductSpecificationGroup
                .setProductSpecificationGroup(productSpecificationGroup);
        dao.create(companyProductSpecificationGroup);

        return Response.status(200)
                .entity(companyProductSpecificationGroup.getId()).build();
    }

    @PUT
    @Path("/updateCompanyProductSpecificationGroup/{companyProductSpecificationGroupId}")
    @ApiOperation(value = "Update company Product specification group")
    @UnitOfWork
    public Response updateCompanyProductSpecificationGroup(
            @PathParam("companyProductSpecificationGroupId") final Long companyProductSpecificationGroupId,
            final UpdateStatusModel model) {

        final User user = new User();
        user.setId(model.getUserId());

        final CompanyProductSpecificationGroup companyProductSpecificationGroup = new CompanyProductSpecificationGroup();
        companyProductSpecificationGroup
                .setId(companyProductSpecificationGroupId);
        dao.load(companyProductSpecificationGroup);

        companyProductSpecificationGroup.setUpdatedDate(new Date());
        companyProductSpecificationGroup.setUpdatedBy(user);
        companyProductSpecificationGroup.setStatus(model.getStatus());
        dao.update(companyProductSpecificationGroup);

        return Response.status(200)
                .entity(companyProductSpecificationGroup.getId()).build();
    }

}
