package mx.software.solutions.centraltextileraonline.apiservices.resttemplate.request.pqtx;

import lombok.Data;

import java.util.List;

@Data
public class PqtxRequestCollect {
    private List<PqtxDataCollect> data;
}
