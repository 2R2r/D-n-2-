package com.example.demo.service.impl;

import com.example.demo.dto.KhachHangDTO;
import com.example.demo.dto.UserDTO;
import com.example.demo.entity.Orders;
import com.example.demo.entity.Role;
import com.example.demo.entity.State;
import com.example.demo.entity.User;
//import com.example.demo.exception.UserException;
import com.example.demo.exception.UserException;
import com.example.demo.repository.UsersRepository;
import com.example.demo.service.CommonService;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

@Service

public class UsersService implements CommonService<User>, UserService {
    private UsersRepository repository;
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JavaMailSender mailSender;

    public UsersService(UsersRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User login(String accountName, String password) {
        Optional<User> user0 = repository.findByAccountName(accountName);
        if (!user0.isPresent()) {
            throw new UserException("User is not found");
        }
        User user = user0.get();
        if (user.getState() != State.ACTIVE) {
            throw new UserException("User is not active");
        }
        if (passwordEncoder.matches(password, user0.get().getHashedPass())) {
            return user0.get();

        } else {
            throw new UserException("Password is incorrect");
        }
    }

    @Override
    public boolean logout(String username) {
        return false;
    }

    private String generateVerificationCode() {
        // tạo một mã xác thực ngẫu nhiên
        return UUID.randomUUID().toString().substring(0, 6);
    }

    private void sendEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        mailSender.send(message);
    }

    @Override
    public  User register(String accountName,String firstName,String lastName, String email ,String password) {
        Optional<User> user0 = repository.findByAccountName(accountName);
        if (user0.isPresent()) {
            throw new UserException("Username already exists");
        }
        Optional<User> user1 = repository.findByEmail(email);
        if (user1.isPresent()) {
            throw new UserException("Email already exists");
        }
        // tạo mã xác thực ngẫu nhiên
        String verificationCode = generateVerificationCode();
        // lưu mã xác thực vào cơ sở dữ liệu
        User user = new User();
        user.setAccountName(accountName);
        user.setLastName(lastName);
        user.setFirstName(firstName);
        user.setEmail(email);
        user.setHashedPass(password);
        user.setVerificationCode(verificationCode);
        user.setState(State.PENDING);
        addUser(user);
        // gửi email xác thực
        String subject = "Xác thực tài khoản của bạn";
        String text = "Cảm ơn bạn đã đăng ký trên trang web của chúng tôi. Để hoàn tất đăng ký, vui lòng nhập mã xác thực sau đây: " + verificationCode;
        sendEmail(user.getEmail(), subject, text);
        return user;
    }

    @Override
    public void verifyUser(String accountName, String verificationCode) {
        Optional<User> user = repository.findByAccountName(accountName);
        if (user.isPresent() && user.get().getVerificationCode().equals(verificationCode)) {
            user.get().setState(State.ACTIVE);
            repository.save(user.get());
        } else {
            throw new UserException("Verification code is invalid");
        }
    }

    @Override
    public void addUser(User user) {
        String encodedPassword = passwordEncoder.encode(user.getHashedPass());
        user.setHashedPass(encodedPassword);
        if(repository.findAll().size() < 1) {
            user.setRole(Role.SuperAdmin);
        }
        repository.save(user);
    }

    @Override
    public void addUserThenAutoActive(User user) {
        String encodedPassword = passwordEncoder.encode(user.getHashedPass());
        user.setHashedPass(encodedPassword);
        user.setState(State.ACTIVE);
        repository.save(user);
    }

    @Override
    public boolean activateUser(String actiation_code) {
        return false;
    }

    @Override
    public boolean updatePassword(String username, String password) {
        return false;
    }

    @Override
    public boolean updateEmail(String email, String newEmail) {
        return false;
    }


    @Override
    public List<User> getAll() {
        return repository.findAll();
    }

    @Override
    public void save(User value) {
        repository.save(value);
    }

    @Override
    public void deleteById(int id) {
        repository.deleteById(id);
    }

    @Override
    public Optional<User> findById(int id) {
        return repository.findById(id);
    }

    public User userBuyProducts(UserDTO userDTO , KhachHangDTO khachHang){
        User user = new User();
        if (userDTO != null) {
            Optional<User> userOptional = findById(userDTO.getId());
            user = userOptional.get();
        } else {

            user.setFirstName(khachHang.getFirstName());
            user.setLastName(khachHang.getLastName());
            user.setRole(Role.Customer);
            user.setState(State.PENDING);
            repository.save(user);
        }
        return user;
    }

}
