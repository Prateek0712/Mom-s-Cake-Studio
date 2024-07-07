package com.pratik.major.paymentPartner;

import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import com.pratik.major.model.Order;
import com.pratik.major.model.User;
import com.pratik.major.repository.OrderRepository;
import com.pratik.major.repository.ProductRepository;
import com.pratik.major.repository.UserRepository;
import com.pratik.major.dto.BillingDetails;
import com.pratik.major.dto.OrderDetails;
import com.pratik.major.dto.ShippingDetails;
import com.pratik.major.model.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class PaypalController {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private PaypalService paypalService;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrderRepository orderRepository;

    private OrderDetails od=null;
    @PostMapping("/paynow")
    private RedirectView /*String*/ paynow(@ModelAttribute("OrderDetails") OrderDetails orderDetails, HttpServletRequest request, Model model) throws PayPalRESTException {
        orderDetails.getBillingDetails().setCurrency("USD");
        orderDetails.getBillingDetails().setMethod("Paypal");
        orderDetails.getBillingDetails().setDescription("Demo");
        orderDetails.getShippingDetails().setCountry("INDIA");
        BillingDetails billingDetails=orderDetails.getBillingDetails();
        ShippingDetails shippingDetails=orderDetails.getShippingDetails();
        od=orderDetails;
        try {
            String cancelUrl = "http://localhost:8080/cancel";
            String successUrl = "http://localhost:8080/success";
            Payment payment = paypalService.createPayment(
                    Double.valueOf(billingDetails.getAmount()),
                    billingDetails.getCurrency(),
                    billingDetails.getMethod(),
                    "sale",
                    billingDetails.getDescription(),
                    cancelUrl,
                    successUrl
            );

            for (Links links: payment.getLinks()) {
                if (links.getRel().equals("approval_url")) {
                    return new RedirectView(links.getHref());
                }
            }
        } catch (PayPalRESTException e) {
            System.out.println(e.getMessage());
            return new RedirectView("/failure");
        }
        return new RedirectView("/cancel");
        /*try{
            userEmail=request.getRemoteUser();
        }
        catch (Exception e)
        {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.isAuthenticated()) {
                OAuth2AuthenticationToken token=(OAuth2AuthenticationToken) authentication; //this will send by google
                userEmail=token.getPrincipal().getAttributes().get("email").toString();
            }
            else return  "failure";
        }*/
    }

    private String getLoggedInUser(HttpServletRequest request)
    {
        String userEmail=null;
        try{
            userEmail=request.getRemoteUser();
        }
        catch (Exception e)
        {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.isAuthenticated()) {
                OAuth2AuthenticationToken token=(OAuth2AuthenticationToken) authentication; //this will send by google
                userEmail=token.getPrincipal().getAttributes().get("email").toString();
            }
            return userEmail;
        }
        return userEmail;
    }
    @GetMapping("/success")
    public String paymentSuccess(
            @RequestParam("paymentId") String paymentId,
            @RequestParam("PayerID") String payerId,HttpServletRequest request, Model model
    ){
        String userEmail=null;
        try{
            userEmail=request.getRemoteUser();
            User user=userRepository.findByEmail(userEmail).get();
            System.out.println("i passed");
        }
        catch (Exception e)
        {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.isAuthenticated()) {
                OAuth2AuthenticationToken token=(OAuth2AuthenticationToken) authentication; //this will send by google
               // Map<String, Object> attributes = token.getPrincipal().getAttributes();
                userEmail=token.getPrincipal().getAttributes().get("email").toString();
                System.out.println(userEmail);
            }
        }
        /*System.err.println(userEmail.substring(100));*/
        Order order=new Order();
        try {
            Payment payment = paypalService.executePayment(paymentId, payerId);
            if (payment.getState().equals("approved")) {

                /*----------------------------CREATING ORDER OBJECT--------------------------------*/
                Optional<User>optionalUser=userRepository.findByEmail(userEmail);
                User user=null;
                if(optionalUser.isPresent())
                {
                    user=userRepository.findByEmail(userEmail).get();
                }
                else{
                    System.err.println("User not found");
                    user=userRepository.findByEmail("prateek7120@gmail.com").get();
                }
                //user=userRepository.findByEmail("prateek7120@gmail.com").get();

                List<Product>productList=user.getCart();
                List<Product>products=new ArrayList<>(productList); //this copy is for model view
                order.setAddress(od.getShippingDetails().getApartment()+" "+od.getShippingDetails().getHouseNumberAndStreetName()+" "+
                        od.getShippingDetails().getCity()+" "+od.getShippingDetails().getPostalCode()+" "+od.getShippingDetails().getCountry());
                order.setTotal(Double.valueOf(od.getBillingDetails().getAmount()));
                order.setProducts(new ArrayList<>(productList));
                order.setUser(user);
                List<Order>orderList=user.getOrderList();
                orderList.add(order);
                user.setOrderList(orderList);
                for(Product p:productList){
                    List<Order>orderList1=p.getOrders();
                    orderList1.add(order);
                    p.setOrders(orderList1);
                    productRepository.save(p);
                }
                List<Product>nc=new ArrayList<>();
                user.setCart(nc);
                order=orderRepository.save(order);
                userRepository.save(user);
                /*--------------------DONE WITH CREATING ORDER OBJECT---------------------------*/
                DecimalFormat df = new DecimalFormat("0.00");
                model.addAttribute("orderDetails",od);
                model.addAttribute("products",products);
                model.addAttribute("cartCount",0);
                model.addAttribute("order",order);
                model.addAttribute("df",df);
                return "success";
            }
        } catch (PayPalRESTException e) {
            System.out.println(e.getMessage());
            return "redirect:/cancel";
        }
        return "redirect:/failure";
    }

    @GetMapping("/cancel")
    public String paymentCancel() {
        return "cancel";
    }

    @GetMapping("/error")
    public String paymentError() {
        return "failure";
    }


















/*
model.addAttribute("orderDetails",orderDetails);
        model.addAttribute("products",products);
        model.addAttribute("cartCount",cartSize);
* */
    /* String userEmail=null;
        List<Product>products=null;
        Integer cartSize=0;
        try{
            userEmail=request.getRemoteUser();
            User user=userRepository.findByEmail(userEmail).get();
            cartSize=user.getCart().size();
            products=user.getCart();
        }
        catch (Exception e)
        {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.isAuthenticated()) {
                OAuth2AuthenticationToken token=(OAuth2AuthenticationToken) authentication; //this will send by google

                userEmail=token.getPrincipal().getAttributes().get("email").toString();
                // Perform actions based on the principal
                User user=userRepository.findByEmail(userEmail).get();
                cartSize=user.getCart().size();
                products=user.getCart();
            }
            else
            {
                return  "404";
            }
        }*/
}
