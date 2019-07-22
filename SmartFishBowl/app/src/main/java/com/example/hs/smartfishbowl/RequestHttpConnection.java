package com.example.hs.smartfishbowl;

import android.content.ContentValues;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

public class RequestHttpConnection {
    public String request(String _url, ContentValues _params){
        //HttpURLConnection 참조 변수
        HttpURLConnection urlConn = null;
        //URL 뒤에 붙여서 보낼 파라미터
        StringBuffer sbParams = new StringBuffer();

        //StringBuffer에 파라미터 연결
        if(_params==null)   //보낼 데이터가 없으면 파라미터를 비운다.
            sbParams.append("");
        else{
            //파라미터가 2개 이상이면 파리미터 연결에 &가 필요하므로 스위칭할 변수 생성.
            boolean isAnd = false;
            //파라미터 키와 값
            String key;
            String value;

            for(Map.Entry<String, Object> parameter : _params.valueSet()){
                key = parameter.getKey();
                value = parameter.getValue().toString();

                //파라미터가 두개 이상일때, 파라미터 사이에 &를 붙인다.
                if(isAnd)
                    sbParams.append("&");
                sbParams.append(key).append("=").append(value);
                //파라미터가 2개 이상이면 isAnd를 true로 바꾸고 다음 루프부터 &를 붙인다.
                if(!isAnd)
                    if(_params.size()>=2)
                        isAnd = true;
            }
        }
        //httpURLConnection을 통해 web의 데이터를 가져온다.
        try{
            URL url = new URL(_url);
            urlConn = (HttpURLConnection)url.openConnection();

            //urlConn설정
            urlConn.setRequestMethod("POST");   //URL 요청에 대한 메소드 설정
            urlConn.setRequestProperty("Accept-Charset","UTF-8");   //Accept-Charset 설정
            urlConn.setRequestProperty("Context_Type","application/x-www-form-urlencoded;cahrset=UTF-8");

            //parameter 전달 및 데이터 읽어오기
            String strParams = sbParams.toString(); //sbParams에 정리한 파라미터들을 스트링으로 저장
            OutputStream os = urlConn.getOutputStream();
            os.write(strParams.getBytes("UTF-8"));  //출력 스트림에 출력.
            os.flush();    //출력 스트림을 비우고 버퍼링된 모든 출력 바이트를 강제 실행.
            os.close();     //출력 스트림을 닫고 모든 시스템 자원을 해제

            //연결 요청 확인
            if(urlConn.getResponseCode()!=HttpURLConnection.HTTP_OK)
                return null;

            //읽어온 결과물 리턴
            BufferedReader reader = new BufferedReader(new InputStreamReader(urlConn.getInputStream(),"UTF-8"));

            //출력물의 라인과 그 합에 대한 변수
            String line;
            String page = "";

            //라인을 받아와 합친다.
            while((line = reader.readLine())!=null){
                page += line;
            }
            return page;
        } catch (MalformedURLException e){
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(urlConn != null)
                urlConn.disconnect();
        }
        return null;
    }
}
