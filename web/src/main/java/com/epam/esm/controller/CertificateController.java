package com.epam.esm.controller;


import com.epam.esm.dto.CertificateResponseDto;
import com.epam.esm.mappers.CertificateRequestMapper;
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
import java.util.Optional;

@RestController
@RequestMapping("/certificates")
@AllArgsConstructor
@Validated
public class CertificateController {

    private final CertificateService certificateService;
    private final CertificateRequestMapper mapper;


    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCertificate(@PathVariable long id) {
        certificateService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.GONE);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable long id) {
        Optional<CertificateResponseDto> certificate = certificateService.getById(id);
        if (certificate.isPresent()) {
            CertificateResponseDto certificateDto = certificate.get();
            return new ResponseEntity<>(certificateDto, HttpStatus.OK);
        }
        return new ResponseEntity<>(String.format("Certificate with id %d not found", id), HttpStatus.NOT_FOUND);
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
    public ResponseEntity<?> addCertificate(@RequestBody @Valid CertificateRequestDto certificateRequestDto) {
        certificateService.addCertificate(certificateRequestDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCertificate(@PathVariable long id, @RequestBody CertificateRequestDto certificateRequestDto) {
        certificateService.updateCertificate(id, certificateRequestDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
