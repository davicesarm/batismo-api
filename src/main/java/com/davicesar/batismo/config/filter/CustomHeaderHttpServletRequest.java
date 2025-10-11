package com.davicesar.batismo.config.filter;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;

import java.util.*;

public class CustomHeaderHttpServletRequest extends HttpServletRequestWrapper {
    private final String headerName;
    private final String headerValue;

    public CustomHeaderHttpServletRequest(HttpServletRequest request, String headerName, String headerValue) {
        super(request);
        this.headerName = headerName;
        this.headerValue = headerValue;
    }

    @Override
    public String getHeader(String name) {
        if (headerName.equalsIgnoreCase(name)) {
            return headerValue;
        }
        return super.getHeader(name);
    }

    @Override
    public Enumeration<String> getHeaderNames() {
        Set<String> names = new HashSet<>();
        Enumeration<String> superNames = super.getHeaderNames();
        while (superNames.hasMoreElements()) {
            names.add(superNames.nextElement());
        }
        names.add(headerName);
        return Collections.enumeration(names);
    }

    @Override
    public Enumeration<String> getHeaders(String name) {
        if (headerName.equalsIgnoreCase(name)) {
            return Collections.enumeration(Collections.singletonList(headerValue));
        }
        return super.getHeaders(name);
    }
}