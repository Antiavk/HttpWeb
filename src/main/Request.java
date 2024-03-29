package src.main;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;



public class Request {
    final char QUERY_DELIMITER = '?';
    private String method;
    private String path;
    private String version;
    private Map<String, String> headers;
    private String body;
    private long queryParams;

    public Request(String requestLine) {
        this.headers = new ConcurrentHashMap<>();
        this.method = "";
        this.path = "";
        this.version = "";
        this.body = "";
    }

    public boolean addHeader(String header) {
        String[] headerParts = header.split(":");
        if (headerParts.length == 2) {
            this.headers.put(headerParts[0], headerParts[1].replace(" ",""));
            return true;
        } else {
            return false;
        }
    }

    public void setMethod(String method) {
        this.method = method;
    }
    public void setPath(String path) {
        this.path = path;
    }
    public void setVersion(String version) {
        this.version = version;
    }
    public void setBody(String body) {
        this.body = body;
    }
    public String getMethod() {
        return method;
    }

    @Override
    public String toString() {
        try {
            StringBuilder sb = new StringBuilder();
            sb.append("\n").append(method).append(" ").append(path).append(" ").append(version).append("\n");
            for (Map.Entry<String, String> entry : headers.entrySet())
                sb.append(entry.getKey()).append(":").append(entry.getValue()).append("\n");
            sb.append("\n");
            sb.append(body);
            return sb.toString();
        } catch (Exception e) {
            System.out.print("Ошибка: ");
            e.printStackTrace();
        }
        return "";
    }

    public Optional<String> extractHeader(String header) { //из списка строк мы вытскивем header, причем его значение
        return headers.entrySet()//преобразовываем его в стрим
                .stream()
                .filter(o -> o.getKey().equals(header))// оставляем только строки которые начинаются с искомого header
                .map(o -> o.getValue())
                .findFirst();
    }

    public void setQueryParams() {
        int delimiter = path.indexOf(QUERY_DELIMITER);
        if (delimiter == -1) return;
        Date URLEncodedUtils = null;
        queryParams = URLEncodedUtils.parse(path.substring(delimiter + 1));
    }


    public String getBody() {
        return body;
    }

    public String getPathWithoutQueryParams() {
        int queryDelimiter = path.indexOf(QUERY_DELIMITER);
        if (queryDelimiter != -1) {
            path = path.substring(0, queryDelimiter);
            System.out.println(path);
        }
        return path;
    }

//        public Optional<String> getQueryParamValue(String queryParam) {
//            return queryParams.stream()
//                    .filter(o -> o.getName().equals(queryParam))
//                    .map(o -> o.getValue())
//                    .findFirst();
//        }

    public Optional<String> getHeaderValue(String header) {
        return headers.entrySet()
                .stream()
                .filter(o -> o.getKey().equals(header))
                .map(o -> o.getValue())
                .findFirst();
    }
}