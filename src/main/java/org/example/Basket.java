package org.example;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;


public class Basket implements Serializable {
    protected int[] amount;
    protected String[] products;
    protected int[] price;
    protected int total;
    Map<Integer, Integer> list = new HashMap<>();
    transient Gson gson = new Gson();


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
    static Basket loadFromTxtFile(File loadFile) {
        try (BufferedReader br = new BufferedReader(new FileReader(loadFile))) {
            String[] productList = br.readLine().split("@");
            String[] priceFromFile = br.readLine().split("@");
            String[] amountFromFile = br.readLine().split("@");
            int[] priceList = new int[productList.length];
            int[] amountList = new int[productList.length];
            for (int i = 0; i < productList.length; i++) {
                priceList[i] = Integer.parseInt(priceFromFile[i]);
                amountList[i] = Integer.parseInt(amountFromFile[i]);
            }
            return new Basket(productList, priceList, amountList);
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    // TODO: 09.12.2022 load from json
    static Basket loadFromJSONFile(File loadFile) {
        Gson gson = new Gson();
        String json = null;
        try (Scanner scanner = new Scanner(new FileInputStream(loadFile))){
            json = scanner.nextLine();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return  gson.fromJson(json, Basket.class);
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

    public void saveTxt(File saveFile) {
        try (PrintWriter printList = new PrintWriter(saveFile)) {
            for (String product : products) {
                printList.print(product + "@");
            }
            printList.print("\n");
            for (int i : price) {
                printList.print(i + "@");
            }
            printList.print("\n");
            for (int i = 0; i < products.length; i++) {
                printList.print(list.get(i) == null ? 0 + "@" : list.get(i) + "@");
            }
        } catch (IOException e) {
            System.out.println("Error: " + e);
        }
    }

    // TODO: 09.12.2022 write to json
    public void saveJson(File saveFile)  {
        int[] amountList = new int[products.length];
        for (int i = 0; i < products.length; i++) {
            amountList[i] = list.get(i) == (null)? 0: list.get(i);
        }
        Basket basket1 = new Basket(this.products, this.price, amountList);
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        try (PrintWriter writer = new PrintWriter(saveFile)) {
            String json = gson.toJson(basket1);

            writer.write(json);

        } catch (IOException e) { e.printStackTrace();}
    }
}

