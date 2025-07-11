package com.arteriatech.ss.msec.iscan.v4.facerecognation;

import com.arteriatech.mutils.log.LogManager;
import com.arteriatech.ss.msec.iscan.v4.common.Constants;

import org.apache.http.conn.ConnectTimeoutException;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownServiceException;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;

public class MultipartUtility {
    private final String boundary;
    private static final String LINE_FEED = "\r\n";
    private HttpsURLConnection httpConn;
    private String charset;
    private OutputStream outputStream;
    private PrintWriter writer;

    /**
     * This constructor initializes a new HTTP POST request with content type
     * is set to multipart/form-data
     *
     * @param requestURL
     * @param charset
     * @throws IOException
     */
    public MultipartUtility(String requestURL, String charset,String basicAuth,String appid)
            throws IOException {
        this.charset = charset;

        // creates a unique boundary based on time stamp
        boundary = "===" + System.currentTimeMillis() + "===";
        HostnameVerifier hostnameVerifier = org.apache.http.conn.ssl.SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER;
        HttpsURLConnection.setDefaultHostnameVerifier(hostnameVerifier);
        URL url = new URL(requestURL);
        httpConn = (HttpsURLConnection) url.openConnection();
        httpConn.setUseCaches(false);
        httpConn.setDoOutput(true);    // indicates POST method
        httpConn.setDoInput(true);
//        httpConn.addRequestProperty("Authorization","Token "+ token);
//        httpConn.addRequestProperty("sskey",""+ token);
        httpConn.setRequestProperty("Authorization", basicAuth);
        httpConn.setConnectTimeout(120000);
        httpConn.setReadTimeout(120000);
        httpConn.setRequestProperty("Content-Type",
                "multipart/form-data; boundary=" + boundary);
        httpConn.addRequestProperty("x-smp-appid",appid);
        outputStream = httpConn.getOutputStream();
        writer = new PrintWriter(new OutputStreamWriter(outputStream, charset),
                true);
    }
    public MultipartUtility(String requestURL, String charset,boolean noAuth)
            throws IOException {
        this.charset = charset;

        // creates a unique boundary based on time stamp
        boundary = "===" + System.currentTimeMillis() + "===";
        URL url = new URL(requestURL);
        httpConn = (HttpsURLConnection) url.openConnection();
        httpConn.setUseCaches(false);
        httpConn.setDoOutput(true);    // indicates POST method
        httpConn.setDoInput(true);
        httpConn.setConnectTimeout(120000);
        httpConn.setReadTimeout(120000);
        httpConn.setRequestProperty("Content-Type",
                "multipart/form-data; boundary=" + boundary);
        outputStream = httpConn.getOutputStream();
        writer = new PrintWriter(new OutputStreamWriter(outputStream, charset),
                true);
    }

    /**
     * Adds a form field to the request
     *
     * @param name  field name
     * @param value field value
     */
    public void addFormField(String name, String value) {
        writer.append("--" + boundary).append(LINE_FEED);
        writer.append("Content-Disposition: form-data; name=\"" + name + "\"")
                .append(LINE_FEED);
        writer.append("Content-Type: text/plain; charset=" + charset).append(
                LINE_FEED);
        writer.append(LINE_FEED);
        writer.append(value).append(LINE_FEED);
        writer.flush();
    }

    /**
     * Adds a upload file section to the request
     *
     * @param fieldName  name attribute in <input type="file" name="..." />
     * @param uploadFile a File to be uploaded
     * @throws IOException
     */
    public void addFilePart(String fieldName, File uploadFile)
            throws IOException {
        String fileName = uploadFile.getName();
        writer.append("--" + boundary).append(LINE_FEED);
        writer.append(
                        "Content-Disposition: form-data; name=\"" + fieldName
                                + "\"; filename=\"" + fileName + "\"")
                .append(LINE_FEED);
        writer.append(
                        "Content-Type: "
                                + URLConnection.guessContentTypeFromName(fileName))
                .append(LINE_FEED);
        writer.append("Content-Transfer-Encoding: binary").append(LINE_FEED);
        writer.append(LINE_FEED);
        writer.flush();

        FileInputStream inputStream = new FileInputStream(uploadFile);
        byte[] buffer = new byte[4096];
        int bytesRead = -1;
        while ((bytesRead = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, bytesRead);
        }
        outputStream.flush();
        inputStream.close();
        writer.append(LINE_FEED);
        writer.flush();
    }

