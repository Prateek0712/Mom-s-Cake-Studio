package com.pratik.major.controller;

import com.pratik.major.dto.OrderDetails;
import com.pratik.major.model.Product;
import com.pratik.major.model.User;
import com.pratik.major.repository.ProductRepository;
import com.pratik.major.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.text.DecimalFormat;
import java.util.List;

@Controller
public class CartController {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/cart")
    public String viewCart(Model model, HttpServletRequest request)
    {
        String userEmail=null;
        try{
            userEmail=request.getRemoteUser();
            //System.out.println(userEmail);
            User user=userRepository.findByEmail(userEmail).get();
            Integer cartSize=user.getCart().size();
            Double totalAmt= 0.0;
            for(Product p:user.getCart())
            {
                totalAmt+=p.getPrice();
            }
            model.addAttribute("cartCount",cartSize);
            model.addAttribute("total",totalAmt);
            model.addAttribute("cart",user.getCart());
        }
        catch (Exception e)
        {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.isAuthenticated()) {
                OAuth2AuthenticationToken token=(OAuth2AuthenticationToken) authentication; //this will send by google

                userEmail=token.getPrincipal().getAttributes().get("email").toString();
                // Perform actions based on the principal
                User user=userRepository.findByEmail(userEmail).get();
                Integer cartSize=user.getCart().size();
                Double totalAmt= 0.0;
                for(Product p:user.getCart())
                {
                    totalAmt+=p.getPrice();
                }
                model.addAttribute("cartCount",cartSize);
                model.addAttribute("total",totalAmt);
                model.addAttribute("cart",user.getCart());
            }
            else
            {
                return  "404";
            }
        }
        return "cart";
    }

    @GetMapping("/addToCart/{id}")
    public String addToCart(@PathVariable Long id, HttpServletRequest request)
    {
        String userEmail=null;
        try{
            userEmail=request.getRemoteUser();
            if(userEmail==null) return "index";
            System.out.println(userEmail);
            Product product=productRepository.findById(id).get();
            User user =userRepository.findByEmail(userEmail).get();
            List<Product> cartList=user.getCart();
            cartList.add(product);
            System.out.println(cartList.size());
            user.setCart(cartList);
            product.setUser(user);
            userRepository.save(user);
        }
        catch (Exception e)
        {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.isAuthenticated()) {
                OAuth2AuthenticationToken token = (OAuth2AuthenticationToken) authentication; //this will send by google
                userEmail = token.getPrincipal().getAttributes().get("email").toString();
                if(userEmail==null) return "index";
                System.out.println(userEmail);
                Product product=productRepository.findById(id).get();
                User user =userRepository.findByEmail(userEmail).get();
                List<Product> cartList=user.getCart();
                cartList.add(product);
                System.out.println(cartList.size());
                user.setCart(cartList);
                product.setUser(user);
                userRepository.save(user);
            }
        }
        return "redirect:/shop";
    }

    @GetMapping("/cart/removeItem/{id}")
    public String removeFromCart(@PathVariable Long id, HttpServletRequest request)
    {
        String userEmail=null;
        try{
            userEmail=request.getRemoteUser();
            User user =userRepository.findByEmail(userEmail).get();
            List<Product> cartList=user.getCart();
            Product productToRemove = productRepository.findById(id).get();
            cartList.remove(productToRemove);
            user.setCart(cartList);
            productToRemove.setUser(null);
            userRepository.save(user);
        }
        catch (Exception e)
        {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.isAuthenticated()) {
                OAuth2AuthenticationToken token = (OAuth2AuthenticationToken) authentication; //this will send by google

                userEmail = token.getPrincipal().getAttributes().get("email").toString();
                User user =userRepository.findByEmail(userEmail).get();
                List<Product> cartList=user.getCart();
                Product productToRemove = productRepository.findById(id).get();
                cartList.remove(productToRemove);
                user.setCart(cartList);
                productToRemove.setUser(null);
                userRepository.save(user);
            }
            else {
                return "404";
            }
        }
        return "redirect:/cart";
    }

    @PostMapping("/checkout")
    public String  getCheckout(@RequestParam Double total,Model model,HttpServletRequest request)
    {
        String userEmail=null;
        try{
            userEmail=request.getRemoteUser();
            User user=userRepository.findByEmail(userEmail).get();
            Integer cartSize=user.getCart().size();
            OrderDetails orderDetails=new OrderDetails();
            DecimalFormat df = new DecimalFormat("0.00");
            model.addAttribute("df", df);
            model.addAttribute("OrderDetails",orderDetails);
            model.addAttribute("cartCount",cartSize);
            model.addAttribute("total",total);
        }
        catch (Exception e)
        {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.isAuthenticated()) {
                OAuth2AuthenticationToken token=(OAuth2AuthenticationToken) authentication; //this will send by google

                userEmail=token.getPrincipal().getAttributes().get("email").toString();
                // Perform actions based on the principal
                User user=userRepository.findByEmail(userEmail).get();
                Integer cartSize=user.getCart().size();
                OrderDetails orderDetails=new OrderDetails();
                DecimalFormat df = new DecimalFormat("0.00");
                model.addAttribute("df", df);
                model.addAttribute("OrderDetails",orderDetails);
                model.addAttribute("cartCount",cartSize);
                model.addAttribute("total",total);
            }
            else
            {
                return  "404";
            }
        }
        return "checkout";
    }
}
