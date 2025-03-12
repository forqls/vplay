package pocopoco_vplay.cloudflare.controller;

import java.io.IOException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import pocopoco_vplay.cloudflare.model.service.R2Service;

@RestController
@RequestMapping("/r2")
@RequiredArgsConstructor
public class R2Controller {
	
	private final R2Service r2Service;
	
	@PostMapping("/upload")
	public ResponseEntity<String> uploadFile(@RequestParam("profile") MultipartFile file){
		try {
			String fileUrl = r2Service.uploadFile(file);
			System.out.println(fileUrl);
			return ResponseEntity.ok("File uploaded: " + fileUrl);
		} catch (IOException e) {
			return ResponseEntity.status(500).body("Upload failed");
		}
	}
}
