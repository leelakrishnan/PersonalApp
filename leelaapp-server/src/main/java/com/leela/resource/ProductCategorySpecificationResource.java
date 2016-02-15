package com.leela.resource;

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

import com.leela.dao.GenericDAO;
import com.leela.dao.HibernateDAO;
import com.leela.dao.QueryOptions;
import com.leela.entity.Company;
import com.leela.entity.ProductCategory;
import com.leela.entity.ProductCategorySpecification;
import com.leela.entity.ProductSpecification;
import com.leela.entity.User;
import com.leela.model.ProductCategorySpecificationModel;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;

import io.dropwizard.hibernate.UnitOfWork;

@Path("/productCategorySpecification")
@Api
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ProductCategorySpecificationResource {

    private final GenericDAO dao;

    public ProductCategorySpecificationResource(
            final SessionFactory sessionFactory) {
        this.dao = new HibernateDAO(sessionFactory);
    }

    @GET
    @Path("/getProductSpecificationByCategoryAndGroup/{companyId}/{productCategoryId}/{productSpecificationGroupId}")
    @ApiOperation(value = "Get product category list")
    @UnitOfWork
    public Response getProductCategory(
            @PathParam("companyId") final Long companyId,
            @PathParam("productCategoryId") final Long productCategoryId,
            @PathParam("productSpecificationGroupId") final Long productSpecificationGroupId) {
        final Map<String, Object> options = new HashMap<String, Object>();
        options.put("companyId", companyId);
        options.put("productCategoryId", productCategoryId);
        options.put("productSpecificationGroupId", productSpecificationGroupId);

        final String query = "SELECT ps.`product_specification_id`, "
                + "ps.`name`, pcs.product_category_specification_id, pcs.status "
                + "FROM product_specifications ps "
                + "LEFT JOIN `product_category_specifications` pcs "
                + "ON ps.`product_specification_id` = pcs.product_specification_id "
                + "AND pcs.product_category_id = :productCategoryId "
                + "LEFT JOIN `company_product_specifications` cps "
                + "ON ps.`product_specification_id` = cps.`product_specification_id` "
                + "WHERE cps.company_id = :companyId "
                + "AND ps.`product_specification_group_id` = :productSpecificationGroupId";
        @SuppressWarnings("unchecked")
        final List<Object[]> resultList = dao.findByNativeQuery(query,
                new QueryOptions(options));
        final List<ProductCategorySpecificationModel> objectModels = new ArrayList<ProductCategorySpecificationModel>();
        if (resultList != null && !resultList.isEmpty()) {
            for (final Object[] obj : resultList) {
                final ProductCategorySpecificationModel model = new ProductCategorySpecificationModel();
                model.setProductSpecificationId(
                        Long.valueOf(obj[0].toString()));
                model.setName(obj[1].toString());
                model.setId(
                        obj[2] != null ? Long.valueOf(obj[2].toString()) : -1L);
                model.setStatus(
                        obj[3] != null ? Short.valueOf(obj[3].toString()) : 0);
                objectModels.add(model);
            }
        }
        return Response.status(200).entity(objectModels).build();
    }

    @POST
    @Path("/saveProductCategorySpecification")
    @ApiOperation(value = "Save new product category specification")
    @UnitOfWork
    public Response saveProductCategorySpecification(
            final ProductCategorySpecificationModel model) {

        final User user = new User();
        user.setId(model.getUserId());

        final Company company = new Company();
        company.setId(model.getCompanyId());

        final ProductCategory productCategory = new ProductCategory();
        productCategory.setId(model.getProductCategoryId());

        final ProductSpecification productSpecification = new ProductSpecification();
        productSpecification.setId(model.getProductSpecificationId());

        final ProductCategorySpecification productCategorySpecification = new ProductCategorySpecification();
        productCategorySpecification.setCreatedDate(new Date());
        productCategorySpecification.setCreatedBy(user);
        productCategorySpecification.setStatus(model.getStatus());
        productCategorySpecification.setCompany(company);
        productCategorySpecification.setProductCategory(productCategory);
        productCategorySpecification
                .setProductSpecification(productSpecification);
        dao.create(productCategorySpecification);

        return Response.status(200).entity(productCategorySpecification.getId())
                .build();
    }

    @PUT
    @Path("/updateProductCategorySpecification/{productCategorySpecificationId}")
    @ApiOperation(value = "Update company product category")
    @UnitOfWork
    public Response updateProductCategorySpecification(
            @PathParam("productCategorySpecificationId") final Long productCategorySpecificationId,
            final ProductCategorySpecificationModel model) {

        final User user = new User();
        user.setId(model.getUserId());

        final ProductCategorySpecification productCategorySpecification = new ProductCategorySpecification();
        productCategorySpecification.setId(productCategorySpecificationId);
        dao.load(productCategorySpecification);

        productCategorySpecification.setUpdatedDate(new Date());
        productCategorySpecification.setUpdatedBy(user);
        productCategorySpecification.setStatus(model.getStatus());
        dao.update(productCategorySpecification);

        return Response.status(200).entity(productCategorySpecification.getId())
                .build();
    }

}
