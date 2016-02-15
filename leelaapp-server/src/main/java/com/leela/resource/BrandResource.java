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
import com.leela.entity.Brand;
import com.leela.entity.Company;
import com.leela.entity.CompanyBrand;
import com.leela.entity.User;
import com.leela.model.BrandModel;
import com.leela.model.ObjectModel;
import com.leela.model.UpdateStatusModel;
import com.leela.util.CommonsUtil;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;

import io.dropwizard.hibernate.UnitOfWork;

@Path("/brand")
@Api
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class BrandResource {

    private final GenericDAO dao;

    public BrandResource(final SessionFactory sessionFactory) {
        this.dao = new HibernateDAO(sessionFactory);
    }

    @GET
    @Path("/getBrand/{companyId}")
    @ApiOperation(value = "Get brand list")
    @UnitOfWork
    public Response getBrand(@PathParam("companyId") final Long companyId) {
        final Map<String, Object> options = new HashMap<String, Object>();
        options.put("companyId", companyId);

        @SuppressWarnings("unchecked")
        final List<Object[]> resultList = dao.findByNamedQuery(
                "get.all.brand.by.company", new QueryOptions(options));
        final List<ObjectModel> objectModels = CommonsUtil
                .convertToObjectModelWithStatus(resultList);
        return Response.status(200).entity(objectModels).build();
    }

    @SuppressWarnings("unchecked")
    @GET
    @Path("/validateBrand/{name}/{brandId}")
    @ApiOperation(value = "Validate brand name")
    @UnitOfWork
    public Response validateBrand(@PathParam("name") final String name,
            @PathParam("brandId") final Long brandId) {
        List<Long> resultList = null;
        final Map<String, Object> options = new HashMap<String, Object>();
        options.put("name", name);
        if (brandId != null && brandId.longValue() != -1) {
            options.put("brandId", brandId);
            resultList = dao.findByNamedQuery("validate.name.brand.edit",
                    new QueryOptions(options));
        } else {
            resultList = dao.findByNamedQuery("validate.name.brand.add",
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
    @Path("/validateBrandByCompany/{companyId}/{name}/{brandId}")
    @ApiOperation(value = "Validate brand name company wise")
    @UnitOfWork
    public Response validateBrandByCompany(
            @PathParam("companyId") final Long companyId,
            @PathParam("name") final String name,
            @PathParam("brandId") final Long brandId) {
        List<Long> resultList = null;
        final Map<String, Object> options = new HashMap<String, Object>();
        options.put("companyId", companyId);
        options.put("name", name);
        if (brandId != null && brandId.longValue() != -1) {
            options.put("brandId", brandId);
            resultList = dao.findByNamedQuery(
                    "validate.name.brand.by.company.edit",
                    new QueryOptions(options));
        } else {
            resultList = dao.findByNamedQuery(
                    "validate.name.brand.by.company.add",
                    new QueryOptions(options));
        }
        if (resultList != null && !resultList.isEmpty()
                && resultList.get(0) != null) {
            return Response.status(200).entity(resultList.get(0)).build();
        }
        return Response.status(200).entity("-1").build();
    }

    @POST
    @Path("/saveBrand")
    @ApiOperation(value = "Add new brand")
    @UnitOfWork
    public Response saveBrand(final BrandModel model) {

        final User user = new User();
        user.setId(model.getUserId());

        final Company company = new Company();
        company.setId(model.getCompanyId());

        final Brand brand = new Brand();
        brand.setCreatedDate(new Date());
        brand.setCreatedBy(user);
        brand.setName(model.getName());
        brand.setStatus(model.getStatus());
        dao.create(brand);

        final CompanyBrand companyBrand = new CompanyBrand();
        companyBrand.setCreatedDate(new Date());
        companyBrand.setCreatedBy(user);
        companyBrand.setCompany(company);
        companyBrand.setBrand(brand);
        companyBrand.setStatus(CommonsUtil.ACTIVE_STATUS);
        dao.create(companyBrand);

        return Response.status(200).entity(brand.getId()).build();
    }

    @PUT
    @Path("/updateBrand/{brandId}")
    @ApiOperation(value = "Update brand")
    @UnitOfWork
    public Response updateBrand(@PathParam("brandId") final Long brandId,
            final BrandModel model) {

        final User user = new User();
        user.setId(model.getUserId());

        final Company company = new Company();
        company.setId(model.getCompanyId());

        final Brand brand = new Brand();
        brand.setId(brandId);
        dao.load(brand);

        brand.setUpdatedDate(new Date());
        brand.setUpdatedBy(user);
        brand.setName(model.getName());
        brand.setStatus(model.getStatus());
        dao.update(brand);

        return Response.status(200).entity(brand.getId()).build();
    }

    @PUT
    @Path("/updateBrandStatus/{brandId}")
    @ApiOperation(value = "Get brand list")
    @UnitOfWork
    public Response updateBrandStatus(@PathParam("brandId") final Long brandId,
            final UpdateStatusModel model) {

        final User user = new User();
        user.setId(model.getUserId());

        final Brand brand = new Brand();
        brand.setId(brandId);
        dao.load(brand);

        brand.setUpdatedDate(new Date());
        brand.setUpdatedBy(user);
        brand.setStatus(model.getStatus());
        dao.update(brand);

        return Response.status(200).entity(brand.getId()).build();
    }

    @GET
    @Path("/getAciveBrand/{companyId}")
    @ApiOperation(value = "Get active brand list")
    @UnitOfWork
    public Response getAciveBrand(
            @PathParam("companyId") final Long companyId) {
        final Map<String, Object> options = new HashMap<String, Object>();
        options.put("companyId", companyId);

        @SuppressWarnings("unchecked")
        final List<Object[]> resultList = dao.findByNamedQuery(
                "get.active.brand.by.company", new QueryOptions(options));
        final List<ObjectModel> objectModels = CommonsUtil
                .convertToObjectModel(resultList);
        return Response.status(200).entity(objectModels).build();
    }

    @PUT
    @Path("/saveCompanyBrand")
    @ApiOperation(value = "Save new company brand")
    @UnitOfWork
    public Response saveCompanyBrand(final UpdateStatusModel model) {

        final User user = new User();
        user.setId(model.getUserId());

        final Company company = new Company();
        company.setId(model.getCompanyId());

        final Brand brand = new Brand();
        brand.setId(model.getSubObjectId());

        final CompanyBrand companyBrand = new CompanyBrand();
        companyBrand.setCreatedDate(new Date());
        companyBrand.setCreatedBy(user);
        companyBrand.setStatus(model.getStatus());
        companyBrand.setCompany(company);
        companyBrand.setBrand(brand);
        dao.create(companyBrand);

        return Response.status(200).entity(companyBrand.getId()).build();
    }

    @PUT
    @Path("/updateCompanyBrand/{companyBrandId}")
    @ApiOperation(value = "Update company brand")
    @UnitOfWork
    public Response updateCompanyBrand(
            @PathParam("companyBrandId") final Long companyBrandId,
            final UpdateStatusModel model) {

        final User user = new User();
        user.setId(model.getUserId());

        final CompanyBrand companyBrand = new CompanyBrand();
        companyBrand.setId(companyBrandId);
        dao.load(companyBrand);

        companyBrand.setUpdatedDate(new Date());
        companyBrand.setUpdatedBy(user);
        companyBrand.setStatus(model.getStatus());
        dao.update(companyBrand);

        return Response.status(200).entity(companyBrand.getId()).build();
    }

}
