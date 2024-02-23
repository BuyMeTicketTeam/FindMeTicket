package com.booking.app.util;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.math.BigDecimal;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Log4j2
public class ExchangeRateUtils {


    public static final String RESULT = "result";
    public static BigDecimal getCurrentExchangeRate(String convertFrom, String convertTo) throws IOException {
        try {
            String urlString = String.format("https://api.apilayer.com/exchangerates_data/convert?to=%s&from=%s&amount=%d", convertTo, convertFrom, 1);

            OkHttpClient client = new OkHttpClient().newBuilder().build();

            Request request = new Request.Builder()
                    .url(urlString)
                    .addHeader("apikey", "UpT7dJ06TkVVBbFLIFgfWazZlTGBY2ZQ")
                    .method("GET", null)
                    .build();

            try (Response response = client.newCall(request).execute()) {
                JsonObject jsonObject = JsonParser.parseString(response.body().string()).getAsJsonObject();
                if (jsonObject.has(RESULT) && jsonObject.get(RESULT).isJsonPrimitive()) {
                    return jsonObject.getAsJsonPrimitive(RESULT).getAsBigDecimal();
                } else {
                    return jsonObject.getAsJsonObject(RESULT).getAsBigDecimal();
                }
            }
        } catch (Exception e) {
            log.error("Api key is invalid or Json result is null");
        }
        return null;
    }

}
