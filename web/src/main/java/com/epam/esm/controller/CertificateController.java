package com.epam.esm.controller;


import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.service.CertificateService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/certificates")
@AllArgsConstructor
public class CertificateController {

    private final CertificateService certificateService;

    @GetMapping("/hello")
    public String hello() {
        return "hello";
    }

    @DeleteMapping("/{id}")
    public void deleteCertificate(@PathVariable long id) {
        certificateService.deleteById(id);
    }

    @GetMapping("/{id}")
    public GiftCertificateDto getById(@PathVariable long id) {
        return certificateService.getById(id);
    }
}
