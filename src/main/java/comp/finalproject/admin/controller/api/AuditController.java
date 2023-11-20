package comp.finalproject.admin.controller.api;

import comp.finalproject.admin.service.AuditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;

@RestController
@CrossOrigin(origins = "*")
@Component("apiAuditController")
@RequestMapping("/api/audit")
public class AuditController {
    @Autowired
    private AuditService auditService;

    @GetMapping("/change")
    public ResponseEntity<?> getChangeLog() throws ParseException {
        return auditService.findAllChange();
    }
}
