package projectx.backend.controller;


import org.springframework.web.bind.annotation.RestController;
import projectx.backend.service.StockManageService;

@RestController
public class StockManageController {

    public StockManageService stockManageService;
}
