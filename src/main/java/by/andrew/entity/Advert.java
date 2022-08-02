package by.andrew.entity;

import java.util.Date;

public class Advert {
    private String nameAds;
    private int CounterView;
    private int CounterShowNumber;
    private int CounterLikes;
    private Date adsSubmitted;
    private Date adsWillExpire;

    public Advert(String nameAds, int counterView, int counterShowNumber, int counterLikes, Date adsSubmitted, Date adsWillExpire) {
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

    public Date getAdsSubmitted() {
        return adsSubmitted;
    }

    public void setAdsSubmitted(Date adsSubmitted) {
        this.adsSubmitted = adsSubmitted;
    }

    public Date getAdsWillExpire() {
        return adsWillExpire;
    }

    public void setAdsWillExpire(Date adsWillExpire) {
        this.adsWillExpire = adsWillExpire;
    }
}
