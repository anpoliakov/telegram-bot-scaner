package by.andrew;

import by.andrew.entity.Account;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.ArrayList;

public class Kufar {
    private ArrayList <Account> accounts = new ArrayList<>();
    CloseableHttpClient httpClient = HttpClients.createDefault();

    public boolean login(String login, String password){
        boolean isLoginSuccessful = false;

        Account account = executeAuthorization(login, password);
        if(account != null){
            isLoginSuccessful = true;
        }

        return isLoginSuccessful;
    }

    private Account executeAuthorization(String login, String password){

        Account account = new Account(login, password);
        HttpPost httpPost = new HttpPost(Constants.AUTH);

        try {
            StringEntity params =
                new StringEntity(
                        "{\"login\":\"" + login + "\"," +
                        "\"password\":\"" + password + "\"," +
                        "\"recaptcha_platform\":\"web\"," +
                        "\"recaptcha_secret_version\":\"v1\"," +
                        "\"recaptcha_user_response\":\" \"}"
                );

            httpPost.setEntity(params);
            httpPost.addHeader("Content-Type", "application/json");
            HttpResponse response = httpClient.execute(httpPost);

            if(response.getStatusLine().getStatusCode() == 200){
                setCookies(account,response);
                setAllInfoAccaunt(account);
            }

        } catch (UnsupportedEncodingException e) {
            System.err.println("Ошибка в JSON, запрос на авторизацию пользователя!");
        } catch (ClientProtocolException e) {
            System.out.println("Ошибка в выполнение метода execute!");
        } catch (IOException e) {
            e.printStackTrace();
        }

        return account;
    }

    private void setCookies(Account account, HttpResponse response){
        Header[] headers = response.getHeaders("Set-Cookie");
        StringBuilder builder = new StringBuilder();

        for (Header header : headers){
            builder.append(header.getValue());
        }

        account.setCookie(builder.toString());
    }

    private void setAllInfoAccaunt(Account account){
        String infoAccauntJson = executeRequestAccauntInfo(account);
        System.out.println(infoAccauntJson);
        try {
            Object parse = new JSONParser().parse(infoAccauntJson);
            JSONObject jo = (JSONObject) parse;
//            String account_id = (String) jo.get("account_id");
//            String area = (String) jo.get("area");
//            String date_of_birth = (String) jo.get("date_of_birth");
//            String email = (String) jo.get("email");
//            String name = (String) jo.get("name");
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private String executeRequestAccauntInfo(Account account){
        String responseJson = null;

        HttpGet httpGet = new HttpGet(Constants.ACCAUNT_INFO);
        httpGet.addHeader("Content-Type", "application/json; charset=UTF-8");
        httpGet.addHeader("Accept-Encoding","gzip, deflate, br");
        httpGet.addHeader("Accept-Language","ru,ru-RU;q=0.9,en-CA;q=0.8,en-US;q=0.7,en;q=0.6");
        httpGet.addHeader("User-Agent","Mozilla/5.0 (Windows NT 6.3; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/103.0.0.0 Safari/537.36");
        httpGet.addHeader("Accept", "*/*");
        httpGet.addHeader("Connection", "keep-alive");
        httpGet.addHeader("Cookie", account.getCookie());

        try {
            HttpResponse response = httpClient.execute(httpGet);
            HttpEntity entity = response.getEntity();
            InputStream inputStream = entity.getContent();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), 8);
            StringBuilder builder = new StringBuilder();
            String line = null;

            while ((line = reader.readLine()) != null){
                builder.append(line);
            }

            responseJson = builder.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return responseJson;
    }
}