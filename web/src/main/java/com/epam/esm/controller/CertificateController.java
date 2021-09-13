package com.epam.esm.controller;


import com.epam.esm.repository.CertificateFilter;
import com.epam.esm.request.CertificateRequestDto;
import com.epam.esm.response.CertificateResponseDto;
import com.epam.esm.service.CertificateService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.AllArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

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
    public ResponseEntity<CertificateResponseDto> deleteById(@ApiParam(value = "id of the specified certificate",
            required = true) @PathVariable long id) {
        CertificateResponseDto certificate = certificateService.deleteById(id);
        certificate.add(
                linkTo(methodOn(CertificateController.class).getById(id)).withRel("getById"),
                linkTo(methodOn(CertificateController.class).deleteById(id)).withSelfRel()
        );
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
        certificate.add(
                linkTo(methodOn(CertificateController.class).getById(id)).withSelfRel(),
                linkTo(methodOn(CertificateController.class).deleteById(id)).withRel("deleteById")
        );
        return new ResponseEntity<>(certificate, HttpStatus.OK);
    }


    @GetMapping("")
    @ApiOperation(value = "Searches list of all certificates depends on keyword (part of name or description)" +
            " or/and tag name in specified order", response = ResponseEntity.class)
    public ResponseEntity<CollectionModel<CertificateResponseDto>> getCertificates(@Validated CertificateFilter filter) {

        List<CertificateResponseDto> certificates = certificateService
                .getCertificates(filter);
        certificates.forEach(cert ->
                cert.add(linkTo(methodOn(CertificateController.class).getById(cert.getId())).withRel("findTag")));
        List<Link> links = new ArrayList<>(Collections.singletonList(
                linkTo(methodOn(CertificateController.class).getCertificates(filter)).withSelfRel()
        ));
        CollectionModel<CertificateResponseDto> model = CollectionModel.of(certificates, links);
        return new ResponseEntity<>(model, HttpStatus.OK);
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
        CertificateResponseDto certificate = certificateService.addCertificate(certificateRequestDto);
        certificate.add(
                linkTo(methodOn(CertificateController.class).getById(certificate.getId())).withRel("getById"),
                linkTo(methodOn(CertificateController.class).deleteById(certificate.getId())).withRel("deleteById"),
                linkTo(methodOn(CertificateController.class).addCertificate(certificateRequestDto)).withSelfRel()
        );
        return new ResponseEntity<>(certificate, HttpStatus.CREATED);
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
        updated.add(
                linkTo(methodOn(CertificateController.class).getById(id)).withRel("getById"),
                linkTo(methodOn(CertificateController.class).deleteById(id)).withRel("deleteById"),
                linkTo(methodOn(CertificateController.class).updateCertificate(id, certificateRequestDto)).withSelfRel()
        );
        return new ResponseEntity<>(updated, HttpStatus.CREATED);
    }

    @PutMapping("/{id}/price")
    @ApiOperation(value = "Updates price for certificate with specified id", response = ResponseEntity.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Certificates were successfully archived"),
            @ApiResponse(code = 40013, message = "Certificate price was negative"),
            @ApiResponse(code = 500, message = "Application failed to process the request")
    })
    public ResponseEntity<CertificateResponseDto> updateCertificatePrice(
            @ApiParam(value = "Id of certificate, which price will be updated") @PathVariable long id,
            @ApiParam(value = "New duration of certificate") @RequestBody @PositiveOrZero(message = "40013")
            @Valid BigDecimal price) {
        CertificateRequestDto newCertificate = CertificateRequestDto.builder().price(price).build();
        CertificateResponseDto updated = certificateService.updateCertificate(id, newCertificate);
        updated.add(
                linkTo(methodOn(CertificateController.class).getById(id)).withRel("getById"),
                linkTo(methodOn(CertificateController.class).deleteById(id)).withRel("deleteById")
        );
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

    @GetMapping("/tags")
    @ApiOperation(value = "Searches certificates which tags ids are present in specified list", response = ResponseEntity.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Certificates were successfully archived"),
            @ApiResponse(code = 40031, message = "Tags string violated format id1,id2,id3..."),
            @ApiResponse(code = 500, message = "Application failed to process the request")
    })
    public ResponseEntity<CollectionModel<CertificateResponseDto>> findByTags(
            @ApiParam("String which contain tags ids") @RequestParam
            @Pattern(regexp = "\\d+(,\\d+)*", message = "40031") String tags) {
        List<CertificateResponseDto> certificates = certificateService.findByTags(tags);
        certificates.forEach(cert ->
                cert.add(linkTo(methodOn(CertificateController.class).getById(cert.getId())).withRel("findTag")));
        return new ResponseEntity<>(CollectionModel.of(certificates), HttpStatus.OK);
    }
}
