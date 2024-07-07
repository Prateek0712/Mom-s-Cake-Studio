package com.pratik.major.controller;

import com.pratik.major.model.User;
import com.pratik.major.repository.UserRepository;
import com.pratik.major.service.CategoryService;
import com.pratik.major.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Controller
public class HomeController {

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping({"/", "/home"})  //if we want to  pass multiple endpoint to  same method we need to wrap it  up
    //with array "{ , ,}"  and comma seprete each end  point
    public String viewHome(Model model, HttpServletRequest request)
    {
        String userEmailOfSimplePasswordBasedSecurity="";
        String userEmailOfOAuth2="";
        try{
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.isAuthenticated()) {
                OAuth2AuthenticationToken token = (OAuth2AuthenticationToken) authentication; //this will send by google

                userEmailOfOAuth2 = token.getPrincipal().getAttributes().get("email").toString();
            }
        }
        catch(Exception e)
        {
            userEmailOfSimplePasswordBasedSecurity=request.getRemoteUser();
        }
        Optional<User>optionalUser=userRepository.findByEmail(userEmailOfSimplePasswordBasedSecurity).isPresent()==false
                ? userRepository.findByEmail(userEmailOfOAuth2) :
                userRepository.findByEmail(userEmailOfSimplePasswordBasedSecurity);
        Integer cartSize=0;
        if(optionalUser.isPresent())
        {
            cartSize=optionalUser.get().getCart().size();
            model.addAttribute("cartCount",cartSize);
        }
        return "index";
    }

    @GetMapping("/shop")
    public String getAllProducts(Model model,HttpServletRequest request)
    {
        String userEmailOfSimplePasswordBasedSecurity="";
        String userEmailOfOAuth2="";
        try{
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.isAuthenticated()) {
                OAuth2AuthenticationToken token = (OAuth2AuthenticationToken) authentication; //this will send by google

                userEmailOfOAuth2 = token.getPrincipal().getAttributes().get("email").toString();
            }
        }
        catch(Exception e)
        {
            userEmailOfSimplePasswordBasedSecurity=request.getRemoteUser();
        }
        Optional<User>optionalUser=userRepository.findByEmail(userEmailOfSimplePasswordBasedSecurity).isPresent()==false
                ? userRepository.findByEmail(userEmailOfOAuth2) :
                userRepository.findByEmail(userEmailOfSimplePasswordBasedSecurity);
        Integer cartSize=0;
        if(optionalUser.isPresent())
        {
            cartSize=optionalUser.get().getCart().size();
            model.addAttribute("cartCount",cartSize);
        }
        model.addAttribute("products",productService.getAllProducts());
        model.addAttribute("categories", categoryService.getAllCategory());
        return "shop";
    }

    @GetMapping("/shop/category/{id}")
    public String getProductsByCategoryId(@PathVariable Integer id,Model model,HttpServletRequest request)
    {
        String userEmailOfSimplePasswordBasedSecurity="";
        String userEmailOfOAuth2="";
        try{
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.isAuthenticated()) {
                OAuth2AuthenticationToken token = (OAuth2AuthenticationToken) authentication; //this will send by google

                userEmailOfOAuth2 = token.getPrincipal().getAttributes().get("email").toString();
            }
        }
        catch(Exception e)
        {
            userEmailOfSimplePasswordBasedSecurity=request.getRemoteUser();
        }
        Optional<User>optionalUser=userRepository.findByEmail(userEmailOfSimplePasswordBasedSecurity).isPresent()==false
                ? userRepository.findByEmail(userEmailOfOAuth2) :
                userRepository.findByEmail(userEmailOfSimplePasswordBasedSecurity);
        Integer cartSize=0;
        if(optionalUser.isPresent())
        {
            cartSize=optionalUser.get().getCart().size();
            model.addAttribute("cartCount",cartSize);
        }
        model.addAttribute("categories",categoryService.getAllCategory());
        model.addAttribute("products",productService.getAllProductByCategoryId(id));
        return "shop";
    }

    @GetMapping("/shop/viewproduct/{id}")
    public String viewProductById(@PathVariable Long id,Model model,HttpServletRequest request)
    {
        String userEmailOfSimplePasswordBasedSecurity="";
        String userEmailOfOAuth2="";
        try{
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.isAuthenticated()) {
                OAuth2AuthenticationToken token = (OAuth2AuthenticationToken) authentication; //this will send by google

                userEmailOfOAuth2 = token.getPrincipal().getAttributes().get("email").toString();
            }
        }
        catch(Exception e)
        {
            userEmailOfSimplePasswordBasedSecurity=request.getRemoteUser();
        }
        Optional<User>optionalUser=userRepository.findByEmail(userEmailOfSimplePasswordBasedSecurity).isPresent()==false
                ? userRepository.findByEmail(userEmailOfOAuth2) :
                userRepository.findByEmail(userEmailOfSimplePasswordBasedSecurity);
        Integer cartSize=0;
        if(optionalUser.isPresent())
        {
            cartSize=optionalUser.get().getCart().size();
            model.addAttribute("cartCount",cartSize);
        }
        model.addAttribute("product",productService.getProductById(id).get());
        return "viewProduct";
    }

}
