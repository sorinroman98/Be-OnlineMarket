package com.springapp.springjwt.utility;

import com.springapp.springjwt.domain.CreditCard;
import com.springapp.springjwt.domain.Order;
import com.springapp.springjwt.domain.Product;
import com.springapp.springjwt.exception.domain.*;
import com.springapp.springjwt.repository.OrderRepository;
import com.springapp.springjwt.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.springapp.springjwt.constant.CreditCardConstants.INVALID_CREDIT_CARD;
import static com.springapp.springjwt.constant.EmailConstant.INVALID_EMAIL_FORMAT;
import static com.springapp.springjwt.constant.OrderConstants.*;
import static com.springapp.springjwt.constant.ProductConstant.*;
import static com.springapp.springjwt.constant.UserImplConstant.INVALID_USERNAME;

@Service
@RequiredArgsConstructor
public class Validator {
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;

    public void validateCreditCard(CreditCard creditCard) throws InvalidCreditCardException, InvalidUsernameFormatException, ParseException {

        validateCreditCardNumberLength(creditCard.getCreditCardNumber());
        validateLuhnCreditCardAlgorithm(creditCard.getCreditCardNumber());
        isValidCIV(creditCard.getCiv());
        isValidName(creditCard.getCardHolderName());
        isValidDate(creditCard.getExpiryDate());

    }

    public void validateNewOrder(String username, List<String> productsId) throws ProductNotFoundException, InvalidOrderException {
        validateProductsIdAndStock(productsId);
        //validateShoppingCartProducts(productsId);
        validateUserOrder(username,productsId);
    }

    public void validateOrder(String orderUuid) throws InvalidOrderException, ProductOutOfStockException, OrderNotFoundException {
        Order order = orderRepository.findOrderById(orderUuid);
        if (order != null) {

            if (order.isPayed())
                throw new InvalidOrderException(ORDER_ALREADY_PAYED);

            for (Product product : order.getProductList()){
                if (product.getQuantity() < 1){
                    throw new ProductOutOfStockException(PRODUCT_OUT_OF_STOCK);
                }
            }
        } else {
            throw new OrderNotFoundException(ORDER_NOT_FOUND);
        }
    }

    public void validateNewUser(String username, String email) throws InvalidEmailFormatException, InvalidUsernameFormatException {
        isValidEmailAddress(email);
        isValidName(username);
    }

    private void validateCreditCardNumberLength(String str) throws InvalidCreditCardException {
        if (str == null) {
            throw new InvalidCreditCardException(INVALID_CREDIT_CARD);
        }
        if (str.matches("[0-9]+") && str.length() == 16)
            return;

        throw new InvalidCreditCardException(INVALID_CREDIT_CARD);


    }

    private void validateLuhnCreditCardAlgorithm(String str) throws InvalidCreditCardException {
        try {
            int[] ints = new int[str.length()];
            for (int i = 0; i < str.length(); i++) {
                ints[i] = Integer.parseInt(str.substring(i, i + 1));
            }
            for (int i = ints.length - 2; i >= 0; i = i - 2) {
                int j = ints[i];
                j = j * 2;
                if (j > 9) {
                    j = j % 10 + 1;
                }
                ints[i] = j;
            }
            int sum = 0;
            for (int anInt : ints) {
                sum += anInt;
            }
            if (sum % 10 != 0) {
                throw new InvalidCreditCardException(INVALID_CREDIT_CARD);
            }

        } catch (Exception e) {
           throw new InvalidCreditCardException(INVALID_CREDIT_CARD);
        }


    }

    private void isValidEmailAddress(String email) throws InvalidEmailFormatException {
        if (email == null) {
            throw new InvalidEmailFormatException(INVALID_EMAIL_FORMAT);
        }

        try {
            InternetAddress emailAddress = new InternetAddress(email);
            emailAddress.validate();
        } catch (AddressException ex) {
            throw new InvalidEmailFormatException(INVALID_EMAIL_FORMAT);
        }

    }

    private void isValidName(String name) throws InvalidUsernameFormatException {
        if (name == null) {
           throw new InvalidUsernameFormatException(INVALID_USERNAME);
        }

        if (name.matches("^[a-zA-Z0-9\\s]*$")) {
            return;
        }
        throw new InvalidUsernameFormatException(INVALID_USERNAME);

    }

    private void isValidCIV(String civ) throws InvalidCreditCardException {

        String regex = "^[0-9]{3,4}$";

        Pattern p = Pattern.compile(regex);

        if (civ == null) {
            throw new InvalidCreditCardException(INVALID_CREDIT_CARD);
        }

        Matcher m = p.matcher(civ);
        if (m.matches()) {
            return;
        }
        throw new InvalidCreditCardException(INVALID_CREDIT_CARD);

    }

    private void isValidDate(String expireDate) throws InvalidCreditCardException, ParseException {
        if (expireDate == null) {
            throw new InvalidCreditCardException(INVALID_CREDIT_CARD);

        }
        if (expireDate.matches("^\\d{2}/\\d{2}$")) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/yy");
            simpleDateFormat.setLenient(false);
            if (simpleDateFormat.parse(expireDate).before(new Date()))
                throw new InvalidCreditCardException(INVALID_CREDIT_CARD);
           return;
        }
        throw new InvalidCreditCardException(INVALID_CREDIT_CARD);

    }

    private void validateProductsIdAndStock(List<String> idProductsList) throws ProductNotFoundException {
        if (idProductsList == null) {
            throw new ProductNotFoundException(PRODUCT_NOT_FOUND);
        }
        for (String idProduct : idProductsList) {
            if (productRepository.findByUuid(idProduct) == null) {
                throw new ProductNotFoundException(PRODUCT_NOT_FOUND);
            }

            if (productRepository.findByUuid(idProduct).getQuantity() < 1) {
                throw new ProductNotFoundException(PRODUCT_OUT_OF_STOCK);
            }
        }
    }

//    private void validateShoppingCartProducts(List<String> idProductsList) throws ProductNotFoundException {
//        if (idProductsList == null) {
//            throw new ProductNotFoundException(PRODUCT_NOT_FOUND);
//        }
//
//
////Wrong
//        for (int i = 0; i < idProductsList.size(); i++) {
//            for (int j = i + 1; j < idProductsList.size(); j++) {
//                if (productRepository.findByUuid(idProductsList.get(i)).getCategory()
//                        .equals(productRepository.findByUuid(idProductsList.get(j)).getCategory())) {
//                    throw new ProductNotFoundException(INVALID_PRODUCT_LIST);
//                }
//            }
//        }
//
//    }


    //Validate if the user has bought a product from same category in other orders
    public void validateUserOrder(String userName, List<String> productsList) throws InvalidOrderException {
        if (userName == null || productsList == null || productsList.isEmpty()) {
         throw new InvalidOrderException("INVALID");
        }

//        List<Order> ordersDatabaseList = orderRepository.findAll();
//
//        for (Order order : ordersDatabaseList) {
//            if (userName.equals(order.getUserName())) {
//                IntStream.range(0, productsList.size()).filter(j -> IntStream.range(0, order.getProductList().size()).anyMatch(k -> productRepository.findByUuid(productsList.get(j)).getCategory()
//                        .equals(productRepository.findByUuid(order.getProductList().get(k).getUuid()).getCategory()))).forEach(j -> {
//                    try {
//                        throw new InvalidOrderException(INVALID_PRODUCTS_IN_ORDER);
//                    } catch (InvalidOrderException e) {
//                        e.printStackTrace();
//                    }
//
//                });
//            }
//        }

    }
}
