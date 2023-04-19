package com.example.demo.controller;

import com.example.demo.dto.ProductDTO;
import com.example.demo.dto.KhachHangDTO;
import com.example.demo.dto.UserDTO;
import com.example.demo.entity.OrderProduct;
import com.example.demo.entity.Orders;
import com.example.demo.entity.Product;
import com.example.demo.entity.Review;
import com.example.demo.entity.User;
import com.example.demo.exception.UserException;
import com.example.demo.service.impl.CategoryService;
import com.example.demo.service.impl.OrderProductService;
import com.example.demo.service.impl.OrdersService;
import com.example.demo.service.impl.ProductService;
import com.example.demo.service.impl.ReviewService;
import com.example.demo.service.impl.UsersService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Controller
public class HomeController {

    @Autowired
    private ProductService productService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private OrdersService ordersService;
    @Autowired
    private ReviewService reviewService;
    @Autowired
    private UsersService usersService;
    @Autowired
    private OrderProductService orderProductService;

    @GetMapping({"/", "/home"})
    public String home(Model model, HttpSession session) {
        UserDTO userDTO = (UserDTO) session.getAttribute("user");
        if (userDTO != null) {
            model.addAttribute("user", userDTO);
        }
        model.addAttribute("listCategory", categoryService.getAll());
        model.addAttribute("listProduct", productService.getAll());
        return "Shop/homeShop";
    }

    @GetMapping("/home/category/{id}")
    public String getProductByCategoryId(Model model, @PathVariable int id) {
        model.addAttribute("listCategory", categoryService.getAll());
        model.addAttribute("listProduct", productService.findAllProductById(id));
        return "Shop/homeShop";
    }

    @GetMapping("/home/sort-price")
    public String fillProduct(Model model, @RequestParam("selectPrice") String selectPrice) {
        if (selectPrice.equals("high")) {
            model.addAttribute("listProduct", productService.findAllByOrderByPriceDesc());

        } else if (selectPrice.equals("low")) {
            model.addAttribute("listProduct", productService.findAllByOrderByPriceAsc());

        } else {
            model.addAttribute("listProduct", productService.getAll());
        }
        model.addAttribute("listCategory", categoryService.getAll());
        return "Shop/homeShop";

    }

    @GetMapping("/home/search")
    public String search(Model model, @RequestParam("search") String search) {
        model.addAttribute("listProduct", productService.findAllByNameContaining(search));
        model.addAttribute("listCategory", categoryService.getAll());
        return "Shop/homeShop";
    }

    Product flagProduct;

    @GetMapping("/home/product-detail/{id}")
    public String productDetail(Model model, @PathVariable int id, HttpSession session) {
        Optional<Product> product = productService.findById(id);
        UserDTO userDTO = (UserDTO) session.getAttribute("user");
        if (userDTO != null) {
            model.addAttribute("user", userDTO);
        }
        if (product.isPresent()) {
            model.addAttribute("review", new Review());
            model.addAttribute("product", product.get());
            flagProduct = product.get();
            model.addAttribute("listReview", reviewService.findByProductId(id));
            int categoryId = product.get().getCategory().getId();
            model.addAttribute("listProduct", productService.findTop4ByCategoryId(categoryId));
            return "Shop/productDetail";
        }
        return "redirect:/home";
    }

    @PostMapping("/home/product-detail/add-review")
    public String productDetailReview(@ModelAttribute("review") Review review, HttpSession session, Model model) {
        UserDTO userDTO = (UserDTO) session.getAttribute("user");
        if (userDTO != null) {
            Optional<User> user = usersService.findById(userDTO.getId());
            if (user.isPresent()) {
                review.setUser(user.get());
                review.setProduct(flagProduct);
                model.addAttribute("user", user.get());
            }
        } else {
            throw new UserException("Đăng nhập mới bình luận được");
        }
        reviewService.save(review);
        return "redirect:/home/product-detail/" + flagProduct.getId();

    }

