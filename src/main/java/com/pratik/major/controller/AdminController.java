package com.pratik.major.controller;

import com.pratik.major.model.Product;
import com.pratik.major.service.CategoryService;
import com.pratik.major.service.ProductService;
import com.pratik.major.dto.ProductDTO;
import com.pratik.major.model.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

@Controller
public class AdminController {
    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ProductService productService;

    public static String uploadDir= System.getProperty("user.dir")+"/src/main/resources/static/productImages";
    // user.dir will give me curr directory on in which this application is stored.
    @GetMapping("/admin")
    public String adminHome()
    {
        return "adminHome"; //file name of  page  will be written
        // which u want  to return and user controller not rest controller
    }
    @GetMapping("/admin/categories")
    public String getCategories(Model model)
    {
        List<Category>categories=categoryService.getAllCategory();
        model.addAttribute("categories",categories);
        return "categories";
    }

    @GetMapping("/admin/categories/add")
    public String addCategoriesGet(Model model)
    {
        model.addAttribute("category",new Category());
        return "categoriesAdd";
    }  //this method is  just use to rendered cateory add  page
    /*without passinng model with category obj our thymeleaf categoryAdd page will not be rendered ? right
    That's correct! In order to render a Thymeleaf template with input fields bound to object properties,
    you need to pass a model object to the template.
     */

    //this will post method  which  will use to add category obj data
    @PostMapping("/admin/categories/add")
    public String addCategoriesPost(@ModelAttribute("category")Category category)
    {
        categoryService.addCategory(category);
        return "redirect:/admin/categories";  //so after this it will redirect to given url
    }

    @GetMapping("/admin/categories/delete/{id}")
    public String removeCategoryById(@PathVariable Integer id)
    {
        categoryService.removeCategoryById(id);
        return "redirect:/admin/categories";
    }

    @GetMapping("/admin/categories/update/{id}")
    public String updateCategoryById(@PathVariable Integer id,Model model)
    {
        Optional<Category> optionalCategory=categoryService.findCategoryById(id);
        if(optionalCategory.isPresent())
        {
            model.addAttribute("category",optionalCategory.get());
            return "categoriesAdd";
        }
        else {
           return  "404";
        }
    }



    /* ------------------------PRODUCT SECTION STARTS---------------------------------------*/
    @GetMapping("/admin/products")
    public String getProducts(Model model)
    {
        List<Product>products=productService.getAllProducts();
        model.addAttribute("products",products);
        return "products";
    }

    @GetMapping("/admin/products/add")
    public String addProductGet(Model model)
    {
        model.addAttribute("productDTO",new ProductDTO());
        model.addAttribute("categories",categoryService.getAllCategory());
        return "productsAdd";
    }
    @PostMapping("/admin/products/add")
    public String addProductPost(@ModelAttribute("productDTO") ProductDTO productDTO,
                                 @RequestParam("productImage")MultipartFile file,
                                 @RequestParam("imgName")String imageName) throws IOException
            //MultipartFile is used when we are sending any file like image ,song,html.css or  any other from view
    {
        Product product=new Product();
        product.setId(productDTO.getId());
        product.setName(productDTO.getName());
        product.setPrice(productDTO.getPrice());
        product.setDescription(productDTO.getDescription());
        product.setWeight(productDTO.getWeight());
        product.setCategory(categoryService.findCategoryById(productDTO.getCategoryId()).get());
        String imageFileName;
        if(file.isEmpty()==false)
        {
            imageFileName=file.getOriginalFilename(); //it will give me original file name
            Path fileNameAndPath= Paths.get(uploadDir,imageFileName); //upload directory will contains
            // the path on which the file need to same
            Files.write(fileNameAndPath,file.getBytes()); //it first  converts given file in byte form
            //then save it to  given path
        }
        else {
            imageFileName=imageName;  //that means user is tryinng to update
        }
        product.setImageName(imageFileName);
        productService.addProduct(product);
        return "redirect:/admin/products";
    }

    @GetMapping("/admin/product/delete/{id}")
    public String removeProductById(@PathVariable  Long id)
    {
        productService.removeProductById(id);
        return "redirect:/products";
    }

    @GetMapping("/admin/product/update/{id}")
    public String updateProductById(@PathVariable  Long id,Model model)
    {
        if(id==null)
        {
            System.out.println("it null");
            return getProducts(model);
        }
        Product product=productService.getProductById(id).get();
        ProductDTO productDto =ProductDTO.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .categoryId(product.getCategory().getId())
                .description(product.getDescription())
                .imageName(product.getImageName())
                .weight(product.getWeight())
                .build();
        model.addAttribute("productDTO",productDto);
        model.addAttribute("categories",categoryService.getAllCategory());
        return "productsAdd";
    }
}
