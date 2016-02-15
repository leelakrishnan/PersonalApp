package com.leela.resource;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.mail.MessagingException;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.leela.dao.GenericDAO;
import com.leela.dao.HibernateDAO;
import com.leela.dao.QueryOptions;
import com.leela.entity.Company;
import com.leela.entity.ForgotPasswordRequestLog;
import com.leela.entity.User;
import com.leela.entity.UserHistory;
import com.leela.mail.SendEmailService;
import com.leela.model.ForgotPasswordModel;
import com.leela.model.SimpleMailMessage;
import com.leela.util.CommonsUtil;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;

import io.dropwizard.hibernate.UnitOfWork;

@Path("/forgot-password")
@Api
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ForgotPasswordResource {
    private static final Logger LOGGER = LoggerFactory
            .getLogger(ForgotPasswordResource.class);

    private final GenericDAO dao;
    private final SendEmailService emailer;

    public ForgotPasswordResource(final SessionFactory sessionFactory,
            final SendEmailService emailer) {
        this.dao = new HibernateDAO(sessionFactory);
        this.emailer = emailer;
    }

    @GET
    @Path("/{companyId}/{email}")
    @ApiOperation(value = "Forgot password by email")
    @UnitOfWork
    public Response forgotPassword(@PathParam("companyId") final Long companyId,
            @PathParam("email") final String email) {
        final Company company = new Company();
        company.setId(companyId);

        final ForgotPasswordRequestLog forgotPasswordRequestLog = new ForgotPasswordRequestLog();
        forgotPasswordRequestLog.setCompany(company);
        forgotPasswordRequestLog.setEmail(email);
        forgotPasswordRequestLog.setCreatedDate(new Date());
        forgotPasswordRequestLog.setStatus(CommonsUtil.IN_ACTIVE_STATUS);
        forgotPasswordRequestLog.setCode(UUID.randomUUID().toString());
        dao.create(forgotPasswordRequestLog);

        final Map<String, Object> options = new HashMap<String, Object>();
        options.put("email", email);
        options.put("companyId", companyId);

        @SuppressWarnings("unchecked")
        final List<Long> resultList = dao.findByNamedQuery(
                "get.users.by.email.and.company", new QueryOptions(options));
        if (resultList == null || resultList.isEmpty()
                || resultList.get(0) == null) {
            return Response.status(400).build();
        }

        final SimpleMailMessage emailMessage = new SimpleMailMessage();
        emailMessage.setFrom("bunty5322@gmail.com");
        emailMessage.setReplyTo("bunty5322@gmail.com");
        emailMessage.setSentDate(new Date());
        emailMessage.setTo(new String[] { email });
        emailMessage.setSubject("Forgot Password Mail");
        emailMessage.setText("<a href=\"" + CommonsUtil.APPLICATION_LINK
                + "/forgot-password/verification/"
                + forgotPasswordRequestLog.getCode() + "\">Link</a>");
        try {
            emailer.send(emailMessage);
        } catch (final MessagingException e) {
            LOGGER.error("Error", e);
        }

        return Response.status(200).entity(resultList.get(0)).build();
    }

    @GET
    @Path("verification/{code}")
    @ApiOperation(value = "Forgot password verification by email")
    @UnitOfWork
    public Response verification(@PathParam("code") final String code) {
        final Map<String, Object> options = new HashMap<String, Object>();
        options.put("code", code);

        @SuppressWarnings("unchecked")
        final List<Long> resultList = dao.findByNamedQuery(
                "get.inactive.forgot.password.request.by.code",
                new QueryOptions(options));
        if (resultList == null || resultList.isEmpty()
                || resultList.get(0) == null) {
            return Response.status(400).build();
        }

        return Response.status(200).entity(resultList.get(0)).build();
    }

    @POST
    @ApiOperation(value = "Forgot password submit")
    @UnitOfWork
    public Response forgotPasswordSubmit(final ForgotPasswordModel model) {
        final ForgotPasswordRequestLog forgotPasswordRequestLog = new ForgotPasswordRequestLog();
        forgotPasswordRequestLog.setId(model.getForgotPasswordRequestLogId());
        dao.load(forgotPasswordRequestLog);
        forgotPasswordRequestLog.setStatus(CommonsUtil.ACTIVE_STATUS);
        dao.update(forgotPasswordRequestLog);

        final Map<String, Object> options = new HashMap<String, Object>();
        options.put("email", forgotPasswordRequestLog.getEmail());
        options.put("companyId", forgotPasswordRequestLog.getCompany().getId());

        @SuppressWarnings("unchecked")
        final List<Long> resultUserList = dao.findByNamedQuery(
                "get.users.by.email.and.company", new QueryOptions(options));
        if (resultUserList != null && !resultUserList.isEmpty()
                && resultUserList.get(0) != null) {
            final User user = new User();
            user.setId(resultUserList.get(0));
            dao.load(user);
            try {
                user.setPassword(
                        CommonsUtil.encryptionUsingSHA512(model.getPassword()));
            } catch (final NoSuchAlgorithmException e) {
                LOGGER.error("Error", e);
            } catch (final UnsupportedEncodingException e) {
                LOGGER.error("Error", e);
            }

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
