package by.andrew.entity;

import java.util.Date;

public class Advert {
    private String nameAds;
    private int CounterView;
    private int CounterShowNumber;
    private int CounterLikes;
    private String adsSubmitted;
    private String adsWillExpire;

    public Advert(String nameAds, int counterView, int counterShowNumber, int counterLikes, String adsSubmitted, String adsWillExpire) {
        this.nameAds = nameAds;
        CounterView = counterView;
        CounterShowNumber = counterShowNumber;
        CounterLikes = counterLikes;
        this.adsSubmitted = adsSubmitted;
        this.adsWillExpire = adsWillExpire;
    }

    @Override
    public String toString() {
        return "Advert{" +
                "nameAds='" + nameAds + '\'' +
                ", CounterView=" + CounterView +
                ", CounterShowNumber=" + CounterShowNumber +
                ", CounterLikes=" + CounterLikes +
                '}';
    }

    public String getNameAds() {
        return nameAds;
    }

    public void setNameAds(String nameAds) {
        this.nameAds = nameAds;
    }

    public int getCounterView() {
        return CounterView;
    }

    public void setCounterView(int counterView) {
        CounterView = counterView;
    }

    public int getCounterShowNumber() {
        return CounterShowNumber;
    }

    public void setCounterShowNumber(int counterShowNumber) {
        CounterShowNumber = counterShowNumber;
    }

    public int getCounterLikes() {
        return CounterLikes;
    }

    public void setCounterLikes(int counterLikes) {
        CounterLikes = counterLikes;
    }

    public String getAdsSubmitted() {
        return adsSubmitted;
    }

    public void setAdsSubmitted(String adsSubmitted) {
        this.adsSubmitted = adsSubmitted;
    }

    public String getAdsWillExpire() {
        return adsWillExpire;
    }

    public void setAdsWillExpire(String adsWillExpire) {
        this.adsWillExpire = adsWillExpire;
    }
}
