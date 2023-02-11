package org.weatherForecast;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Scanner;

public class weather {
    public static void main(String[] args) {
        try {
            callWeatherForecastApp();
        }catch(Exception e){
            throw new RuntimeException(e);
        }
    }

    public static void callWeatherForecastApp() throws Exception {
        System.out.println("Enter the location for which you want the weather forcast information : ");
        Scanner sc = new Scanner(System.in);
        String location = sc.nextLine();

        URIBuilder builder = new URIBuilder("https://weather.visualcrossing.com/VisualCrossingWebServices/rest/services/weatherdata/forecast");
        builder.setParameter("aggregateHours", "24");
        builder.setParameter("contentType", "json");
        builder.setParameter("unitGroup", "metric");
        builder.setParameter("locationMode", "single");
        builder.setParameter("key", "1PYNQ6AWUDJE9AFERDCHJHSXK");
        builder.setParameter("location", location);

        HttpGet getData = new HttpGet(builder.build());

        CloseableHttpClient httpClient = HttpClients.createDefault();

        CloseableHttpResponse response = httpClient.execute(getData);

        if(response.getStatusLine().getStatusCode() == 200){
            HttpEntity entityResponse = response.getEntity();
            String result = EntityUtils.toString(entityResponse);
            JSONObject responseObject = new JSONObject(result);

            JSONObject locationObject = responseObject.getJSONObject("location");

            JSONArray valueObject = locationObject.getJSONArray("values");

            System.out.println("datetimeStr            \t     mint     \t  maxt   \t visibility \t humidity");
            for(int i=0; i<valueObject.length(); i++){

                JSONObject value = valueObject.getJSONObject(i);

                String dateTime = value.getString("datetimeStr");
                Double minTemp = value.getDouble("mint");
                Double maxTemp = value.getDouble("maxt");
                Double visi = value.getDouble("visibility");
                Double humi = value.getDouble("humidity");
                System.out.printf("%s \t %f \t %f \t %f \t   %f \n",dateTime,minTemp,maxTemp,visi,humi);
            }
            //System.out.println(result);
        }
        else{
            System.out.println("Something went wrong !");
            return;
        }
        httpClient.close();
    }
}