    //cart
    @GetMapping("/home/add-cart/{id}")
    public String addProductToCart(Model model, @PathVariable("id") int id, HttpSession session) {
        ProductDTO cartItem = productService.getProductDTO(id);
        if (cartItem != null) {
            Map<Integer, ProductDTO> cart = (Map<Integer, ProductDTO>) session.getAttribute("cart");
            if (cart == null) {
                cart = new HashMap<>();
                session.setAttribute("cart", cart);
            }
            ProductDTO existingCartItem = cart.get(id);
            if (existingCartItem != null) {
                existingCartItem.setQuantity(existingCartItem.getQuantity() + 1);
                existingCartItem.setTotalPrice(existingCartItem.getTotalPrice().add(existingCartItem.getTotalPrice().divide(BigDecimal.valueOf(Long.valueOf((existingCartItem.getQuantity() - 1))))));
            } else {
                cart.put(id, cartItem);
            }
        }
        return "redirect:/home/product-detail/" + flagProduct.getId();
    }

    @GetMapping("/home/cart")
    public String cartProduct(Model model, HttpSession session) {
        UserDTO userDTO = (UserDTO) session.getAttribute("user");
        if (userDTO != null) {
            model.addAttribute("user", userDTO);
        }
        Map<Integer, ProductDTO> cart = (Map<Integer, ProductDTO>) session.getAttribute("cart");
        if (cart == null) {
            cart = new HashMap<>();
        }
        model.addAttribute("cartItems", cart.values());
        model.addAttribute("totalPrice", productService.getTotalAllPrice(cart.values()));
        return "Shop/cart";
    }

    @GetMapping("/home/product-detail/cart/delete/{id}")
    public String cartProductDelete(Model model, HttpSession session, @PathVariable("id") int id) {
        Map<Integer, ProductDTO> cart = (Map<Integer, ProductDTO>) session.getAttribute("cart");
        if (cart != null && cart.containsKey(id)) {
            cart.remove(id);
            session.setAttribute("cart", cart);
        }
        return "redirect:/home/product-detail/cart";
    }

    @GetMapping("/home/check-out")
    public String showCheckOut(Model model, HttpSession session) {
        UserDTO userDTO = (UserDTO) session.getAttribute("user");
        if (userDTO != null) {
            model.addAttribute("user", userDTO);
        }
        Map<Integer, ProductDTO> cart = (Map<Integer, ProductDTO>) session.getAttribute("cart");
        if (cart != null) {
            model.addAttribute("totalPrice", productService.getTotalAllPrice(cart.values()));
        }
        model.addAttribute("khachHang", new KhachHangDTO());
        return "Shop/checkout";

    }

    @PostMapping("/home/check-out")
    public String handleCheckOut(@Valid Model model, HttpSession session, @ModelAttribute("khachHang") KhachHangDTO khachHang, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "Shop/checkout";
        }

        UserDTO userDTO = (UserDTO) session.getAttribute("user");
        Map<Integer, ProductDTO> cart = (Map<Integer, ProductDTO>) session.getAttribute("cart");
        if (cart != null) {
            BigDecimal total = new BigDecimal("0");
            for (ProductDTO product : cart.values()) {
                total = total.add(product.getTotalPrice());
            }
            Set<ProductDTO> set = new HashSet<>(cart.values());
            Set<Product> products = ordersService.convertToProduct(set);
            Orders order = ordersService.createOrder(usersService.userBuyProducts(userDTO, khachHang), set);
            for (Product product : products) {
                OrderProduct orderProduct = new OrderProduct(); // tạo mới đối tượng OrderProduct
                orderProduct.setProduct(product);
                orderProduct.setOrders(order);
                orderProduct.setQuantity(product.getQuantity());
                Optional<Product> optionalProduct = productService.findById(product.getId());
                if (optionalProduct.isPresent()){
                    optionalProduct.get().setQuantity(optionalProduct.get().getQuantity() - product.getQuantity());
                    productService.save(optionalProduct.get());
                }
                orderProductService.save(orderProduct); // lưu trữ đối tượng OrderProduct mới
            }
        }
        cart.clear();
        session.removeAttribute("cart");
        return "redirect:/home";
    }
}
