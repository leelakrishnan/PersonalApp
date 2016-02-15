package com.leela.resource;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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
import com.leela.entity.User;
import com.leela.entity.UserHistory;
import com.leela.entity.UserPhoto;
import com.leela.entity.UserStatus;
import com.leela.entity.UserType;
import com.leela.model.ObjectModel;
import com.leela.model.UpdateStatusModel;
import com.leela.model.UserModel;
import com.leela.util.CommonsUtil;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;

import io.dropwizard.hibernate.UnitOfWork;

@Path("/user")
@Api
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserResource {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(UserResource.class);

    private final SimpleDateFormat dateFormat = new SimpleDateFormat(
            "MM/dd/yyyy");

    private final GenericDAO dao;

    public UserResource(final SessionFactory sessionFactory) {
        this.dao = new HibernateDAO(sessionFactory);
    }

    @GET
    @Path("/getUsersByCompanyAndUserType/{companyId}/{userTypeId}")
    @ApiOperation(value = "Get user list by company and user type")
    @UnitOfWork
    public Response getCompanies(@PathParam("companyId") final Long companyId,
            @PathParam("userTypeId") final Long userTypeId) {
        final Map<String, Object> options = new HashMap<String, Object>();
        options.put("companyId", companyId);
        options.put("userTypeId", userTypeId);

        @SuppressWarnings("unchecked")
        final List<Object[]> resultList = dao.findByNamedQuery(
                "get.all.users.by.company.and.user.type",
                new QueryOptions(options));
        final List<ObjectModel> objectModels = CommonsUtil
                .convertToObjectModelWithStatus(resultList);
        return Response.status(200).entity(objectModels).build();
    }

    @SuppressWarnings("unchecked")
    @GET
    @Path("/validateUserPhone/{companyId}/{phone}/{userId}")
    @ApiOperation(value = "Validate User phone")
    @UnitOfWork
    public Response validateUserPhone(
            @PathParam("companyId") final Long companyId,
            @PathParam("phone") final String phone,
            @PathParam("userId") final Long userId) {
        List<Long> resultList = null;
        final Map<String, Object> options = new HashMap<String, Object>();
        options.put("phone", phone);
        options.put("companyId", companyId);
        if (userId != null && userId.longValue() != -1) {
            options.put("userId", userId);
            resultList = dao.findByNamedQuery(
                    "validate.phone.user.by.company.edit",
                    new QueryOptions(options));
        } else {
            resultList = dao.findByNamedQuery(
                    "validate.phone.user.by.company.add",
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
    @Path("/validateUserEmail/{companyId}/{email}/{userId}")
    @ApiOperation(value = "Validate User email")
    @UnitOfWork
    public Response validateUserEmail(
            @PathParam("companyId") final Long companyId,
            @PathParam("email") final String email,
            @PathParam("userId") final Long userId) {
        List<Long> resultList = null;
        final Map<String, Object> options = new HashMap<String, Object>();
        options.put("email", email);
        options.put("companyId", companyId);
        if (userId != null && userId.longValue() != -1) {
            options.put("userId", userId);
            resultList = dao.findByNamedQuery(
                    "validate.email.user.by.company.edit",
                    new QueryOptions(options));
        } else {
            resultList = dao.findByNamedQuery(
                    "validate.email.user.by.company.add",
                    new QueryOptions(options));
        }
        if (resultList != null && !resultList.isEmpty()
                && resultList.get(0) != null) {
            return Response.status(200).entity(resultList.get(0)).build();
        }
        return Response.status(200).entity("-1").build();
    }

    @POST
    @Path("/saveUser")
    @ApiOperation(value = "Add new user")
    @UnitOfWork
    public Response saveUser(final UserModel model) {

        final User createdBy = new User();
        createdBy.setId(model.getUserId());

        final Company company = new Company();
        company.setId(model.getCompanyId());

        final UserType userType = new UserType();
        userType.setId(model.getUserTypeId());

        final UserStatus userStatus = new UserStatus();
        userStatus.setId(model.getUserStatusId());

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

        final User user = new User();
        user.setCreatedDate(new Date());
        user.setCreatedBy(createdBy);
        user.setFirstName(model.getFirstName());
        user.setLastName(model.getLastName());
        user.setEmail(model.getEmail());
        user.setPhone(model.getPhone());
        user.setGender(model.getGender());
        try {
            user.setBirthDate(dateFormat.parse(model.getBirthDate()));
        } catch (final ParseException e) {
            LOGGER.error("Error", e);
        }
        user.setAddress(address);
        user.setStatus(model.getStatus());
        user.setUserName(model.getUserName());
        user.setUserType(userType);
        user.setUserStatus(userStatus);
        user.setCompany(company);
        dao.create(user);

        final UserHistory userHistory = new UserHistory();
        userHistory.setFirstName(user.getFirstName());
        userHistory.setLastName(user.getLastName());
        userHistory.setPhone(user.getPhone());
        userHistory.setEmail(user.getEmail());
        userHistory.setPassword(user.getPassword());
        userHistory.setGender(user.getGender());
        userHistory.setBirthDate(user.getBirthDate());
        userHistory.setUserName(user.getUserName());
        userHistory.setUserPhoto(user.getUserPhoto());
        userHistory.setAddress(user.getAddress());
        userHistory.setCompany(user.getCompany());
        userHistory.setUserType(user.getUserType());
        userHistory.setUserStatus(user.getUserStatus());

        userHistory.setStatus(user.getStatus());
        userHistory.setCreatedDate(new Date());
        userHistory.setCreatedBy(createdBy);
        userHistory.setUser(user);

        dao.create(userHistory);

        return Response.status(200).entity(user.getId()).build();
    }

    @PUT
    @Path("/updateUser/{userId}")
    @ApiOperation(value = "Update user")
    @UnitOfWork
    public Response updateUser(@PathParam("userId") final Long userId,
            final UserModel model) {

        final User updatedBy = new User();
        updatedBy.setId(model.getUserId());

        final Company company = new Company();
        company.setId(model.getCompanyId());

        final UserType userType = new UserType();
        userType.setId(model.getUserTypeId());

        final UserStatus userStatus = new UserStatus();
        userStatus.setId(model.getUserStatusId());

        final Country country = new Country();
        country.setId(model.getCountryId());

        final State state = new State();
        state.setId(model.getStateId());

        final User user = new User();
        user.setId(userId);
        dao.load(user);

        user.setUpdatedDate(new Date());
        user.setUpdatedBy(updatedBy);
        user.setFirstName(model.getFirstName());
        user.setLastName(model.getLastName());
        user.setEmail(model.getEmail());
        user.setPhone(model.getPhone());
        user.setGender(model.getGender());
        try {
            user.setBirthDate(dateFormat.parse(model.getBirthDate()));
        } catch (final ParseException e) {
            LOGGER.error("Error", e);
        }
        user.setStatus(model.getStatus());
        user.setUserName(model.getUserName());
        user.setUserType(userType);
        user.setUserStatus(userStatus);
        user.setCompany(company);
        dao.update(user);

        final Address address = user.getAddress();
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

        final UserHistory userHistory = new UserHistory();
        userHistory.setFirstName(user.getFirstName());
        userHistory.setLastName(user.getLastName());
        userHistory.setPhone(user.getPhone());
        userHistory.setEmail(user.getEmail());
        userHistory.setPassword(user.getPassword());
        userHistory.setGender(user.getGender());
        userHistory.setBirthDate(user.getBirthDate());
        userHistory.setUserName(user.getUserName());
        userHistory.setUserPhoto(user.getUserPhoto());
        userHistory.setAddress(user.getAddress());
        userHistory.setCompany(user.getCompany());
        userHistory.setUserType(user.getUserType());
        userHistory.setUserStatus(user.getUserStatus());

        userHistory.setStatus(user.getStatus());
        userHistory.setCreatedDate(new Date());
        userHistory.setCreatedBy(updatedBy);
        userHistory.setUser(user);

        dao.create(userHistory);

        return Response.status(200).entity(user.getId()).build();
    }

    @PUT
    @Path("/updateUserStatus/{userId}")
    @ApiOperation(value = "Update user status")
    @UnitOfWork
    public Response updateUserStatus(@PathParam("userId") final Long userId,
            final UpdateStatusModel model) {

        final User updatedBy = new User();
        updatedBy.setId(model.getUserId());

        final User user = new User();
        user.setId(userId);
        dao.load(user);

        user.setUpdatedDate(new Date());
        user.setUpdatedBy(updatedBy);
        user.setStatus(model.getStatus());
        dao.update(user);

        final UserHistory userHistory = new UserHistory();
        userHistory.setFirstName(user.getFirstName());
        userHistory.setLastName(user.getLastName());
        userHistory.setPhone(user.getPhone());
        userHistory.setEmail(user.getEmail());
        userHistory.setPassword(user.getPassword());
        userHistory.setGender(user.getGender());
        userHistory.setBirthDate(user.getBirthDate());
        userHistory.setUserName(user.getUserName());
        userHistory.setUserPhoto(user.getUserPhoto());
        userHistory.setAddress(user.getAddress());
        userHistory.setCompany(user.getCompany());
        userHistory.setUserType(user.getUserType());
        userHistory.setUserStatus(user.getUserStatus());

        userHistory.setStatus(user.getStatus());
        userHistory.setCreatedDate(new Date());
        userHistory.setCreatedBy(updatedBy);
        userHistory.setUser(user);

        dao.create(userHistory);

        return Response.status(200).entity(user.getId()).build();
    }

    @GET
    @Path("/getAciveUser/{companyId}/{userTypeId}")
    @ApiOperation(value = "Get active user list")
    @UnitOfWork
    public Response getAciveUser(@PathParam("companyId") final Long companyId,
            @PathParam("userTypeId") final Long userTypeId) {
        final Map<String, Object> options = new HashMap<String, Object>();
        options.put("companyId", companyId);
        options.put("userTypeId", userTypeId);

        @SuppressWarnings("unchecked")
        final List<Object[]> resultList = dao.findByNamedQuery(
                "get.active.users.by.company.and.user.type",
                new QueryOptions(options));
        final List<ObjectModel> objectModels = CommonsUtil
                .convertToObjectModel(resultList);
        return Response.status(200).entity(objectModels).build();
    }

    @Path("/updateUserPhoto/{updatedById}/{userId}")
    @PUT
    @ApiOperation(value = "Update user photo")
    @Consumes("multipart/form-data")
    @UnitOfWork
    public Response updateUserPhoto(
            @PathParam("updatedById") final Long updatedById,
            @PathParam("userId") final Long userId,
            @Context final HttpServletRequest request) {

        if (ServletFileUpload.isMultipartContent(request)) {

            final User updatedBy = new User();
            updatedBy.setId(updatedById);

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

                final FileItem item = items.get(0);

                final UserPhoto userPhoto = new UserPhoto();
                userPhoto.setCreatedDate(new Date());
                userPhoto.setCreatedBy(updatedBy);
                userPhoto.setMimeType(item.getContentType());
                userPhoto.setName(item.getName());
                userPhoto.setPhoto(item.get());
                userPhoto.setStatus(CommonsUtil.ACTIVE_STATUS);
                dao.create(userPhoto);

                final User user = new User();
                user.setId(userId);
                dao.load(user);

                user.setUpdatedDate(new Date());
                user.setUpdatedBy(updatedBy);
                user.setUserPhoto(userPhoto);
                dao.update(user);

                final UserHistory userHistory = new UserHistory();
                userHistory.setFirstName(user.getFirstName());
                userHistory.setLastName(user.getLastName());
                userHistory.setPhone(user.getPhone());
                userHistory.setEmail(user.getEmail());
                userHistory.setPassword(user.getPassword());
                userHistory.setGender(user.getGender());
                userHistory.setBirthDate(user.getBirthDate());
                userHistory.setUserName(user.getUserName());
                userHistory.setUserPhoto(user.getUserPhoto());
                userHistory.setAddress(user.getAddress());
                userHistory.setCompany(user.getCompany());
                userHistory.setUserType(user.getUserType());
                userHistory.setUserStatus(user.getUserStatus());

                userHistory.setStatus(user.getStatus());
                userHistory.setCreatedDate(new Date());
                userHistory.setCreatedBy(updatedBy);
                userHistory.setUser(user);

                dao.create(userHistory);

                return Response.status(200).build();
            }
        }
        return Response.status(400).build();
    }

    @GET
    @Path("/getUserById/{userId}")
    @ApiOperation(value = "Get user By id")
    @UnitOfWork
    public Response getUserById(@PathParam("userId") final Long userId) {
        UserModel model = null;
        final Map<String, Object> options = new HashMap<String, Object>();
        options.put("userId", userId);

        @SuppressWarnings("unchecked")
        final List<Object[]> resultList = dao.findByNamedQuery("get.user.by.id",
                new QueryOptions(options));
        if (resultList != null && !resultList.isEmpty()
                && resultList.get(0) != null) {
            final Object[] obj = resultList.get(0);

            model = new UserModel();
            model.setId(obj[0] != null ? Long.valueOf(obj[0].toString()) : -1L);
            model.setFirstName(obj[1] != null ? obj[1].toString() : "");
            model.setLastName(obj[2] != null ? obj[2].toString() : "");
            model.setEmail(obj[3] != null ? obj[3].toString() : "");
            model.setPhone(obj[4] != null ? obj[4].toString() : "");
            model.setGender(
                    obj[5] != null ? Short.valueOf(obj[5].toString()) : 0);
            model.setBirthDate(obj[6] != null ? dateFormat.format(obj[6]) : "");
            model.setAddress1(obj[7] != null ? obj[7].toString() : "");
            model.setAddress2(obj[8] != null ? obj[8].toString() : "");
            model.setCountryId(
                    obj[9] != null ? Long.valueOf(obj[9].toString()) : -1L);
            model.setStateId(
                    obj[10] != null ? Long.valueOf(obj[10].toString()) : -1L);
            model.setCityId(
                    obj[11] != null ? Long.valueOf(obj[11].toString()) : -1L);
            model.setCity(obj[12] != null ? obj[12].toString() : "");
            model.setZipcode(obj[13] != null ? obj[13].toString() : "");
            model.setCompanyId(
                    obj[14] != null ? Long.valueOf(obj[14].toString()) : -1L);
            model.setUserStatusId(
                    obj[15] != null ? Long.valueOf(obj[15].toString()) : -1L);
            model.setUserTypeId(
                    obj[16] != null ? Long.valueOf(obj[16].toString()) : -1L);
            model.setStatus(
                    obj[17] != null ? Short.valueOf(obj[17].toString()) : 0);
            model.setUserName(obj[18] != null ? obj[18].toString() : "");
        }
        return Response.status(200).entity(model).build();
    }

}
