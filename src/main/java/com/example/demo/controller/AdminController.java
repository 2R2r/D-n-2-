package com.example.demo.controller;

import com.example.demo.dto.UserDTO;
import com.example.demo.entity.Category;
import com.example.demo.entity.OrderProduct;
import com.example.demo.entity.Orders;
import com.example.demo.entity.Product;
import com.example.demo.entity.Status;
import com.example.demo.entity.User;
import com.example.demo.service.impl.CategoryService;
import com.example.demo.service.impl.OrderProductService;
import com.example.demo.service.impl.OrdersService;
import com.example.demo.service.impl.ProductService;
import com.example.demo.service.impl.ReviewService;
import com.example.demo.service.impl.UsersService;
import jakarta.servlet.http.HttpServletRequest;
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
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Controller
public class AdminController {


    public static String uploadDir = System.getProperty("user.dir") + "/src/main/resources/static/productImages";

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

    @GetMapping("/admin")
    public String adminHome(Model model, HttpSession session) {
        UserDTO userDTO = (UserDTO) session.getAttribute("user");
        if (userDTO != null) {
            model.addAttribute("user", userDTO);
        }
        return "adminHome";
    }

//Category
    @GetMapping("/admin/categories")
    public String listCategory(Model model) {
        model.addAttribute("listCategory", categoryService.getAll());
        return "Manage/Category/listCategory";
    }

    @GetMapping("/admin/categories/add")
    public String viewAddCategory(Model model) {
        model.addAttribute("category", new Category());
        return "Manage/Category/viewAdd";
    }

    @PostMapping("/admin/categories/add")
    public String addCategory(@ModelAttribute("category") Category category) {
        categoryService.save(category);
        return "redirect:/admin/categories";
    }

    @GetMapping("/admin/categories/delete/{id}")
    public String deleteCategory(@PathVariable int id) {
        categoryService.deleteById(id);
        return "redirect:/admin/categories";
    }

    @GetMapping("/admin/categories/update/{id}")
    public String updateCategory(@PathVariable int id, Model model) {
        Optional<Category> category = categoryService.findById(id);
        if (category.isPresent()) {
            model.addAttribute("category", category.get());
            return "Manage/Category/viewAdd";
        }
        return "redirect:/admin/categories";
    }
    //Product

    @GetMapping("/admin/products")
    public String listProduct(Model model) {
        model.addAttribute("listProduct", productService.getAll());
        return "Manage/Product/listProduct";
    }

    @GetMapping("/admin/products/add")
    public String viewAddProduct(Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("listCategory", categoryService.getAll());
        return "Manage/Product/viewAdd";
    }

    @PostMapping("/admin/products/add")
    public String addProduct(@ModelAttribute("product") @Valid Product product, BindingResult result,
                             @RequestParam("imageFile") MultipartFile imageFile, HttpSession session, HttpServletRequest request,
                             @RequestParam("imagName") String imagName) throws IOException {
        // Check for validation errors
        if (result.hasErrors()) {
            return "Manage/Product/viewAdd";
        }
        String imageUUID = "";
        if (!imageFile.isEmpty()) {
            imageUUID = imageFile.getOriginalFilename();
            Path fileNamAndPath = Paths.get(uploadDir, imageUUID);
            Files.write(fileNamAndPath, imageFile.getBytes());
        } else {
            imageUUID = imagName;
        }
        product.setImage(imageUUID);
        // Set other properties
        UserDTO userDTO = (UserDTO) session.getAttribute("user");
        product.setCreated_by(usersService.findById(userDTO.getId()).get());
        product.setCreated_at(LocalDate.now());
        // Save product to database
        productService.save(product);
        return "redirect:/admin/products";
    }

    @GetMapping("/admin/products/delete/{id}")
    public String deleteProduct(@PathVariable int id) {
        productService.deleteById(id);
        return "redirect:/admin/products";
    }

    @GetMapping("/admin/products/update/{id}")
    public String viewUpdateProduct(@PathVariable int id, Model model) {
        Product product = productService.findById(id).get();
        model.addAttribute("listCategory", categoryService.getAll());
        model.addAttribute("product", product);
        return "Manage/Product/viewUpdate";
    }

//User

    @GetMapping("/admin/users")
    public String listUser(Model model) {
        model.addAttribute("listUser", usersService.getAll());
        return "Manage/User/listUser";
    }

