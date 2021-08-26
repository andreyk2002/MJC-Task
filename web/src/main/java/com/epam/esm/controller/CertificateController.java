package com.epam.esm.controller;


import com.epam.esm.dto.CertificateResponseDto;
import com.epam.esm.service.CertificateService;
import com.epam.esm.validation.CertificateRequestDto;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
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

    private final CertificateService certificateService;

    @DeleteMapping("/{id}")
    @ApiOperation(value = "Removes certificate by specified id", response = ResponseEntity.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Certificate was successfully removed"),
            @ApiResponse(code = 40411, message = "Certificate which should be deleted not found"),
            @ApiResponse(code = 500, message = "Application failed to process the request")
    }
    )
    public ResponseEntity<CertificateResponseDto> deleteCertificate(@ApiParam(value = "id of the specified certificate",
            required = true) @PathVariable long id) {
        CertificateResponseDto certificate = certificateService.deleteById(id);
        return new ResponseEntity<>(certificate, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "Search for certificate with specified id", response = ResponseEntity.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Certificate was successfully found"),
            @ApiResponse(code = 40411, message = "Certificate with specified id not found"),
            @ApiResponse(code = 500, message = "Application failed to process the request")
    }
    )
    public ResponseEntity<CertificateResponseDto> getById(@ApiParam(value = "id of the specified certificate",
            required = true) @PathVariable long id) {
        CertificateResponseDto certificate = certificateService.getById(id);
        return new ResponseEntity<>(certificate, HttpStatus.OK);
    }


    @GetMapping("")
    @ApiOperation(value = "Searches list of all certificates depends on keyword (part of name or description)" +
            " or/and tag name in specified order", response = List.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Certificates were successfully archived"),
            @ApiResponse(code = 40016, message = "Parameters were not in correct format: order string " +
                    "violated regexp (name|createDate),(asc|desc)"),
            @ApiResponse(code = 500, message = "Application failed to process the request")
    }
    )
    public ResponseEntity<List<CertificateResponseDto>> getCertificates(
            @ApiParam(value = "name of the tag which all certificates should contain")
            @RequestParam(required = false) String tagName,
            @ApiParam(value = "part of the name or / and description which all certificates should contain")
            @RequestParam(required = false) String keyword,
            @ApiParam(value = "Specifies how certificates will be sorted")
            @RequestParam(defaultValue = "name,asc")
            @Pattern(message = "40016",
                    regexp = "(name|createDate),(asc|desc)")
                    String sort) {
        List<CertificateResponseDto> certificates = certificateService.getCertificates(tagName, keyword, sort);
        return new ResponseEntity<>(certificates, HttpStatus.OK);
    }


    @PostMapping("")
    @ApiOperation(value = "Add specified certificate", response = ResponseEntity.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Certificate was successfully found"),
            @ApiResponse(code = 40011, message = "Certificate for adding was in incorrect format: field name was null"),
            @ApiResponse(code = 40012, message = "Certificate for adding was in incorrect format: field " +
                    "description was null"),
            @ApiResponse(code = 40013, message = "Certificate for adding was in incorrect format:" +
                    " field price was negative"),
            @ApiResponse(code = 40014, message = "Certificate for adding was in incorrect format:" +
                    " field duration was negative"),
            @ApiResponse(code = 40015, message = "Certificate for adding was in incorrect format:" +
                    " list of certificate tag was null"),
            @ApiResponse(code = 500, message = "Application failed to process the request")
    }
    )
    public ResponseEntity<CertificateResponseDto> addCertificate(
            @ApiParam(value = "Certificate for adding")
            @RequestBody @Valid CertificateRequestDto certificateRequestDto) {
        CertificateResponseDto certificateResponseDto = certificateService.addCertificate(certificateRequestDto);
        return new ResponseEntity<>(certificateResponseDto, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @ApiOperation(value = "Updates state of certificate with specified id", response = ResponseEntity.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Certificate was successfully updated"),
            @ApiResponse(code = 500, message = "Application failed to process the request")
    }
    )
    public ResponseEntity<CertificateResponseDto> updateCertificate(
            @ApiParam(value = "Id of certificate, which will be updated") @PathVariable long id,
            @ApiParam(value = "New state of certificate") @RequestBody CertificateRequestDto certificateRequestDto) {
        CertificateResponseDto updated = certificateService.updateCertificate(id, certificateRequestDto);
        return new ResponseEntity<>(updated, HttpStatus.CREATED);
    }
}
