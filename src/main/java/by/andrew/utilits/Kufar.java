package by.andrew.utilits;

import by.andrew.Constants;
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
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.*;

//работа с аккаунтом kufar
public class Kufar {
    private CloseableHttpClient httpClient = HttpClients.createDefault();

    //ВХОД в аккаунт
    public Account login(String login, String password){
        HttpPost httpPost = new HttpPost(Constants.AUTH);
        Account account = null;

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
                account = new Account(login,password);
                setCookie(account,response);
                getInformationAccount(account);

            }else if (response.getStatusLine().getStatusCode() >= 300 && response.getStatusLine().getStatusCode() < 400){
                System.err.println("Ошибка входа на стороне клиента");
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

    //УСТАНАВЛИВАЕМ куки для дальнейшей работы с аккаунтом
    private void setCookie(Account account, HttpResponse response){
        Header[] headers = response.getHeaders("Set-Cookie");
        StringBuilder builder = new StringBuilder();

        for (Header header : headers){
            String[] values = header.getValue().split(" ");
            builder.append(values[0] + " ");
        }

        account.setCookie(builder.toString());
    }

    public void show_ads(Account account){
        //запрос на куфар и получение всех обьявлений
        HttpGet httpGet = new HttpGet(Constants.GET_ALL_ADS);
        httpGet.addHeader("accept-encoding", "gzip, deflate, br");
        httpGet.addHeader("User-Agent","Mozilla/5.0 (Windows NT 6.3; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/103.0.0.0 Safari/537.36");
        httpGet.addHeader("Connection","keep-alive");
        httpGet.addHeader("Cookie", account.getCookie());


        System.out.println("КУКИ: " + account.getCookie());

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

            preperAdsFromHtml(builder);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //вытягивает информацию со страницы
    private void preperAdsFromHtml(StringBuilder builder){
        Document document = Jsoup.parse(builder.toString());
        System.out.println(document.body().toString());
    }

    //Получение пользовательской информации об куфар-аккаунте
    private void getInformationAccount(Account account){
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

        System.out.println("Information from Accaunt:" + responseJson);
        try {
            Object parse = new JSONParser().parse(responseJson);
            JSONObject jsonObj = (JSONObject) parse;

            account.setName((String) jsonObj.get("name"));
            account.setPhone((String) jsonObj.get("phone"));
            account.setToken((String) jsonObj.get("token"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
