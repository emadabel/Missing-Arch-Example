package com.emadabel.missingarchexample.data.network;

import android.text.TextUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Headers;
import okhttp3.ResponseBody;
import retrofit2.Response;
import timber.log.Timber;

public abstract class ApiResponse<T> {

    public static ApiErrorResponse create(Throwable error) {
        String message = error.getMessage();
        if (message == null) return new ApiErrorResponse("unknown error");
        else return new ApiErrorResponse(error.getMessage());
    }

    public static ApiResponse create(Response<?> response) {
        if (response.isSuccessful()) {
            Object body = response.body();
            if (body != null && response.code() != 204) {
                Headers headers = response.headers();
                return new ApiSuccessResponse(body, headers != null ? headers.get("link") : null);
            } else {
                return new ApiEmptyResponse();
            }
        } else {
            ResponseBody responseBody = response.errorBody();
            String msg = null;
            try {
                msg = responseBody != null ? responseBody.string() : null;
            } catch (IOException e) {
                e.printStackTrace();
            }
            String errorMsg;
            if (TextUtils.isEmpty(msg)) {
                errorMsg = response.message();
            } else {
                errorMsg = msg;
            }
            return new ApiErrorResponse(errorMsg == null ? "unknown error" : errorMsg);
        }
    }
}

final class ApiEmptyResponse<T> extends ApiResponse<T> {
    public ApiEmptyResponse() {
    }
}

final class ApiSuccessResponse<T> extends ApiResponse<T> {
    private static final Pattern LINK_PATTERN = Pattern.compile("<([^>]*)>[\\s]*;[\\s]*rel=\"([a-zA-Z0-9]+)\"");
    private static final Pattern PAGE_PATTERN = Pattern.compile("\\bpage=(\\d+)");
    private static final String NEXT_LINK = "next";

    final T body;
    final Map<String, String> links;

    public ApiSuccessResponse(T body, String linkHeader) {
        this.body = body;
        this.links = extractLinks(linkHeader);
    }

    public final Integer nextPage() {
        String next = links.get(NEXT_LINK);
        Integer page = null;
        if (next != null) {
            Matcher matcher = PAGE_PATTERN.matcher(next);
            if (!matcher.find() || matcher.groupCount() != 1) {
                return null;
            } else {
                try {
                    page = Integer.parseInt(matcher.group(1));
                } catch (Exception NumberFormatException) {
                    Timber.w("cannot parse next page from %s", next);
                    return null;
                }
            }
        }
        return page;
    }

    private Map<String, String> extractLinks(String str) {
        Map<String, String> links = new HashMap<>();
        Matcher matcher = LINK_PATTERN.matcher(str);

        while (matcher.find()) {
            int count = matcher.groupCount();
            if (count == 2) {
                links.put(matcher.group(2), matcher.group(1));
            }
        }
        return links;
    }
}

final class ApiErrorResponse<T> extends ApiResponse<T> {
    final String errorMessage;

    public ApiErrorResponse(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}