package pocopoco_vplay.board.model.service; // 효빈이의 패키지 경로에 맞게 수정

import org.springframework.data.jpa.repository.JpaRepository;
import pocopoco_vplay.video.domain.Video; // Video 엔티티의 경로

import java.util.List;

public interface VideoRepository extends JpaRepository<Video, Long> {
    List<Video> findRecommended();
    // 앞으로 여기에 필요한 쿼리 메소드를 추가할 수 있어.
    // 예를 들어 제목으로 비디오를 찾는 기능:
    // List<Video> findByTitle(String title);
}