package pocopoco_vplay.board;

import lombok.Getter;
import pocopoco_vplay.board.model.vo.Content;

@Getter // Thymeleaf에서 데이터를 꺼내려면 Getter가 꼭 필요해!
public class VideoResponseDto {

    private int id;
    private String title;
    private String uploaderName;
    private String fullVideoUrl; // 👈 최종 URL이 담길 필드

    // Entity를 DTO로 변환하면서 URL을 조합하는 생성자
    // VideoResponseDto.java

    public VideoResponseDto(Content content, String bucketBaseUrl) {
        this.id = content.getContentNo();
        this.title = content.getContentTitle();

        // mapper가 content 객체의 storedFileName 필드를 채워줬기 때문에 이제 에러 없이 값을 가져올 수 있어!
        String fileName = content.getStoredFileName();

        this.fullVideoUrl = bucketBaseUrl + "/" + fileName;
    }
}