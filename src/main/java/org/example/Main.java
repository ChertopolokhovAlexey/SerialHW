package org.example;

import java.io.File;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        String[] products = {"Хлеб", "Молоко", "Яйца", "Сыр"};
        int[] price = {30, 55, 57, 170};

        Basket basket;

        Scanner scanner = new Scanner(System.in);

        File file = new File("Basket.bin");
        if (file.exists()) {
            basket = Basket.loadFromBinFile(file);
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
            } catch (NumberFormatException e) {
                System.out.println("Неверный ввод! Вводить нужно числа!");
            }
            basket.saveBin(file);
        }
        basket.printCart();
    }
}
