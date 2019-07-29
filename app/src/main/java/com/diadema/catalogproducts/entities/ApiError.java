package com.diadema.catalogproducts.entities;

import java.util.List;
import java.util.Map;

/**
 * Created by Sergio Ayestas on 27/03/2019.
 */

public class ApiError {
    private String message;
    private Map<String, List<String>> errors;

    public String getMessage() {
        return message;
    }

    public Map<String, List<String>> getErrors() {
        return errors;
    }
}