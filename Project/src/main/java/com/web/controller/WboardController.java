package com.web.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
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

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.web.entity.Wboard;
import com.web.service.WboardService;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
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
            existingWboard.setWboardAbv(wboard.getWboardAbv());
            existingWboard.setWboardYo(wboard.getWboardYo());
            existingWboard.setWboardAbvType(wboard.getWboardAbvType());

            wboardService.saveWboard(existingWboard);
            return ResponseEntity.ok(existingWboard);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating wboard: " + e.getMessage());
        }
    }

    @DeleteMapping("/deleteProduct/{wboardName}")
    public ResponseEntity<Map<String, String>> deleteWboard(@PathVariable String wboardName, @RequestBody Map<String, String> requestBody) {
        String imageName = requestBody.get("imageName");
        Map<String, String> response = new HashMap<>();
        try {
            // 데이터베이스에서 제품 삭제
            boolean isDeleted = wboardService.deleteProductByName(wboardName);
            if (!isDeleted) {
                response.put("error", wboardName + " 제품을 찾을 수 없습니다");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }

            // 이미지 파일 삭제 로직
            if (imageName != null && !imageName.isEmpty()) {
                Path path = Paths.get(UPLOAD_DIR + imageName);
                if (Files.exists(path)) {
                    Files.deleteIfExists(path);
                    System.out.println("이미지 파일 " + imageName + " 삭제 완료");
                } else {
                    System.out.println("경로에 해당 이미지 파일이 없습니다: " + imageName);
                }
            }

            response.put("message", "제품 및 이미지가 성공적으로 삭제되었습니다");
            return ResponseEntity.ok(response);
        } catch (IOException e) {
            response.put("error", "Error deleting file: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    @GetMapping
    public ResponseEntity<List<Wboard>> getAllWboards() {
        List<Wboard> wboards = wboardService.getAllWboards();
        return ResponseEntity.ok(wboards);
    }
    
//    // 중복 체크
//    @GetMapping("/checkDuplicate/{wboardName}")
//    public ResponseEntity<?> checkDuplicateWboardName(@PathVariable String wboardName) {
//        boolean exists = wboardService.existsByWboardName(wboardName);
//        return ResponseEntity.ok(exists);
//    }
    
    // 카테고리 필터
    @GetMapping("/filter")
    public ResponseEntity<List<Wboard>> getFilteredWboards(
            @RequestParam(required = false) String query,
            @RequestParam(required = false) String wboard_of_type,
            @RequestParam(required = false) String wboard_origin,
            @RequestParam(required = false) String wboard_abv_type,
            @RequestParam(required = false) String wboard_yo) {
        List<Wboard> wboards = wboardService.getAllWboards();

        if (query != null && !query.isEmpty()) {
            wboards = wboards.stream()
                    .filter(w -> w.getWboardName().toLowerCase().contains(query.toLowerCase()))
                    .collect(Collectors.toList());
        }

        if (wboard_of_type != null && !wboard_of_type.isEmpty()) {
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                List<String> wboardTypes = objectMapper.readValue(wboard_of_type, new TypeReference<List<String>>() {});
                wboards = wboards.stream()
                        .filter(w -> wboardTypes.contains(w.getWboardType()))
                        .collect(Collectors.toList());
            } catch (IOException e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        }

        if (wboard_origin != null && !wboard_origin.isEmpty()) {
            wboards = wboards.stream()
                    .filter(w -> w.getWboardOrigin().equals(wboard_origin))
                    .collect(Collectors.toList());
        }

        if (wboard_abv_type != null && !wboard_abv_type.isEmpty()) {
            wboards = wboards.stream()
                    .filter(w -> w.getWboardAbvType().equals(wboard_abv_type))
                    .collect(Collectors.toList());
        }

        if (wboard_yo != null && !wboard_yo.isEmpty()) {
            wboards = wboards.stream()
                    .filter(w -> w.getWboardYo().equals(wboard_yo))
                    .collect(Collectors.toList());
        }

        return ResponseEntity.ok(wboards);
    }
}
