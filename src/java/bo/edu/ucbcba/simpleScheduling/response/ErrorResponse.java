/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bo.edu.ucbcba.simpleScheduling.response;

import java.util.List;
import java.util.UUID;
import javax.ws.rs.core.Response;

/**
 *
 * @author da_20
 */
public class ErrorResponse {
    private final UUID id;
    private final Response.Status status;
    private final String code;
    private final String title;
    private final String detail;
    private final List<String> meta;

    public ErrorResponse(UUID id, Response.Status status, String code, String title, String detail, List<String> meta) {
        this.id = id;
        this.status = status;
        this.code = code;
        this.title = title;
        this.detail = detail;
        this.meta = meta;
    }

    public UUID getId() {
        return id;
    }

    public Response.Status getStatus() {
        return status;
    }

    public String getCode() {
        return code;
    }

    public String getTitle() {
        return title;
    }

    public String getDetail() {
        return detail;
    }

    public List<String> getMeta() {
        return meta;
    }
    
    
}
