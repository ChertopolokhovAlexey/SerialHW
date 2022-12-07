package org.example;

import au.com.bytecode.opencsv.bean.ColumnPositionMappingStrategy;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import com.opencsv.ICSVWriter;
import java.util.List;

public class ClientLog {
    protected List<Integer> productNumLog;
    protected List<Integer> amountLog;

    public ClientLog(List<Integer> productNumLog, List<Integer> amountLog) {
        this.productNumLog = productNumLog;
        this.amountLog = amountLog;
    }

    public void log(int productNum, int amount) {
        this.productNumLog.add(productNum+1);
        this.amountLog.add(amount);
        ClientLog clientLog = new ClientLog(productNumLog,amountLog);
    }

    void exportAsCSV(File txtFile) {
//        ColumnPositionMappingStrategy<ClientLog> strategy = new ColumnPositionMappingStrategy<>();
//        strategy.setType(ClientLog.class); //Он позволяет указать класс с которым будем работать
//        strategy.setColumnMapping(); //И имена полей в csv файле для автоматического ппрсинга

        try (ICSVWriter writer = new CSVWriterBuilder(new FileWriter("log.csv"))
                .withParser(",")
                .build()) {

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}