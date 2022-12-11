package org.example;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.HashMap;
import java.util.Map;


public class Basket {
    protected int[] amount;
    protected String[] products;
    protected int[] price;
    protected int total;
    Map<Integer, Integer> list = new HashMap<>();
//    Gson json = new Gson();


    public Basket(String[] products, int[] price) {
        this.products = products;
        this.price = price;
    }

    public Basket(String[] products, int[] price, int[] amount) {
        this.products = products;
        this.price = price;
        this.amount = amount;
        for (int i = 0; i < products.length; i++) {
            addToCart(i, amount[i]);
        }
    }

    // TODO: 09.12.2022 load from json
    static Basket loadFromTxtFile(File textFile) {
        JSONParser parser = new JSONParser();
        try {
            Object obj = parser.parse(new FileReader(textFile));
            JSONObject productsBasket = (JSONObject) obj;
            JSONArray productJson = (JSONArray) productsBasket.get("product");
            JSONArray priceJson = (JSONArray) productsBasket.get("price");
            JSONArray amountJson = (JSONArray) productsBasket.get("amount");
            String[] productList = toArr(productJson.toString());
            String[] prices = toArr(priceJson.toString());
            String[] amounts = toArr(amountJson.toString());
            int[] priceList = new int[productList.length];
            int[] amountList = new int[productList.length];
            for (int i = 0; i < productList.length; i++) {
                priceList[i] = Integer.parseInt(prices[i]);
                amountList[i] = Integer.parseInt(amounts[i]);
            }
            return new Basket(productList, priceList, amountList);
        } catch (IOException | ParseException e) {
            throw new RuntimeException();
        }
    }

    protected static String[] toArr(String inputString) {
        String replace = inputString
                .replace("[", "")
                .replace(",", ",\t")
                .replace("]", "")
                .replace("\"", "");
        return replace.split(",\t");
    }


    public void addToCart(int productNum, int amount) {
        productNum = getProductNum(productNum);
        amount = getAmount(amount);
        if (productNum == -1) {
            System.out.println("Неправильный ввод позиции товара!");
            productNum = 0;
            amount = 0;
        }
        if (list.containsKey(productNum)) {
            list.put(productNum, list.get(productNum) + amount);
        }
        if (!list.containsKey(productNum)) {
            list.put(productNum, amount);
        }
    }

    int getProductNum(int productNum) {
        return (productNum < 0 || productNum > products.length - 1) ? -1 : productNum;
    }

    int getAmount(int amount) {
        if (amount < 0) {
            System.out.println("Количество товара не  может быть отрицательным!");
        }
        return Math.max(amount, 0);
    }

    public void printCart() {
        System.out.println("Ваша корзина:");
        for (Integer i : list.keySet()) {
            if (list.get(i) != 0) {
                System.out.println(products[i] + ": " + list.get(i) + " шт на сумму: " + (list.get(i) * price[i]) + " руб");
                this.total = total + (list.get(i) * price[i]);
            }
        }
        System.out.println("Итого: " + this.total);
    }

    public void printBasket() {
        System.out.println("В вашей корзине уже находится: ");
        for (int i = 0; i < products.length; i++) {
            if (amount[i] != 0) {
                System.out.println(products[i] + ": " + amount[i] + " шт.");
            }
        }
        System.out.println("Желаете добавить что-то из списка:");
    }

    // TODO: 09.12.2022 write to json
    public void saveJson(File textFile) {
        JSONObject productsBasket = new JSONObject();
        JSONArray productsArray = new JSONArray();
        JSONArray pricesArray = new JSONArray();
        JSONArray amountsArray = new JSONArray();
        for (int i = 0; i < products.length; i++) {
            productsArray.add(products[i]);
            pricesArray.add(price[i]);
            if (list.get(i) == null) {
                amountsArray.add(0);
            } else {
                amountsArray.add(list.get(i));
            }
        }
        productsBasket.put("product", productsArray);
        productsBasket.put("price", pricesArray);
        productsBasket.put("amount", amountsArray);

        try (PrintWriter printList = new PrintWriter(textFile)) {
            printList.write(productsBasket.toJSONString());
        } catch (IOException e) {
            System.out.println("Error: " + e);
        }
    }
}