    /**
     * Adds a header field to the request.
     *
     * @param name  - name of the header field
     * @param value - value of the header field
     */
    public void addHeaderField(String name, String value) {
        writer.append(name + ": " + value).append(LINE_FEED);
        writer.flush();
    }

    /**
     * Completes the request and receives response from the server.
     *
     * @return a list of Strings as response in case the server returned
     * status OK, otherwise an exception is thrown.
     * @throws IOException
     */
    public List<String> finish() throws IOException {
        List<String> response = new ArrayList<String>();
        writer.append(LINE_FEED).flush();
        writer.append("--" + boundary + "--").append(LINE_FEED);
        writer.close();

        // checks server's status code first
        int status = httpConn.getResponseCode();
        if (status == HttpURLConnection.HTTP_OK) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    httpConn.getInputStream()));
            String line = null;
            while ((line = reader.readLine()) != null) {
                response.add(line);
            }
            reader.close();
            httpConn.disconnect();
        } else {
            response.add(readResponse(httpConn.getErrorStream()));
//            throw new IOException("Server returned non-OK status: " + status);
        }
        return response;
    }

    public JSONObject finish1(){
        JSONObject response = new JSONObject();
        writer.append(LINE_FEED).flush();
        writer.append("--" + boundary + "--").append(LINE_FEED);
        writer.close();

        // checks server's status code first
        try {
            int status = httpConn.getResponseCode();
            String responseStr = "";
            // http code = 200
            if (status == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(
                        httpConn.getInputStream()));
                String line = null;
                while ((line = reader.readLine()) != null) {
                    responseStr = line;
                }
                try {
                    response.put(Constants.HTTPCODE,status);
                    response.put(Constants.ResponseBody,responseStr);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                reader.close();
            } else {
                // http code != 200
//                responseStr = readResponse(httpConn.getErrorStream());
                try {
                    Constants.httphashmaperrorcodes();
                    if(Constants.httpErrorCodes.containsKey(""+status)) {
                        response.put(Constants.HTTPCODE, status);
                        response.put(Constants.ResponseBody, Constants.httpErrorCodes.get(""+status));
                    }else{
                        // unknown http code
                        response = getExceptionMessages("Unknown error","Unknown error","Face Recgn unknown",status);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } catch (SocketTimeoutException exception){
            response = getExceptionMessages("Socket Timeout",exception.getMessage(),"Face Recgn Socket Timeout",-1);
        } catch (ConnectTimeoutException connectTimeoutException){
            response = getExceptionMessages("Connect Timeout",connectTimeoutException.getMessage(),"Face Recgn Connect Timeout",-2);
            connectTimeoutException.printStackTrace();
        } catch (UnknownServiceException e) {
            response = getExceptionMessages("Unknown Service",e.getMessage(),"Face Recgn Unknown Service",-3);
            e.printStackTrace();
        } catch (IOException e) {
            response = getExceptionMessages("IO Exception",e.getMessage(),"Face Recgn IO Exception",-4);
            e.printStackTrace();
        }
        try {
            httpConn.disconnect();
        } catch (Throwable e) {
            e.printStackTrace();
        }

        return response;
    }

    public JSONObject getExceptionMessages(String errorMsg , String exceptionMsg,String logMsg,int httpcode){
        JSONObject response = new JSONObject();
        try {
            response.put(Constants.HTTPCODE,httpcode);
            response.put(Constants.ResponseBody,errorMsg);
            LogManager.writeLogError(logMsg+" : "+ exceptionMsg);
        } catch (JSONException e) {
            e.printStackTrace();
            LogManager.writeLogError(logMsg + " : " + exceptionMsg);
        }
        return response;
    }
    public static String readResponse(InputStream stream) throws IOException {
        BufferedReader reader = null;
        reader = new BufferedReader(new InputStreamReader(stream));
        StringBuilder buffer = new StringBuilder();

        String line;
        while ((line = reader.readLine()) != null) {
            buffer.append(line).append('\n');
        }

        return buffer.toString();
    }
}