package comp.finalproject.admin.controller.web;

import comp.finalproject.admin.entity.User;
import comp.finalproject.admin.service.AuditService;
import comp.finalproject.admin.service.web.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@Controller
@Component("webAuditController")
public class AuditController {
    @Autowired
    private AuditService auditService;
    @Autowired
    private UserService userService;

    @GetMapping("/audits")
    public String showAudit(Model model, Principal principal) {
        String email = principal.getName();

        User user = userService.findUserByEmail(email);
        String name = user.getName();
        // Menambahkan atribut firstName ke objek Model
        model.addAttribute("email", email);
        model.addAttribute("name", name);

        // Panggil findAllChange dan tambahkan hasilnya ke model
        ResponseEntity<List<Map<String, Object>>> responseEntity = auditService.findAllChange();
        model.addAttribute("auditChanges", responseEntity.getBody());

        return "audit/audits";
    }
}