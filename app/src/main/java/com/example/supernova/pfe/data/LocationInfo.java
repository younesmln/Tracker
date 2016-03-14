package com.example.supernova.pfe.data;


public class LocationInfo {
    Long id;
    String uuid;
    double lg, lt;
    long time;

    public LocationInfo setUuid(String uuid) {
        this.uuid = uuid;
        return this;
    }

    public LocationInfo setLg(double lg) {
        this.lg = lg;
        return this;
    }

    public LocationInfo setLt(double lt) {
        this.lt = lt;
        return this;
    }

    public LocationInfo setTime(long time) {
        this.time = time;
        return this;
    }

    public String getUuid() {
        return uuid;
    }

    public double getLg() {
        return lg;
    }

    public double getLt() {
        return lt;
    }

    public long getTime() {
        return time;
    }

    public Long getId() {
        return id;
    }
}
