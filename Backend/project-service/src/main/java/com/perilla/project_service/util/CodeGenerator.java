package com.perilla.project_service.util;

import java.util.UUID;

public class CodeGenerator {

    public static String generateProjectCode() {
        return "PRJ-" + UUID.randomUUID()
                .toString()
                .substring(0, 8)
                .toUpperCase();
    }

    public static String generateTaskCode() {
        return "TSK-" + UUID.randomUUID()
                .toString()
                .substring(0, 8)
                .toUpperCase();
    }

    public static String generateTicketCode() {
        return "TCK-" + UUID.randomUUID()
                .toString()
                .substring(0, 8)
                .toUpperCase();
    }

    public static String generatePostCode() {
        return "PST-" + UUID.randomUUID()
                .toString()
                .substring(0, 8)
                .toUpperCase();
    }
}
