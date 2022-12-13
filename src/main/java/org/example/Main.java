package org.example;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        String[] products = {"Хлеб", "Молоко", "Яйца", "Сыр"};
        int[] price = {30, 55, 57, 170};

        Basket basket = new Basket(products, price);
        ClientLog clientLog = new ClientLog(0, 0);


        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        boolean isLoadEnabled;
        String loadFileName;
        String loadFormat;
        boolean isSaveEnabled;
        String saveFileName;
        String saveFormat;
        boolean isLogEnabled;
        String logFile;

        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse("shop.xml");

            XPath xPath = XPathFactory.newInstance().newXPath();

            isLoadEnabled = Boolean.parseBoolean(xPath
                    .compile("config/load/enabled")
                    .evaluate(document));
            loadFileName = xPath
                    .compile("config/load/fileName")
                    .evaluate(document);
            loadFormat = xPath
                    .compile("config/load/format")
                    .evaluate(document);
            isSaveEnabled = Boolean.parseBoolean(xPath
                    .compile("config/save/enabled")
                    .evaluate(document));
            saveFileName = xPath
                    .compile("config/save/fileName")
                    .evaluate(document);
            saveFormat = xPath
                    .compile("config/save/format")
                    .evaluate(document);
            isLogEnabled = Boolean.parseBoolean(xPath
                    .compile("config/log/enabled")
                    .evaluate(document));
            logFile = xPath
                    .compile("config/log/fileName")
                    .evaluate(document);

        } catch (ParserConfigurationException e) {
            throw new RuntimeException(e);
        } catch (XPathExpressionException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (SAXException e) {
            throw new RuntimeException(e);
        }
        File loadFile = new File(loadFileName);
        File txtFile = new File(logFile);

        if (isLoadEnabled && loadFile.exists()) {
            switch (loadFormat) {
                case "text" -> basket = Basket.loadFromTxtFile(loadFile);
                case "json" -> basket = Basket.loadFromJSONFile(loadFile);
            }
            basket.printBasket();
        } else {
            System.out.println("Ваша корзина пока пуста");
            basket = new Basket(products, price);
        }
// объявление корзины
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
                if (isLogEnabled) {
                    clientLog.log(productNum, amount);
                }
            } catch (NumberFormatException e) {
                System.out.println("Неверный ввод! Вводить нужно числа!");
            }

            if (isSaveEnabled){
                File saveFile = new File(saveFileName);

                switch (saveFormat){
                    case "text"-> basket.saveTxt(saveFile);
                    case "json"-> basket.saveJson(saveFile);
                }
            }
        }
        basket.printCart();
        clientLog.exportAsCSV(txtFile);
    }
}

