package com.leela.resource;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.leela.dao.GenericDAO;
import com.leela.dao.HibernateDAO;
import com.leela.dao.QueryOptions;
import com.leela.entity.Address;
import com.leela.entity.City;
import com.leela.entity.Company;
import com.leela.entity.Country;
import com.leela.entity.Order;
import com.leela.entity.OrderPayment;
import com.leela.entity.OrderProduct;
import com.leela.entity.OrderStatus;
import com.leela.entity.OrderSubType;
import com.leela.entity.OrderType;
import com.leela.entity.PaymentType;
import com.leela.entity.Product;
import com.leela.entity.State;
import com.leela.entity.User;
import com.leela.entity.UserHistory;
import com.leela.entity.UserStatus;
import com.leela.entity.UserType;
import com.leela.model.OrderModel;
import com.leela.model.OrderPaymentModel;
import com.leela.model.OrderProductModel;
import com.leela.util.CommonsUtil;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;

import io.dropwizard.hibernate.UnitOfWork;

@Path("/order")
@Api
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class OrderResource {
    private static final Logger LOGGER = LoggerFactory
            .getLogger(OrderResource.class);

    private final GenericDAO dao;

    public OrderResource(final SessionFactory sessionFactory) {
        this.dao = new HibernateDAO(sessionFactory);
    }

    @GET
    @Path("/getOrder/{companyId}")
    @ApiOperation(value = "Get order list")
    @UnitOfWork
    public Response getOrder(@PathParam("companyId") final Long companyId) {
        final SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        final List<OrderModel> models = new ArrayList<OrderModel>();
        final Map<String, Object> options = new HashMap<String, Object>();
        options.put("companyId", companyId);

        @SuppressWarnings("unchecked")
        final List<Object[]> resultList = dao.findByNamedQuery(
                "get.all.orders.by.company.id", new QueryOptions(options));
        if (resultList != null && !resultList.isEmpty()) {
            for (final Object[] obj : resultList) {
                final OrderModel model = new OrderModel();
                model.setOrderId(Long.valueOf(obj[0].toString()));
                model.setOrderType(obj[1] != null ? obj[1].toString() : "");
                model.setOrderStatus(obj[2] != null ? obj[2].toString() : "");
                model.setCustName(obj[3] != null ? obj[3].toString() : "");
                model.setOrderDate(
                        obj[4] != null ? dateFormat.format(obj[4]) : "");
                model.setTotal(obj[5] != null
                        ? Double.valueOf(obj[5].toString()) : 0D);
                model.setStatus(
                        obj[6] != null ? Short.valueOf(obj[6].toString()) : 0);
                models.add(model);
            }
        }
        return Response.status(200).entity(models).build();
    }

    @SuppressWarnings("unchecked")
    @POST
    @Path("/saveOrder")
    @ApiOperation(value = "Add new order")
    @UnitOfWork
    public Response saveOrder(final OrderModel model) {

        final SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");

        final Map<String, Object> options = new HashMap<String, Object>();

        final User user = new User();
        user.setId(model.getUserId());

        final Company company = new Company();
        company.setId(model.getCompanyId());

        final Country countryShipping = new Country();
        countryShipping.setId(model.getCountryIdShipping());

        final State stateShipping = new State();
        stateShipping.setId(model.getStateIdShipping());

        final Country countryBilling = new Country();
        countryBilling.setId(model.getCountryIdBilling());

        final State stateBilling = new State();
        stateBilling.setId(model.getStateIdBilling());

        final Address shippingAddress = new Address();
        shippingAddress.setAddress1(model.getAddress1Shipping());
        shippingAddress.setAddress2(model.getAddress2Shipping());
        shippingAddress.setCountry(countryShipping);
        shippingAddress.setState(stateShipping);
        if (model.getCityIdShipping() != null
                && model.getCityIdShipping().longValue() != -1L) {
            final City city = new City();
            city.setId(model.getCityIdShipping());
            shippingAddress.setCity(city);
        }
        shippingAddress.setOtherCity(model.getCityShipping());
        shippingAddress.setZipcode(model.getZipcodeShipping());
        dao.create(shippingAddress);

        final Address billingAddress = new Address();
        billingAddress.setAddress1(model.getAddress1Billing());
        billingAddress.setAddress2(model.getAddress2Billing());
        billingAddress.setCountry(countryBilling);
        billingAddress.setState(stateBilling);
        if (model.getCityIdBilling() != null
                && model.getCityIdBilling().longValue() != -1L) {
            final City city = new City();
            city.setId(model.getCityIdBilling());
            billingAddress.setCity(city);
        }
        billingAddress.setOtherCity(model.getCityBilling());
        billingAddress.setZipcode(model.getZipcodeBilling());
        dao.create(billingAddress);

        Long customerId = null;

        options.put("email", model.getEmail());
        options.put("phone", model.getPhone());
        options.put("companyId", model.getCompanyId());

        List<Long> resultList = dao.findByNamedQuery(
                "validate.email.user.by.company.add",
                new QueryOptions(options));
        if (resultList != null && !resultList.isEmpty()) {
            customerId = resultList.get(0);
        }
        if (customerId == null) {
            resultList = dao.findByNamedQuery(
                    "validate.phone.user.by.company.add",
                    new QueryOptions(options));
            if (resultList != null && !resultList.isEmpty()) {
                customerId = resultList.get(0);
            }
        }

        final UserType userType = new UserType();
        userType.setId(CommonsUtil.USER_TYPE_USER);

        final UserStatus userStatus = new UserStatus();
        userStatus.setId(CommonsUtil.NOT_VERIFIED_USER);

        final User customer = new User();
        if (customerId != null) {
            customer.setId(customerId);
        } else {
            customer.setCreatedDate(new Date());
            customer.setCreatedBy(user);
            customer.setFirstName(model.getFirstName());
            customer.setLastName(model.getLastName());
            customer.setEmail(model.getEmail());
            customer.setPhone(model.getPhone());
            customer.setGender(CommonsUtil.GENDER_MALE);
            customer.setAddress(billingAddress);
            customer.setStatus(CommonsUtil.ACTIVE_STATUS);
            customer.setUserName("");
            customer.setUserType(userType);
            customer.setUserStatus(userStatus);
            customer.setCompany(company);

            dao.create(customer);

            final UserHistory userHistory = new UserHistory();
            userHistory.setFirstName(customer.getFirstName());
            userHistory.setLastName(customer.getLastName());
            userHistory.setPhone(customer.getPhone());
            userHistory.setEmail(customer.getEmail());
            userHistory.setPassword(customer.getPassword());
            userHistory.setGender(customer.getGender());
            userHistory.setBirthDate(customer.getBirthDate());
            userHistory.setUserName(customer.getUserName());
            userHistory.setUserPhoto(customer.getUserPhoto());
            userHistory.setAddress(customer.getAddress());
            userHistory.setCompany(customer.getCompany());
            userHistory.setUserType(customer.getUserType());
            userHistory.setUserStatus(customer.getUserStatus());

            userHistory.setStatus(customer.getStatus());
            userHistory.setCreatedDate(new Date());
            userHistory.setCreatedBy(user);
            userHistory.setUser(customer);

            dao.create(userHistory);
        }

        final OrderType orderType = new OrderType();
        orderType.setId(model.getOrderTypeId());

        final OrderStatus orderStatus = new OrderStatus();
        orderStatus.setId(model.getOrderStatusId());

        final Order order = new Order();
        order.setOrderType(orderType);
        order.setOrderStatus(orderStatus);
        order.setCompany(company);
        order.setUser(customer);
        order.setBillingAddress(billingAddress);
        order.setShippingAddress(shippingAddress);
        try {
            order.setOrderDate(dateFormat.parse(model.getOrderDate()));
        } catch (final ParseException e) {
            LOGGER.error("Error", e);
        }
        try {
            order.setDeliveryDate(dateFormat.parse(model.getDeliveryDate()));
        } catch (final ParseException e) {
            LOGGER.error("Error", e);
        }
        order.setNotes(model.getNotes());
        order.setSubTotal(model.getSubTotal());
        order.setShipping(model.getShipping());
        order.setDiscount(model.getDiscount());
        order.setTotal(model.getTotal());

        order.setStatus(model.getStatus());
        order.setCreatedBy(user);
        order.setCreatedDate(new Date());

        dao.create(order);

        if (model.getOrderPayments() != null
                && !model.getOrderPayments().isEmpty()) {
            for (final OrderPaymentModel orderPaymentModel : model
                    .getOrderPayments()) {
                final OrderPayment orderPayment = new OrderPayment();
                orderPayment.setOrder(order);
                orderPayment.setPayment(orderPaymentModel.getPayment());

                final PaymentType paymentType = new PaymentType();
                paymentType.setId(orderPaymentModel.getPaymentTypeId());
                orderPayment.setPaymentType(paymentType);

                orderPayment.setStatus(CommonsUtil.ACTIVE_STATUS);
                orderPayment.setCreatedBy(user);
                orderPayment.setCreatedDate(new Date());

                dao.create(orderPayment);
            }
        }

        if (model.getOrderProducts() != null
                && !model.getOrderProducts().isEmpty()) {
            for (final OrderProductModel orderProductModel : model
                    .getOrderProducts()) {
                final OrderProduct orderProduct = new OrderProduct();
                orderProduct.setOrder(order);

                final Product product = new Product();
                product.setId(orderProductModel.getProductId());
                orderProduct.setProduct(product);

                final OrderSubType orderSubType = new OrderSubType();
                orderSubType.setId(orderProductModel.getOrderSubTypeId());
                orderProduct.setOrderSubType(orderSubType);

                orderProduct.setPrice(orderProductModel.getPrice());
                orderProduct.setStatus(CommonsUtil.ACTIVE_STATUS);
                orderProduct.setCreatedBy(user);
                orderProduct.setCreatedDate(new Date());

                dao.create(orderProduct);
            }
        }
        return Response.status(200).entity(order.getId()).build();
    }

    @PUT
    @Path("/updateOrder/{orderId}")
    @ApiOperation(value = "Update order")
    @UnitOfWork
    public Response updateOrder(@PathParam("orderId") final Long orderId,
            final OrderModel model) {

        final SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");

        final User user = new User();
        user.setId(model.getUserId());

        final Company company = new Company();
        company.setId(model.getCompanyId());

        final Country countryShipping = new Country();
        countryShipping.setId(model.getCountryIdShipping());

        final State stateShipping = new State();
        stateShipping.setId(model.getStateIdShipping());

        final Country countryBilling = new Country();
        countryBilling.setId(model.getCountryIdBilling());

        final State stateBilling = new State();
        stateBilling.setId(model.getStateIdBilling());

        final OrderType orderType = new OrderType();
        orderType.setId(model.getOrderTypeId());

        final OrderStatus orderStatus = new OrderStatus();
        orderStatus.setId(model.getOrderStatusId());

        final Order order = new Order();
        order.setId(orderId);
        dao.load(order);

        order.setUpdatedDate(new Date());
        order.setUpdatedBy(user);
        order.setOrderType(orderType);
        order.setOrderStatus(orderStatus);
        order.setCompany(company);
        try {
            order.setOrderDate(dateFormat.parse(model.getOrderDate()));
        } catch (final ParseException e) {
            LOGGER.error("Error", e);
        }
        try {
            order.setDeliveryDate(dateFormat.parse(model.getDeliveryDate()));
        } catch (final ParseException e) {
            LOGGER.error("Error", e);
        }
        order.setNotes(model.getNotes());
        order.setSubTotal(model.getSubTotal());
        order.setShipping(model.getShipping());
        order.setDiscount(model.getDiscount());
        order.setTotal(model.getTotal());

        order.setStatus(model.getStatus());
        dao.update(order);

        final Address billingAddress = order.getBillingAddress();
        billingAddress.setAddress1(model.getAddress1Billing());
        billingAddress.setAddress2(model.getAddress2Billing());
        billingAddress.setCountry(countryBilling);
        billingAddress.setState(stateBilling);
        if (model.getCityIdBilling() != null
                && model.getCityIdBilling().longValue() != -1L) {
            final City city = new City();
            city.setId(model.getCityIdBilling());
            billingAddress.setCity(city);
        }
        billingAddress.setOtherCity(model.getCityBilling());
        billingAddress.setZipcode(model.getZipcodeBilling());
        dao.update(billingAddress);

        final Address shippingAddress = order.getShippingAddress();
        shippingAddress.setAddress1(model.getAddress1Shipping());
        shippingAddress.setAddress2(model.getAddress2Shipping());
        shippingAddress.setCountry(countryShipping);
        shippingAddress.setState(stateShipping);
        if (model.getCityIdShipping() != null
                && model.getCityIdShipping().longValue() != -1L) {
            final City city = new City();
            city.setId(model.getCityIdShipping());
            shippingAddress.setCity(city);
        }
        shippingAddress.setOtherCity(model.getCityShipping());
        shippingAddress.setZipcode(model.getZipcodeShipping());
        dao.update(shippingAddress);

        return Response.status(200).entity(company.getId()).build();
    }

    @GET
    @Path("/getOrderById/{orderId}")
    @ApiOperation(value = "Get order By id")
    @UnitOfWork
    public Response getOrderById(@PathParam("orderId") final Long orderId) {
        final SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");

        final OrderModel model = new OrderModel();
        final Map<String, Object> options = new HashMap<String, Object>();
        options.put("orderId", orderId);

        @SuppressWarnings("unchecked")
        final List<Object[]> resultList = dao.findByNamedQuery(
                "get.order.by.order.id", new QueryOptions(options));
        if (resultList != null && !resultList.isEmpty()
                && resultList.get(0) != null) {
            final Object[] obj = resultList.get(0);

            model.setOrderId(
                    obj[0] != null ? Long.valueOf(obj[0].toString()) : -1L);
            model.setFirstName(obj[1] != null ? obj[1].toString() : "");
            model.setLastName(obj[2] != null ? obj[2].toString() : "");
            model.setPhone(obj[3] != null ? obj[3].toString() : "");
            model.setEmail(obj[4] != null ? obj[4].toString() : "");
            model.setOrderStatusId(
                    obj[5] != null ? Long.valueOf(obj[5].toString()) : -1);
            model.setOrderTypeId(
                    obj[6] != null ? Long.valueOf(obj[6].toString()) : -1);
            model.setOrderDate(obj[7] != null ? dateFormat.format(obj[7]) : "");
            model.setDeliveryDate(
                    obj[8] != null ? dateFormat.format(obj[8]) : "");
            model.setAddress1Shipping(obj[9] != null ? obj[9].toString() : "");
            model.setAddress2Shipping(
                    obj[10] != null ? obj[10].toString() : "");
            model.setCountryIdShipping(
                    obj[11] != null ? Long.valueOf(obj[11].toString()) : -1L);
            model.setStateIdShipping(
                    obj[12] != null ? Long.valueOf(obj[12].toString()) : -1L);
            model.setCityIdShipping(
                    obj[13] != null ? Long.valueOf(obj[13].toString()) : -1L);
            model.setCityShipping(obj[14] != null ? obj[14].toString() : "");
            model.setZipcodeShipping(obj[15] != null ? obj[15].toString() : "");
            model.setAddress1Billing(obj[16] != null ? obj[16].toString() : "");
            model.setAddress2Billing(obj[17] != null ? obj[17].toString() : "");
            model.setCountryIdBilling(
                    obj[18] != null ? Long.valueOf(obj[18].toString()) : -1L);
            model.setStateIdBilling(
                    obj[19] != null ? Long.valueOf(obj[19].toString()) : -1L);
            model.setCityIdBilling(
                    obj[20] != null ? Long.valueOf(obj[20].toString()) : -1L);
            model.setCityBilling(obj[21] != null ? obj[21].toString() : "");
            model.setZipcodeBilling(obj[22] != null ? obj[22].toString() : "");
            model.setSubTotal(
                    obj[23] != null ? Double.valueOf(obj[23].toString()) : 0D);
            model.setShipping(
                    obj[24] != null ? Double.valueOf(obj[24].toString()) : 0D);
            model.setDiscount(
                    obj[25] != null ? Double.valueOf(obj[25].toString()) : 0D);
            model.setTotal(
                    obj[26] != null ? Double.valueOf(obj[26].toString()) : 0D);
            model.setNotes(obj[27] != null ? obj[27].toString() : "");
            model.setStatus(
                    obj[28] != null ? Short.valueOf(obj[28].toString()) : 0);
        }

        double totalpayment = 0;
        List<OrderPaymentModel> orderPaymentModels = null;
        @SuppressWarnings("unchecked")
        final List<Object[]> resultListOrderPayment = dao.findByNamedQuery(
                "get.order.payment.by.order.id", new QueryOptions(options));
        if (resultListOrderPayment != null
                && !resultListOrderPayment.isEmpty()) {
            orderPaymentModels = new ArrayList<OrderPaymentModel>();
            for (final Object[] obj : resultListOrderPayment) {
                final OrderPaymentModel orderPaymentModel = new OrderPaymentModel();
                orderPaymentModel.setId(
                        obj[0] != null ? Long.valueOf(obj[0].toString()) : -1L);
                orderPaymentModel.setPayment(obj[1] != null
                        ? Double.valueOf(obj[1].toString()) : 0D);
                orderPaymentModel.setPaymentTypeId(
                        obj[2] != null ? Long.valueOf(obj[2].toString()) : -1L);
                orderPaymentModel.setPaymentType(
                        obj[3] != null ? obj[3].toString() : "");
                totalpayment += orderPaymentModel.getPayment();

                orderPaymentModels.add(orderPaymentModel);
            }
        }

        List<OrderProductModel> orderProductModels = null;
        @SuppressWarnings("unchecked")
        final List<Object[]> resultListOrderProduct = dao.findByNamedQuery(
                "get.order.product.by.order.id", new QueryOptions(options));
        if (resultListOrderProduct != null
                && !resultListOrderProduct.isEmpty()) {
            orderProductModels = new ArrayList<OrderProductModel>();
            for (final Object[] obj : resultListOrderProduct) {
                final OrderProductModel orderProductModel = new OrderProductModel();
                orderProductModel.setId(
                        obj[0] != null ? Long.valueOf(obj[0].toString()) : -1L);
                orderProductModel.setPrice(obj[1] != null
                        ? Double.valueOf(obj[1].toString()) : 0D);
                orderProductModel.setProductId(
                        obj[2] != null ? Long.valueOf(obj[2].toString()) : -1L);
                orderProductModel
                        .setProduct(obj[3] != null ? obj[3].toString() : "");
                orderProductModel.setOrderSubTypeId(
                        obj[4] != null ? Long.valueOf(obj[4].toString()) : -1L);
                orderProductModel.setOrderSubType(
                        obj[5] != null ? obj[5].toString() : "");

                orderProductModels.add(orderProductModel);
            }
        }

        model.setPaid(totalpayment);
        model.setDue(model.getTotal() - model.getPaid());
        model.setOrderPayments(orderPaymentModels);
        model.setOrderProducts(orderProductModels);

        return Response.status(200).entity(model).build();
    }

}
