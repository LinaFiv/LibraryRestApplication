package org.example.utils;

import java.io.BufferedReader;
import java.io.IOException;

public class ServletUtil {

    public static String getRequestBody(BufferedReader reader) throws IOException {
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }
        return sb.toString();
    }
}
