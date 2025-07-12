package com.arshmed.ticketreservation.dto;

import java.io.Serializable;
import java.util.List;

public record PageDto<T>(
        List<T> content,
        int totalPages,
        long totalElements,
        int pageNumber,
        int pageSize
) implements Serializable {}