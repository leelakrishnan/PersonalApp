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
import com.leela.entity.Brand;
import com.leela.entity.Company;
import com.leela.entity.Product;
import com.leela.entity.ProductCategory;
import com.leela.entity.ProductImage;
import com.leela.entity.ProductSubCategory;
import com.leela.entity.ProductSubType;
import com.leela.entity.ProductType;
import com.leela.entity.User;
import com.leela.model.ObjectModel;
import com.leela.model.ProductModel;
import com.leela.model.UpdateStatusModel;
import com.leela.util.CommonsUtil;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;

import io.dropwizard.hibernate.UnitOfWork;

@Path("/product")
@Api
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ProductResource {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(ProductResource.class);

    private final GenericDAO dao;

    public ProductResource(final SessionFactory sessionFactory) {
        this.dao = new HibernateDAO(sessionFactory);
    }

    @GET
    @Path("/getProduct/{companyId}")
    @ApiOperation(value = "Get product list")
    @UnitOfWork
    public Response getProduct(@PathParam("companyId") final Long companyId) {
        final Map<String, Object> options = new HashMap<String, Object>();
        options.put("companyId", companyId);

        @SuppressWarnings("unchecked")
        final List<Object[]> resultList = dao.findByNamedQuery(
                "get.all.products.by.company", new QueryOptions(options));
        final List<ObjectModel> objectModels = CommonsUtil
                .convertToObjectModelWithStatus(resultList);
        return Response.status(200).entity(objectModels).build();
    }

    @SuppressWarnings("unchecked")
    @GET
    @Path("/validateProduct/{companyId}/{name}/{productId}")
    @ApiOperation(value = "Validate product name")
    @UnitOfWork
    public Response validateProduct(
            @PathParam("companyId") final Long companyId,
            @PathParam("name") final String name,
            @PathParam("productId") final Long productId) {
        List<Long> resultList = null;
        final Map<String, Object> options = new HashMap<String, Object>();
        options.put("name", name);
        options.put("companyId", companyId);
        if (productId != null && productId.longValue() != -1) {
            options.put("productId", productId);
            resultList = dao.findByNamedQuery(
                    "validate.name.products.by.company.edit",
                    new QueryOptions(options));
        } else {
            resultList = dao.findByNamedQuery(
                    "validate.name.products.by.company.add",
                    new QueryOptions(options));
        }
        if (resultList != null && !resultList.isEmpty()
                && resultList.get(0) != null) {
            return Response.status(200).entity(resultList.get(0)).build();
        }
        return Response.status(200).entity("-1").build();
    }

    @POST
    @Path("/saveProduct")
    @ApiOperation(value = "Add new product")
    @UnitOfWork
    public Response saveProduct(final ProductModel model) {

        final User user = new User();
        user.setId(model.getUserId());

        final Company company = new Company();
        company.setId(model.getCompanyId());

        final ProductCategory productCategory = new ProductCategory();
        productCategory.setId(model.getProductCategoryId());

        final Brand brand = new Brand();
        brand.setId(model.getBrandId());

        final Product product = new Product();
        product.setCreatedDate(new Date());
        product.setCreatedBy(user);
        product.setName(model.getName());
        product.setDescription(model.getDescription());
        product.setShortDescription(model.getShortDescription());
        product.setListPrice(model.getListPrice());
        product.setProductCategory(productCategory);
        product.setBrand(brand);
        if (model.getProductSubCategoryId() != null
                && model.getProductSubCategoryId().longValue() != -1L) {
            final ProductSubCategory productSubCategory = new ProductSubCategory();
            productSubCategory.setId(model.getProductSubCategoryId());
            product.setProductSubCategory(productSubCategory);
        }
        if (model.getProductTypeId() != null
                && model.getProductTypeId().longValue() != -1L) {
            final ProductType productType = new ProductType();
            productType.setId(model.getProductTypeId());
            product.setProductType(productType);
        }
        if (model.getProductSubTypeId() != null
                && model.getProductSubTypeId().longValue() != -1L) {
            final ProductSubType productSubType = new ProductSubType();
            productSubType.setId(model.getProductSubTypeId());
            product.setProductSubType(productSubType);
        }
        product.setStatus(model.getStatus());
        product.setCompany(company);
        dao.create(product);
        return Response.status(200).entity(product.getId()).build();
    }

    @PUT
    @Path("/updateProduct/{productId}")
    @ApiOperation(value = "Update product")
    @UnitOfWork
    public Response updateProduct(@PathParam("productId") final Long productId,
            final ProductModel model) {

        final User user = new User();
        user.setId(model.getUserId());

        final Company company = new Company();
        company.setId(model.getCompanyId());

        final ProductCategory productCategory = new ProductCategory();
        productCategory.setId(model.getProductCategoryId());

        final Brand brand = new Brand();
        brand.setId(model.getBrandId());

        final Product product = new Product();
        product.setId(productId);
        dao.load(product);

        product.setUpdatedDate(new Date());
        product.setUpdatedBy(user);
        product.setName(model.getName());
        product.setDescription(model.getDescription());
        product.setShortDescription(model.getShortDescription());
        product.setListPrice(model.getListPrice());
        product.setProductCategory(productCategory);
        product.setBrand(brand);
        if (model.getProductSubCategoryId() != null
                && model.getProductSubCategoryId().longValue() != -1L) {
            final ProductSubCategory productSubCategory = new ProductSubCategory();
            productSubCategory.setId(model.getProductSubCategoryId());
            product.setProductSubCategory(productSubCategory);
        }
        if (model.getProductTypeId() != null
                && model.getProductTypeId().longValue() != -1L) {
            final ProductType productType = new ProductType();
            productType.setId(model.getProductTypeId());
            product.setProductType(productType);
        }
        if (model.getProductSubTypeId() != null
                && model.getProductSubTypeId().longValue() != -1L) {
            final ProductSubType productSubType = new ProductSubType();
            productSubType.setId(model.getProductSubTypeId());
            product.setProductSubType(productSubType);
        }
        product.setStatus(model.getStatus());
        product.setCompany(company);
        dao.update(product);

        return Response.status(200).entity(product.getId()).build();
    }

    @PUT
    @Path("/updateProductStatus/{productId}")
    @ApiOperation(value = "Update product status")
    @UnitOfWork
    public Response updateProductStatus(
            @PathParam("productId") final Long productId,
            final UpdateStatusModel model) {

        final User user = new User();
        user.setId(model.getUserId());

        final Product product = new Product();
        product.setId(productId);
        dao.load(product);

        product.setUpdatedDate(new Date());
        product.setUpdatedBy(user);
        product.setStatus(model.getStatus());
        dao.update(product);

        return Response.status(200).entity(product.getId()).build();
    }

    @GET
    @Path("/getAciveProduct/{companyId}")
    @ApiOperation(value = "Get active product list")
    @UnitOfWork
    public Response getAciveProduct(
            @PathParam("companyId") final Long companyId) {
        final Map<String, Object> options = new HashMap<String, Object>();
        options.put("companyId", companyId);

        @SuppressWarnings("unchecked")
        final List<Object[]> resultList = dao.findByNamedQuery(
                "get.active.products.by.company", new QueryOptions(options));
        final List<ObjectModel> objectModels = CommonsUtil
                .convertToObjectModel(resultList);
        return Response.status(200).entity(objectModels).build();
    }

    @GET
    @Path("/getAciveProductByProductCategoryAndBrand/{companyId}/{productCategoryId}/{brandId}")
    @ApiOperation(value = "Get active product list by product category and brand")
    @UnitOfWork
    public Response getAciveProduct(
            @PathParam("companyId") final Long companyId,
            @PathParam("companyId") final Long productCategoryId,
            @PathParam("companyId") final Long brandId) {
        final Map<String, Object> options = new HashMap<String, Object>();
        options.put("companyId", companyId);
        options.put("productCategoryId", productCategoryId);
        options.put("brandId", brandId);

        @SuppressWarnings("unchecked")
        final List<Object[]> resultList = dao.findByNamedQuery(
                "get.active.products.by.product.category.and.brand",
                new QueryOptions(options));
        final List<ObjectModel> objectModels = CommonsUtil
                .convertToObjectModel(resultList);
        return Response.status(200).entity(objectModels).build();
    }

    @Path("/addProductImage/{userId}/{productId}")
    @POST
    @ApiOperation(value = "Update product logo")
    @Consumes("multipart/form-data")
    @UnitOfWork
    public Response addProductImage(@PathParam("userId") final Long userId,
            @PathParam("productId") final Long productId,
            @Context final HttpServletRequest request) {

        if (ServletFileUpload.isMultipartContent(request)) {

            final User user = new User();
            user.setId(userId);

            final Product product = new Product();
            product.setId(productId);

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

                final ProductImage productImage = new ProductImage();
                productImage.setProduct(product);
                productImage.setCreatedDate(new Date());
                productImage.setCreatedBy(user);
                productImage.setStatus(CommonsUtil.ACTIVE_STATUS);

                final FileItem item = items.get(0);
                productImage.setImage(item.get());
                dao.update(product);

                return Response.status(200).build();
            }
        }
        return Response.status(400).build();
    }

    @GET
    @Path("/getProductById/{productId}")
    @ApiOperation(value = "Get product By id")
    @UnitOfWork
    public Response getProductById(
            @PathParam("productId") final Long productId) {
        ProductModel model = null;
        final Map<String, Object> options = new HashMap<String, Object>();
        options.put("productId", productId);

        @SuppressWarnings("unchecked")
        final List<Object[]> resultList = dao.findByNamedQuery(
                "get.product.by.id", new QueryOptions(options));
        if (resultList != null && !resultList.isEmpty()
                && resultList.get(0) != null) {
            final Object[] obj = resultList.get(0);

            model = new ProductModel();
            model.setId(obj[0] != null ? Long.valueOf(obj[0].toString()) : -1L);
            model.setName(obj[1] != null ? obj[1].toString() : "");
            model.setDescription(obj[2] != null ? obj[2].toString() : "");
            model.setShortDescription(obj[3] != null ? obj[3].toString() : "");
            model.setProductCategoryId(
                    obj[4] != null ? Long.valueOf(obj[4].toString()) : -1L);
            model.setProductSubCategoryId(
                    obj[5] != null ? Long.valueOf(obj[5].toString()) : -1L);
            model.setProductTypeId(
                    obj[6] != null ? Long.valueOf(obj[6].toString()) : -1L);
            model.setProductSubTypeId(
                    obj[7] != null ? Long.valueOf(obj[7].toString()) : -1L);
            model.setBrandId(
                    obj[8] != null ? Long.valueOf(obj[8].toString()) : -1L);
            model.setCompanyId(
                    obj[9] != null ? Long.valueOf(obj[9].toString()) : -1L);
            model.setStatus(
                    obj[10] != null ? Short.valueOf(obj[10].toString()) : 0);
            model.setListPrice(
                    obj[11] != null ? Double.valueOf(obj[11].toString()) : 0);
        }
        return Response.status(200).entity(model).build();
    }

}
