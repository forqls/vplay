package pocopoco_vplay.board;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import pocopoco_vplay.users.model.vo.Users;


@Entity // 이 클래스는 데이터베이스 테이블과 연결되는 객체(Entity)라고 알려주는 거야.
@Getter // 다른 곳에서 video.getId() 처럼 데이터를 꺼낼 수 있게 해줘.
@NoArgsConstructor // 기본 생성자를 만들어줘. JPA에서는 꼭 필요해.
public class Video {

    @Id // 이 필드가 테이블의 고유한 식별자(Primary Key)라는 뜻이야.
    @GeneratedValue(strategy = GenerationType.IDENTITY) // ID 값을 자동으로 생성해줘.
    private Long id;

    private String title; // 영상 제목

    // DB에는 실제 파일 이름만 저장. (예: --pub-9125...mp4)
    private String storedFileName;

    // User 엔티티와 관계를 맺어야 uploaderName을 가져올 수 있어.
    // 이건 예시 코드니, 효빈이의 User 엔티티에 맞게 수정해야 할 수도 있어!
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Users uploader;
}