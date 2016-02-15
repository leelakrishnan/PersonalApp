package com.leela.resource;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.mail.MessagingException;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
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
import com.leela.entity.Registration;
import com.leela.entity.User;
import com.leela.entity.UserHistory;
import com.leela.entity.UserStatus;
import com.leela.entity.UserType;
import com.leela.entity.UserVerificationCode;
import com.leela.mail.SendEmailService;
import com.leela.model.FrontEndUserModel;
import com.leela.model.SimpleMailMessage;
import com.leela.util.CommonsUtil;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;

import io.dropwizard.hibernate.UnitOfWork;

@Path("/signUp")
@Api
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class FrontEndUserRegistrationResource {
    private static final Logger LOGGER = LoggerFactory
            .getLogger(FrontEndUserRegistrationResource.class);

    private final GenericDAO dao;
    private final SendEmailService emailer;

    private final SimpleDateFormat dateFormat = new SimpleDateFormat(
            "MM/dd/yyyy");

    public FrontEndUserRegistrationResource(final SessionFactory sessionFactory,
            final SendEmailService emailer) {
        this.dao = new HibernateDAO(sessionFactory);
        this.emailer = emailer;
    }

    @POST
    @ApiOperation(value = "Register a new fron end user")
    @UnitOfWork
    public Response register(final FrontEndUserModel userModel) {

        if (!isUserExist(userModel.getPhone(), userModel.getEmail(),
                userModel.getCompanyId())) {
            final User user = new User();
            user.setFirstName(userModel.getFirstName());
            user.setLastName(userModel.getLastName());
            user.setPhone(userModel.getPhone());
            user.setEmail(userModel.getEmail());
            try {
                user.setPassword(CommonsUtil
                        .encryptionUsingSHA512(userModel.getPassword()));
            } catch (final NoSuchAlgorithmException e) {
                LOGGER.error("Error", e);
            } catch (final UnsupportedEncodingException e) {
                LOGGER.error("Error", e);
            }
            user.setGender(userModel.getGender());
            try {
                user.setBirthDate(dateFormat.parse(userModel.getBirthDate()));
            } catch (final ParseException e) {
                LOGGER.error("Error", e);
            }

            final Company company = new Company();
            company.setId(userModel.getCompanyId());
            user.setCompany(company);

            final UserType userType = new UserType();
            userType.setId(userModel.getUserTypeId());
            user.setUserType(userType);

            final UserStatus userStatus = new UserStatus();
            userStatus.setId(CommonsUtil.NOT_VERIFIED_USER);
            user.setUserStatus(userStatus);

            user.setStatus(CommonsUtil.ACTIVE_STATUS);
            user.setCreatedDate(new Date());
            user.setCreatedBy(user);

            dao.create(user);

            final UserHistory userHistory = new UserHistory();
            userHistory.setFirstName(userModel.getFirstName());
            userHistory.setLastName(userModel.getLastName());
            userHistory.setPhone(userModel.getPhone());
            userHistory.setEmail(userModel.getEmail());
            try {
                userHistory.setPassword(CommonsUtil
                        .encryptionUsingSHA512(userModel.getPassword()));
            } catch (final NoSuchAlgorithmException e) {
                LOGGER.error("Error", e);
            } catch (final UnsupportedEncodingException e) {
                LOGGER.error("Error", e);
            }
            userHistory.setGender(userModel.getGender());
            try {
                userHistory.setBirthDate(
                        dateFormat.parse(userModel.getBirthDate()));
            } catch (final ParseException e) {
                LOGGER.error("Error", e);
            }

            userHistory.setCompany(company);
            userHistory.setUserType(userType);
            userHistory.setUserStatus(userStatus);

            userHistory.setStatus(CommonsUtil.ACTIVE_STATUS);
            userHistory.setCreatedDate(new Date());
            userHistory.setCreatedBy(user);
            userHistory.setUser(user);

            dao.create(userHistory);

            final Registration registration = new Registration();
            registration.setFirstName(userModel.getFirstName());
            registration.setLastName(userModel.getLastName());
            registration.setEmail(userModel.getEmail());
            registration.setPhone(userModel.getPhone());

            registration.setLat(userModel.getLat());
            registration.setLang(userModel.getLang());

            registration.setUser(user);
            registration.setStatus(CommonsUtil.ACTIVE_STATUS);

            dao.create(registration);

            final UserVerificationCode userVerificationCode = new UserVerificationCode();
            userVerificationCode.setCode(UUID.randomUUID().toString());
            userVerificationCode.setUser(user);
            userVerificationCode.setCreatedDate(new Date());
            userVerificationCode.setStatus(CommonsUtil.IN_ACTIVE_STATUS);

            dao.create(userVerificationCode);

            final SimpleMailMessage email = new SimpleMailMessage();
            email.setFrom("bunty5322@gmail.com");
            email.setReplyTo("bunty5322@gmail.com");
            email.setSentDate(new Date());
            email.setTo(new String[] { userModel.getEmail() });
            email.setSubject("Activation Mail");
            email.setText("<a href=\"" + CommonsUtil.APPLICATION_LINK
                    + "/user-activation/" + userVerificationCode.getCode()
                    + "\">Link</a>");
            try {
                emailer.send(email);
            } catch (final MessagingException e) {
                LOGGER.error("Error", e);
            }
            return Response.status(200).entity(user.getId()).build();
        }
        return Response.status(400).build();
    }

    private boolean isUserExist(final String phone, final String email,
            final Long companyId) {
        final Map<String, Object> options = new HashMap<String, Object>();
        options.put("phone", phone);
        options.put("email", email);
        options.put("companyId", companyId);

        @SuppressWarnings("unchecked")
        final List<Object> resultList = dao.findByNamedQuery(
                "get.users.by.phone.email.and.company",
                new QueryOptions(options));
        if (resultList != null && !resultList.isEmpty()) {
            return true;
        }
        return false;
    }
}