    @PostMapping("/admin/users/add")
    public String addUser(@ModelAttribute("user") User user) {
        usersService.save(user);
        return "redirect:/admin/users";
    }

    @GetMapping("/admin/users/delete/{id}")
    public String deleteUser(@PathVariable int id) {
        usersService.deleteById(id);
        return "redirect:/admin/users";
    }

    @GetMapping("/admin/users/update/{id}")
    public String updateUser(@PathVariable int id, Model model) {
        Optional<User> user = usersService.findById(id);
        if (user.isPresent()) {
            model.addAttribute("user", user.get());
            return "Manage/User/viewUpdate";
        }
        return "redirect:/admin/users";
    }

    @GetMapping("/admin/users/detail/{id}")
    public String viewDetailCustomer(@PathVariable int id, Model model) {
        Optional<User> userOptional = usersService.findById(id);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            model.addAttribute("user", user);
            return "Manage/User/viewDetail";
        } else {
            return "redirect:/admin/users";

        }
    }

    //Order

    @GetMapping("/admin/orders")
    public String listOrder(Model model) {
        model.addAttribute("listOrder", ordersService.getAll());
        return "Manage/Orders/listOrder";
    }


    @GetMapping("/admin/orders/detail/{id}")
    public String viewDetail(@PathVariable int id, Model model) {
        List<OrderProduct> listOrder = orderProductService.findByOrders_Id(id);
        Optional<Orders> orders = ordersService.findById(id);
        if (orders.isPresent()) {
            model.addAttribute("order", orders.get());
        }
        model.addAttribute("listOrderProduct", listOrder);
        model.addAttribute("total", orderProductService.totalAllProductInOrder(listOrder));
        return "Manage/Orders/viewDetail";
    }

    //
    @PostMapping("/admin/orders/update/{id}")
    public String updateOrderStatus(@RequestParam("status") String status, @PathVariable("id") int id) {
        Optional<Orders> optionalOrders = ordersService.findById(id);
        if (optionalOrders.isPresent()) {
            Orders orders = optionalOrders.get();
            if (status.equals("accept")) {
                orders.setStatus(Status.Accept);
                ordersService.save(orders);
            } else {
                orders.setStatus(Status.Cancel);
                ordersService.save(orders);
            }
        }
        return "redirect:/admin/orders";
    }

    @GetMapping("/admin/orders/search")
    public String searchOrder(@RequestParam(name = "search", required = false) String search,
                              @RequestParam(name = "statusSelect", required = false) String statusSelect,
                              Model model) {
        model.addAttribute("listOrder", ordersService.findByUserNameContaining(search));
        return "Manage/Orders/listOrder";
    }

    @GetMapping("/admin/orders/sort")
    public String sortOrderProduct(@RequestParam("statusSelect") String statusSelect, Model model) {
        if (statusSelect.equals("all")) {
            return "redirect:/admin/orders";
        }
        Status statusEnum = Status.valueOf(statusSelect);
        model.addAttribute("listOrder", ordersService.findByOrderByStatus(statusEnum));

        return "Manage/Orders/listOrder";
    }
//
//    @GetMapping("/admin/orders/delete/{id}")
//    public String deleteOrder(@PathVariable int id) {
//        ordersService.deleteById(id);
//        return "redirect:/admin/orders";
//    }


    //Customer

//    @GetMapping("/admin/customers")
//    public String listCustomer(Model model) {
//        model.addAttribute( "listCustomer", customerService.getAll());
//        return "Manage/Customer/listCustomer";
//    }
//
//
//    @GetMapping("/admin/customers/detail/{id}")
//    public String viewDetailCustomer(@PathVariable int id, Model model) {
//        Optional<Customer> optionalCustomer = customerService.findById(id);
//        if (optionalCustomer.isPresent()) {
//            Customer customer = optionalCustomer.get();
//            model.addAttribute("customer", customer);
//            model.addAttribute("listOrder", customerService.findByOrderByCustomerId(id));
//            return "Manage/Customer/viewDetail";
//        } else {
//            return "redirect:/admin/customers";
//
//        }
//    }
//
//    @GetMapping("/admin/customers/delete/{id}")
//    public String deleteCustomer(@PathVariable int id) {
//        customerService.deleteById(id);
//        return "redirect:/admin/customers";
//    }

}
