package com.project.gangrg.common.exception;

import static com.project.gangrg.common.exception.ErrorCode.SERVER_ERROR;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.gangrg.common.ApiResponse;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.filter.OncePerRequestFilter;

@RequiredArgsConstructor
public class ExceptionHandlingFilter extends OncePerRequestFilter {

    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (CustomException e) {
            handleErrorResponse(response, e.getErrorCode().getStatus(), e.getErrorCode().getMessage());
        } catch (Exception e) {
            handleErrorResponse(response, SERVER_ERROR.getStatus(), SERVER_ERROR.getMessage());
        }
    }

    private void handleErrorResponse(HttpServletResponse response, int status, String message) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(status);
        String apiResponseJson = objectMapper.writeValueAsString(ApiResponse.errorResponse(message));
        PrintWriter writer = response.getWriter();
        writer.print(apiResponseJson);
        writer.flush();
    }
}
