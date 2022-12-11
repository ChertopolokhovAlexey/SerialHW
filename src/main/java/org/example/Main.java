package org.example;

import java.io.File;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        String[] products = {"Хлеб", "Молоко", "Яйца", "Сыр"};
        int[] price = {30, 55, 57, 170};

        Basket basket;
        ClientLog clientLog = new ClientLog(0, 0);

        File txtFile = new File("log.csv");
        File textFile = new File("basket.json");
        if (textFile.exists()) {
            // TODO: 09.12.2022 загрузка корзину десериализацией из json-а из файла basket.json
            basket = Basket.loadFromTxtFile(textFile);
            basket.printBasket();
        } else {
            basket = new Basket(products, price);
        }

        for (int i = 0; i < price.length; i++) {
            System.out.println((i + 1) + " " + products[i] + ": " + price[i] + " руб/шт");
        }

        while (true) {
            System.out.println("Выберите товар и количество или введите 'end'");
            String choice = scanner.nextLine();
            if ("end".equals(choice)) {
                break;
            }
            String[] parts = choice.split(" ");

            if (parts.length != 2) {
                System.out.println("Позиция и количество товара должны вводиться двумя числами через пробел!");
                continue;
            }
            try {
                int productNum = Integer.parseInt(parts[0]) - 1;
                int amount = Integer.parseInt(parts[1]);
                basket.addToCart(productNum, amount);
                clientLog.log(productNum, amount);
            } catch (NumberFormatException e) {
                System.out.println("Неверный ввод! Вводить нужно числа!");
            }
            // TODO: 09.12.2022 вместо вызова метода saveTxt в методе main сериализуйте корзину в json-формате в файл basket.json 
            basket.saveJson(textFile);
        }
        basket.printCart();
        clientLog.exportAsCSV(txtFile);
    }
}

