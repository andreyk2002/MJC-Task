package com.epam.esm.controller;


import com.epam.esm.dto.CertificateResponseDto;
import com.epam.esm.localization.Localizer;
import com.epam.esm.service.CertificateService;
import com.epam.esm.validation.CertificateRequestDto;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import java.util.List;

@RestController
@RequestMapping("/certificates")
@AllArgsConstructor
@Validated
public class CertificateController {

    private final Localizer localizer;
    private final CertificateService certificateService;
    private static final String WRONG_ID_MSG = "certificate.wrongId";
    private static final String CANT_DELETE = "certificate.cantDelete";

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCertificate(@PathVariable long id) {
        CertificateResponseDto certificate = certificateService.deleteById(id);
        return new ResponseEntity<>(certificate, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable long id) {
        CertificateResponseDto certificate = certificateService.getById(id);
        return new ResponseEntity<>(certificate, HttpStatus.OK);
    }

    @GetMapping("")
    public ResponseEntity<List<CertificateResponseDto>> getAll() {
        List<CertificateResponseDto> certificates = certificateService.getAll();
        return new ResponseEntity<>(certificates, HttpStatus.OK);
    }

    @GetMapping("/searchTest")
    public ResponseEntity<List<CertificateResponseDto>> getCertificates(@RequestParam(required = false) String tagName,
                                                                        @RequestParam(required = false) String keyword,
                                                                        @RequestParam(defaultValue = "id,desc")
                                                                        @Pattern(regexp = "(name|createDate),(asc|desc)")
                                                                        @Valid String sort) {
        List<CertificateResponseDto> certificates = certificateService.getCertificates(tagName, keyword, sort);
        return new ResponseEntity<>(certificates, HttpStatus.OK);
    }


    @PostMapping("")
    public ResponseEntity<CertificateResponseDto> addCertificate(
            @RequestBody @Valid CertificateRequestDto certificateRequestDto) {
        CertificateResponseDto certificateResponseDto = certificateService.addCertificate(certificateRequestDto);
        return new ResponseEntity<>(certificateResponseDto, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CertificateResponseDto> updateCertificate(@PathVariable long id, @RequestBody CertificateRequestDto certificateRequestDto) {
        CertificateResponseDto updated = certificateService.updateCertificate(id, certificateRequestDto);
        return new ResponseEntity<>(updated, HttpStatus.CREATED);
    }
}
