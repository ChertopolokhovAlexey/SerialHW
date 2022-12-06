package org.example;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class Basket implements Serializable{
    protected String[] products;
    protected int[] amount;
    protected int[] price;
    protected int total;
    Map<Integer, Integer> list = new HashMap<>();

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

    static Basket loadFromBinFile(File file) {
        Basket basket = null;
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))){
            basket =  (Basket) ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return basket;

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

    public void saveBin(File file) {
        int[] amountList = new int[products.length];
        for (int i = 0; i < products.length; i++) {
            amountList[i] = list.get(i) == (null)? 0: list.get(i);
        }
        Basket basket = new Basket(products, price, amountList);
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
            oos.writeObject(basket);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

