package com.leela.resource;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.hibernate.SessionFactory;

import com.leela.dao.GenericDAO;
import com.leela.dao.HibernateDAO;
import com.leela.dao.QueryOptions;
import com.leela.entity.User;
import com.leela.entity.UserHistory;
import com.leela.entity.UserStatus;
import com.leela.entity.UserVerificationCode;
import com.leela.util.CommonsUtil;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;

import io.dropwizard.hibernate.UnitOfWork;

@Path("/user-activation")
@Api
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class FrontEndUserVerificationResource {

    private final GenericDAO dao;

    public FrontEndUserVerificationResource(
            final SessionFactory sessionFactory) {
        this.dao = new HibernateDAO(sessionFactory);
    }

    @GET
    @Path("/{code}")
    @ApiOperation(value = "User verification of new fron end user")
    @UnitOfWork
    public Response userVerification(@PathParam("code") final String code) {
        final Map<String, Object> options = new HashMap<String, Object>();
        options.put("code", code);

        @SuppressWarnings("unchecked")
        final List<UserVerificationCode> resultList = dao.findByNamedQuery(
                "get.inactive.users.verification.by.code",
                new QueryOptions(options));
        if (resultList != null && !resultList.isEmpty()
                && resultList.get(0) != null) {
            final UserVerificationCode userVerificationCode = resultList.get(0);
            userVerificationCode.setStatus(CommonsUtil.ACTIVE_STATUS);
            dao.update(userVerificationCode);

            final User user = userVerificationCode.getUser();

            final UserStatus userStatus = new UserStatus();
            userStatus.setId(CommonsUtil.VERIFIED_USER);
            user.setUserStatus(userStatus);

            user.setUpdatedBy(user);
            user.setUpdatedDate(new Date());

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
            userHistory.setCreatedBy(user);
            userHistory.setUser(user);

            dao.create(userHistory);

            return Response.status(200).build();
        }
        return Response.status(400).build();
    }

}
