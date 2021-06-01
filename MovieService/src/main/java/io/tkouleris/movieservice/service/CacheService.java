package io.tkouleris.movieservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

@Service
public class CacheService {
    @Value("${cachefolder}")
    private String cachefolder;

    public void save(String data, String key) throws IOException {

        FileWriter myWriter = new FileWriter(cachefolder+key+".json");
        myWriter.write(data);
        myWriter.close();
    }

    public <T> T getKey(String key, T obj, Class<T> classpath) throws FileNotFoundException, JsonProcessingException {
        File file = new File(cachefolder + key +".json");
        Scanner myReader = new Scanner(file);
        while (myReader.hasNextLine()) {
            String response = myReader.nextLine();
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
            obj = objectMapper.readValue(response, classpath);
        }

        return obj;
    }
}
