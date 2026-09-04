package com.certforge.library.lambda;

import java.util.LinkedHashMap;
import java.util.Map;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

/**
 * A deliberately small Lambda used as a Step Functions task demo.
 * The workflow, not this class, owns the three-step orchestration.
 */
public final class LibraryWorkflowTaskHandler implements RequestHandler<Map<String, Object>, Map<String, Object>> {
    @Override
    public Map<String, Object> handleRequest(Map<String, Object> input, Context context) {
        String step = input == null ? null : String.valueOf(input.get("step"));
        String message = switch (step) {
            case "validate" -> "借阅请求已通过校验";
            case "record" -> "借阅记录已创建";
            case "notify" -> "借阅结果已通知读者";
            default -> throw new IllegalArgumentException("Unsupported workflow step: " + step);
        };

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("step", step);
        result.put("status", "OK");
        result.put("message", message);
        if (input != null && input.containsKey("requestId")) {
            result.put("requestId", input.get("requestId"));
        }
        return result;
    }
}
