package com.example.gauranggoel.addingordersdelenintern;

public class MyOrder {

    String time;
    String id;
    String workType;
    String workerName;
    String cost;
    String status;
    String address;
    String url;


    public MyOrder(String time, String id, String workType, String workerName, String cost, String status, String address, String url) {
        this.time = time;
        this.id = id;
        this.workType = workType;
        this.workerName = workerName;
        this.cost = cost;
        this.status = status;
        this.address = address;
        this.url=url;
    }
}
