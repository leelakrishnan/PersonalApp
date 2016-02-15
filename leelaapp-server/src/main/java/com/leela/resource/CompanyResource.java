package com.leela.resource;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.leela.dao.GenericDAO;
import com.leela.dao.HibernateDAO;
import com.leela.dao.QueryOptions;
import com.leela.entity.Address;
import com.leela.entity.BusinessType;
import com.leela.entity.City;
import com.leela.entity.Company;
import com.leela.entity.Country;
import com.leela.entity.State;
import com.leela.entity.User;
import com.leela.model.CompanyModel;
import com.leela.model.ObjectModel;
import com.leela.model.UpdateStatusModel;
import com.leela.util.CommonsUtil;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;

import io.dropwizard.hibernate.UnitOfWork;

@Path("/company")
@Api
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CompanyResource {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(CompanyResource.class);

    private final GenericDAO dao;

    public CompanyResource(final SessionFactory sessionFactory) {
        this.dao = new HibernateDAO(sessionFactory);
    }

    @GET
    @Path("/getCompanies")
    @ApiOperation(value = "Get company list")
    @UnitOfWork
    public Response getCompanies() {
        final Map<String, Object> options = new HashMap<String, Object>();

        @SuppressWarnings("unchecked")
        final List<Object[]> resultList = dao.findByNamedQuery(
                "get.all.companies", new QueryOptions(options));
        final List<ObjectModel> objectModels = CommonsUtil
                .convertToObjectModelWithStatus(resultList);
        return Response.status(200).entity(objectModels).build();
    }

    @GET
    @Path("/getActiveCompanies")
    @ApiOperation(value = "Get active company list")
    @UnitOfWork
    public Response getActiveCompanies() {
        final Map<String, Object> options = new HashMap<String, Object>();

        @SuppressWarnings("unchecked")
        final List<Object[]> resultList = dao.findByNamedQuery(
                "get.active.companies", new QueryOptions(options));
        final List<ObjectModel> objectModels = CommonsUtil
                .convertToObjectModel(resultList);
        return Response.status(200).entity(objectModels).build();
    }

    @SuppressWarnings("unchecked")
    @GET
    @Path("/validateCompany/{name}/{companyId}")
    @ApiOperation(value = "Validate company name")
    @UnitOfWork
    public Response validateCompany(@PathParam("name") final String name,
            @PathParam("companyId") final Long companyId) {
        List<Long> resultList = null;
        final Map<String, Object> options = new HashMap<String, Object>();
        options.put("name", name);
        if (companyId != null && companyId.longValue() != -1) {
            options.put("companyId", companyId);
            resultList = dao.findByNamedQuery("validate.name.company.edit",
                    new QueryOptions(options));
        } else {
            resultList = dao.findByNamedQuery("validate.name.company.add",
                    new QueryOptions(options));
        }
        if (resultList != null && !resultList.isEmpty()
                && resultList.get(0) != null) {
            return Response.status(200).entity(resultList.get(0)).build();
        }
        return Response.status(200).entity("-1").build();
    }

    @POST
    @Path("/saveCompany")
    @ApiOperation(value = "Add new company")
    @UnitOfWork
    public Response saveCompany(final CompanyModel model) {

        final User user = new User();
        user.setId(model.getUserId());

        final Country country = new Country();
        country.setId(model.getCountryId());

        final State state = new State();
        state.setId(model.getStateId());

        final Address address = new Address();
        address.setAddress1(model.getAddress1());
        address.setAddress2(model.getAddress2());
        address.setCountry(country);
        address.setState(state);
        if (model.getCityId() != null && model.getCityId().longValue() != -1L) {
            final City city = new City();
            city.setId(model.getCityId());
            address.setCity(city);
        }
        address.setOtherCity(model.getCity());
        address.setZipcode(model.getZipcode());
        dao.create(address);

        final Company company = new Company();
        company.setCreatedDate(new Date());
        company.setCreatedBy(user);
        company.setName(model.getName());
        company.setStatus(model.getStatus());
        company.setAddress(address);
        company.setPhone(model.getPhone());
        company.setUrl(model.getUrl());
        company.setFax(model.getFax());

        if (model.getOwnerId() != null
                && model.getOwnerId().longValue() != -1L) {
            final User owner = new User();
            user.setId(model.getOwnerId());
            company.setOwner(owner);
        }
        if (model.getBusinessTypeId() != null
                && model.getBusinessTypeId().longValue() != -1L) {
            final BusinessType businessType = new BusinessType();
            businessType.setId(model.getBusinessTypeId());
            company.setBusinessType(businessType);
        }
        dao.create(company);
        return Response.status(200).entity(company.getId()).build();
    }

    @PUT
    @Path("/updateCompany/{companyId}")
    @ApiOperation(value = "Update company")
    @UnitOfWork
    public Response updateCompany(@PathParam("companyId") final Long companyId,
            final CompanyModel model) {

        final User user = new User();
        user.setId(model.getUserId());

        final Country country = new Country();
        country.setId(model.getCountryId());

        final State state = new State();
        state.setId(model.getStateId());

        final Company company = new Company();
        company.setId(companyId);
        dao.load(company);

        company.setUpdatedDate(new Date());
        company.setUpdatedBy(user);
        company.setName(model.getName());
        company.setPhone(model.getPhone());
        company.setUrl(model.getUrl());
        company.setFax(model.getFax());
        company.setStatus(model.getStatus());

        if (model.getOwnerId() != null
                && model.getOwnerId().longValue() != -1L) {
            final User owner = new User();
            owner.setId(model.getOwnerId());
            company.setOwner(owner);
        }

        if (model.getBusinessTypeId() != null
                && model.getBusinessTypeId().longValue() != -1L) {
            final BusinessType businessType = new BusinessType();
            businessType.setId(model.getBusinessTypeId());
            company.setBusinessType(businessType);
        }
        dao.update(company);

        final Address address = company.getAddress();
        address.setAddress1(model.getAddress1());
        address.setAddress2(model.getAddress2());
        address.setCountry(country);
        address.setState(state);
        if (model.getCityId() != null && model.getCityId().longValue() != -1L) {
            final City city = new City();
            city.setId(model.getCityId());
            address.setCity(city);
        }
        address.setOtherCity(model.getCity());
        address.setZipcode(model.getZipcode());
        dao.update(address);

        return Response.status(200).entity(company.getId()).build();
    }

    @PUT
    @Path("/updateCompanyStatus/{companyId}")
    @ApiOperation(value = "Update company status")
    @UnitOfWork
    public Response updateCompanyStatus(
            @PathParam("companyId") final Long companyId,
            final UpdateStatusModel model) {

        final User user = new User();
        user.setId(model.getUserId());

        final Company company = new Company();
        company.setId(companyId);
        dao.load(company);

        company.setUpdatedDate(new Date());
        company.setUpdatedBy(user);
        company.setStatus(model.getStatus());
        dao.update(company);

        return Response.status(200).entity(company.getId()).build();
    }

    @Path("/updateCompanyLogo/{userId}/{companyId}")
    @PUT
    @ApiOperation(value = "Update company logo")
    @Consumes("multipart/form-data")
    @UnitOfWork
    public Response updateCompanyLogo(@PathParam("userId") final Long userId,
            @PathParam("companyId") final Long companyId,
            @Context final HttpServletRequest request) {

        if (ServletFileUpload.isMultipartContent(request)) {

            final User user = new User();
            user.setId(userId);

            final FileItemFactory factory = new DiskFileItemFactory();
            final ServletFileUpload fileUpload = new ServletFileUpload(factory);
            List<FileItem> items = null;
            try {
                items = fileUpload.parseRequest(request);
            } catch (final FileUploadException e) {
                LOGGER.error("Error", e);
                return Response.status(400).build();
            }

            if (items != null && !items.isEmpty() && items.get(0) != null) {

                final Company company = new Company();
                company.setId(companyId);
                dao.load(company);

                company.setUpdatedDate(new Date());
                company.setUpdatedBy(user);
                final FileItem item = items.get(0);
                company.setLogo(item.get());
                dao.update(company);

                return Response.status(200).build();
            }
        }
        return Response.status(400).build();
    }

    @GET
    @Path("/getCompanyById/{companyId}")
    @ApiOperation(value = "Get company By id")
    @UnitOfWork
    public Response getCompanyById(
            @PathParam("companyId") final Long companyId) {
        CompanyModel model = null;
        final Map<String, Object> options = new HashMap<String, Object>();
        options.put("companyId", companyId);

        @SuppressWarnings("unchecked")
        final List<Object[]> resultList = dao.findByNamedQuery(
                "get.company.by.id", new QueryOptions(options));
        if (resultList != null && !resultList.isEmpty()
                && resultList.get(0) != null) {
            final Object[] obj = resultList.get(0);

            model = new CompanyModel();
            model.setId(obj[0] != null ? Long.valueOf(obj[0].toString()) : -1L);
            model.setName(obj[1] != null ? obj[1].toString() : "");
            model.setAddress1(obj[2] != null ? obj[2].toString() : "");
            model.setAddress2(obj[3] != null ? obj[3].toString() : "");
            model.setCountryId(
                    obj[4] != null ? Long.valueOf(obj[4].toString()) : -1L);
            model.setStateId(
                    obj[5] != null ? Long.valueOf(obj[5].toString()) : -1L);
            model.setCityId(
                    obj[6] != null ? Long.valueOf(obj[6].toString()) : -1L);
            model.setCity(obj[7] != null ? obj[7].toString() : "");
            model.setZipcode(obj[8] != null ? obj[8].toString() : "");
            model.setPhone(obj[9] != null ? obj[9].toString() : "");
            model.setUrl(obj[10] != null ? obj[10].toString() : "");
            model.setFax(obj[11] != null ? obj[11].toString() : "");
            model.setOwnerId(
                    obj[12] != null ? Long.valueOf(obj[12].toString()) : -1L);
            model.setBusinessTypeId(
                    obj[13] != null ? Long.valueOf(obj[13].toString()) : -1L);
            model.setStatus(
                    obj[14] != null ? Short.valueOf(obj[14].toString()) : 0);
        }
        return Response.status(200).entity(model).build();
    }

}
