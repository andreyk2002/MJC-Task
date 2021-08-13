package com.epam.esm.controller;


import com.epam.esm.dto.GiftCertificateDto;
import com.epam.esm.mapping.CertificateModelDtoMapper;
import com.epam.esm.service.CertificateService;
import com.epam.esm.validation.CertificateModel;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/certificates")
@AllArgsConstructor
public class CertificateController {

    private final CertificateService certificateService;
    private final CertificateModelDtoMapper mapper;


    @DeleteMapping("/{id}")
    public void deleteCertificate(@PathVariable long id) {
        certificateService.deleteById(id);
    }

    @GetMapping("/{id}")
    public GiftCertificateDto getById(@PathVariable long id) {
        return certificateService.getById(id);
    }

    @GetMapping("")
    public List<GiftCertificateDto>getAll(){
        return certificateService.getAll();
    }

    @PostMapping("")
    public void addCertificate(@RequestBody @Valid CertificateModel certificateModel){
        GiftCertificateDto giftCertificateDto = mapper.certificateModelToDto(certificateModel);
        certificateService.addCertificate(giftCertificateDto);
    }

    @PutMapping("/{id}")
    public void updateCertificate(@PathVariable long id, @RequestBody CertificateModel certificateModel){

    }
}
