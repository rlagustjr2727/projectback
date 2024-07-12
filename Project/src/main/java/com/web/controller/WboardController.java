package com.web.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.web.entity.Wboard;
import com.web.service.WboardService;

@RestController
@RequestMapping("/api/wboard")
public class WboardController {

    @Autowired
    private WboardService wboardService;

    private static String UPLOAD_DIR = "uploads/";

    @PostMapping("/upload")
    public ResponseEntity<Map<String, String>> uploadFile(@RequestParam("file") MultipartFile file) {
        Map<String, String> response = new HashMap<>();
        try {
            String fileName = saveFile(file);
            response.put("fileName", fileName);
            return ResponseEntity.ok(response);
        } catch (IOException e) {
            response.put("error", "Failed to upload file");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    private String saveFile(MultipartFile file) throws IOException {
        byte[] bytes = file.getBytes();
        Path path = Paths.get(UPLOAD_DIR + file.getOriginalFilename());
        Files.write(path, bytes);
        return file.getOriginalFilename();
    }

    @PostMapping("/addProduct")
    public ResponseEntity<?> createWboard(@RequestPart("wboard") String wboardString,
                                          @RequestPart(value = "file", required = false) MultipartFile file) {
        try {
            // JSON 문자열을 객체로 변환
            ObjectMapper objectMapper = new ObjectMapper();
            Wboard wboard = objectMapper.readValue(wboardString, Wboard.class);

            System.out.println("Received Wboard: " + wboard.toString());

            if (file != null && !file.isEmpty()) {
                String fileName = saveFile(file);
                wboard.setWboardImg(fileName);
            }

            if (wboard.getWboardName() == null || wboard.getWboardName().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("wboard_name is required");
            }

            wboardService.saveWboard(wboard);
            return ResponseEntity.ok(wboard);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error saving wboard: " + e.getMessage());
        }
    }


    @PutMapping("/updateProduct/{wboardName}")
    public ResponseEntity<?> updateWboard(@PathVariable String wboardName, @RequestBody Wboard wboard) {
        try {
            Wboard existingWboard = wboardService.findByWboardName(wboardName);
            if (existingWboard == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found");
            }

            existingWboard.setWboardImg(wboard.getWboardImg());
            existingWboard.setWboardInfo(wboard.getWboardInfo());
            existingWboard.setWboardTip(wboard.getWboardTip());
            existingWboard.setWboardType(wboard.getWboardType());
            existingWboard.setWboardOrigin(wboard.getWboardOrigin());
            existingWboard.setWboardAlcohol(wboard.getWboardAlcohol());
            existingWboard.setWboardAge(wboard.getWboardAge());
            existingWboard.setWboardAlcoholType(wboard.getWboardAlcoholType());

            wboardService.saveWboard(existingWboard);
            return ResponseEntity.ok(existingWboard);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating wboard: " + e.getMessage());
        }
    }

    @DeleteMapping("/deleteProduct/{fileName}")
    public ResponseEntity<?> deleteImage(@PathVariable String fileName) {
        try {
            Path path = Paths.get(UPLOAD_DIR + fileName);
            Files.deleteIfExists(path);
            return ResponseEntity.ok("File deleted successfully");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting file: " + e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<Wboard>> getAllWboards() {
        List<Wboard> wboards = wboardService.getAllWboards();
        return ResponseEntity.ok(wboards);
    }
}
