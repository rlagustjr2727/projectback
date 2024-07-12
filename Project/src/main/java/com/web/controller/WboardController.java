package com.web.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.web.entity.Wboard;
import com.web.repository.WboardRepository;
import com.web.service.WboardService;

@RestController
@RequestMapping("/api/wboard")
public class WboardController {

	// 파일 경로
	private static final String UPLOAD_DIR = "uploads/";
	
	@Autowired
	private WboardRepository wboardRepository;
	
	@Autowired
	private WboardService wboardService;
	
	@GetMapping
	public List<Wboard> getAllWboards(){
		return wboardRepository.findAll();
	}
	
	@PostMapping
    public Wboard createWboard(@RequestBody Wboard wboard) {
        return wboardRepository.save(wboard);
    }

	   @PostMapping("/upload")
	    public ResponseEntity<String> uploadImage(@RequestParam("file") MultipartFile file) {
	        try {
	            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
	            Path path = Paths.get(UPLOAD_DIR + fileName);
	            Files.createDirectories(path.getParent());
	            Files.write(path, file.getBytes());
	            return ResponseEntity.ok(fileName);
	        } catch (IOException e) {
	            return ResponseEntity.status(500).body("File upload failed: " + e.getMessage());
	        }
	    }

	    @PutMapping("/{wboardName}")
	    public ResponseEntity<Wboard> updateWboard(@PathVariable String wboardName, @RequestBody Wboard wboardDetails) {
	        Wboard updatedWboard = wboardService.updateWboard(wboardName, wboardDetails);
	        return ResponseEntity.ok(updatedWboard);
	    }

	    @DeleteMapping("/{wboardName}")
	    public ResponseEntity<Void> deleteWboard(@PathVariable String wboardName) {
	        wboardService.deleteWboard(wboardName);
	        return ResponseEntity.noContent().build();
	    }
}
