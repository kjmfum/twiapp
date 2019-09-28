package com.learnakantwi.twiguides;

public class Weather {
    String weatherEnglish;
    String weatherTwi;

    public Weather(String weatherEnglish, String weatherTwi) {
        this.weatherEnglish = weatherEnglish;
        this.weatherTwi = weatherTwi;
    }

    public String getWeatherEnglish() {
        return weatherEnglish;
    }

    public void setWeatherEnglish(String weatherEnglish) {
        this.weatherEnglish = weatherEnglish;
    }

    public String getWeatherTwi() {
        return weatherTwi;
    }

    public void setWeatherTwi(String weatherTwi) {
        this.weatherTwi = weatherTwi;
    }
}
