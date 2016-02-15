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
import com.leela.entity.City;
import com.leela.entity.Company;
import com.leela.entity.Country;
import com.leela.entity.State;
import com.leela.entity.Supplier;
import com.leela.entity.User;
import com.leela.model.ObjectModel;
import com.leela.model.SupplierModel;
import com.leela.model.UpdateStatusModel;
import com.leela.util.CommonsUtil;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;

import io.dropwizard.hibernate.UnitOfWork;

@Path("/supplier")
@Api
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SupplierResource {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(SupplierResource.class);

    private final GenericDAO dao;

    public SupplierResource(final SessionFactory sessionFactory) {
        this.dao = new HibernateDAO(sessionFactory);
    }

    @GET
    @Path("/getSupplier/{companyId}")
    @ApiOperation(value = "Get supplier list")
    @UnitOfWork
    public Response getSupplier(@PathParam("companyId") final Long companyId) {
        final Map<String, Object> options = new HashMap<String, Object>();
        options.put("companyId", companyId);

        @SuppressWarnings("unchecked")
        final List<Object[]> resultList = dao.findByNamedQuery(
                "get.all.suppliers.by.company", new QueryOptions(options));
        final List<ObjectModel> objectModels = CommonsUtil
                .convertToObjectModelWithStatus(resultList);
        return Response.status(200).entity(objectModels).build();
    }

    @SuppressWarnings("unchecked")
    @GET
    @Path("/validateSupplier/{companyId}/{name}/{supplierId}")
    @ApiOperation(value = "Validate supplier name")
    @UnitOfWork
    public Response validateSupplier(
            @PathParam("companyId") final Long companyId,
            @PathParam("name") final String name,
            @PathParam("supplierId") final Long supplierId) {
        List<Long> resultList = null;
        final Map<String, Object> options = new HashMap<String, Object>();
        options.put("name", name);
        options.put("companyId", companyId);
        if (supplierId != null && supplierId.longValue() != -1) {
            options.put("supplierId", supplierId);
            resultList = dao.findByNamedQuery(
                    "validate.name.suppliers.by.company.edit",
                    new QueryOptions(options));
        } else {
            resultList = dao.findByNamedQuery(
                    "validate.name.suppliers.by.company.add",
                    new QueryOptions(options));
        }
        if (resultList != null && !resultList.isEmpty()
                && resultList.get(0) != null) {
            return Response.status(200).entity(resultList.get(0)).build();
        }
        return Response.status(200).entity("-1").build();
    }

    @POST
    @Path("/saveSupplier")
    @ApiOperation(value = "Add new supplier")
    @UnitOfWork
    public Response saveSupplier(final SupplierModel model) {

        final User user = new User();
        user.setId(model.getUserId());

        final Company company = new Company();
        company.setId(model.getCompanyId());

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

        final Supplier supplier = new Supplier();
        supplier.setCreatedDate(new Date());
        supplier.setCreatedBy(user);
        supplier.setName(model.getName());
        supplier.setStatus(model.getStatus());
        supplier.setAddress(address);
        supplier.setPhone(model.getPhone());
        supplier.setUrl(model.getUrl());
        supplier.setFax(model.getFax());
        supplier.setCompany(company);
        dao.create(supplier);
        return Response.status(200).entity(supplier.getId()).build();
    }

    @PUT
    @Path("/updateSupplier/{supplierId}")
    @ApiOperation(value = "Update supplier")
    @UnitOfWork
    public Response updateSupplier(
            @PathParam("supplierId") final Long supplierId,
            final SupplierModel model) {

        final User user = new User();
        user.setId(model.getUserId());

        final Company company = new Company();
        company.setId(model.getCompanyId());

        final Country country = new Country();
        country.setId(model.getCountryId());

        final State state = new State();
        state.setId(model.getStateId());

        final Supplier supplier = new Supplier();
        supplier.setId(supplierId);
        dao.load(supplier);

        supplier.setUpdatedDate(new Date());
        supplier.setUpdatedBy(user);
        supplier.setName(model.getName());
        supplier.setPhone(model.getPhone());
        supplier.setUrl(model.getUrl());
        supplier.setFax(model.getFax());
        supplier.setStatus(model.getStatus());
        supplier.setCompany(company);
        dao.update(supplier);

        final Address address = supplier.getAddress();
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

        return Response.status(200).entity(supplier.getId()).build();
    }

    @PUT
    @Path("/updateSupplierStatus/{supplierId}")
    @ApiOperation(value = "Update supplier status")
    @UnitOfWork
    public Response updateSupplierStatus(
            @PathParam("supplierId") final Long supplierId,
            final UpdateStatusModel model) {

        final User user = new User();
        user.setId(model.getUserId());

        final Supplier supplier = new Supplier();
        supplier.setId(supplierId);
        dao.load(supplier);

        supplier.setUpdatedDate(new Date());
        supplier.setUpdatedBy(user);
        supplier.setStatus(model.getStatus());
        dao.update(supplier);

        return Response.status(200).entity(supplier.getId()).build();
    }

    @GET
    @Path("/getAciveSupplier/{companyId}")
    @ApiOperation(value = "Get active supplier list")
    @UnitOfWork
    public Response getAciveSupplier(
            @PathParam("companyId") final Long companyId) {
        final Map<String, Object> options = new HashMap<String, Object>();
        options.put("companyId", companyId);

        @SuppressWarnings("unchecked")
        final List<Object[]> resultList = dao.findByNamedQuery(
                "get.active.suppliers.by.company", new QueryOptions(options));
        final List<ObjectModel> objectModels = CommonsUtil
                .convertToObjectModel(resultList);
        return Response.status(200).entity(objectModels).build();
    }

    @Path("/updateSupplierLogo/{userId}/{supplierId}")
    @PUT
    @ApiOperation(value = "Update supplier logo")
    @Consumes("multipart/form-data")
    @UnitOfWork
    public Response updateSupplierLogo(@PathParam("userId") final Long userId,
            @PathParam("supplierId") final Long supplierId,
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

                final Supplier supplier = new Supplier();
                supplier.setId(supplierId);
                dao.load(supplier);

                supplier.setUpdatedDate(new Date());
                supplier.setUpdatedBy(user);
                final FileItem item = items.get(0);
                supplier.setLogo(item.get());
                dao.update(supplier);

                return Response.status(200).build();
            }
        }
        return Response.status(400).build();
    }

    @GET
    @Path("/getSupplierById/{supplierId}")
    @ApiOperation(value = "Get supplier By id")
    @UnitOfWork
    public Response getSupplierById(
            @PathParam("supplierId") final Long supplierId) {
        SupplierModel model = null;
        final Map<String, Object> options = new HashMap<String, Object>();
        options.put("supplierId", supplierId);

        @SuppressWarnings("unchecked")
        final List<Object[]> resultList = dao.findByNamedQuery(
                "get.supplier.by.id", new QueryOptions(options));
        if (resultList != null && !resultList.isEmpty()
                && resultList.get(0) != null) {
            final Object[] obj = resultList.get(0);

            model = new SupplierModel();
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
            model.setCompanyId(
                    obj[9] != null ? Long.valueOf(obj[9].toString()) : -1L);
            model.setStatus(
                    obj[10] != null ? Short.valueOf(obj[10].toString()) : 0);
            model.setPhone(obj[11] != null ? obj[11].toString() : "");
            model.setUrl(obj[12] != null ? obj[12].toString() : "");
            model.setFax(obj[13] != null ? obj[13].toString() : "");
        }
        return Response.status(200).entity(model).build();
    }

}
